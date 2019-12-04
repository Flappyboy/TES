package top.jach.tes.plugin.jach.code.go;

import lombok.Data;

import java.util.List;

@Data
public class GoFile {
    private String name;

    private List<Package> packages;
}
