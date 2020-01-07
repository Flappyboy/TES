package top.jach.tes.plugin.jhkt.analysis;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import top.jach.tes.plugin.jhkt.arcsmell.ArcSmell;
import top.jach.tes.plugin.jhkt.maintain.MainTain;

@Getter
@Setter
@ToString
public class MicroserviceAttr {
    String microserviceName;
    Long codeLines;
    int pubTopicCount;
    int subTopicCount;
    Long cyclic;
    Long hublink;
    Long hublinkForIn;
    Long hublinkForOut;
    Long bugCount;
    Long commitCount;
    Long commitAddLineCount;
    Long commitDeleteLineCount;
    Double commitOverlapRatio;
    Double commitFilesetOverlapRatio;
    Double pairwiseCommitterOverlap;

    public MicroserviceAttr setMicroserviceName(String microserviceName) {
        this.microserviceName = microserviceName;
        return this;
    }

    public MicroserviceAttr setHublinkForIn(Long hublinkForIn) {
        this.hublinkForIn = hublinkForIn;
        return this;
    }

    public MicroserviceAttr setHublinkForOut(Long hublinkForOut) {
        this.hublinkForOut = hublinkForOut;
        return this;
    }

    public MicroserviceAttr setCodeLines(Long codeLines) {
        this.codeLines = codeLines;
        return this;
    }

    public MicroserviceAttr setPubTopicCount(int pubTopicCount) {
        this.pubTopicCount = pubTopicCount;
        return this;
    }

    public MicroserviceAttr setSubTopicCount(int subTopicCount) {
        this.subTopicCount = subTopicCount;
        return this;
    }

    public MicroserviceAttr setCyclic(Long cyclic) {
        this.cyclic = cyclic;
        return this;
    }

    public MicroserviceAttr setHublink(Long hublink) {
        this.hublink = hublink;
        return this;
    }

    public MicroserviceAttr setBugCount(Long bugCount) {
        this.bugCount = bugCount;
        return this;
    }

    public MicroserviceAttr setCommitCount(Long commitCount) {
        this.commitCount = commitCount;
        return this;
    }

    public MicroserviceAttr setCommitAddLineCount(Long commitAddLineCount) {
        this.commitAddLineCount = commitAddLineCount;
        return this;
    }

    public MicroserviceAttr setCommitDeleteLineCount(Long commitDeleteLineCount) {
        this.commitDeleteLineCount = commitDeleteLineCount;
        return this;
    }

    public MicroserviceAttr setCommitOverlapRatio(Double commitOverlapRatio) {
        this.commitOverlapRatio = commitOverlapRatio;
        return this;
    }

    public MicroserviceAttr setCommitFilesetOverlapRatio(Double commitFilesetOverlapRatio) {
        this.commitFilesetOverlapRatio = commitFilesetOverlapRatio;
        return this;
    }

    public MicroserviceAttr setPairwiseCommitterOverlap(Double pairwiseCommitterOverlap) {
        this.pairwiseCommitterOverlap = pairwiseCommitterOverlap;
        return this;
    }
}
