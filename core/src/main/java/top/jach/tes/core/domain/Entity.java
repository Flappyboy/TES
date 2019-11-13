package top.jach.tes.core.domain;

import lombok.Getter;
import top.jach.tes.core.factory.IdGenerator;

import java.util.Date;

@Getter
public abstract class Entity {
    private Long id;

    private Date createdTime;

    private Date updatedTime;

    public void initBuild(){
        this.id = IdGenerator.nextId();
        this.createdTime = new Date();
        this.updatedTime = new Date();
    }

    public void updateTime(){
        this.updatedTime = new Date();
    }

    public Entity setId(Long id) {
        this.id = id;
        return this;
    }

    public Entity setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    public Entity setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
        return this;
    }
}
