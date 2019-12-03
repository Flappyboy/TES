package top.jach.tes.core.domain.meta;

import top.jach.tes.core.domain.info.Info;
import top.jach.tes.core.factory.info.InfoRepositoryFactory;

public interface Field<I> {

    /**
     * 字段名
     * @return
     */
    String getName();


    String displayName();

    /**
     * 字段数据的类型
     * @return
     */
    Class<I> getInputClass();

    /**
     * 根据输入获取Info
     * @param input 根据字段数据的类型，将前端输入转成对应类型的对象传入
     * @return
     */
    Info getInfo(I input, InfoRepositoryFactory infoRepositoryFactory);

    interface FieldCriteria {
        String criteria();
    }
}
