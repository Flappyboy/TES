package top.jach.tes.app.web.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.slf4j.ILoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.jach.tes.app.web.AppApplication;
import top.jach.tes.app.web.dto.ArcSmellResultInfo;
import top.jach.tes.app.web.entity.ProjectEntity;
import top.jach.tes.app.web.entity.TaskEntity;
import top.jach.tes.app.web.repository.TaskEntityRepository;
import top.jach.tes.core.api.domain.Project;
import top.jach.tes.core.api.domain.Task;
import top.jach.tes.core.api.dto.PageQueryDto;
import top.jach.tes.core.api.factory.ContextFactory;
import top.jach.tes.core.api.factory.InfoRepositoryFactory;
import top.jach.tes.core.api.repository.ProjectRepository;
import top.jach.tes.core.api.repository.TaskRepository;
import top.jach.tes.core.impl.domain.element.ElementsValue;
import top.jach.tes.core.impl.domain.relation.PairRelationsInfo;
import top.jach.tes.plugin.jhkt.arcsmell.cyclic.CyclicAction;
import top.jach.tes.plugin.jhkt.arcsmell.hublink.HublinkAction;
import top.jach.tes.plugin.jhkt.arcsmell.result.Mv;
import top.jach.tes.plugin.jhkt.arcsmell.result.ResultForMs;
import top.jach.tes.plugin.jhkt.microservice.MicroservicesInfo;
import top.jach.tes.plugin.tes.code.git.commit.GitCommit;
import top.jach.tes.plugin.tes.code.git.commit.GitCommitsInfo;
import top.jach.tes.plugin.tes.code.go.GoPackagesInfo;
import top.jach.tes.plugin.tes.utils.FileCompress;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController()
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TaskEntityRepository taskEntityRepository;

    @Autowired
    InfoRepositoryFactory infoRepositoryFactory;

    @Autowired
    ILoggerFactory iLoggerFactory;

    @Autowired
    ContextFactory contextFactory;


    @RequestMapping(value = "/execute", method = RequestMethod.POST)
    public ResponseEntity execute(TaskEntity taskEntity) {
        Task task = taskEntity.toTask();
        task.initBuild();
        task = taskRepository.save(task);
//        task.setAction(AppApplication.actionMap.get(taskEntity.getAction()));
        task.execute(contextFactory.createContext(task.getProject()));
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/execute/im", method = RequestMethod.GET)
    public ResponseEntity im(@RequestParam long projectId, @RequestParam int mvcount, @RequestParam int mvdistance, @RequestParam double mvratio,
                             @RequestParam int hd,
                             @RequestParam(required = false, defaultValue = "-1") double hdth,
                             @RequestParam(required = false, defaultValue = "-1") double hdnth,
                             @RequestParam(required = false, defaultValue = "-1") double hdinth,
                             @RequestParam boolean hddirect,
                             @RequestParam String desc,
                             @RequestParam String datapath) throws IOException {
        datapath = URLDecoder.decode(datapath, "utf8");
//        System.out.println(String.format("mvcount %d, mvdistance %d, mvratio %f,, mvth %d,, hdth %df,, hddirect %b,, datapath %s,",mvcount, mvdistance, mvratio, hd, hdth, hddirect, datapath));

        File file = FileCompress.unZip(datapath, new File(datapath).getParentFile().getAbsolutePath());
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            file = files[i];
            if (file.getName().endsWith(".json")){
                break;
            }
        }

        String jsonStr = FileUtils.readFileToString(file, "utf8");
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        /*
        * Map<String, Object> data = new HashMap<>();
            allData.put(version.getVersionName(), data);
            data.put("VersionName", version.getVersionName());
            data.put("Microservices", microservices);
            data.put("GitCommits", gitCommits);
            data.put("PairRelationsInfoWithWeight", pairRelationsInfoWithWeight);
            data.put("pairRelationsInfoWithoutWeight", pairRelationsInfoWithoutWeight);
        * */
        ArcSmellResultInfo arcSmellResultInfo = ArcSmellResultInfo.createInfo();
        for (Object o :
                jsonObject.values()) {
            JSONObject data = (JSONObject) o;
            String versionName = data.getString("VersionName");
            MicroservicesInfo microservices = data.getJSONObject("Microservices").toJavaObject(MicroservicesInfo.class);
            JSONArray jsonArray = data.getJSONArray("GitCommits");
            List<GitCommit> gitCommits = new ArrayList<>();
            for (Object jo :
                    jsonArray) {
                JSONObject joo = (JSONObject) jo;
                gitCommits.add(joo.toJavaObject(GitCommit.class));
            }
            PairRelationsInfo pairRelationsInfoWithWeight = data.getJSONObject("PairRelationsInfoWithWeight").toJavaObject(PairRelationsInfo.class);
            PairRelationsInfo pairRelationsInfoWithoutWeight = data.getJSONObject("pairRelationsInfoWithoutWeight").toJavaObject(PairRelationsInfo.class);

            ElementsValue hublike_weight = HublinkAction.calculateHublike(pairRelationsInfoWithWeight);
            ElementsValue hublike_no_weight = HublinkAction.calculateHublike(pairRelationsInfoWithoutWeight);
            ElementsValue cyclicResult = CyclicAction.CalculateCyclic(microservices, pairRelationsInfoWithWeight);

            List<Mv> mvs = Mv.CalculateMvs(new int[]{mvdistance}, new int[]{mvcount}, new double[]{mvratio}, gitCommits, microservices.getMicroservices());

            ResultForMs resultForMs = new ResultForMs();
//            result.put(version.getVersionName(), resultForMs);
            resultForMs.setMicroservice(microservices.microserviceNames());
            resultForMs.setCyclic(cyclicResult.getValueMap());
            resultForMs.setHublikeWithWeight(hublike_weight.getValueMap());
            resultForMs.setHublikes(hublike_no_weight.getValueMap());
            resultForMs.setMvs(mvs);

            arcSmellResultInfo.put(versionName, resultForMs, hdth, hdnth, hdinth);
        }
        /*ElementsValue hublike_weight = HublinkAction.calculateHublike(pairRelationsInfoWithWeight);
            ElementsValue hublike_no_weight = HublinkAction.calculateHublike(pairRelationsInfoWithoutWeight);
            ElementsValue cyclicResult = CyclicAction.CalculateCyclic(context, microservices, pairRelationsInfoWithWeight);

            List<Mv> mvs = Mv.CalculateMvs(new int[]{5},new int[]{10},new double[]{0.5},gitCommits, microservices.getMicroservices());

            ResultForMs resultForMs = new ResultForMs();
            result.put(version.getVersionName(), resultForMs);
            resultForMs.setMicroservice(microserviceNames);
            resultForMs.setCyclic(cyclicResult.getValueMap());
            resultForMs.setHublikeWithWeight(hublike_weight.getValueMap());
            resultForMs.setHublikes(hublike_no_weight.getValueMap());
            resultForMs.setMvs(mvs);*/
        arcSmellResultInfo.setDesc(desc);
        infoRepositoryFactory.getRepository(ArcSmellResultInfo.class).saveProfile(arcSmellResultInfo, projectId);
        infoRepositoryFactory.getRepository(ArcSmellResultInfo.class).saveDetail(arcSmellResultInfo);

        return ResponseEntity.ok().build();
    }

    @GetMapping()
    public ResponseEntity query(Long projectId, Integer pageNum, Integer pageSize) {
//        Project p = project.toProject();
        TaskEntity taskEntity = new TaskEntity();
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId(projectId);
        taskEntity.setProjectEntity(projectEntity);
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "createdTime"));
        Page<TaskEntity> result = taskEntityRepository.findAll(Example.of(taskEntity),pageable);
        PageQueryDto pageQueryDto = PageQueryDto.create(pageNum, pageSize).addResult(TaskEntity.entitiesToTasks(result.getContent()), result.getTotalElements());
        return ResponseEntity.ok(pageQueryDto);
    }
}
