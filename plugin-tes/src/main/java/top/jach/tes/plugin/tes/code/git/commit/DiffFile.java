package top.jach.tes.plugin.tes.code.git.commit;

import lombok.Getter;
import lombok.Setter;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.patch.FileHeader;
import org.eclipse.jgit.patch.HunkHeader;

import java.io.IOException;
import java.util.List;

@Getter
@Setter
public class DiffFile {
    private String changeType;
    private String oldPath;
    private String newPath;
    private Integer addSize = 0;
    private Integer subSize = 0;

    public static DiffFile createFromDiffEntry(DiffEntry diffEntry, DiffFormatter df) throws IOException {
        // 相似度
        diffEntry.getScore();

        FileHeader fileHeader = df.toFileHeader(diffEntry);
        List<HunkHeader> hunks = (List<HunkHeader>) fileHeader.getHunks();
        int addSize = 0, subSize = 0;

        for (HunkHeader hunkHeader :
                hunks) {
            EditList editList = hunkHeader.toEditList();
            for (Edit edit :
                    editList) {
                addSize += edit.getLengthB();
                subSize += edit.getLengthA();
            }
        }

        return new DiffFile().setChangeType(diffEntry.getChangeType().name())
                .setOldPath(diffEntry.getOldPath())
                .setNewPath(diffEntry.getNewPath())
                .setAddSize(addSize)
                .setSubSize(subSize);
    }

    public DiffFile setChangeType(String changeType) {
        this.changeType = changeType;
        return this;
    }

    public DiffFile setOldPath(String oldPath) {
        if ("/dev/null".equals(oldPath)){
            return this;
        }
        this.oldPath = oldPath;
        return this;
    }

    public DiffFile setNewPath(String newPath) {
        if ("/dev/null".equals(newPath)){
            return this;
        }
        this.newPath = newPath;
        return this;
    }

    public DiffFile setAddSize(Integer addSize) {
        this.addSize = addSize;
        return this;
    }

    public DiffFile setSubSize(Integer subSize) {
        this.subSize = subSize;
        return this;
    }
}
