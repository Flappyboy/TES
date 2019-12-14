package top.jach.tes.plugin.tes.repository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonWriterSettings;
import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.core.api.domain.info.InfoProfile;
import top.jach.tes.core.api.dto.PageQueryDto;
import top.jach.tes.core.api.repository.InfoRepository;

import java.util.*;

import static com.alibaba.fastjson.parser.Feature.DisableFieldSmartMatch;
import static com.alibaba.fastjson.parser.Feature.IgnoreNotMatch;
import static com.alibaba.fastjson.serializer.SerializerFeature.DisableCircularReferenceDetect;

public class GeneraInfoMongoRepository implements InfoRepository<Info, Map<GeneraInfoMongoRepository.BsonType, Bson>> {
    public static final String PROJECT_ID = "_projectId";
    private MongoCollection mongoCollection;

    public enum BsonType{
        Filter(),
        Projection(),
    }

    public GeneraInfoMongoRepository(MongoCollection mongoCollection) {
        this.mongoCollection = mongoCollection;
    }

    @Override
    public Info saveProfile(Info info, Long projectId) {
        mongoCollection.deleteMany(Filters.eq("id", info.getId()));

        InfoProfile infoProfile = InfoProfile.createFromInfo(info);
        Document document = Document.parse(JSONObject.toJSONString(infoProfile, DisableCircularReferenceDetect));
        document.append("_projectId", projectId);
        mongoCollection.insertOne(document);
        return info;
    }

    @Override
    public Info saveDetail(Info info) {
        return updateProfileByInfoId(info);
    }

    @Override
    public Info updateProfileByInfoId(Info info) {
        Document document = Document.parse(JSONObject.toJSONString(info, DisableCircularReferenceDetect));
        List<Bson> updates = new ArrayList<>();
        for (Map.Entry<String, Object> entry :
                document.entrySet()) {
            updates.add(Updates.set(entry.getKey(), entry.getValue()));
        }
        mongoCollection.updateOne(Filters.eq("id",info.getId()),Updates.combine(updates));
        return info;
    }

    @Override
    public Info deleteByInfoId(Long infoId) {
        List<Info> infos = queryDetailsByInfoIds(Arrays.asList(infoId));
        Info info = infos.size()>0?infos.get(0):null;
        mongoCollection.deleteOne(Filters.eq("id", infoId));
        return info;
    }

    @Override
    public PageQueryDto queryProfileByInfoAndProjectId(Info queryInfo, Long projectId, PageQueryDto pageQueryDto) {
        Bson bson = Document.parse(JSONObject.toJSONString(queryInfo, DisableCircularReferenceDetect)).append(PROJECT_ID, projectId);
        Map<BsonType, Bson> bsonTypeBsonMap = new HashMap<>();
        bsonTypeBsonMap.put(BsonType.Filter, bson);
        return queryProfileByCustom(bsonTypeBsonMap, pageQueryDto);
    }

    @Override
    public PageQueryDto<Info> queryProfileByCustom(Map<BsonType, Bson> bsonTypeBsonMap, PageQueryDto pageQueryDto) {
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
        FindIterable<Document> documents = mongoCollection
                .find(getBson(bsonTypeBsonMap, BsonType.Filter))
                .projection(getBson(bsonTypeBsonMap, BsonType.Projection))
                .sort(sort)
                .limit(pageQueryDto.getPageSize())
                .skip(pageQueryDto.getPageSize()*(pageQueryDto.getPageNum()-1));
                ;
        for (Document document :
                documents) {
            try {
                Info info = (Info) JSON.parseObject(document.toJson(JsonWriterSettings.builder().build()),
                        Class.forName(document.getString("infoClass")),DisableFieldSmartMatch);
                pageQueryDto.resultAdd(info);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return pageQueryDto;
    }

    @Override
    public List queryDetailsByInfoIds(List<Long> infoIds) {
        FindIterable<Document> documents = mongoCollection.find(Filters.in("id",infoIds));
        List<Info> infos = new ArrayList<>();
        for (Document d :
                documents) {
            try {
                d.remove("_id");
//                System.out.println(d.toJson(JsonWriterSettings.builder().build()));
                infos.add((Info) JSON.parseObject(d.toJson(JsonWriterSettings.builder().build()), Class.forName(d.getString("infoClass"))));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return infos;
    }

    public static Bson getBson(Map<BsonType, Bson> bsonTypeBsonMap, BsonType bsonType){
        Bson bson = bsonTypeBsonMap.get(bsonType);
        if(bson != null){
            return bson;
        }
        switch (bsonType){
            case Filter:
                return Filters.and();
            case Projection:
                return Projections.include(
                        "id","createdTime","updatedTime","name","infoClass", "status", "desc"
                );
        }
        return null;
    }
}
