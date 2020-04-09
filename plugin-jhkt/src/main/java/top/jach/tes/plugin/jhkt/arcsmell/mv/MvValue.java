package top.jach.tes.plugin.jhkt.arcsmell.mv;

import lombok.Data;

@Data
public class MvValue {
    Double dependency;
    Double doubleDependency; // 双方概率都超过阈值
    Integer file;
    Integer doubleFile;

}
