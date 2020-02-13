package top.jach.tes.plugin.tes.code.bug;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import top.jach.tes.core.impl.domain.element.Element;

@Getter
@Setter
public class Bug extends Element {
    private String name;

    private Long findTime;

    private Long fixTime;

    @Override
    public String getElementName() {
        return name;
    }
}
