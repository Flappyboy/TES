package top.jach.tes.dev.app.jach.code.go;

import lombok.Data;

import java.util.List;

@Data
public class GoFile {
    private String name;

    private List<Package> packages;
}
