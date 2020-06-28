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
import top.jach.tes.core.api.domain.info.InfoProfile;
import top.jach.tes.core.api.domain.meta.Meta;
import top.jach.tes.core.api.exception.ActionExecuteFailedException;
import top.jach.tes.core.impl.domain.action.SaveInfoAction;
import top.jach.tes.core.impl.domain.info.InfoOfInfo;
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
//json对象转换为java数据结构的逻辑
public class ImportDataAction implements Action {
    public static final String ImportDir = "ImportDir";
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
                InfoField.createField(ImportDir).setInfoClass(FileInfo.class));
    }

    @Override
    public OutputInfos execute(InputInfos inputInfos, Context context) throws ActionExecuteFailedException {
        File importDir = ValueInfo.getValueFromInputInfos(inputInfos, ImportDir, FileInfo.class);
        if(importDir == null || !importDir.exists() || !importDir.isDirectory()){
            throw new ActionExecuteFailedException("import dir must be exist and must be directory!");
        }
        SaveInfoAction saveInfoAction = new SaveInfoAction();

        DefaultOutputInfos result = new DefaultOutputInfos();
        File dataDir = DataDir.lastDataDir(importDir);
        context.Logger().info("开始导入 {} ", dataDir.getAbsolutePath());
        if(dataDir.isDirectory()){
            for (File dataFile:
                    dataDir.listFiles()) {
                if(!dataFile.getName().endsWith(".json")){
                    continue;
                }
                try {
                    //读取文件，将文件序列化成一个json对象
                    JSONObject data = JSONObject.parseObject(FileUtils.readFileToString(dataFile, "utf8"));
                    Class infoClass = Class.forName(data.getString("infoClass"));
                    Info info = (Info) data.toJavaObject(infoClass);
                    context.InfoRepositoryFactory().getRepository(info.getInfoClass()).deleteByInfoId(info.getId());
                    result.addInfo(InfoProfile.createFromInfo(info));
                    InputInfos tmp = new DefaultInputInfos();
                    tmp.put(String.valueOf(tmp.size()), info);
                    saveInfoAction.execute(tmp, context);
                } catch (IOException e) {
                    throw new ActionExecuteFailedException(e);
                } catch (ClassNotFoundException e) {
                    throw new ActionExecuteFailedException(e);
                }
            }
        }
        context.Logger().info("导入成功 {} ", dataDir.getAbsolutePath());
        InfoOfInfo<Info> infoOfInfo = InfoOfInfo.createInfoOfInfo(result.getInfoList());
        infoOfInfo.setName("TES_IMPORT_DATA");
        InputInfos tmp = new DefaultInputInfos();
        tmp.put(String.valueOf(tmp.size()), infoOfInfo);
        saveInfoAction.execute(tmp, context);

        return result;
    }
}
