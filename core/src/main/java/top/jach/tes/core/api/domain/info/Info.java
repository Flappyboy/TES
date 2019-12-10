package top.jach.tes.core.api.domain.info;

import lombok.Getter;
import lombok.ToString;
import top.jach.tes.core.api.domain.Entity;

/**
 * Info是系统处理数据的最小粒度，继承Info的类都需要是想一个静态的createInfo()方法
 */
@Getter
@ToString
public abstract class Info extends Entity {
/*
    所有继承Info的类应该要包含如下静态方法,并调用info.initBuild()方法,
    可以添加参数，用于表示创建新的Info，Info替换成对应类。
    public static Info createInfo(...){
        Info info = new Info();
        info.initBuild();
        return info;
    }
*/

    /**
     * 用于向用户表明该Info的含义，不具备具体的业务属性，可能会用来作为筛选条件
     */
    private String name;

    private String desc;

    private String status = InfoStatus.NONE.name();

    public enum InfoStatus {
        SAVING(),
        COMPLETE(),
        NONE()
    }

    // 用于识别Info的Class，正常情况下就是类本身，但在仅用于传输info部分属性，而不知道具体Info时需要指定
    public Class<? extends Info> getInfoClass(){
        return getClass();
    }

    public Info setName(String name) {
        this.name = name;
        return this;
    }

    public Info setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    public Info setStatus(String status) {
        try {
            InfoStatus infoStatus = InfoStatus.valueOf(status);
            if(infoStatus != null) {
                this.status = status;
            }else{
                this.status = InfoStatus.NONE.name();
            }
        }catch (IllegalArgumentException e){
            this.status = InfoStatus.NONE.name();
        }
        return this;
    }
}
