package top.jach.tes.core.domain.context;

import java.io.File;

public class DefaultTempSpace implements TempSpace {
    File file;

    public DefaultTempSpace(File file) {
        this.file = file;
    }

    @Override
    public File getTmpDir() {
        if(!file.exists()){
            file.mkdirs();
        }
        return file;
    }
}
