package top.jach.tes.plugin.tes.code.git.commit;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiffFile {
    private String changeType;
    private String oldPath;
    private String newPath;
    private Integer addSize = 0;
    private Integer subSize = 0;
    private Integer modifyAddSize = 0;
    private Integer modifySubSize = 0;

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

    public DiffFile setModifyAddSize(Integer modifyAddSize) {
        this.modifyAddSize = modifyAddSize;
        return this;
    }

    public DiffFile setModifySubSize(Integer modifySubSize) {
        this.modifySubSize = modifySubSize;
        return this;
    }
}
