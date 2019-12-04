package top.jach.tes.dev.app.jach.code.go;

import lombok.Data;
import top.jach.tes.core.domain.info.Info;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class PackagesInfo extends Info {

    String repoId;

    List<Package> packages = new ArrayList<>();

    public static PackagesInfo createInfo(){
        PackagesInfo info = new PackagesInfo();
        info.initBuild();
        return info;
    }

    public PackagesInfo addPackages(Package... packages){
        this.packages.addAll(Arrays.asList(packages));
        return this;
    }
}
