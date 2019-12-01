package top.jach.tes.core.domain.meta;

public abstract class TimeField extends ValueField<Long> {

    @Override
    public Class<Long> getInputClass() {
        return Long.class;
    }
}
