package top.jach.tes.core.domain.meta;

import top.jach.tes.core.factory.ResourceFactory;

public enum FormDataType {
    FILE(){
        @Override
        public Object getObj(String value) {
            return ResourceFactory.DefaultResource().getFile(value);
        }
    },
    STRING() {
        @Override
        public Object getObj(String value) {
            return value;
        }
    },
    TIMESTAMP() {
        @Override
        public Object getObj(String value) {
            return Long.valueOf(value);
        }
    },
    ;

    FormDataType() {}

    public abstract Object getObj(String value);
}
