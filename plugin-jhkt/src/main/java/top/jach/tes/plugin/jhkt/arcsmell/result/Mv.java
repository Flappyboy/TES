package top.jach.tes.plugin.jhkt.arcsmell.result;

import lombok.Data;
import top.jach.tes.plugin.jhkt.arcsmell.mv.MvAction;
import top.jach.tes.plugin.jhkt.arcsmell.mv.MvResult;
import top.jach.tes.plugin.jhkt.arcsmell.mv.MvValue;
import top.jach.tes.plugin.jhkt.microservice.Microservice;
import top.jach.tes.plugin.tes.code.git.commit.GitCommit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class Mv {
    Integer len;
    Integer minCommitCount;
    Double minPer;
    Map<String, MvValue> mvValues;
    MvResult mvResult;

    public static List<Mv> CalculateMvs(int[] lens, int[] minCommitCounts, double[] minPers, List<GitCommit> gitCommits, List<Microservice> microservices){
        List<Mv> mvs = new ArrayList<>();
        for (int len:
                lens) {
            mvs.addAll(CalculateMvs(len, minCommitCounts, minPers, gitCommits, microservices));
        }
        return mvs;
    }

    public static List<Mv> CalculateMvs(int len, int[] minCommitCounts, double[] minPers, List<GitCommit> gitCommits, List<Microservice> microservices){
        MvResult mvResult = MvAction.detectMvResult(gitCommits, len, microservices);
        List<Mv> mvs = new ArrayList<>();
        for (int mcc :
                minCommitCounts) {
            for (double mp :
                    minPers) {
                Map<String, MvValue> mvValues= mvResult.calculateMvValues(mcc, mp);
                Mv mv = new Mv();
                mv.setLen(len);
                mv.setMinCommitCount(mcc);
                mv.setMinPer(mp);
                mv.setMvValues(mvValues);
                mv.setMvResult(mvResult);
                mvs.add(mv);
            }
        }
        return mvs;
    }
}
