package top.jach.tes.plugin.tes.code.git.commit;

import lombok.Data;
import org.eclipse.jgit.diff.DiffEntry;

import java.util.List;

@Data
public class StatisticDiffFiles {
    private Long modifyFileCount = 0l;
    private Long addFileCount = 0l;
    private Long deleteFileCount = 0l;
    private Long renameFileCount = 0l;
    private Long copyFileCount = 0l;

    private Long addSize = 0l;
    private Long subSize = 0l;

    public static StatisticDiffFiles create(List<DiffFile> diffFiles){
        StatisticDiffFiles statisticDiffFiles = new StatisticDiffFiles();
        if(diffFiles != null) {
            for (DiffFile diffFile :
                    diffFiles) {
                statisticDiffFiles.addDiffFiles(diffFile);
            }
        }
        return statisticDiffFiles;
    }

    public StatisticDiffFiles addDiffFiles(List<DiffFile> diffFiles){
        for (DiffFile diffFile :
                diffFiles) {
            if(diffFile != null){
                addDiffFiles(diffFile);
            }
        }
        return this;
    }

    public StatisticDiffFiles addDiffFiles(DiffFile... diffFiles){
        for (DiffFile diffFile :
                diffFiles) {
            if(diffFile == null){
                continue;
            }
            DiffEntry.ChangeType changeType = DiffEntry.ChangeType.valueOf(diffFile.getChangeType());
            switch (changeType){
                case ADD:
                    addFileCount++;
                    break;
                case DELETE:
                    deleteFileCount++;
                    break;
                case MODIFY:
                    modifyFileCount++;
                    break;
                case RENAME:
                    renameFileCount++;
                    break;
                case COPY:
                    copyFileCount++;
                    break;
            }
            addSize += diffFile.getAddSize();
            subSize += diffFile.getSubSize();
        }
        return this;
    }
}
