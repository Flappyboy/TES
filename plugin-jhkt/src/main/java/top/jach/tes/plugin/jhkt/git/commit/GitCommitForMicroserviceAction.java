package top.jach.tes.plugin.jhkt.git.commit;

import top.jach.tes.core.api.domain.action.Action;
import top.jach.tes.core.api.domain.action.DefaultOutputInfos;
import top.jach.tes.core.api.domain.action.InputInfos;
import top.jach.tes.core.api.domain.action.OutputInfos;
import top.jach.tes.core.api.domain.context.Context;
import top.jach.tes.core.api.domain.meta.Meta;
import top.jach.tes.core.api.exception.ActionExecuteFailedException;
import top.jach.tes.core.impl.domain.info.value.FileInfo;
import top.jach.tes.core.impl.domain.meta.InfoField;
import top.jach.tes.core.impl.domain.meta.LongField;
import top.jach.tes.core.impl.domain.meta.StringField;
import top.jach.tes.plugin.jhkt.microservice.Microservice;
import top.jach.tes.plugin.jhkt.microservice.MicroservicesInfo;
import top.jach.tes.plugin.tes.code.git.commit.GitCommitsInfo;

import java.util.Arrays;
import java.util.List;

public class GitCommitForMicroserviceAction implements Action {
    public static final String MICROSERVICES_INFO = "MICROSERVICES_INFO";
    public static final String GIT_COMMITS_INFO = "GIT_COMMITS_INFO";
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
                InfoField.createField(MICROSERVICES_INFO).setInfoClass(MicroservicesInfo.class),
                InfoField.createField(GIT_COMMITS_INFO).setInfoClass(GitCommitsInfo.class)
        );
    }

    @Override
    public OutputInfos execute(InputInfos inputInfos, Context context) throws ActionExecuteFailedException {
        MicroservicesInfo microservices = inputInfos.getInfo(MICROSERVICES_INFO, MicroservicesInfo.class);
        GitCommitsInfo gitCommitsInfo = inputInfos.getInfo(GIT_COMMITS_INFO, GitCommitsInfo.class);
        List<GitCommitsForMicroserviceInfo> infoList = GitCommitsForMicroserviceInfo.createInfoFromMicroservicesInfo(microservices, gitCommitsInfo);
        return new DefaultOutputInfos().addInfoReadyToSave(infoList.toArray(new GitCommitsForMicroserviceInfo[infoList.size()]));
    }
}
