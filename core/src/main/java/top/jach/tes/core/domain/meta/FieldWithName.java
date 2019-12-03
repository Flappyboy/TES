package top.jach.tes.core.domain.meta;

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
