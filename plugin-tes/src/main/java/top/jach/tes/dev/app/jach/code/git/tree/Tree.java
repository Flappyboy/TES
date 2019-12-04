package top.jach.tes.dev.app.jach.code.git.tree;

import lombok.Data;

@Data
public class Tree {
    String relativePath;
    String fileMode;

    public Tree() {
    }

    public Tree setRelativePath(String relativePath) {
        this.relativePath = relativePath;
        return this;
    }

    public Tree setFileMode(String fileMode) {
        this.fileMode = fileMode;
        return this;
    }
}
