package top.jach.tes.core.context;

import java.io.File;

public class DefaultTempSpace implements TempSpace {
    @Override
    public File getTmpDir() {
        return new File("./");
    }
}
