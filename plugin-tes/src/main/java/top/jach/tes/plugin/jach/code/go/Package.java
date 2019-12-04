package top.jach.tes.plugin.jach.code.go;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class Package {
    private String name;

    private String path;

    private List<GoFile> goFiles = new ArrayList<>();

    public Package addGoFiles(GoFile... goFiles){
        this.goFiles.addAll(Arrays.asList(goFiles));
        return this;
    }

    public Package setName(String name) {
        this.name = name;
        return this;
    }

    public Package setPath(String path) {
        this.path = path;
        return this;
    }
}
