package top.jach.tes.app.web.controller;

import javafx.scene.shape.Arc;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.ILoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.jach.tes.app.web.dto.ArcSmellResultInfo;
import top.jach.tes.app.web.entity.ProjectEntity;
import top.jach.tes.app.web.entity.TaskEntity;
import top.jach.tes.app.web.repository.TaskEntityRepository;
import top.jach.tes.app.web.service.InfoService;
import top.jach.tes.core.api.domain.Task;
import top.jach.tes.core.api.dto.PageQueryDto;
import top.jach.tes.core.api.factory.ContextFactory;
import top.jach.tes.core.api.factory.InfoRepositoryFactory;
import top.jach.tes.core.api.repository.ProjectRepository;
import top.jach.tes.core.api.repository.TaskRepository;
import top.jach.tes.plugin.tes.repository.GeneraInfoMongoRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/api/info")
public class InfoController {

    @Autowired
    InfoService infoService;

    @Autowired
    InfoRepositoryFactory infoRepositoryFactory;

    @GetMapping(value = "/allTypes")
    public ResponseEntity allTypes(Long projectId) {
        return ResponseEntity.ok(infoService.findAllInfoTypes(projectId));
    }

    @GetMapping(value = "/arcSmellInfo")
    public ResponseEntity arcSmellInfos(Long projectId) {
        Map<GeneraInfoMongoRepository.BsonType, Bson> bsonTypeBsonMap = new HashMap<>();
        bsonTypeBsonMap.put(GeneraInfoMongoRepository.BsonType.Filter, new Document("infoClass", ArcSmellResultInfo.class.getName()));
        bsonTypeBsonMap.put(GeneraInfoMongoRepository.BsonType.Filter, new Document("_projectId", projectId));
        PageQueryDto dto = infoRepositoryFactory.getRepository(ArcSmellResultInfo.class).queryProfileByCustom(bsonTypeBsonMap, PageQueryDto.create(0,1000));
        return ResponseEntity.ok(dto.getResult());
    }

    @GetMapping(value = "/arcSmellInfo/{id}")
    public ResponseEntity arcSmellInfo(@PathVariable Long id) {
        List<Long> ids = new ArrayList<>();
        ids.add(id);
        List list = infoRepositoryFactory.getRepository(ArcSmellResultInfo.class).queryDetailsByInfoIds(ids);
        return ResponseEntity.ok(list.get(0));
    }
}
