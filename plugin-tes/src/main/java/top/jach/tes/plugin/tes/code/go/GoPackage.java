package top.jach.tes.plugin.tes.code.go;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class GoPackage {
    private String name;

    private String path;

    private List<GoFile> goFiles = new ArrayList<>();

    public GoPackage addGoFiles(GoFile... goFiles){
        this.goFiles.addAll(Arrays.asList(goFiles));
        return this;
    }

    public GoPackage setName(String name) {
        this.name = name;
        return this;
    }

    public GoPackage setPath(String path) {
        this.path = path;
        return this;
    }
}
