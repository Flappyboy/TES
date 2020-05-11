package top.jach.tes.app.web.controller;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.ILoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import top.jach.tes.app.web.AppApplication;
import top.jach.tes.app.web.entity.ProjectEntity;
import top.jach.tes.app.web.entity.TaskEntity;
import top.jach.tes.app.web.repository.TaskEntityRepository;
import top.jach.tes.core.api.domain.Task;
import top.jach.tes.core.api.domain.action.Action;
import top.jach.tes.core.api.domain.action.StatefulAction;
import top.jach.tes.core.api.dto.PageQueryDto;
import top.jach.tes.core.api.factory.ContextFactory;
import top.jach.tes.core.api.factory.InfoRepositoryFactory;
import top.jach.tes.core.api.repository.ProjectRepository;
import top.jach.tes.core.api.repository.TaskRepository;
import top.jach.tes.plugin.jhkt.arcsmell.ArcSmellAction2;

import java.util.ArrayList;
import java.util.List;

@RestController()
@RequestMapping("/api/action")
public class ActionController {

    @GetMapping()
    public ResponseEntity query() {
        List<JSONObject> actions= new ArrayList<>();
        for (Action a :
                AppApplication.actionMap.values()) {
            actions.add(JSONObject.parseObject(StatefulAction.serializeActionToJson(a)));
        }
        PageQueryDto pageQueryDto = PageQueryDto.create(1, actions.size()).addResult(actions, Long.valueOf(actions.size()));
        return ResponseEntity.ok(pageQueryDto);
    }
}
