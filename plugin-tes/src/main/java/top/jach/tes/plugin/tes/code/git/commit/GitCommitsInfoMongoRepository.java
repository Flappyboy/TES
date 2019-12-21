package top.jach.tes.plugin.tes.code.git.commit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.NameFilter;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.*;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonWriterSettings;
import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.core.api.domain.info.InfoProfile;
import top.jach.tes.core.api.dto.PageQueryDto;
import top.jach.tes.core.api.repository.InfoRepository;
import top.jach.tes.plugin.tes.repository.GeneraInfoMongoRepository;

import java.util.*;

import static com.alibaba.fastjson.parser.Feature.DisableFieldSmartMatch;
import static com.alibaba.fastjson.serializer.SerializerFeature.DisableCircularReferenceDetect;

public class GitCommitsInfoMongoRepository implements InfoRepository<GitCommitsInfo, Map<GeneraInfoMongoRepository.BsonType, Bson>> {
    public static final String PROJECT_ID = "_projectId";
    public static final String INFO_ID = "id";
    public static final String COMMIT_SHAS = "_commit_shas";

    MongoCollection infoProfileCollection;

    PropertyFilter profilter = (object, name, value) -> {
        if(name.equals("gitCommits")) {
            return false;
        }
        return true;
    };

    GitCommitRepository gitCommitRepository;

    public GitCommitsInfoMongoRepository(MongoCollection infoProfileCollection, GitCommitRepository gitCommitRepository) {
        this.infoProfileCollection = infoProfileCollection;
        infoProfileCollection.createIndex(Indexes.ascending(INFO_ID));
        this.gitCommitRepository = gitCommitRepository;
    }

    @Override
    public GitCommitsInfo saveProfile(GitCommitsInfo info, Long projectId) {
        infoProfileCollection.deleteMany(Filters.eq("id", info.getId()));
        Document document = Document.parse(JSONObject.toJSONString(info, profilter, DisableCircularReferenceDetect))
                .append(PROJECT_ID, projectId);
        document.append(COMMIT_SHAS, info.allShas());
        infoProfileCollection.insertOne(document);
        return info;
    }

    @Override
    public GitCommitsInfo saveDetail(GitCommitsInfo info) {
        List<GitCommit> gitCommits = info.getGitCommits();
        gitCommitRepository.saveGitCommits(gitCommits, info.getReposId(), info.getRepoName());
        return info;
    }

    @Override
    public GitCommitsInfo updateProfileByInfoId(GitCommitsInfo info) {
        Document document = Document.parse(JSONObject.toJSONString(info, profilter, DisableCircularReferenceDetect));
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
        List<GitCommitsInfo> infos = queryDetailsByInfoIds(Arrays.asList(infoId));
        GitCommitsInfo info = infos.size()>0?infos.get(0):null;
        infoProfileCollection.deleteOne(Filters.eq("id", infoId));
        return info;
    }

    @Override
    public PageQueryDto<GitCommitsInfo> queryProfileByInfoAndProjectId(Info info, Long projectId, PageQueryDto pageQueryDto) {
        Bson bson = Document.parse(JSONObject.toJSONString(info, DisableCircularReferenceDetect)).append(PROJECT_ID, projectId);
        Map<GeneraInfoMongoRepository.BsonType, Bson> bsonTypeBsonMap = new HashMap<>();
        bsonTypeBsonMap.put(GeneraInfoMongoRepository.BsonType.Filter, bson);
        bsonTypeBsonMap.put(GeneraInfoMongoRepository.BsonType.Projection, Projections.include(
                "id","createdTime","updatedTime","name", "status", "desc", "reposId", "repoName", "infoClass"
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
            Info info = null;
            try {
                info = (Info) JSON.parseObject(document.toJson(JsonWriterSettings.builder().build()),
                        Class.forName(document.getString("infoClass")), DisableFieldSmartMatch);
                pageQueryDto.resultAdd(info);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return pageQueryDto;
    }

    @Override
    public List<GitCommitsInfo> queryDetailsByInfoIds(List<Long> infoIds) {
        Iterable<Document> documents = infoProfileCollection.find(Filters.in("id", infoIds));
        List<GitCommitsInfo> gitCommitsInfos = new ArrayList<>();
        for (Document document :
                documents) {
            try {
                GitCommitsInfo info = (GitCommitsInfo) JSON.parseObject(document.toJson(JsonWriterSettings.builder().build()),
                        Class.forName(document.getString("infoClass")), DisableFieldSmartMatch);
                gitCommitsInfos.add(info);

                Iterable<GitCommit> gitCommitIterator = gitCommitRepository.findByRepoAndShas(info.getReposId(), info.getRepoName(), document.getList(COMMIT_SHAS, String.class));
                for (GitCommit gitCommit :
                        gitCommitIterator) {
                    info.addGitCommits(gitCommit);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return gitCommitsInfos;
    }
}
