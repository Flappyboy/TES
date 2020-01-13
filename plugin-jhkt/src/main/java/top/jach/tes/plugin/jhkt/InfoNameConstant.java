package top.jach.tes.plugin.jhkt;

import top.jach.tes.plugin.tes.code.git.tree.GitTreeAction;

public interface InfoNameConstant {
    String MicroservicesForRepo = "Microservice";
    String MicroservicesForRepos = "MicroserviceForRepos";
    String MicroservicesForReposExcludeSomeHistory = "MicroservicesForReposExcludeSomeHistory";
    String BugDts = "BugDts";
    String RelationBugAndCommit = "RelationBugAndGitCommit";
    String RelationBugAndMicroservice = "RelationBugAndMicroservice";
    String GoAstPackageForRepo = "GoAstPackage";
    String TargetSystem = "TargetSystem";
    String GitTreeInfoForRepo = GitTreeAction.GitTreeInfoName;
    String GitCommitsForRepo = "GitCommitsForRepo";
    String GitCommitsForRepoForVersion = "GitCommitsForRepoForVersion";
    String GitCommitsForMicroservice = "GitCommitsForMicroservice";
    String MergeRequestForRepo = "MergeRequestForRepo";

    String MicroserviceCallRelation = "MicroserviceCallRelation";
    String MicroserviceExcludeSomeCallRelation = "MicroserviceExcludeSomeCallRelation";

    String VersionsForMaster = "VersionsForMaster";
    String VersionsForRelease = "VersionsForRelease";
}
