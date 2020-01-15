package top.jach.tes.core.api.domain;

import lombok.Getter;
import top.jach.tes.core.api.factory.IdGenerator;

/**
 * 基本的实体类，继承该实体的类，在创建对象时需要执行{@link #initBuild()}来初始化实体
 */
@Getter
public abstract class Entity {
    private Long id;
    public static final String FIELD_ID = "id";

    private Long createdTime;
    public static final String FIELD_CREATED_TIME = "createdTime";

    private Long updatedTime;
    public static final String FIELD_UPDATED_TIME = "updatedTime";

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
