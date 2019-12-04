package top.jach.tes.dev.app.jach.repository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonWriterSettings;
import top.jach.tes.core.domain.info.Info;
import top.jach.tes.core.domain.info.InfoProfile;
import top.jach.tes.core.dto.PageQueryDto;
import top.jach.tes.core.repository.InfoRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GeneraInfoMongoRepository implements InfoRepository<Info, Map<GeneraInfoMongoRepository.BsonType, Bson>> {
    public static final String PROJECT_ID = "_projectId";
    MongoCollection mongoCollection;

    public enum BsonType{
        Filter(),
        Projection(),
    }

    public GeneraInfoMongoRepository(MongoCollection mongoCollection) {
        this.mongoCollection = mongoCollection;
    }

    @Override
    public Info saveProfile(Info info, Long projectId) {
        InfoProfile infoProfile = InfoProfile.createFromInfo(info);
        Document document = Document.parse(JSONObject.toJSONString(infoProfile));
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
        Document document = Document.parse(JSONObject.toJSONString(info));
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
    public PageQueryDto queryProfileByInfoAndProjectId(Info info, Long projectId, PageQueryDto pageQueryDto) {
        return null;
    }

    @Override
    public PageQueryDto<Info> queryProfileByCustom(Map<BsonType, Bson> bsonTypeBsonMap, PageQueryDto pageQueryDto) {
        if (pageQueryDto == null){
            pageQueryDto = PageQueryDto.create(1,-1);
        }
        FindIterable<Document> documents = mongoCollection
                .find(getBson(bsonTypeBsonMap, BsonType.Filter))
                .projection(getBson(bsonTypeBsonMap, BsonType.Projection));
        for (Document document :
                documents) {
            try {
                Info info = (Info) JSON.parseObject(document.toJson(JsonWriterSettings.builder().build()),
                        Class.forName(document.getString("infoClass")));
                pageQueryDto.resultAdd(info);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return pageQueryDto;
    }

    @Override
    public List queryDetailsByInfoIds(List infoIds) {
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

    private Bson getBson(Map<BsonType, Bson> bsonTypeBsonMap, BsonType bsonType){
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
