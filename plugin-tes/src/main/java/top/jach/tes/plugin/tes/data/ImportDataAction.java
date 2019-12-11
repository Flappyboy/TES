package top.jach.tes.plugin.tes.data;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import top.jach.tes.core.api.domain.action.Action;
import top.jach.tes.core.api.domain.action.InputInfos;
import top.jach.tes.core.api.domain.action.OutputInfos;
import top.jach.tes.core.api.domain.context.Context;
import top.jach.tes.core.api.domain.action.DefaultInputInfos;
import top.jach.tes.core.api.domain.action.DefaultOutputInfos;
import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.core.api.domain.meta.Meta;
import top.jach.tes.core.api.exception.ActionExecuteFailedException;
import top.jach.tes.core.impl.domain.action.SaveInfoAction;
import top.jach.tes.core.impl.domain.info.value.FileInfo;
import top.jach.tes.core.impl.domain.info.value.ValueInfo;
import top.jach.tes.core.impl.domain.meta.InfoField;
import top.jach.tes.plugin.tes.code.repo.ReposInfo;
import top.jach.tes.plugin.tes.code.repo.WithRepo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImportDataAction implements Action {
    public static final String DATA_FILE = "data_file";
    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getDesc() {
        return null;
    }

    @Override
    public Meta getInputMeta() {
        return () -> Arrays.asList(
                InfoField.createField(DATA_FILE).setInfoClass(FileInfo.class));
    }

    @Override
    public OutputInfos execute(InputInfos inputInfos, Context context) throws ActionExecuteFailedException {
        File dataFile = ValueInfo.getValueFromInputInfos(inputInfos, DATA_FILE, FileInfo.class);
        if(dataFile == null || !dataFile.exists()){
            throw new ActionExecuteFailedException("data file must be exist!");
        }
        try {
            DefaultOutputInfos result = new DefaultOutputInfos();
            List<Info> infos = new ArrayList<>();
            JSONArray dataArray = JSONObject.parseArray(FileUtils.readFileToString(dataFile, "utf8"));
            ReposInfo reposInfo = null;
            for (Object object :
                    dataArray) {
                JSONObject data = (JSONObject) object;
                Class infoClass = Class.forName(data.getString("infoClass"));
                Info info = (Info) data.toJavaObject(infoClass);
                info.initBuild();
                if(info instanceof ReposInfo){
                    reposInfo = (ReposInfo) info;
                    reposInfo.setId(1001l);
                }
                infos.add(info);
            }
            InputInfos tmp = new DefaultInputInfos();
            for (Info info :
                    infos) {
                if (info instanceof WithRepo){
                    ((WithRepo) info).setReposId(reposInfo.getId());
                }
                result.addInfo(info);
                tmp.put(String.valueOf(tmp.size()), info);
            }
            SaveInfoAction saveInfoAction = new SaveInfoAction();
            saveInfoAction.execute(tmp, context);
            return result;
        } catch (IOException e) {
            throw new ActionExecuteFailedException(e);
        } catch (ClassNotFoundException e) {
            throw new ActionExecuteFailedException(e);
        }
    }
}
