package top.jach.tes.plugin.jhkt.arcsmell.mv;

import org.apache.commons.lang3.StringUtils;
import top.jach.tes.plugin.jhkt.microservice.Microservice;

import java.util.*;

import static top.jach.tes.plugin.jhkt.arcsmell.mv.MvAction.getMicroserviceByPathname;

public class MvResult {
    private Integer param;
    private Map<String, MvResultForMicroservice> results = new HashMap<>();
    private Map<String, Integer> fileCommitCount;
    private List<Microservice> microservices;
    private Map<String,Map<String,Integer>> resultFiles;

    public MvResult(Integer param, Map<String,Map<String,Integer>> resultFiles, Map<String, Integer> fileCommitCount, List<Microservice> microservices) {
        for (Map.Entry<String,Map<String, Integer>> entry:
                resultFiles.entrySet()) {
            String file = entry.getKey();
            String microservice = getMicroserviceByPathname(file, microservices);
            MvResultForMicroservice mvResultForMicroservice = results.get(microservice);
            if (mvResultForMicroservice==null){
                mvResultForMicroservice = new MvResultForMicroservice();
                results.put(microservice, mvResultForMicroservice);
            }
            mvResultForMicroservice.add(file, entry.getValue());
        }
        this.param = param;
        this.resultFiles = resultFiles;
        this.fileCommitCount = fileCommitCount;
        this.microservices = microservices;
    }

    class MvResultForMicroservice{
        private Map<String, Map<String, Integer>> fileToFile = new HashMap<>();
        public MvResultForMicroservice() {
        }
        public void add(String file, Map<String, Integer> files){
            fileToFile.put(file, files);
        }

        public Map<String, Map<String, Integer>> getFileToFile() {
            return fileToFile;
        }
    }

    public Map<String, MvResultForMicroservice> getResults() {
        return results;
    }

    public Map<String, Integer> getFileCommitCount() {
        return fileCommitCount;
    }

    public Map<String, MvValue> calculateMvValues(int minCommitCount, double minPer) {
        Map<String, Double> mDepens = new HashMap<>();
        Map<String, Double> mDoubleDepens = new HashMap<>();
        Map<String, Set<String>> mfiles = new HashMap<>();
        Map<String, Set<String>> mDoublefiles = new HashMap<>();
        Map<String, MvValue> mmvs = new HashMap<>();
        for (Microservice m :
                microservices) {
            mDepens.put(m.getElementName(), 0d);
            mDoubleDepens.put(m.getElementName(), 0d);
            mfiles.put(m.getElementName(), new HashSet<>());
            mDoublefiles.put(m.getElementName(), new HashSet<>());
        }
        for (Map.Entry<String,Map<String, Integer>> entry:
                resultFiles.entrySet()){
            String file = entry.getKey();
            Double fcc = Double.valueOf(fileCommitCount.get(file));
            if(fcc<minCommitCount){
                continue;
            }
            for (Map.Entry<String, Integer> entry2 :
                    entry.getValue().entrySet()) {
                String tfile = entry2.getKey();
                Integer count = entry2.getValue();
                String m = getMicroserviceByPathname(file, microservices);
                String tm = getMicroserviceByPathname(tfile, microservices);
                if(m.equals(tm) || StringUtils.isBlank(m) || StringUtils.isBlank(tm)){
                    continue;
                }
                Double tfcc = Double.valueOf(fileCommitCount.get(tfile));
                if(count/fcc >= minPer){
                    Double mc = mDepens.get(m);
                    mDepens.put(m, mc+1);
                    Double tmc = mDepens.get(tm);
                    mDepens.put(tm, tmc+1);

                    mfiles.get(m).add(file);

                    if(count/tfcc >= minPer){
                        mc = mDoubleDepens.get(m);
                        mDoubleDepens.put(m, mc+1);
                        tmc = mDoubleDepens.get(tm);
                        mDoubleDepens.put(tm, tmc+1);

                        mDoublefiles.get(m).add(file);
                    }
                }
            }
        }

        for (Microservice m :
                microservices) {
            String mn = m.getElementName();
            MvValue mvValue = new MvValue();
            mvValue.setDependency(mDepens.get(mn));
            mvValue.setDoubleDependency(mDoubleDepens.get(mn));
            mvValue.setFile(mfiles.get(mn).size());
            mvValue.setDoubleFile(mDoublefiles.get(mn).size());
            mmvs.put(m.getElementName(), mvValue);
        }
        return mmvs;
    }

    public List<Microservice> getMicroservices() {
        return microservices;
    }

    public Map<String, Map<String, Integer>> getResultFiles() {
        return resultFiles;
    }
}
