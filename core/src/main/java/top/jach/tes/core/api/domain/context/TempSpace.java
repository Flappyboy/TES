package top.jach.tes.core.api.domain.context;

import java.io.File;

public interface TempSpace {

    File getTmpDir();

    void dispose();
}
