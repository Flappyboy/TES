package top.jach.tes.core.domain.meta;

public abstract class IntegerField extends ValueField<Long> {

    @Override
    public Class<Long> getInputClass() {
        return Long.class;
    }
}
