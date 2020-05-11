package top.jach.tes.plugin.jhkt.arcsmell.result;

import lombok.Data;
import top.jach.tes.plugin.jhkt.maintain.MainTain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ResultForMs {
    List<String> microservice;
    Map<String, Double> hublikes = new HashMap<>();
    Map<String, Double> hublikeWithWeight = new HashMap<>();
    Map<String, Double> cyclic = new HashMap<>();
    Map<String, Double> undirectedCyclic = new HashMap<>();
    List<Mv> mvs;

    Map<String, Long> commitCount = new HashMap<>();
    Map<String, Long> bugCount = new HashMap<>();
    Map<String, Long> commitAddLineCount = new HashMap<>();
    Map<String, Long> commitDeleteLineCount = new HashMap<>();
    Map<String, Long> commitLineCount = new HashMap<>();
    Map<String, Double> CommitOverlapRatio = new HashMap<>();
    Map<String, Double> CommitFilesetOverlapRatio = new HashMap<>();
    Map<String, Double> PairwiseCommitterOverlap = new HashMap<>();


    public void addMainTain(MainTain mainTain){
        commitCount.put(mainTain.getElementName(), mainTain.getCommitCount());
        bugCount.put(mainTain.getElementName(), mainTain.getBugCount());
        commitAddLineCount.put(mainTain.getElementName(), mainTain.getCommitAddLineCount());
        commitDeleteLineCount.put(mainTain.getElementName(), mainTain.getCommitDeleteLineCount());
        commitLineCount.put(mainTain.getElementName(), mainTain.getCommitAddLineCount()+mainTain.getCommitDeleteLineCount());
        CommitOverlapRatio.put(mainTain.getElementName(), mainTain.getCommitOverlapRatio());
        CommitFilesetOverlapRatio.put(mainTain.getElementName(), mainTain.getCommitFilesetOverlapRatio());
        PairwiseCommitterOverlap.put(mainTain.getElementName(), mainTain.getPairwiseCommitterOverlap());
    }
}
