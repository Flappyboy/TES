package top.jach.tes.plugin.tes.code.go;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class GoFile {
    private String name;

    private List<GoPackage> goPackages = new ArrayList<>();

    public GoFile setName(String name) {
        this.name = name;
        return this;
    }

    public GoFile setGoPackages(List<GoPackage> goPackages) {
        this.goPackages = goPackages;
        return this;
    }

    public GoFile addGoPackages(GoPackage... goPackage){
        goPackages.addAll(Arrays.asList(goPackage));
        return this;
    }
}
