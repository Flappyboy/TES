package top.jach.tes.core.domain.meta;

public abstract class StringField extends ValueField<String> {

    @Override
    public Class<String> getInputClass() {
        return String.class;
    }
}
