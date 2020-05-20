package top.jach.tes.app.web.dto;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.ListUtils;
import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.plugin.jhkt.arcsmell.mv.MvValue;
import top.jach.tes.plugin.jhkt.arcsmell.result.Mv;
import top.jach.tes.plugin.jhkt.arcsmell.result.Result;
import top.jach.tes.plugin.jhkt.arcsmell.result.ResultForMs;
import top.jach.tes.plugin.tes.code.go.GoPackagesInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArcSmellResultInfo extends Info {
    Map<String, ResultAll> resultMap = new HashMap<>();

    public Map<String, ResultAll> getResultMap() {
        return resultMap;
    }

    @Override
    public String getName() {
        return "ArcSmellResultInfo";
    }

    public static ArcSmellResultInfo createInfo(){
        ArcSmellResultInfo info = new ArcSmellResultInfo();
        info.initBuild();
        return info;
    }

    public void put(String versionName, ResultForMs resultForMs, Double hdth, Double hdnth,Double hdinth){
        ResultAll results = new ResultAll();
        List<Double> hdns = new ArrayList<>();
        List<Double> hdins = new ArrayList<>();
        for (String microservice:
                resultForMs.getMicroservice()) {
            Double hub = resultForMs.getHublikes().get(microservice);
            if(hub ==null || hub==0){
                continue;
            }

            Mv mv = resultForMs.getMvs().get(0);
            MvValue mvValue = mv.getMvValues().get(microservice);
            Result result = new Result();
            result.setMicroserviceName(microservice);
            result.setMvdn(mvValue.getDependency().intValue());
            result.setMvfn(mvValue.getFile().intValue());

            result.setHdn(resultForMs.getHublikes().get(microservice).intValue());
            hdns.add(Double.valueOf(result.getHdn()));
            result.setHdin(resultForMs.getHublikeWithWeight().get(microservice).intValue());
            hdins.add(Double.valueOf(result.getHdin()));
            result.setCn(resultForMs.getCyclic().get(microservice).intValue());
            results.getResultMap().put(result.getMicroserviceName(), result);
        }
        if (hdth!= null && hdth>0) {
            double total = 0.0;
            for (Double hdn :
                    hdns) {
                total+=hdn;
            }
            hdnth = total/hdns.size() + StandardDiviation(hdns.toArray(new Double[hdns.size()])) * hdth;
            total = 0.0;
            for (Double hdin :
                    hdins) {
                total+=hdin;
            }
            hdinth = total/hdns.size() + StandardDiviation(hdins.toArray(new Double[hdins.size()])) * hdth;
        }

        results.setHdinTh(hdnth);
        results.setHdinTh(hdinth);

        resultMap.put(versionName, results);
    }

    public static double StandardDiviation(Double[] x) {
        int m=x.length;
        double sum=0;
        for(int i=0;i<m;i++){//求和
            sum+=x[i];
        }
        double dAve=sum/m;//求平均值
        double dVar=0;
        for(int i=0;i<m;i++){//求方差
            dVar+=(x[i]-dAve)*(x[i]-dAve);
        }
              //reture Math.sqrt(dVar/(m-1));
        return Math.sqrt(dVar/m);
    }

    public static class Result{
        String microserviceName;
        Integer hdn;
        Integer hdin;
        Integer cn;
        Integer mvdn;
        Integer mvfn;

        public String getMicroserviceName() {
            return microserviceName;
        }

        public Result setMicroserviceName(String microserviceName) {
            this.microserviceName = microserviceName;
            return this;
        }

        public Integer getHdn() {
            return hdn;
        }

        public Result setHdn(Integer hdn) {
            this.hdn = hdn;
            return this;
        }

        public Integer getHdin() {
            return hdin;
        }

        public Result setHdin(Integer hdin) {
            this.hdin = hdin;
            return this;
        }

        public Integer getCn() {
            return cn;
        }

        public Result setCn(Integer cn) {
            this.cn = cn;
            return this;
        }

        public Integer getMvdn() {
            return mvdn;
        }

        public Result setMvdn(Integer mvdn) {
            this.mvdn = mvdn;
            return this;
        }

        public Integer getMvfn() {
            return mvfn;
        }

        public Result setMvfn(Integer mvfn) {
            this.mvfn = mvfn;
            return this;
        }
    }

    public static class ResultAll{
        Map<String, Result> resultMap = new HashMap<>();

        Double hdnTh;
        Double hdinTh;

        public Double getHdnTh() {
            return hdnTh;
        }

        public ResultAll setHdnTh(Double hdnTh) {
            this.hdnTh = hdnTh;
            return this;
        }

        public Double getHdinTh() {
            return hdinTh;
        }

        public ResultAll setHdinTh(Double hdinTh) {
            this.hdinTh = hdinTh;
            return this;
        }

        public Map<String, Result> getResultMap() {
            return resultMap;
        }

        public ResultAll setResultMap(Map<String, Result> resultMap) {
            this.resultMap = resultMap;
            return this;
        }
    }

}
