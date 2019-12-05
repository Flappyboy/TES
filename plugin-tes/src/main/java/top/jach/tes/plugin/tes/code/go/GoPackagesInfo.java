package top.jach.tes.plugin.tes.code.go;

import lombok.Data;
import top.jach.tes.core.domain.info.Info;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class GoPackagesInfo extends Info {

    Long reposId;

    String repoName;

    List<GoPackage> goPackages = new ArrayList<>();

    String version;

    public static GoPackagesInfo createInfo(){
        GoPackagesInfo info = new GoPackagesInfo();
        info.initBuild();
        return info;
    }

    public GoPackagesInfo addPackages(GoPackage... goPackages){
        this.goPackages.addAll(Arrays.asList(goPackages));
        return this;
    }

    public GoPackagesInfo setReposId(Long reposId) {
        this.reposId = reposId;
        return this;
    }

    public GoPackagesInfo setRepoName(String repoName) {
        this.repoName = repoName;
        return this;
    }

    public GoPackagesInfo setVersion(String version) {
        this.version = version;
        return this;
    }
}
