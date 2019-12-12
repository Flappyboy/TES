package top.jach.tes.plugin.tes.code.repo;

public interface WithRepo {
    String getRepoName();
    WithRepo setRepoName(String repoName);
    Long getReposId();
    WithRepo setReposId(Long reposId);

    default boolean repoEquals(WithRepo withRepo){
        try {
            if (getReposId().equals(withRepo.getReposId()) && getRepoName().equals(getRepoName())) {
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

    default void checkRepoEquals(WithRepo withRepo){
        if (!repoEquals(withRepo)){
            throw new RuntimeException(String.format("%s:{reposId: %d, repoName: %s},  %s:{reposId: %d, repoName: %s} 应该属于同一repo下！",
                    getClass().getName(),getReposId(), getRepoName(),
                    withRepo.getClass().getName(), withRepo.getReposId(), withRepo.getRepoName()));
        }
    }
}
