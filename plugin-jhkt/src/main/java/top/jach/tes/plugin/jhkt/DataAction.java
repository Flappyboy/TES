package top.jach.tes.plugin.jhkt;

import top.jach.tes.core.api.domain.action.Action;
import top.jach.tes.core.api.domain.action.InputInfos;
import top.jach.tes.core.api.domain.action.OutputInfos;
import top.jach.tes.core.api.domain.context.Context;
import top.jach.tes.core.api.domain.action.DefaultInputInfos;
import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.core.api.domain.meta.Meta;
import top.jach.tes.core.api.exception.ActionExecuteFailedException;
import top.jach.tes.core.impl.domain.action.SaveInfoAction;
import top.jach.tes.core.impl.domain.info.value.FileInfo;
import top.jach.tes.core.impl.domain.relation.PairRelationsInfo;
import top.jach.tes.plugin.jhkt.git.commit.GitCommitForMicroserviceAction;
import top.jach.tes.plugin.jhkt.microservice.MicroservicesInfo;
import top.jach.tes.plugin.tes.code.git.commit.GitCommitsInfo;
import top.jach.tes.plugin.tes.code.repo.Repo;
import top.jach.tes.plugin.tes.code.repo.ReposInfo;
import top.jach.tes.plugin.tes.data.ImportDataAction;

import java.util.List;

public class DataAction implements Action {
    public static final String DATAS_DIR = "data_file";
    public static final Long DefaultReposId = 10001l;

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
        return null;
    }

    @Override
    public OutputInfos execute(InputInfos inputInfos, Context context) throws ActionExecuteFailedException {
        // 导入数据
        List<Info> infos = new ImportDataAction().execute(new DefaultInputInfos().
                putInfo(ImportDataAction.ImportDir, inputInfos.getInfo(DATAS_DIR, FileInfo.class)), context).getInfoList();

        ReposInfo reposInfo = findOne(infos, ReposInfo.class);

        // 通过MicroserviceForRepo 统计 整个系统的 MicroservicesForRepos
        MicroservicesInfo microservices = MicroservicesInfo.createInfo(infos.toArray(new Info[infos.size()])).setReposId(reposInfo.getId());
        microservices.setName(InfoNameConstant.MicroservicesForRepos);
        new SaveInfoAction().execute(new DefaultInputInfos().putInfo("1", microservices),context);

        // 根据Microservice的topic，统计出CallRelation
        PairRelationsInfo pairRelationsInfo = microservices.callRelationsInfoByTopic();
        pairRelationsInfo.setName(InfoNameConstant.MicroserviceCallRelation);
        new SaveInfoAction().execute(new DefaultInputInfos().putInfo("1", pairRelationsInfo),context);

        // 根据GitCommitsInfo 和 MicroservicesForRepos 统计 各Microservice下的Commits
        for (Repo repo :
                reposInfo.getRepos()) {
            GitCommitsInfo gitCommitsInfo = null;
            for (Info info :
                    infos) {
                if (info instanceof GitCommitsInfo && ((GitCommitsInfo) info).getRepoName().equals(repo.getName())) {
                    gitCommitsInfo = (GitCommitsInfo) info;
                    break;
                }
            }
            MicroservicesInfo microservicesInfo = null;
            for (Info info :
                    infos) {
                if (info instanceof MicroservicesInfo && ((MicroservicesInfo) info).getRepoName().equals(repo.getName())) {
                    microservicesInfo = (MicroservicesInfo) info;
                    break;
                }
            }
            if(microservicesInfo == null || gitCommitsInfo == null){
                continue;
            }
            InputInfos inputInfosForGitCommitsForMicroservice = new DefaultInputInfos()
                    .putInfo(GitCommitForMicroserviceAction.MICROSERVICES_INFO, microservicesInfo)
                    .putInfo(GitCommitForMicroserviceAction.GIT_COMMITS_INFO, gitCommitsInfo);
            OutputInfos outputInfos = new GitCommitForMicroserviceAction().execute(inputInfosForGitCommitsForMicroservice, context);
            for (Info info :
                    outputInfos.getInfoList()) {
                new SaveInfoAction().execute(new DefaultInputInfos().putInfo("1", info),context);
            }
        }


        // 根据Microservice之间的CallRelation，计算依赖相关的架构异味

        return null;
    }

    private <I> I findOne(List objects, Class<I> clazz){
        for (Object o :
                objects) {
            if (clazz.isAssignableFrom(o.getClass())) {
                return (I) o;
            }
        }
        return null;
    }
}
