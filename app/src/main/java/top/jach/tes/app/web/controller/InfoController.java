package top.jach.tes.app.web.controller;

import org.slf4j.ILoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
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

@RestController()
@RequestMapping("/api/info")
public class InfoController {

    @Autowired
    InfoService infoService;

    @GetMapping(value = "/allTypes")
    public ResponseEntity allTypes(Long projectId) {
        return ResponseEntity.ok(infoService.findAllInfoTypes(projectId));
    }
}
