package top.jach.tes.core.domain.info;

import lombok.Getter;
import lombok.ToString;
import top.jach.tes.core.domain.Entity;

@Getter
@ToString
public class Info extends Entity {
    private String name;

    private String desc;

    private InfoStatus status;

    public enum InfoStatus {
        SAVING(),//0
        COMPLETE();//1
    }

    public Info setName(String name) {
        this.name = name;
        return this;
    }

    public Info setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    public Info setStatus(InfoStatus status) {
        this.status = status;
        return this;
    }
}
