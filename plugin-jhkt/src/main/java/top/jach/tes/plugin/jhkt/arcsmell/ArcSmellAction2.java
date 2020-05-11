package top.jach.tes.plugin.jhkt.arcsmell;

import top.jach.tes.core.api.domain.action.Action;
import top.jach.tes.core.api.domain.action.DefaultOutputInfos;
import top.jach.tes.core.api.domain.action.InputInfos;
import top.jach.tes.core.api.domain.action.OutputInfos;
import top.jach.tes.core.api.domain.context.Context;
import top.jach.tes.core.api.domain.meta.Meta;
import top.jach.tes.core.api.exception.ActionExecuteFailedException;
import top.jach.tes.core.impl.domain.element.ElementsInfo;
import top.jach.tes.core.impl.domain.element.ElementsValue;
import top.jach.tes.core.impl.domain.info.value.FileInfo;
import top.jach.tes.core.impl.domain.info.value.ValueInfo;
import top.jach.tes.core.impl.domain.meta.InfoField;
import top.jach.tes.core.impl.domain.relation.PairRelationsInfo;
import top.jach.tes.plugin.jhkt.arcsmell.cyclic.CyclicAction;
import top.jach.tes.plugin.jhkt.arcsmell.hublink.HublinkAction;
import top.jach.tes.plugin.jhkt.arcsmell.mv.MvAction;
import top.jach.tes.plugin.jhkt.microservice.MicroservicesInfo;
import top.jach.tes.plugin.tes.code.git.commit.GitCommitsInfo;
import top.jach.tes.plugin.tes.utils.FileCompress;

import java.io.File;
import java.util.Arrays;
import java.util.Map;

public class ArcSmellAction2 implements Action {
    public static final String DATA__FILE_INFO = "datafile";
    @Override
    public String getName() {
        return "ImportDataAndDetectArcSmell";
    }

    @Override
    public String getDesc() {
        return "导入数据并检测架构异味";
    }

    @Override
    public Meta getInputMeta() {
        return () -> Arrays.asList(
                InfoField.createField(DATA__FILE_INFO).setInfoClass(FileInfo.class)
        );
    }

    @Override
    public OutputInfos execute(InputInfos inputInfos, Context context) throws ActionExecuteFailedException {
        File dataFile = ValueInfo.getValueFromInputInfos(inputInfos, DATA__FILE_INFO, FileInfo.class);
        File unzip = context.TempSpace().getTmpDir();
        FileCompress.unZip(dataFile, unzip.getAbsolutePath());

        return DefaultOutputInfos.WithSaveFlag(null);
    }
}
