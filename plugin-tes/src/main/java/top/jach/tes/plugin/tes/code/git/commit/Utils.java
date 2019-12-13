package top.jach.tes.plugin.tes.code.git.commit;

import org.eclipse.jgit.diff.DiffAlgorithm;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.lib.Repository;

import java.io.ByteArrayOutputStream;

public class Utils {
    public static DiffFormatter diffFormatter(Repository repository){
        DiffFormatter df = new DiffFormatter(new ByteArrayOutputStream());
//            df.setDiffComparator(RawTextComparator.WS_IGNORE_ALL);
        df.setRepository(repository);
        // 选择文本对比算法，最小编辑距离或者直方图
        df.setDiffAlgorithm(DiffAlgorithm.getAlgorithm(DiffAlgorithm.SupportedAlgorithm.MYERS));
        return df;
    }
}
