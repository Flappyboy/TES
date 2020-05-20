package top.jach.tes.app.jhkt.lijiaqi.forimportexport;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import top.jach.tes.app.dev.DevApp;
import top.jach.tes.app.jhkt.chenjiali.CorrelationData;
import top.jach.tes.app.jhkt.chenjiali.CorrelationDataInfo;
import top.jach.tes.app.jhkt.chenjiali.Ttest;
import top.jach.tes.app.mock.Environment;
import top.jach.tes.app.mock.InfoTool;
import top.jach.tes.core.api.domain.context.Context;
import top.jach.tes.core.api.exception.ActionExecuteFailedException;
import top.jach.tes.core.impl.domain.element.ElementsValue;
import top.jach.tes.core.impl.domain.relation.PairRelationsInfo;
import top.jach.tes.plugin.jhkt.DataAction;
import top.jach.tes.plugin.jhkt.InfoNameConstant;
import top.jach.tes.plugin.jhkt.analysis.MicroserviceAttr;
import top.jach.tes.plugin.jhkt.analysis.MicroserviceAttrsInfo;
import top.jach.tes.plugin.jhkt.arcsmell.ArcSmell;
import top.jach.tes.plugin.jhkt.arcsmell.ArcSmellsInfo;
import top.jach.tes.plugin.jhkt.arcsmell.cyclic.CyclicAction;
import top.jach.tes.plugin.jhkt.arcsmell.hublink.HublinkAction;
import top.jach.tes.plugin.jhkt.arcsmell.mv.MvResult;
import top.jach.tes.plugin.jhkt.arcsmell.mv.MvValue;
import top.jach.tes.plugin.jhkt.arcsmell.result.Mv;
import top.jach.tes.plugin.jhkt.arcsmell.result.Result;
import top.jach.tes.plugin.jhkt.arcsmell.result.ResultForMs;
import top.jach.tes.plugin.jhkt.dts.DtssInfo;
import top.jach.tes.plugin.jhkt.git.commit.GitCommitsForMicroserviceInfo;
import top.jach.tes.plugin.jhkt.maintain.MainTain;
import top.jach.tes.plugin.jhkt.maintain.MainTainsInfo;
import top.jach.tes.plugin.jhkt.metrics.Metrics;
import top.jach.tes.plugin.jhkt.metrics.MetricsInfo;
import top.jach.tes.plugin.jhkt.microservice.Microservice;
import top.jach.tes.plugin.jhkt.microservice.MicroservicesInfo;
import top.jach.tes.plugin.tes.code.git.commit.GitCommit;
import top.jach.tes.plugin.tes.code.git.version.Version;
import top.jach.tes.plugin.tes.code.git.version.VersionsInfo;
import top.jach.tes.plugin.tes.code.go.GoPackagesInfo;
import top.jach.tes.plugin.tes.code.repo.ReposInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

// 继承DevApp 已加载InfoRepository等上下文环境
public class IE extends DevApp {
    public static void main(String[] args) throws ActionExecuteFailedException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException {
        Context context = Environment.contextFactory.createContext(Environment.defaultProject);

        ReposInfo reposInfo = InfoTool.queryLastInfoByNameAndInfoClass(InfoNameConstant.TargetSystem, ReposInfo.class);

        VersionsInfo versionsInfoForRelease = DataAction.queryLastInfo(context, InfoNameConstant.VersionsForRelease, VersionsInfo.class);

        Map<String, Map<String, Object>> allData = new LinkedHashMap<>();
        Result result = new Result();
        for (int i = 0; i < versionsInfoForRelease.getVersions().size()-1; i++) {
            Version version = versionsInfoForRelease.getVersions().get(i);
        /*}
        for (Version version:
                versionsInfoForRelease.getVersions()) {//每一轮循环代表一个sheet页*/

            //查询version name
            String n_version=version.getVersionName();

            // 查询version版本下的所有微服务
            MicroservicesInfo microservices = DataAction.queryLastMicroservices(context, reposInfo.getId(), null, version);

            //存储单个版中所有微服务名称
            List<String> microserviceNames = microservices.microserviceNames();

            // 计算并存储微服务间的调用关系，用于后续架构异味的计算
            PairRelationsInfo pairRelationsInfoWithWeight = microservices.callRelationsInfoByTopic(true);
            pairRelationsInfoWithWeight.setName(InfoNameConstant.MicroserviceExcludeSomeCallRelation);
            InfoTool.saveInputInfos(pairRelationsInfoWithWeight);
            PairRelationsInfo pairRelationsInfoWithoutWeight = microservices.callRelationsInfoByTopic(false).deWeight();
            pairRelationsInfoWithoutWeight.setName(InfoNameConstant.MicroserviceExcludeSomeCallRelation);
            InfoTool.saveInputInfos(pairRelationsInfoWithoutWeight);
            //InfoTool.saveInputInfos(pairRelationsInfo);

            // 查询version版本下问题单数据
            DtssInfo dtssInfo = InfoTool.queryLastInfoByNameAndInfoClass(InfoNameConstant.BugDts, DtssInfo.class);

            // 查询version版本下问题单和微服务关系的数据
            PairRelationsInfo bugMicroserviceRelations = InfoTool.queryLastInfoByNameAndInfoClass(InfoNameConstant.RelationBugAndMicroservice, PairRelationsInfo.class);

            //查询version版本下所有微服务的commit信息
            Map<String, GitCommitsForMicroserviceInfo> gitCommitsForMicroserviceInfoMap = new HashMap<>();
            List<GitCommit> gct=new ArrayList<>();
            for(Microservice microservice: microservices){//由于MicroserviceInfo类实现了Iterator方法，因此可以这样遍历
                GitCommitsForMicroserviceInfo gitCommitsForMicroserviceInfo = DataAction.queryLastGitCommitsForMicroserviceInfo(context, reposInfo.getId(), microservice.getElementName(), version);
                gitCommitsForMicroserviceInfoMap.put(microservice.getElementName(),gitCommitsForMicroserviceInfo);
                if(gitCommitsForMicroserviceInfo==null){
                    //System.out.println("GitCommitsForMicroserviceInfo  "+microservice.getElementName()+"  "+version.getVersionName());
                    continue;
                }
                gct.addAll(gitCommitsForMicroserviceInfo.getGitCommits());
            }
            //给gitCommits去重
           List<GitCommit> gitCommits=gct.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getReposId() + "#" + o.getRepoName() + "#" + o.getSha()))),ArrayList::new));;
           

//            ElementsValue undirectedCyclicResult = CyclicAction.CalculateUndirectedCyclic(context, microservices, pairRelationsInfo);
            //计算mv架构异味
//            List<Mv> mvs = Mv.CalculateMvs(new int[]{3,5,7,9},new int[]{5,7,10},new double[]{0.5,0.7,0.8,0.9},gitCommits, microservices.getMicroservices());
            Map<String, Object> data = new HashMap<>();
            allData.put(version.getVersionName(), data);
            data.put("VersionName", version.getVersionName());
            data.put("Microservices", microservices);
            data.put("GitCommits", gitCommits);
            data.put("PairRelationsInfoWithWeight", pairRelationsInfoWithWeight);
            data.put("pairRelationsInfoWithoutWeight", pairRelationsInfoWithoutWeight);

            /*ElementsValue hublike_weight = HublinkAction.calculateHublike(pairRelationsInfoWithWeight);
            ElementsValue hublike_no_weight = HublinkAction.calculateHublike(pairRelationsInfoWithoutWeight);
            ElementsValue cyclicResult = CyclicAction.CalculateCyclic(context, microservices, pairRelationsInfoWithWeight);

            List<Mv> mvs = Mv.CalculateMvs(new int[]{5},new int[]{10},new double[]{0.5},gitCommits, microservices.getMicroservices());

            ResultForMs resultForMs = new ResultForMs();
            result.put(version.getVersionName(), resultForMs);
            resultForMs.setMicroservice(microserviceNames);
            resultForMs.setCyclic(cyclicResult.getValueMap());
            resultForMs.setHublikeWithWeight(hublike_weight.getValueMap());
            resultForMs.setHublikes(hublike_no_weight.getValueMap());
            resultForMs.setMvs(mvs);*/

            /*MainTainsInfo mainTainsInfo = MainTainsInfo.newCreateInfo(DataAction.DefaultReposId,
                    microservices,
                    gitCommitsForMicroserviceInfoMap,
                    dtssInfo,
                    bugMicroserviceRelations,
                    version.getVersionName()
            );*/
            /*Map<String, MainTain> maintainMap = mainTainsInfo.nameMainTainMap();
            for (String mn :
                    microserviceNames) {
                MainTain mainTain = maintainMap.get(mn);
                resultForMs.addMainTain(mainTain);
            }*/
        }

        File dataFile = new File("E:/tmp/tes/ljq/data.json");

        FileUtils.write(dataFile, JSONObject.toJSONString(allData, SerializerFeature.PrettyFormat), "utf8");
        // 数据导出
//        exportCSV(microserviceAttrsInfos, new File("D:\\data\\tes\\analysis\\csv"));
//        exportExcel(microserviceAttrsInfos,correlationDataInfos,metricsInfos, new File("F:\\data\\tes\\analysis"));
        //exportCsv(result, new File("F:/data/tes/analysisforlijiaqi/result"));

//        for (Map.Entry<String, ResultForMs> entry:
//                result.getResults().entrySet()) {
//            String version = entry.getKey();
//            ResultForMs resultForMs = entry.getValue();
//            for (String microservice:
//                    resultForMs.getMicroservice()) {
//                resultForMs.getMvs().get(0);
//            }
//        }
    }
    public static void exportCsv(Result result, File dir) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if(!dir.exists()){
            dir.mkdirs();
        }
        FileUtils.cleanDirectory(dir);
        StringBuilder sb = new StringBuilder();
        Field[] fields = ResultForMs.class.getDeclaredFields();
        for (Field field:
                fields) {
            String fieldName = field.getName();
            switch (fieldName){
                case "mvs":
//                    Double dependency;
//                    Double doubleDependency; // 双方概率都超过阈值
//                    Integer file;
//                    Integer doubleFile;
                    sb.append("mv_dependency").append(",")
//                            .append("mv_doubleDependency").append(",")
                            .append("mv_file").append(",")
//                            .append("mv_doubleFile").append(",")
                           ;
                    break;
                case "undirectedCyclic":
                case "bugCount":
                case "commitAddLineCount":
                case "commitDeleteLineCount":
                    break;
                default:
                    sb.append(fieldName).append(",");
                    break;
            }
        }
        sb.append('\n');

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonResults = new JSONArray();
        jsonObject.put("result", jsonResults);
        for (Map.Entry<String, ResultForMs> entry:
                result.getResults().entrySet()) {
            String version = entry.getKey();
            ResultForMs resultForMs = entry.getValue();
            sb.append(version);
            sb.append('\n');
            JSONObject resultValues = new JSONObject();
            jsonResults.add(resultValues);
            resultValues.put("version", entry.getKey());
            JSONArray jsonArrayMs = new JSONArray();
            resultValues.put("ms", jsonArrayMs);
            for (String microservice:
                resultForMs.getMicroservice()) {
                Double hub = resultForMs.getHublikes().get(microservice);
                if(hub ==null || hub==0){
                    continue;
                }
                JSONObject jsonM = new JSONObject();
                jsonArrayMs.add(jsonM);
                for (Field field:
                        fields) {
                    String fieldName = field.getName();
                    Method m = ResultForMs.class.getMethod(getMethodName(fieldName));

                    switch (fieldName){
                        case "mvs":
                            Mv mv = resultForMs.getMvs().get(0);
                            MvValue mvValue = mv.getMvValues().get(microservice);
                            sb.append(mvValue.getDependency()).append(',')
//                                .append(mvValue.getDoubleDependency()).append(',')
                                .append(mvValue.getFile()).append(',')
//                                .append(mvValue.getDoubleFile()).append(',')
                            ;
                            jsonM.put("MVDN", mvValue.getDependency());
                            jsonM.put("MVFN", mvValue.getFile());
                            break;
                        case "undirectedCyclic":
                        case "bugCount":
                        case "commitAddLineCount":
                        case "commitDeleteLineCount":
                            break;
                        case "microservice":
                            sb.append(microservice).append(',');
                            jsonM.put("microservice", microservice);
                            break;
                        default:
                            Map map = (Map) m.invoke(resultForMs);
                            Object val = map.get(microservice);
                            if(val!=null) {
                                sb.append(val);
                                jsonM.put(fieldName, val);
                            }
                            sb.append(',');
                            break;
                    }
                }
                sb.append('\n');
            }
        }
        sb.append("\n\n");

        sb.append("Hub-like All\n");
        Set<String> allMicroservices = result.allMicroservices();
        Map<String, StringBuilder> hds = new HashMap<>();
        sb.append("microservice,");
        for (Map.Entry<String, ResultForMs> entry:
                result.getResults().entrySet()) {
            sb.append("hdn,hdin,");
            String version = entry.getKey();
            ResultForMs resultForMs = entry.getValue();
            Map<String, Double> hdns = resultForMs.getHublikes();
            Map<String, Double> hdins = resultForMs.getHublikeWithWeight();
            for (String microservice:
                    allMicroservices) {
                StringBuilder sbhd = hds.get(microservice);
                if (sbhd==null){
                    sbhd = new StringBuilder();
                    hds.put(microservice, sbhd);
                }
                Double hdn = hdns.get(microservice);
                if(hdn ==null || hdn==0){
                    sbhd.append(',');
                }else {
                    sbhd.append(hdn).append(',');
                }
                Double hdin = hdins.get(microservice);
                if(hdin ==null || hdin==0){
                    sbhd.append(',');
                }else {
                    sbhd.append(hdin).append(',');
                }
            };
        }
        sb.append('\n');
        for (String microservice:
                allMicroservices) {
            sb.append(microservice).append(',')
                .append(hds.get(microservice)).append('\n');
        }
        sb.append('\n');

        sb.append("CD All\n");
        Map<String, StringBuilder> cds = new HashMap<>();
        sb.append("microservice,");
        for (Map.Entry<String, ResultForMs> entry:
                result.getResults().entrySet()) {
            sb.append("cdn,");
            String version = entry.getKey();
            ResultForMs resultForMs = entry.getValue();
            Map<String, Double> cdns = resultForMs.getCyclic();
            for (String microservice:
                    allMicroservices) {
                StringBuilder sbcd = cds.get(microservice);
                if (sbcd==null){
                    sbcd = new StringBuilder();
                    cds.put(microservice, sbcd);
                }
                Double cdn = cdns.get(microservice);
                if(cdn ==null ){
                    sbcd.append(',');
                }else {
                    sbcd.append(cdn).append(',');
                }
            };
        }
        sb.append('\n');
        sb.append('\n');
        for (String microservice:
                allMicroservices) {
            sb.append(microservice).append(',')
                    .append(cds.get(microservice)).append('\n');
        }
        sb.append('\n');
        sb.append("MV All\n");
        Map<String, StringBuilder> mvs = new HashMap<>();
        sb.append("microservice,");
        for (Map.Entry<String, ResultForMs> entry:
                result.getResults().entrySet()) {
            sb.append("mvdn,mvin,");
            String version = entry.getKey();
            ResultForMs resultForMs = entry.getValue();
            Map<String, MvValue> mvValues = resultForMs.getMvs().get(0).getMvValues();
            for (String microservice:
                    allMicroservices) {
                StringBuilder sbmv = mvs.get(microservice);
                if (sbmv==null){
                    sbmv = new StringBuilder();
                    mvs.put(microservice, sbmv);
                }
                MvValue mv = mvValues.get(microservice);
                if(mv == null){
                    sbmv.append(",,");
                    continue;
                }
                Double mvdn = mv.getDependency();
                Integer mvin = mv.getFile();
                if(mvdn ==null ){
                    sbmv.append(',');
                }else {
                    sbmv.append(mvdn).append(',');
                }
                if(mvin ==null ){
                    sbmv.append(',');
                }else {
                    sbmv.append(mvin).append(',');
                }
            };
        }
        sb.append('\n');
        for (String microservice:
                allMicroservices) {
            sb.append(microservice).append(',')
                    .append(mvs.get(microservice)).append('\n');
        }
        sb.append('\n');

        FileUtils.write(new File(dir.getAbsolutePath()+"/lijiaqidata_3_4.csv"),sb, "utf8", false);
        FileUtils.write(new File(dir.getAbsolutePath()+"/lijiaqidata.json"),jsonObject.toJSONString(), "utf8", false);
        sb = null;

        StringBuilder sb1 = new StringBuilder().append("file,targetFile,count,per\n");
        StringBuilder sb2 = new StringBuilder(sb1);
        StringBuilder sb3 = new StringBuilder(sb1);
        int i=0;
        for (Map.Entry<String, ResultForMs> entry:
                result.getResults().entrySet()) {
            i++;
            if(i!=4){
                continue;
            }
            String version = entry.getKey();
            ResultForMs resultForMs = entry.getValue();
            Mv mv = resultForMs.getMvs().get(0);
            Map<String, MvResult.MvResultForMicroservice> map = mv.getMvResult().getResults();
            MvResult.MvResultForMicroservice mrm1 = map.get("x_13/x_46f");
            statisticsMvResult(sb1, mv, mrm1);
            MvResult.MvResultForMicroservice mrm2 = map.get("x_13/x_663");
            statisticsMvResult(sb2, mv, mrm2);
            MvResult.MvResultForMicroservice mrm3 = map.get("x_3f/x_6f2b");
            statisticsMvResult(sb3, mv, mrm3);
        }
        FileUtils.write(new File(dir.getAbsolutePath()+"/lijiaqidata_3_4_mv_46f.csv"),sb1, "utf8", false);
        FileUtils.write(new File(dir.getAbsolutePath()+"/lijiaqidata_3_4_mv_663.csv"),sb2, "utf8", false);
        FileUtils.write(new File(dir.getAbsolutePath()+"/lijiaqidata_3_4_mv_6f2b.csv"),sb3, "utf8", false);

    }

    private static void statisticsMvResult(StringBuilder sb1, Mv mv, MvResult.MvResultForMicroservice mrm) {
        for (Map.Entry<String, Map<String, Integer>> fileEntry :
                mrm.getFileToFile().entrySet()) {
            String file = fileEntry.getKey();
            Integer fcc = mv.getMvResult().getFileCommitCount().get(file);
            if(fcc<10){
                continue;
            }
            for (Map.Entry<String, Integer> targetFileEntry :
                    fileEntry.getValue().entrySet()) {
                String targetFile = targetFileEntry.getKey();
                Integer count = targetFileEntry.getValue();
                Double per = Double.valueOf(count)/fcc;
                if(per>=0.5) {
                    sb1.append(file).append(',').append(targetFile).append(',').append(count).append(',').append(per).append('\n');
                }
            }
        }
    }

    //皮尔森相关性检验
    public static Double getPearsonBydim(List<Double> ratingOne, List<Double> ratingTwo) {
    if(ratingOne.size() != ratingTwo.size()) {//两个变量的观测值是成对的，每对观测值之间相互独立。
        return null;
    }
    double sim = 0D;//最后的皮尔逊相关度系数
    double commonItemsLen = ratingOne.size();//操作数的个数
    double oneSum = 0D;//第一个相关数的和
    double twoSum = 0D;//第二个相关数的和
    double onePSum = 0D;//第一个相关数平方的和
    double twoPSum = 0D;//第二个相关数平方的和
    double oneTwoSum = 0D;
    for(int i=0; i<commonItemsLen; i++) {
        oneSum += ratingOne.get(i);
        twoSum += ratingTwo.get(i);
        oneTwoSum += ratingOne.get(i) * ratingTwo.get(i);
        onePSum += Math.pow(ratingOne.get(i), 2);
        twoPSum += Math.pow(ratingTwo.get(i), 2);
    }
    double sonSum = oneTwoSum-((oneSum*twoSum)/commonItemsLen);
    double fatherSum = Math.sqrt((onePSum-(Math.pow(oneSum, 2)/commonItemsLen))*(twoPSum-(Math.pow(twoSum, 2)/commonItemsLen)));
    sim = (fatherSum == 0) ? 1 : sonSum / fatherSum;
    return sim;
}
    private static String getMethodName(String fildeName){
        byte[] items = fildeName.getBytes();
        if((char) items[0] >= 'A' && (char) items[0]<='Z'){
            return "get"+fildeName;
        }
        items[0] = (byte) ((char) items[0] - 'a' + 'A');
        return "get"+ new String(items);
    }
    public static MetricsInfo MetricsResult(String version, HashMap<String, GoPackagesInfo> packagesInfoHashMap, List<String> microserviceName, MicroservicesInfo microserviceInfo){
        //if(version.equals("x_1635-x_95d.x_4af.x_893_x_1ff_x_e0af_x_e0a3_x_e0b1")){
        MetricsInfo metricsInfo = MetricsInfo.createInfo(version,packagesInfoHashMap,microserviceName,microserviceInfo);
        for(Metrics metrics:metricsInfo.getMetricsList()){
//                    if(metrics.getElementName().equals("x_13/x_46f")){

            System.out.println(metrics);
//                    }
        }
        System.out.println("版本号："+version+"中共有"+metricsInfo.getMetricsList().size()+"个微服务");
        // }
        return metricsInfo;
    }

    public static void exportExcel(List<MicroserviceAttrsInfo> microserviceAttrsInfos,List<CorrelationDataInfo> correlationDataInfos, List<MetricsInfo> metricsInfos,File dir) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, IOException {
        if(!dir.exists()){
            dir.mkdirs();
        }
        exportExcelBase(microserviceAttrsInfos,correlationDataInfos,metricsInfos, dir);
       // exportExcelForTCommitcountHubLink(microserviceAttrsInfos, dir);
        //exportExcelForTHubLinkCommitcount(microserviceAttrsInfos, dir);
        //exportExcelForTHubLinkPair(microserviceAttrsInfos, dir);

    }
    //计算某一个sheet页的相关性分析
    public static CorrelationDataInfo analysis(MicroserviceAttrsInfo microserviceAttrsInfo,String version) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
       CorrelationDataInfo cdi=new CorrelationDataInfo();
        Field[] fields=MicroserviceAttr.class.getDeclaredFields();
        Map<String,List<Double>> attrsMap=new HashMap<>();//每个String对应一个属性，每个List<Double>对应该属性的一整列的数据
        for(int i=0;i<fields.length;i++){
            if(fields[i].getName().equals("microserviceName")){//微服务名不纳入计算
                     continue;
            }
            List<Double> attrList=new ArrayList<>();
            for(MicroserviceAttr ma:microserviceAttrsInfo.getMicroserviceAttrs()){
                Method m = ma.getClass().getMethod("get" + getMethodName(fields[i].getName()));
                Object val = m.invoke(ma);
                if(val instanceof Double){
                    attrList.add((Double)val);
                }
                else{
                    double p=(val==null?0.0:Double.valueOf(val.toString()));
                    attrList.add((Double)p);
                }
            }
            attrsMap.put(fields[i].getName(),new ArrayList<>(attrList));//深克隆
        }
        List<String> atrlist=new ArrayList<>();//按顺序存储所有待计算的数据项（属性名）
        Set<String> set=attrsMap.keySet();
        for(String key:set){
            atrlist.add(key);
        }
        for(int i=0;i<attrsMap.size();i++){

            for(int j=i;j<attrsMap.size();j++){
                String sourceAttr=atrlist.get(i);
                String targetAttr=atrlist.get(j);
                //是否出现深浅克隆问题
                Ttest t=new Ttest(invertDouble(attrsMap.get(sourceAttr)),invertDouble(attrsMap.get(targetAttr)));
                double pValue=t.getPValue();
                double rValue=getPearsonBydim(attrsMap.get(sourceAttr),attrsMap.get(targetAttr));
                cdi.addCorrelationDta(new CorrelationData(sourceAttr,targetAttr,pValue,rValue));
            }
        }
        cdi.setVersion(version);
        return cdi;
    }
    //List转double[]
    public static  double[] invertDouble(List<Double> dlist){
        int size=dlist.size();
        double[] result=new double[size];
        for(int i=0;i<size;i++){
            result[i]=dlist.get(i).doubleValue();
        }
        return result;
    }

    private static void exportExcelBase(List<MicroserviceAttrsInfo> microserviceAttrsInfos,List<CorrelationDataInfo> correlationDataInfos, List<MetricsInfo> metricsInfos,File dir)throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, IOException {
        Workbook wb = new XSSFWorkbook();
int vi=1;
        for (MicroserviceAttrsInfo mai :
                microserviceAttrsInfos) {
            //创建sheet页，生成表格
            String version = mai.getVersion();
            Sheet sheet = wb.createSheet("version"+vi);
            //创建表头
            Row row = sheet.createRow(0);//第0行
            Field[] fields = MicroserviceAttr.class.getDeclaredFields();//获取表头信息
            for (int i = 0; i < fields.length; i++) {
                Cell cell = row.createCell(i);//每个属性一个格子
                cell.setCellValue(fields[i].getName());
            }
             //基本信息行赋值
            List<MicroserviceAttr> mas = mai.getMicroserviceAttrs();//每个mas都是一行数据
            for (int i = 0; i < mas.size(); i++) {//一行一行赋值
                MicroserviceAttr ma = mas.get(i);
                Row r = sheet.createRow(i + 1);//第1行
                for (int j = 0; j < fields.length; j++) {
                    Cell cell = r.createCell(j);

                    Method m = ma.getClass().getMethod("get" + getMethodName(fields[j].getName()));
                    Object val = m.invoke(ma);
                    if (val != null) {
                        if (val instanceof Double) {
                            cell.setCellValue((Double) val);
                        } else if (val instanceof Long) {
                            cell.setCellValue((Long) val);
                        } else if (val instanceof Integer) {
                            cell.setCellValue((Integer) val);
                        } else if (val instanceof Float) {
                            cell.setCellValue((Float) val);
                        } else if (val instanceof Date) {
                            cell.setCellValue((Date) val);
                        } else {
                            cell.setCellValue(val.toString());
                        }
                    }
                }
            }
            //分析信息行赋值
            //找出version版本对应的分析数据
            List<CorrelationData> cod=new ArrayList<>();//是否会发生深度复制问题？
            for(CorrelationDataInfo cdif:correlationDataInfos){
                if((cdif.getVersion()).equals(version)){
                    cod=cdif.getCorrelationDatas();
                }
            }
            //保存所有参与分析的属性名
            List<String> thead=new ArrayList<>();
            for(CorrelationData heads:cod){
                if(!thead.contains(heads.getSourceAttr())){
                    thead.add(heads.getSourceAttr());
                }
            }
            //创建pValue矩阵表
            Row title=sheet.createRow(mas.size()+2);
            Cell cll=title.createCell(6);
            cll.setCellValue("T-testAnalysis");
            int start=mas.size()+4;//分析数据的起始行行号
            Row rw=sheet.createRow(start);
            for (int i=1;i<thead.size()+1;i++){//第一行第一个空格不赋值
                Cell cell = rw.createCell(i);
                cell.setCellValue(thead.get(i-1));
            }
            for(int i=0;i<thead.size();i++){
                Row r=sheet.createRow(start+i+1);
                Cell cell=r.createCell(0);
                cell.setCellValue(thead.get(i));
            }
            //遍历本页所有分析数据结果，对于每一个结果找到对应的格子位置，填充p值进去
            for(CorrelationData codata:cod){
                int datarow=start+1+getIndexByString(thead,codata.getSourceAttr());
                int datacol=getIndexByString(thead,codata.getTargetAttr())+1;
                Row rowi=sheet.getRow(datarow);
                Cell cell=rowi.createCell(datacol);
                cell.setCellValue(codata.getpValue());
            }

            //创建rValue矩阵表
            Row title1=sheet.createRow(start+thead.size()+2);
            Cell cll1=title1.createCell(6);
            cll1.setCellValue("PearsonAnalysis");
            int start1=start+thead.size()+4;//分析数据的起始行行号
            Row rw1=sheet.createRow(start1);
            for (int i=1;i<thead.size()+1;i++){//第一行第一个空格不赋值
                Cell cell = rw1.createCell(i);
                cell.setCellValue(thead.get(i-1));
            }
            for(int i=0;i<thead.size();i++){
                Row r=sheet.createRow(start1+i+1);
                Cell cell=r.createCell(0);
                cell.setCellValue(thead.get(i));
            }
            //遍历本页所有分析数据结果，对于每一个结果找到对应的格子位置，填充p值进去
            for(CorrelationData codata:cod){
                int datarow1=start1+1+getIndexByString(thead,codata.getSourceAttr());
                int datacol1=getIndexByString(thead,codata.getTargetAttr())+1;
                Row rowj=sheet.getRow(datarow1);
                Cell cell=rowj.createCell(datacol1);
                cell.setCellValue(codata.getrValue());
            }

            //可维护性数据赋值
            //找出version版本对应的可维护性数据
            List<Metrics> mtr=new ArrayList<>();
            for(MetricsInfo mif:metricsInfos){
                if((mif.getVersion()).equals(version)){
                    mtr=mif.getMetricsList();
                }
            }
            Row title3=sheet.createRow(start1+thead.size()+2);
            Cell cell=title3.createCell(6);
            cell.setCellValue("Maintainability Data");
            int start2=start1+thead.size()+4;
            Row rw2=sheet.createRow(start2);
            List<String> attrList=Arrays.asList("elementName","packageCout","fileCount","subTopicsCount","pubTopicsCount","dewightPackageCout","independencePackageCout",
                    "IUCohesion","subTopicSimilarity","pubTopicSimilarity","connectingBlocks","fanInCoupling","fanOutCoupling","fanInAndOutCoupling","fanInMultiOutCoupling",
                    "interdependenceCoupling","fanInOrOutCouplingInSameMicro","fanInMultiOutCouplingInSameMicro","packageIndependenceLevel","fileIndependenceLevel","propagationCost",
                    "subDataStructureCount","pubDataStructureCount","allDataStructureCount","pathNum");
            //Field[] fields1 = Metrics.class.getDeclaredFields();//获取表头信息
            for (int i = 0; i < attrList.size(); i++) {
                Cell cell1 = rw2.createCell(i);//每个属性一个格子
                cell1.setCellValue(attrList.get(i));
            }
            for(int i=0;i<mtr.size();i++){
                Metrics mt=mtr.get(i);
                Row r1=sheet.createRow(start2+i+1);
                for(int j=0;j<attrList.size();j++){
                    Cell cl=r1.createCell(j);
                    Object val;
                    try{
                        Method m = mt.getClass().getMethod("get" + getMethodName(attrList.get(j)));
                        val = m.invoke(mt);
                    }
                    catch(NoSuchMethodException e) {
                        e.printStackTrace();
                        continue;
                    }
                    if (val != null) {
                        if (val instanceof Double) {
                            cl.setCellValue((Double) val);
                        } else if (val instanceof Long) {
                            cl.setCellValue((Long) val);
                        } else if (val instanceof Integer) {
                            cl.setCellValue((Integer) val);
                        } else if (val instanceof Float) {
                            cl.setCellValue((Float) val);
                        } else if (val instanceof Date) {
                            cl.setCellValue((Date) val);
                        } else {
                            cl.setCellValue(val.toString());
                        }
                    }

                }
            }
            vi++;
        }
        File file = new File(dir.getAbsolutePath()+"/"+"analysis.xlsx");
        if(file.exists()){
            FileUtils.forceDelete(file);
        }
        file.createNewFile();
        wb.write(new FileOutputStream(file));
    }
    public static int getIndexByString(List<String> sarr,String attrs){
        int index=0;
        for(int i=0;i<sarr.size();i++){
            if((sarr.get(i)).equals(attrs)){
                index=i;
                break;
            }
        }
        return index;
    }
    private static MicroserviceAttrsInfo microserviceAttrsInfos(String versionName,MicroservicesInfo microservices,
                                                                DtssInfo dtssInfo,
                                                                PairRelationsInfo bugMicroserviceRelations,
                                                                Map<String, GitCommitsForMicroserviceInfo> gitCommitsForMicroserviceInfoMap,
                                                                ArcSmellsInfo arcSmellsInfo) {
                MainTainsInfo mainTainsInfo = MainTainsInfo.newCreateInfo(DataAction.DefaultReposId,
                        microservices,
                        gitCommitsForMicroserviceInfoMap,
                        dtssInfo,
                        bugMicroserviceRelations,
                        versionName
                );
                Map<String, MainTain> map = mainTainsInfo.nameMainTainMap();

                MicroserviceAttrsInfo microserviceAttrsInfo = MicroserviceAttrsInfo.createInfo();
                microserviceAttrsInfo.setVersion(versionName);
                for (Microservice m :
                        microservices) {
                    String name = m.getElementName();
                    Long codeLines = m.getCodeLines();
                    Long annotationLines = m.getAnnotationLines();
                    int pubTopicCount = m.getPubTopics().size();
                    int subTopicCount = m.getSubTopics().size();

                    ArcSmell arcSmell = arcSmellsInfo.find(name);
                    if(arcSmell==null){
                        continue;
                    }
                    else{
                        Long cyclic = arcSmell.getCyclic();
                        Long hublink = arcSmell.getHublink();
                        Long hublinkForIn = arcSmell.getHublinkForIn();
                        Long hublinkForOut = arcSmell.getHublinkForOut();
                        Long mv=arcSmell.getMv();

                        MainTain mainTain = map.get(name);
                        Long bugCount = mainTain.getBugCount();
                        Long commitCount = mainTain.getCommitCount();
                        Long commitAddLineCount = mainTain.getCommitAddLineCount();
                        Long commitDeleteLineCount = mainTain.getCommitDeleteLineCount();
                        Double commitOverlapRatio = mainTain.getCommitOverlapRatio();
                        Double commitFilesetOverlapRatio = mainTain.getCommitFilesetOverlapRatio();
                        Double pairwiseCommitterOverlap = mainTain.getPairwiseCommitterOverlap();

                        MicroserviceAttr ma = new MicroserviceAttr();
                        microserviceAttrsInfo.addMicroserviceAttr(ma);
                        ma.setMicroserviceName(name)
                                .setCodeLines(codeLines)
                                .setPubTopicCount(pubTopicCount)
                                .setSubTopicCount(subTopicCount)
                                .setCyclic(cyclic==null?0:cyclic)
                                .setHublink(hublink==null?0:hublink)
                                .setHublinkForIn(hublinkForIn==null?0:hublinkForIn)
                                .setHublinkForOut(hublinkForOut==null?0:hublinkForOut)
                                .setMv(mv==null?0:mv)
                                .setBugCount(bugCount)
                                .setCommitCount(commitCount)
                                .setCommitAddLineCount(commitAddLineCount)
                                .setCommitDeleteLineCount(commitDeleteLineCount)
                                .setCommitOverlapRatio(commitOverlapRatio)
                                .setCommitFilesetOverlapRatio(commitFilesetOverlapRatio)
                                .setPairwiseCommitterOverlap(pairwiseCommitterOverlap)
                        ;
                    }
                }
        return microserviceAttrsInfo;
    }
}
