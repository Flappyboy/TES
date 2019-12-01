package top.jach.tes.plugin.jach.repository;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import top.jach.tes.core.domain.info.Info;
import top.jach.tes.core.domain.info.InfoProfile;
import top.jach.tes.core.dto.PageQueryDto;
import top.jach.tes.core.repository.InfoRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GeneraMongoRepository implements InfoRepository {
    MongoCollection mongoCollection;

    public GeneraMongoRepository(MongoCollection mongoCollection) {
        this.mongoCollection = mongoCollection;
    }

    @Override
    public Info saveProfile(Info info, Long projectId) {
        InfoProfile infoProfile = InfoProfile.createFromInfo(info);
        Document document = Document.parse(JSONObject.toJSONString(infoProfile));
        document.append("projectId", projectId);
        document.append("class", info.getInfoClass().getName());
        mongoCollection.insertOne(document);
        return info;
    }

    @Override
    public Info saveDetail(Info info) {

        FindIterable<Document> documents = mongoCollection.find(Filters.eq("id",info.getId()));
        Document profile = documents.first();
        Document document = Document.parse(JSONObject.toJSONString(info));
        document.append("projectId", profile.get("projectId"));
        document.append("class", info.getInfoClass().getName());
        List<Bson> updates = new ArrayList<>();
        for (Map.Entry<String, Object> entry :
                document.entrySet()) {
           updates.add(Updates.set(entry.getKey(), entry.getValue()));
        }
        mongoCollection.updateOne(Filters.eq("id",info.getId()),Updates.combine(updates));
        return info;
    }

    @Override
    public Info updateProfileByInfoId(Info info) {
        InfoProfile infoProfile = InfoProfile.createFromInfo(info);
        Document document = Document.parse(JSONObject.toJSONString(infoProfile));
        document.append("class", info.getInfoClass().getName());
        mongoCollection.updateOne(Filters.eq("id",info.getId()),document);
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
    public List queryDetailsByInfoIds(List infoIds) {
        FindIterable<Document> documents = mongoCollection.find(Filters.in("id",infoIds));
        List<Info> infos = new ArrayList<>();
        for (Document d :
                documents) {
            try {
                infos.add((Info) JSONObject.parseObject(d.toJson(), Class.forName(d.getString("class"))));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return infos;
    }
}
