package top.jach.tes.plugin.tes.code.go;

import lombok.Data;
import top.jach.tes.core.domain.info.Info;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class GoPackagesInfo extends Info {

    Long repoId;

    List<GoPackage> goPackages = new ArrayList<>();

    public static GoPackagesInfo createInfo(){
        GoPackagesInfo info = new GoPackagesInfo();
        info.initBuild();
        return info;
    }

    public GoPackagesInfo addPackages(GoPackage... goPackages){
        this.goPackages.addAll(Arrays.asList(goPackages));
        return this;
    }
}
