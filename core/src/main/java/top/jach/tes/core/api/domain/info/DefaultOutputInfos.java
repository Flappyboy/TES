package top.jach.tes.core.api.domain.info;

import top.jach.tes.core.api.domain.action.OutputInfo;
import top.jach.tes.core.api.domain.action.OutputInfos;

import java.util.ArrayList;
import java.util.List;

public class DefaultOutputInfos implements OutputInfos {
    List<OutputInfo> outputInfoList = new ArrayList<>();

    public static DefaultOutputInfos WithSaveFlag(Info... infos){
        DefaultOutputInfos defaultOutputInfos = new DefaultOutputInfos().addInfoReadyToSave(infos);
        return defaultOutputInfos;
    }

    public DefaultOutputInfos addInfo(Info info, String... flags){
        DefaultOutputInfo defaultOutputInfo = new DefaultOutputInfo(info);
        for (String flag :
                flags) {
            defaultOutputInfo.addFlag(flag);
        }
        addOutputInfo(defaultOutputInfo);
        return this;
    }

    public DefaultOutputInfos addInfoReadyToSave(Info... infos){
        for (Info info:
                infos) {
            addInfo(info, OutputInfo.Flag.SAVE.name());
        }
        return this;
    }

    public DefaultOutputInfos addOutputInfo(OutputInfo... outputInfos){
        for (OutputInfo outputInfo:
                outputInfos) {
            outputInfoList.add(outputInfo);
        }
        return this;
    }


    @Override
    public List<OutputInfo> getOutputInfoList() {
        return outputInfoList;
    }
}
