package top.jach.tes.plugin.jhkt.maintain;

import lombok.Data;

@Data
public class MainTain {
    private String elementName;
    private Long bugCount;
    private Long commitCount;
    private Long commitAddLineCount;
    private Long commitDeleteLineCount;
    private Double CommitOverlapRatio;
    private Double CommitFilesetOverlapRatio;
    private Double PairwiseCommitterOverlap;
}
