package top.jach.tes.plugin.tes.code.git.gitlab;

import lombok.Data;

import java.util.List;
import java.util.Set;
/*记录每次提交的一些额外的信息，一定要在代码托管平台操作才有的信息*/
@Data
public class MergeRequest {
    Long reposId;
    String repoName;
    String title;
    String description;
    String authorEmail;
    String assigneeEmail;
    List<String> approvedByEmails;
    String mergeStatus;
    String state;
    Long createdTime;
    Long updatedTime;
    String targetBranch;
    String sourceSha;
    String targetSha;
    String resultSha;
    Set<String> bugName;
}
