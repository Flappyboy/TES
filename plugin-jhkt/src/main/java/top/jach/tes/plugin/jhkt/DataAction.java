package top.jach.tes.plugin.jhkt;

import top.jach.tes.core.api.domain.action.Action;
import top.jach.tes.core.api.domain.action.InputInfos;
import top.jach.tes.core.api.domain.action.OutputInfos;
import top.jach.tes.core.api.domain.context.Context;
import top.jach.tes.core.api.domain.action.DefaultInputInfos;
import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.core.api.domain.meta.Meta;
import top.jach.tes.core.api.dto.PageQueryDto;
import top.jach.tes.core.api.exception.ActionExecuteFailedException;
import top.jach.tes.core.impl.domain.action.SaveInfoAction;
import top.jach.tes.core.impl.domain.info.value.FileInfo;
import top.jach.tes.core.impl.domain.relation.PairRelationsInfo;
import top.jach.tes.plugin.jhkt.microservice.MicroservicesInfo;
import top.jach.tes.plugin.tes.code.git.version.Version;
import top.jach.tes.plugin.tes.code.git.version.VersionsInfo;
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
        OutputInfos outputInfos = new ImportDataAction().execute(new DefaultInputInfos().
                putInfo(ImportDataAction.ImportDir, inputInfos.getInfo(DATAS_DIR, FileInfo.class)), context);

        ReposInfo reposInfo = outputInfos.getFirstFromProfileByInfoClassAndName(
                ReposInfo.class, InfoNameConstant.TargetSystem, context.InfoRepositoryFactory());

        VersionsInfo versionsInfoForRelease = outputInfos.getFirstFromProfileByInfoClassAndName(
                VersionsInfo.class, InfoNameConstant.VersionsForRelease, context.InfoRepositoryFactory());

        VersionsInfo versionsInfoForMaster = outputInfos.getFirstFromProfileByInfoClassAndName(
                VersionsInfo.class, InfoNameConstant.VersionsForMaster, context.InfoRepositoryFactory());

        for (Version version :
                versionsInfoForRelease.getVersions()) {
            // 查询当前版本的微服务
            MicroservicesInfo microservices = new MicroservicesInfo();
            microservices.setName(InfoNameConstant.MicroservicesForRepos);
            microservices.setVersion(version.getVersionName());
            microservices = getValidLastInfo(context, outputInfos, microservices);
            if (microservices == null){
                continue;
            }

            // 根据Microservice的topic，统计出CallRelation
            PairRelationsInfo msCallRelations = microservices.callRelationsInfoByTopic();
            saveInfo(context, msCallRelations.setName(InfoNameConstant.MicroserviceCallRelation));
            msCallRelations = null;

            List<Info> infoProfiles = outputInfos.getInfoList();

        }

        // 通过MicroserviceForRepo 统计 整个系统的 MicroservicesForRepos
        /*MicroservicesInfo microservices = MicroservicesInfo.createInfo(infos.toArray(new Info[infos.size()])).setReposId(reposInfo.getId());
        microservices.setName(InfoNameConstant.MicroservicesForRepos);
        new SaveInfoAction().execute(new DefaultInputInfos().putInfo("1", microservices),context);*/

        // 根据GitCommitsInfo 和 MicroservicesForRepos 统计 各Microservice下的Commits
        /*for (Repo repo :
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
                saveInfo(context, info);
            }
        }
*/

        // 根据Microservice之间的CallRelation，计算依赖相关的架构异味

        return null;
    }

    private void saveInfo(Context context, Info info) {
        new SaveInfoAction().execute(new DefaultInputInfos().putInfo("1", info), context);
    }

    private MicroservicesInfo getValidLastInfo(Context context, OutputInfos outputInfos, MicroservicesInfo info) {
        info = queryLastProfileInfo(context, info, MicroservicesInfo.class);
        if (outputInfos.contains(info.getId())){
            info = queryLastInfo(context, info, MicroservicesInfo.class);
        }else{
            info = null;
        }
        return info;
    }

    private <I extends Info> I queryLastProfileInfo(Context context, Info info, Class<I> infoClass) {
        List<Info> infos = context.InfoRepositoryFactory().getRepository(infoClass).queryProfileByInfoAndProjectId(
                info, context.currentProject().getId(), PageQueryDto.LastInfoQueryDto()
        ).getResult();
        if (infos != null && infos.size()>0){
            return (I) infos.get(0);
        }
        return null;
    }

    private <I extends Info> I queryLastInfo(Context context, Info info, Class<I> infoClass) {
        List<Info> infos = context.InfoRepositoryFactory().getRepository(infoClass).queryDetailsByInfoAndProjectId(
                info, context.currentProject().getId(), PageQueryDto.LastInfoQueryDto()
        );
        if (infos != null && infos.size()>0){
            return (I) infos.get(0);
        }
        return null;
    }
}
