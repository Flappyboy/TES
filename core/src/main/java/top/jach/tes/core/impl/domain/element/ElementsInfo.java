package top.jach.tes.core.impl.domain.element;

import top.jach.tes.core.api.domain.info.Info;

public abstract class ElementsInfo<E extends Element> extends Info implements Iterable<E> {
    public abstract E getElementByName(String elementName);
}
