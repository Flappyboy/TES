package top.jach.tes.core.domain.context;

import java.io.File;

public class DefaultContext {
    public File getFile(String relativePath){
        return new File(relativePath);
    }
    public void log(String log){
        System.out.println(log);
    }
}
