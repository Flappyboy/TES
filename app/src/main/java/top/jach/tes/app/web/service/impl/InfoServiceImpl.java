package top.jach.tes.app.web.service.impl;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.jach.tes.app.web.AppApplication;
import top.jach.tes.app.web.dto.InfoType;
import top.jach.tes.app.web.entity.InfoProfileEntity;
import top.jach.tes.app.web.service.InfoService;
import top.jach.tes.core.api.domain.Project;
import top.jach.tes.core.api.domain.info.InfoProfile;
import top.jach.tes.plugin.tes.repository.GeneraInfoMongoRepository;

import java.util.*;

@Service
public class InfoServiceImpl implements InfoService {

    @Override
    public List<InfoType> findAllInfoTypes(Long projectId) {
        MongoCollection<Document> collection = AppApplication.generalInfoCollection();
        FindIterable<Document> documents = collection.find(Filters.eq("_projectId",projectId))
                .projection(Projections.include("infoClass", "name"));
        Set<InfoType> infoTypes = new HashSet<>();
        for (Document d :
                documents) {
            InfoType infoType = new InfoType();
            infoType.setClassName(d.getString("infoClass"));
            infoType.setInfoName(d.getString("name"));
            infoTypes.add(infoType);
        }
        return new ArrayList<>(infoTypes);
    }
}
