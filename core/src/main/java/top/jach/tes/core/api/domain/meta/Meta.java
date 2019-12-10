package top.jach.tes.core.api.domain.meta;

import java.util.List;

/**
 * 用于向前端描述该{@link top.jach.tes.core.api.domain.action.Action}的输入
 */
public interface Meta {
    List<Field> getFieldList();
}
