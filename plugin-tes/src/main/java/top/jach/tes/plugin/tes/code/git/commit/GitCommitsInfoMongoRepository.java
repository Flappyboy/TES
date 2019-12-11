package top.jach.tes.plugin.tes.code.git.commit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.*;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonWriterSettings;
import org.n3r.idworker.Id;
import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.core.api.domain.info.InfoProfile;
import top.jach.tes.core.api.dto.PageQueryDto;
import top.jach.tes.core.api.repository.InfoRepository;
import top.jach.tes.plugin.tes.repository.GeneraInfoMongoRepository;

import javax.print.Doc;
import java.util.*;

import static com.alibaba.fastjson.parser.Feature.DisableFieldSmartMatch;
import static com.alibaba.fastjson.serializer.SerializerFeature.DisableCircularReferenceDetect;

public class GitCommitsInfoMongoRepository implements InfoRepository<GitCommitsInfo, Map<GeneraInfoMongoRepository.BsonType, Bson>> {
    public static final String PROJECT_ID = "_projectId";
    public static final String INFO_ID = "id";
    public static final String COMMIT_REPOS_ID = "_reposId";
    public static final String COMMIT_REPO_NAME = "_repoName";
    public static final String COMMIT_ID = "_commit_id";

    MongoCollection infoProfileCollection;
    MongoCollection infoDetailCollection;
    GeneraInfoMongoRepository generaInfoMongoRepository;

    public GitCommitsInfoMongoRepository(MongoCollection infoProfileCollection, MongoCollection infoDetailCollection) {
        this.infoProfileCollection = infoProfileCollection;
        this.infoDetailCollection = infoDetailCollection;
        infoProfileCollection.createIndex(Indexes.ascending(INFO_ID));
        infoDetailCollection.createIndex(Indexes.ascending(COMMIT_REPOS_ID));
        infoDetailCollection.createIndex(Indexes.ascending(COMMIT_REPO_NAME));
        this.generaInfoMongoRepository = new GeneraInfoMongoRepository(infoProfileCollection);
    }

    @Override
    public GitCommitsInfo saveProfile(GitCommitsInfo info, Long projectId) {
        InfoProfile infoProfile = InfoProfile.createFromInfo(info);
        Document document = Document.parse(JSONObject.toJSONString(infoProfile, DisableCircularReferenceDetect))
                .append(PROJECT_ID, projectId)
                .append("reposId", info.getReposId())
                .append("repoName", info.getRepoName());
        document.remove("infoClass");
        infoProfileCollection.insertOne(document);
        return info;
    }

    @Override
    public GitCommitsInfo saveDetail(GitCommitsInfo info) {
        List<GitCommit> gitCommits = info.getGitCommits();
        List<Document> documents = new ArrayList<>();
        List<Long> ids = new ArrayList<>();

        Map<String, Long> shas = new HashMap<>();
        Iterable<Document> commitDocuments = infoDetailCollection.find(
                        Filters.and(Filters.eq(COMMIT_REPOS_ID, info.getReposId()),
                        Filters.eq(COMMIT_REPO_NAME, info.getRepoName()))).projection(Projections.include("sha",COMMIT_ID));
        for (Document d :
                commitDocuments) {
            String sha = d.getString("sha");
            Long id = d.getLong(COMMIT_ID);
            if(sha != null && id != null){
                shas.put(sha, id);
            }
        }

        for (GitCommit gitCommit :
                gitCommits) {
            if (gitCommit == null){
                continue;
            }
            Long id = shas.get(gitCommit.getSha());
            if(id != null){
                ids.add(id);
                continue;
            }
            Document document = Document.parse(JSONObject.toJSONString(gitCommit, DisableCircularReferenceDetect));
            document.append(COMMIT_REPOS_ID, info.getReposId());
            document.append(COMMIT_REPO_NAME, info.getRepoName());
            id = Id.next();
            ids.add(id);
            document.append(COMMIT_ID, id);
            documents.add(document);
        }
        if (documents.size() > 0) {
            infoDetailCollection.insertMany(documents);
        }
        infoProfileCollection.updateOne(Filters.eq("id",info.getId()),Updates.set("_commit_ids", ids));
        return info;
    }

    @Override
    public GitCommitsInfo updateProfileByInfoId(GitCommitsInfo info) {
        InfoProfile infoProfile = InfoProfile.createFromInfo(info);
        Document document = Document.parse(JSONObject.toJSONString(infoProfile, DisableCircularReferenceDetect))
                .append("reposId", info.getReposId())
                .append("repoName", info.getRepoName());
        document.remove("infoClass");
        List<Bson> updates = new ArrayList<>();
        for (Map.Entry<String, Object> entry :
                document.entrySet()) {
            updates.add(Updates.set(entry.getKey(), entry.getValue()));
        }
        infoProfileCollection.updateOne(Filters.eq(INFO_ID,info.getId()),Updates.combine(updates));
        return info;
    }

    @Override
    public GitCommitsInfo deleteByInfoId(Long infoId) {
        infoProfileCollection.deleteMany(Filters.eq(INFO_ID, infoId));
        return null;
    }

    @Override
    public PageQueryDto<GitCommitsInfo> queryProfileByInfoAndProjectId(Info info, Long projectId, PageQueryDto pageQueryDto) {
        Bson bson = Document.parse(JSONObject.toJSONString(info, DisableCircularReferenceDetect)).append(PROJECT_ID, projectId);
        Map<GeneraInfoMongoRepository.BsonType, Bson> bsonTypeBsonMap = new HashMap<>();
        bsonTypeBsonMap.put(GeneraInfoMongoRepository.BsonType.Filter, bson);
        bsonTypeBsonMap.put(GeneraInfoMongoRepository.BsonType.Projection, Projections.include(
                "id","createdTime","updatedTime","name", "status", "desc", "reposId", "repoName"
        ));
        return queryProfileByCustom(bsonTypeBsonMap, pageQueryDto);
    }

    @Override
    public PageQueryDto<GitCommitsInfo> queryProfileByCustom(Map<GeneraInfoMongoRepository.BsonType, Bson> bsonTypeBsonMap, PageQueryDto pageQueryDto) {
        if (pageQueryDto == null){
            pageQueryDto = PageQueryDto.create(1,-1);
        }
        Bson sort = null;
        if (StringUtils.isNoneBlank(pageQueryDto.getSortField())){
            switch (pageQueryDto.getSortType()){
                case ASC:
                    sort = Sorts.ascending(pageQueryDto.getSortField());
                    break;
                case DESC:
                    sort = Sorts.descending(pageQueryDto.getSortField());
                    break;
            }
        }
        FindIterable<Document> documents = infoProfileCollection
                .find(GeneraInfoMongoRepository.getBson(bsonTypeBsonMap, GeneraInfoMongoRepository.BsonType.Filter))
                .projection(GeneraInfoMongoRepository.getBson(bsonTypeBsonMap, GeneraInfoMongoRepository.BsonType.Projection))
                .sort(sort)
                .limit(pageQueryDto.getPageSize())
                .skip(pageQueryDto.getPageSize()*(pageQueryDto.getPageNum()-1));
        ;
        for (Document document :
                documents) {
            Info info = (Info) JSON.parseObject(document.toJson(JsonWriterSettings.builder().build()),
                    GitCommitsInfo.class, DisableFieldSmartMatch);
            pageQueryDto.resultAdd(info);
        }
        return pageQueryDto;
    }

    @Override
    public List<GitCommitsInfo> queryDetailsByInfoIds(List<Long> infoIds) {
        Iterable<Document> documents = infoProfileCollection.find(Filters.in("id", infoIds));
        List<GitCommitsInfo> gitCommitsInfos = new ArrayList<>();
        for (Document document :
                documents) {
            GitCommitsInfo info = (GitCommitsInfo) JSON.parseObject(document.toJson(JsonWriterSettings.builder().build()),
                    GitCommitsInfo.class, DisableFieldSmartMatch);
            gitCommitsInfos.add(info);

            String commitId = document.getString(COMMIT_ID);
            Iterable<Document> commitDocuments = infoDetailCollection.find(Filters.in(COMMIT_ID, commitId));
            for (Document d :
                    commitDocuments) {
                GitCommit gitCommit = (GitCommit) JSON.parseObject(d.toJson(JsonWriterSettings.builder().build()), GitCommit.class);
                info.addGitCommits(gitCommit);
            }
        }
        return gitCommitsInfos;
    }
}
