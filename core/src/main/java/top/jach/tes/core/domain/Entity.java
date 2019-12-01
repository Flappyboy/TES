package top.jach.tes.core.domain;

import lombok.Getter;
import top.jach.tes.core.factory.IdGenerator;

import java.util.Date;

@Getter
public abstract class Entity {
    private Long id;

    private Long createdTime;

    private Long updatedTime;

    public void initBuild(){
        this.id = IdGenerator.nextId();
        this.createdTime = System.currentTimeMillis();
        this.updatedTime = System.currentTimeMillis();
    }

    public void updateTime(){
        this.updatedTime = System.currentTimeMillis();
    }

    public Entity setId(Long id) {
        this.id = id;
        return this;
    }

    public Entity setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    public Entity setUpdatedTime(Long updatedTime) {
        this.updatedTime = updatedTime;
        return this;
    }
}
