package top.jach.tes.core.impl.domain.info.value;

import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Getter
@Setter
public class FileInfo extends ValueInfo<File> {
    File value;
    public static FileInfo createInfo(File file){
        FileInfo info = new FileInfo();
        info.setValue(file);
        info.initBuild();
        return info;
    }
}
