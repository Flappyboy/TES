package top.jach.tes.core.impl.domain.meta;

import top.jach.tes.core.api.domain.meta.Field;

public abstract class FieldWithName<I> implements Field<I> {
    protected String name;
    protected String displayName;
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String displayName() {
        return displayName;
    }
}
