package top.jach.tes.plugin.jhkt;

import org.apache.commons.lang3.StringUtils;
import top.jach.tes.core.api.domain.action.Action;
import top.jach.tes.core.api.domain.action.InputInfos;
import top.jach.tes.core.api.domain.action.OutputInfos;
import top.jach.tes.core.api.domain.context.Context;
import top.jach.tes.core.api.domain.action.DefaultInputInfos;
import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.core.api.domain.info.InfoProfile;
import top.jach.tes.core.api.domain.meta.Meta;
import top.jach.tes.core.api.dto.PageQueryDto;
import top.jach.tes.core.api.exception.ActionExecuteFailedException;
import top.jach.tes.core.impl.domain.action.SaveInfoAction;
import top.jach.tes.core.impl.domain.info.value.FileInfo;
import top.jach.tes.core.impl.domain.relation.PairRelationsInfo;
import top.jach.tes.plugin.jhkt.git.commit.GitCommitForMicroserviceAction;
import top.jach.tes.plugin.jhkt.git.commit.GitCommitsForMicroserviceInfo;
import top.jach.tes.plugin.jhkt.microservice.Microservice;
import top.jach.tes.plugin.jhkt.microservice.MicroservicesInfo;
import top.jach.tes.plugin.tes.code.git.commit.GitCommitsInfo;
import top.jach.tes.plugin.tes.code.git.commit.GitCommitsInfoMongoRepository;
import top.jach.tes.plugin.tes.code.git.tree.TreesInfo;
import top.jach.tes.plugin.tes.code.git.version.Version;
import top.jach.tes.plugin.tes.code.git.version.VersionsInfo;
import top.jach.tes.plugin.tes.code.go.GoPackagesInfo;
import top.jach.tes.plugin.tes.code.repo.Repo;
import top.jach.tes.plugin.tes.code.repo.ReposInfo;
import top.jach.tes.plugin.tes.data.ImportDataAction;

import java.util.List;
import java.util.Map;

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
        new ImportDataAction().execute(new DefaultInputInfos().
                putInfo(ImportDataAction.ImportDir, inputInfos.getInfo(DATAS_DIR, FileInfo.class)), context);

        /*ReposInfo reposInfo = outputInfos.getFirstFromProfileByInfoClassAndName(
                ReposInfo.class, InfoNameConstant.TargetSystem, context.InfoRepositoryFactory());*/

        ReposInfo reposInfo = queryLastInfo(context, InfoNameConstant.TargetSystem, ReposInfo.class);

/*        VersionsInfo versionsInfoForRelease = outputInfos.getFirstFromProfileByInfoClassAndName(
                VersionsInfo.class, InfoNameConstant.VersionsForRelease, context.InfoRepositoryFactory());*/
        VersionsInfo versionsInfoForRelease = queryLastInfo(context, InfoNameConstant.VersionsForRelease, VersionsInfo.class);

/*        VersionsInfo versionsInfoForMaster = outputInfos.getFirstFromProfileByInfoClassAndName(
                VersionsInfo.class, InfoNameConstant.VersionsForMaster, context.InfoRepositoryFactory());*/
        VersionsInfo versionsInfoForMaster = queryLastInfo(context, InfoNameConstant.VersionsForMaster, VersionsInfo.class);

        // 当前版本微服务数据存在错，进行纠错处理
        for (Version version :
                versionsInfoForRelease.getVersions()) {
            // 查询当前版本的微服务
            MicroservicesInfo microservices = queryLastMicroservices(context, reposInfo.getId(), null, version);
            if (microservices == null || microservices.getMicroservices().size()==0) {
                continue;
            }
            Microservice tm = microservices.getMicroservices().get(0);
            if (StringUtils.isNoneBlank(tm.getRepoName())) {
                continue;
            }
            for (Microservice m :
                    microservices.getMicroservices()) {
                String mName = m.getElementName();
                String repoName = m.getPath();
                m.setRepoName(repoName);
                int i = mName.indexOf("/");
                if(i<0){
                    m.setPath("");
                }else{
                    m.setPath(mName.substring(i+1));
                }
            }
            saveInfo(context, microservices);
        }

        for (Repo repo :
                reposInfo.getRepos()) {
            GitCommitsInfo gitCommitsInfo = queryGitCommitsInfo(context, repo.getName(), reposInfo.getId());
            try {
                List<GitCommitsInfo> gitCommitsInfoListForVersion = GitCommitsInfo.createInfosForVersions(gitCommitsInfo, versionsInfoForRelease.shasForRepo(repo.getName()));
                for (int i = 0; i < gitCommitsInfoListForVersion.size(); i++) {
                    Version version = versionsInfoForRelease.getVersions().get(i);
                    GitCommitsInfo gitCommitsInfoForRepoVersion = gitCommitsInfoListForVersion.get(i);
                    gitCommitsInfoForRepoVersion.setName(InfoNameConstant.GitCommitsForRepoForVersion);
                    gitCommitsInfoForRepoVersion.setRevision(version.getVersionName());
                    saveInfo(context, gitCommitsInfoForRepoVersion);

                    // 按微服务划分gitcommits
                    MicroservicesInfo microservices = queryLastMicroservices(context, reposInfo.getId(), null, version);
                    List<Microservice> ms = microservices.microservicesForRepo(repo.getName());
                    for (Microservice m :
                            ms) {
                        GitCommitsForMicroserviceInfo gitCommitsForMicroserviceInfo = GitCommitsForMicroserviceInfo.createInfoFromGitCommitsInfo(microservices, m.getElementName(), gitCommitsInfoForRepoVersion);
                        gitCommitsForMicroserviceInfo.setName(InfoNameConstant.GitCommitsForMicroservice);
                        gitCommitsForMicroserviceInfo.setVersion(version.getVersionName());
                        saveInfo(context, gitCommitsForMicroserviceInfo);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (Version version :
                versionsInfoForRelease.getVersions()) {
            MicroservicesInfo microservices = queryLastMicroservices(context, reposInfo.getId(), null, version);
            if (microservices == null){
                continue;
            }
            // 根据Microservice的topic，统计出CallRelation
            PairRelationsInfo msCallRelations = microservices.callRelationsInfoByTopic();
            saveInfo(context, msCallRelations.setName(InfoNameConstant.MicroserviceCallRelation));
            msCallRelations = null;
        }


/*        for (Version version :
                versionsInfoForRelease.getVersions()) {
            Map<String, String> map = version.getRepoShaMap();
            for (Map.Entry<String, String> entry:
                    map.entrySet()) {
                String repoName = entry.getKey();
                GitCommitsInfo gitCommitsInfo = queryGitCommitsInfo(context, repoName, version.getReposId());
                GitCommitsInfo.createInfosForVersions(gitCommitsInfo, );
            }


            // 查询当前版本的微服务
            MicroservicesInfo microservices = new MicroservicesInfo();
            microservices.setName(InfoNameConstant.MicroservicesForRepos);
            microservices.setVersion(version.getVersionName());
            microservices.setMicroservices(null);
//            microservices = getValidLastInfo(context, outputInfos, microservices);
            microservices = queryLastInfo(context, microservices, MicroservicesInfo.class);
            if (microservices == null){
                continue;
            }

            // 根据Microservice的topic，统计出CallRelation
            PairRelationsInfo msCallRelations = microservices.callRelationsInfoByTopic();
            saveInfo(context, msCallRelations.setName(InfoNameConstant.MicroserviceCallRelation));
            msCallRelations = null;

            for (Microservice microservice :
                    microservices.getMicroservices()) {
                String repoName = microservice.getRepoName();
                GitCommitsInfo gitCommitsInfo = queryGitCommitsInfo(context, repoName, microservices.getReposId());

                GitCommitsForMicroserviceInfo gitCommitsForMicroserviceInfo = GitCommitsForMicroserviceInfo.createInfoFromGitCommitsInfo(microservices, microservice.getElementName(), gitCommitsInfo);
                gitCommitsForMicroserviceInfo.setName(InfoNameConstant.GitCommitsForMicroservice);
                saveInfo(context, gitCommitsForMicroserviceInfo);
            }
        }*/
        return null;
    }

    public static TreesInfo queryLastTreesInfo(Context context, Long reposId, String repoName, Version version) {
        TreesInfo info = new TreesInfo();
        info.setReposId(reposId);
        info.setRepoName(repoName);
        info.setName(InfoNameConstant.GitTreeInfoForRepo);
        info.setSha(version.getSha());
        info.setTrees(null);
        info = queryLastInfo(context, info, TreesInfo.class);
        return info;
    }
    public static GoPackagesInfo queryLastGoPackagesInfo(Context context, Long reposId, String repoName, Version version) {
        GoPackagesInfo info = new GoPackagesInfo();
        info.setReposId(reposId);
        info.setRepoName(repoName);
        info.setName(InfoNameConstant.GoAstPackageForRepo);
        info.setVersion(version.getSha());
        info.setGoPackages(null);
        info = queryLastInfo(context, info, GoPackagesInfo.class);
        return info;
    }

    public static MicroservicesInfo queryLastMicroservices(Context context,Long reposId, String repoName, Version version) {
        MicroservicesInfo info = new MicroservicesInfo();
        info.setReposId(reposId);
        info.setRepoName(repoName);
        info.setName(InfoNameConstant.MicroservicesForRepos);
        info.setVersion(version.getVersionName());
        info.setMicroservices(null);
        info = queryLastInfo(context, info, MicroservicesInfo.class);
        return info;
    }

    public static GitCommitsInfo queryLastGitCommitsInfoForVersion(Context context, Long reposId, String repoName, Version version) {
        GitCommitsInfo info = new GitCommitsInfo();
        info.setName(InfoNameConstant.GitCommitsForRepoForVersion);
        info.setReposId(reposId);
        info.setRepoName(repoName);
        info.setRevision(version.getVersionName());
        info.setGitCommits(null);
        info = queryLastInfo(context, info, GitCommitsInfo.class);
        return info;
    }

    public static GitCommitsForMicroserviceInfo queryLastGitCommitsForMicroserviceInfo(Context context, Long reposId, String microserviceName, Version version){
        GitCommitsForMicroserviceInfo info = new GitCommitsForMicroserviceInfo();
        info.setName(InfoNameConstant.GitCommitsForMicroservice);
        info.setReposId(reposId);
        info.setMicroserviceName(microserviceName);
        info.setVersion(version.getVersionName());
        info.setGitCommits(null);
        info = queryLastInfo(context, info, GitCommitsForMicroserviceInfo.class);
        return info;
    }

    public static GitCommitsInfo queryGitCommitsInfo(Context context, String repoName, Long reposId) {
        GitCommitsInfo gitCommitsInfo = new GitCommitsInfo();
        gitCommitsInfo.setName(InfoNameConstant.GitCommitsForRepo);
        gitCommitsInfo.setReposId(reposId).setRepoName(repoName);
        gitCommitsInfo.setGitCommits(null);
        gitCommitsInfo = queryLastInfo(context, gitCommitsInfo, GitCommitsInfo.class);
        return gitCommitsInfo;
    }

    public static void saveInfo(Context context, List infos) {
        for (Object info :
                infos) {
            saveInfo(context, (Info) info);
        }
    }

    public static void saveInfo(Context context, Info info) {
        new SaveInfoAction().execute(new DefaultInputInfos().putInfo("1", info), context);
    }

    private MicroservicesInfo getValidLastInfo(Context context, OutputInfos outputInfos, MicroservicesInfo info) {
        info = queryLastProfileInfo(context, info, MicroservicesInfo.class);
        if (info == null){
            return null;
        }
        if (outputInfos.contains(info.getId())){
            info = queryLastInfo(context, info, MicroservicesInfo.class);
        }else{
            info = null;
        }
        return info;
    }

    public static <I extends Info> I queryLastProfileInfo(Context context, Info info, Class<I> infoClass) {
        List<Info> infos = context.InfoRepositoryFactory().getRepository(infoClass).queryProfileByInfoAndProjectId(
                info, context.currentProject().getId(), PageQueryDto.LastInfoQueryDto()
        ).getResult();
        if (infos != null && infos.size()>0){
            return (I) infos.get(0);
        }
        return null;
    }

    public static <I extends Info> I queryLastInfo(Context context, String infoName, Class<I> infoClass) {
        InfoProfile info = new InfoProfile();
        info.setName(infoName);
        info.setInfoClass(infoClass);
        return queryLastInfo(context, info, infoClass);
    }

    public static <I extends Info> I queryLastInfo(Context context, Info info, Class<I> infoClass) {
        List<Info> infos = context.InfoRepositoryFactory().getRepository(infoClass).queryDetailsByInfoAndProjectId(
                info, context.currentProject().getId(), PageQueryDto.LastInfoQueryDto()
        );
        if (infos != null && infos.size()>0){
            return (I) infos.get(0);
        }
        return null;
    }
}
