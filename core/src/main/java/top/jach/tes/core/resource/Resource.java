package top.jach.tes.core.resource;

import java.io.File;

public class Resource {
    public File getFile(String relativePath){
        return new File(relativePath);
    }
    public void log(String log){
        System.out.println(log);
    }
}
