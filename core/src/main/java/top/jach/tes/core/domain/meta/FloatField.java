package top.jach.tes.core.domain.meta;

public abstract class FloatField extends ValueField<Double> {

    @Override
    public Class<Double> getInputClass() {
        return Double.class;
    }
}
