package top.jach.tes.plugin.tes.code.repo;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.TagOpt;

import java.io.File;
import java.io.IOException;

public class Repo {
    String name;

    String remoteUrl;

    public String getName() {
        return name;
    }

    public Repo setName(String name) {
        this.name = name;
        return this;
    }

    public String getRemoteUrl() {
        return remoteUrl;
    }

    public Repo setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
        return this;
    }

    public interface RepoToGit{
        Git repoToGit(Repo repo) throws IOException;
    }

    public Git cloneAndFetch(File dir) throws IOException, GitAPIException {
        Git git = null;
        try {
            git = Git.open(dir);
        }catch (Exception e){
            System.out.println("没有");
        }
        if(git == null){
            FileUtils.cleanDirectory(dir);
            dir.mkdirs();
            Git.cloneRepository().setDirectory(dir).setURI(remoteUrl).setCloneAllBranches(true).call();
        }
        git.fetch().setTagOpt(TagOpt.FETCH_TAGS).call();
        return git;
    }
}
