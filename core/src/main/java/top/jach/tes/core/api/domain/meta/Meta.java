package top.jach.tes.core.api.domain.meta;

import java.util.List;

/**
 * 用于向前端描述该{@link top.jach.tes.core.api.domain.action.Action}的输入
 * 前端根据描述显示用户需要输入哪些数据
 */
public interface Meta {
    List<Field> getFieldList();//每个filed是用户输入的选项
}
