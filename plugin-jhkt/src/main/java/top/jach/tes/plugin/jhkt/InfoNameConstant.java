package top.jach.tes.plugin.jhkt;

import top.jach.tes.plugin.tes.code.git.tree.GitTreeAction;

public interface InfoNameConstant {
    String MicroservicesForRepo = "Microservice";
    String MicroservicesForRepos = "MicroserviceForRepos";
    String BugDts = "BugDts";
    String RelationBugAndCommit = "RelationBugAndGitCommit";
    String RelationBugAndMicroservice = "RelationBugAndMicroservice";
    String GoAstPackageForRepo = "GoAstPackage";
    String TargetSystem = "TargetSystem";
    String GitTreeInfoForRepo = GitTreeAction.GitTreeInfoName;

    String MicroserviceCallRelation = "MicroserviceCallRelation";
}
