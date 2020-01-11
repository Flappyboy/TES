package top.jach.tes.plugin.tes.code.git.gitlab;

import lombok.Data;

import java.util.List;

@Data
public class MergeRequest {
    String reposId;
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
}
