package top.jach.tes.plugin.tes.code.git.commit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonWriterSettings;

import java.util.*;

import static com.alibaba.fastjson.parser.Feature.DisableFieldSmartMatch;
import static com.alibaba.fastjson.serializer.SerializerFeature.DisableCircularReferenceDetect;

public class GitCommitMongoReository implements GitCommitRepository {
    public static final String COMMIT_REPOS_ID = "_reposId";
    public static final String COMMIT_REPO_NAME = "_repoName";
    public static final String GIT_COMMIT_DATA_STRUCT_VERSION = "_data_struct_version";

    MongoCollection collection;

    public GitCommitMongoReository(MongoCollection collection) {
        this.collection = collection;
        collection.createIndex(Indexes.ascending(COMMIT_REPOS_ID,COMMIT_REPO_NAME,"sha"));
    }

    @Override
    public void saveGitCommits(List<GitCommit> gitCommits, Long reposId, String repoName) {
        synchronized (reposId + "&&" + repoName) {
            Set<String> shas = findShasByRepo(reposId, repoName);
            List<Document> documents = new ArrayList<>();
            for (GitCommit gitCommit :
                    gitCommits) {
                if (gitCommit == null) {
                    continue;
                }
                if (shas.contains(gitCommit.getSha())) {
                    continue;
                }
                Document document = Document.parse(JSONObject.toJSONString(gitCommit, DisableCircularReferenceDetect))
                        .append(COMMIT_REPOS_ID, reposId)
                        .append(COMMIT_REPO_NAME, repoName)
                        .append(GIT_COMMIT_DATA_STRUCT_VERSION, GitCommit._data_struct_version);
                documents.add(document);
            }
            if(documents.size()>0) {
                collection.insertMany(documents);
            }
        }
    }

    @Override
    public Set<String> findShasByRepo(Long reposId, String repoName) {
        Set<String> shas = new HashSet<>();
        Iterable<Document> commitDocuments = collection.find(
                Filters.and(Filters.eq(COMMIT_REPOS_ID, reposId),
                        Filters.eq(COMMIT_REPO_NAME, repoName),
                        Filters.eq(GIT_COMMIT_DATA_STRUCT_VERSION, GitCommit._data_struct_version)))
                .projection(Projections.include("sha"));
        deleteOldVersionData(reposId, repoName);
        for (Document d :
                commitDocuments) {
            String sha = d.getString("sha");
            if(sha != null){
                shas.add(sha);
            }
        }
        return shas;
    }

    @Override
    public Iterable<GitCommit> findByRepoAndShas(Long reposId, String repoName, Iterable<String> shas) {
        Iterable<Document> documents = collection.find(Filters.and(Filters.eq(COMMIT_REPOS_ID, reposId),
                Filters.eq(COMMIT_REPO_NAME, repoName),
                Filters.eq(GIT_COMMIT_DATA_STRUCT_VERSION, GitCommit._data_struct_version),
                Filters.in("sha", shas)));
        List<GitCommit> gitCommits = new ArrayList<>();
        for (Document document :
                documents) {
            GitCommit commit = (GitCommit) JSON.parseObject(document.toJson(JsonWriterSettings.builder().build()),
                    GitCommit.class, DisableFieldSmartMatch);
            gitCommits.add(commit);
        }
        return gitCommits;
    }

    @Override
    public void deleteOldVersionData(Long reposId, String repoName) {
        List<Bson> bsons = new ArrayList<>();
        if (reposId!=null){
            bsons.add(Filters.eq(COMMIT_REPOS_ID, reposId));
        }
        if (repoName!=null){
            bsons.add(Filters.eq(COMMIT_REPO_NAME, repoName));
        }
        bsons.add(Filters.ne(GIT_COMMIT_DATA_STRUCT_VERSION, GitCommit._data_struct_version));
        collection.deleteMany(Filters.and(bsons.toArray(new Bson[bsons.size()])));
    }
}
