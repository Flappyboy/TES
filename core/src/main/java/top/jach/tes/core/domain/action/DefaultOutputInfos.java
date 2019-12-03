package top.jach.tes.core.domain.action;

import top.jach.tes.core.domain.info.Info;

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
        outputInfoList.add(new DefaultOutputInfo(info));
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
        return null;
    }
}
