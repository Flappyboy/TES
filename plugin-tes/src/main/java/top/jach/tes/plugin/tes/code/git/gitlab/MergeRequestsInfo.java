package top.jach.tes.plugin.tes.code.git.gitlab;

import org.apache.commons.lang3.StringUtils;
import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.plugin.tes.code.repo.WithRepo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MergeRequestsInfo extends Info implements WithRepo {
    private Long reposId;
    private String repoName;
    private List<MergeRequest> mergeRequestList = new ArrayList<>();

    public static MergeRequestsInfo createInfo(){
        MergeRequestsInfo info = new MergeRequestsInfo();
        info.initBuild();
        return info;
    }

    public Set<String> allMasterSha(){
        Set<String> shas = new HashSet<>();
        for (MergeRequest mr :
                mergeRequestList) {
            if ("master".equals(mr.getTargetBranch())) {
                if (StringUtils.isNoneBlank(mr.getTargetSha())) {
                    shas.add(mr.getTargetSha());
                }
                if (StringUtils.isNoneBlank(mr.getResultSha())) {
                    shas.add(mr.getResultSha());
                }
            }
        }
        return shas;
    }

    public MergeRequestsInfo addMergeRequest(MergeRequest mergeRequest){
        mergeRequestList.add(mergeRequest);
        return this;
    }

    public List<MergeRequest> getMergeRequestList() {
        return mergeRequestList;
    }

    public MergeRequestsInfo setMergeRequestList(List<MergeRequest> mergeRequestList) {
        this.mergeRequestList = mergeRequestList;
        return this;
    }

    @Override
    public Long getReposId() {
        return reposId;
    }

    @Override
    public MergeRequestsInfo setReposId(Long reposId) {
        this.reposId = reposId;
        return this;
    }

    @Override
    public String getRepoName() {
        return repoName;
    }

    @Override
    public MergeRequestsInfo setRepoName(String repoName) {
        this.repoName = repoName;
        return this;
    }
}
