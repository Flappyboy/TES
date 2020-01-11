package top.jach.tes.app.jhkt.lijiaqi;

import top.jach.tes.app.dev.DevApp;
import top.jach.tes.app.mock.Environment;
import top.jach.tes.app.mock.InfoTool;
import top.jach.tes.app.mock.InputInfoProfiles;
import top.jach.tes.core.api.domain.action.Action;
import top.jach.tes.core.api.domain.context.Context;
import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.core.api.dto.PageQueryDto;
import top.jach.tes.core.api.exception.ActionExecuteFailedException;
import top.jach.tes.core.impl.domain.relation.PairRelationsInfo;
import top.jach.tes.plugin.jhkt.InfoNameConstant;
import top.jach.tes.plugin.jhkt.analysis.MicroserviceAttrsInfo;
import top.jach.tes.plugin.jhkt.arcsmell.ArcSmellAction;
import top.jach.tes.plugin.jhkt.arcsmell.ArcSmellsInfo;
import top.jach.tes.plugin.jhkt.dts.DtssInfo;
import top.jach.tes.plugin.jhkt.git.commit.GitCommitsForMicroserviceInfo;
import top.jach.tes.plugin.jhkt.microservice.Microservice;
import top.jach.tes.plugin.jhkt.microservice.MicroservicesInfo;
import top.jach.tes.plugin.tes.code.git.commit.GitCommitsInfo;
import top.jach.tes.plugin.tes.code.git.tree.TreesInfo;
import top.jach.tes.plugin.tes.code.go.GoPackagesInfo;
import top.jach.tes.plugin.tes.code.repo.Repo;
import top.jach.tes.plugin.tes.code.repo.ReposInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryAllDataMain extends DevApp {
    public static void main(String[] args) throws ActionExecuteFailedException {
        Context context = Environment.contextFactory.createContext(Environment.defaultProject);

        ReposInfo reposInfo = InfoTool.queryLastInfoByNameAndInfoClass(InfoNameConstant.TargetSystem, ReposInfo.class);

        // 原始的微服务
        MicroservicesInfo microservices = InfoTool.queryLastInfoByNameAndInfoClass(InfoNameConstant.MicroservicesForRepos, MicroservicesInfo.class);
        // 排除部分历史微服务
        microservices = MicroservicesInfo.createInfoByExcludeMicroservice(microservices,
                "x_2b", "x_1b", "x_23", "x_1d/x_6eed",
                "x_39","x_1f","x_27/x_25","c_demo/c_demoa","c_demo/c_demob",
                "x_13/x_ae5", "x_25", "x_21/7103");
        microservices.setName(InfoNameConstant.MicroservicesForReposExcludeSomeHistory);

        // 排除了部分微服务后的微服务调用关系
        PairRelationsInfo pairRelationsInfo = microservices.callRelationsInfoByTopic();
        pairRelationsInfo.setName(InfoNameConstant.MicroserviceExcludeSomeCallRelation);
        InfoTool.saveInputInfos(microservices, pairRelationsInfo);

        // 问题单
        DtssInfo dtssInfo = InfoTool.queryLastInfoByNameAndInfoClass(InfoNameConstant.BugDts, DtssInfo.class);
        // 问题单和微服务的关系
        PairRelationsInfo bugMicroserviceRelations = InfoTool.queryLastInfoByNameAndInfoClass(InfoNameConstant.RelationBugAndMicroservice, PairRelationsInfo.class);

        // 所有的微服务对应的commit信息
        Map<String, GitCommitsForMicroserviceInfo> gitCommitsForMicroserviceInfoMap = new HashMap<>();
        for (Microservice microservice :
                microservices.getMicroservices()) {
            GitCommitsForMicroserviceInfo gitCommitsForMicroserviceInfo = new GitCommitsForMicroserviceInfo();
            gitCommitsForMicroserviceInfo
                    .setReposId(microservices.getReposId())
                    .setMicroserviceName(microservice.getElementName())
                    .setStatisticDiffFiles(null)
                    .setGitCommits(null);
            List<Info> infos = Environment.infoRepositoryFactory.getRepository(GitCommitsForMicroserviceInfo.class)
                    .queryDetailsByInfoAndProjectId(gitCommitsForMicroserviceInfo, Environment.defaultProject.getId(), PageQueryDto.create(1,1).setSortField("createdTime"));
            if(infos.size()>0) {
                gitCommitsForMicroserviceInfoMap.put(microservice.getElementName(), (GitCommitsForMicroserviceInfo)infos.get(0));
            }
        }

        // 每个代码仓下目录信息
        Map<String, TreesInfo> treesInfoMap = new HashMap<>();
        // 每个代码仓下包的信息
        Map<String, GoPackagesInfo> goPackagesInfoMap = new HashMap<>();
        // 每个代码仓下commit信息
        Map<String, GitCommitsInfo> gitCommitsInfoMap = new HashMap<>();
        for (Repo repo:
              reposInfo.getRepos()) {
            TreesInfo treesInfo = new TreesInfo();
            treesInfo.setReposId(reposInfo.getId());
            treesInfo.setRepoName(repo.getName());
            treesInfo.setName(InfoNameConstant.GitTreeInfoForRepo);
            List<Info> infos = Environment.infoRepositoryFactory.getRepository(TreesInfo.class)
                    .queryDetailsByInfoAndProjectId(treesInfo, Environment.defaultProject.getId(), PageQueryDto.create(1,1).setSortField("createdTime"));
            if(infos.size()>0){
                treesInfoMap.put(repo.getName(), (TreesInfo)infos.get(0));
            }

            GoPackagesInfo goPackagesInfo = new GoPackagesInfo();
            goPackagesInfo.setGoPackages(null);
            goPackagesInfo.setReposId(reposInfo.getId());
            goPackagesInfo.setRepoName(repo.getName());
            goPackagesInfo.setName(InfoNameConstant.GoAstPackageForRepo);
            infos = Environment.infoRepositoryFactory.getRepository(GoPackagesInfo.class)
                    .queryDetailsByInfoAndProjectId(goPackagesInfo, Environment.defaultProject.getId(), PageQueryDto.create(1,1).setSortField("createdTime"));
            if(infos.size()>0){
                goPackagesInfoMap.put(repo.getName(), (GoPackagesInfo)infos.get(0));
            }

            GitCommitsInfo gitCommitsInfo = new GitCommitsInfo();
            gitCommitsInfo.setGitCommits(null);
            gitCommitsInfo.setReposId(reposInfo.getId());
            gitCommitsInfo.setRepoName(repo.getName());
            gitCommitsInfo.setName(InfoNameConstant.GitCommitsForRepo);
            infos = Environment.infoRepositoryFactory.getRepository(GitCommitsInfo.class)
                    .queryDetailsByInfoAndProjectId(gitCommitsInfo, Environment.defaultProject.getId(), PageQueryDto.create(1,1).setSortField("createdTime"));
            if(infos.size()>0){
                gitCommitsInfoMap.put(repo.getName(), (GitCommitsInfo)infos.get(0));
            }
        }
        System.out.println(reposInfo);
        System.out.println(microservices);
        System.out.println(gitCommitsForMicroserviceInfoMap);
        System.out.println(dtssInfo);
        System.out.println(bugMicroserviceRelations);
        System.out.println(treesInfoMap);
        System.out.println(goPackagesInfoMap);
        System.out.println(gitCommitsInfoMap);
}
}
