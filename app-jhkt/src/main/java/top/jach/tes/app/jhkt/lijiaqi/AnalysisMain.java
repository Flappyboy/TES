package top.jach.tes.app.jhkt.lijiaqi;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import top.jach.tes.app.dev.DevApp;
import top.jach.tes.app.mock.Environment;
import top.jach.tes.app.mock.InfoTool;
import top.jach.tes.app.mock.InputInfoProfiles;
import top.jach.tes.core.api.domain.action.Action;
import top.jach.tes.core.api.domain.context.Context;
import top.jach.tes.core.api.domain.info.Info;
import top.jach.tes.core.api.dto.PageQueryDto;
import top.jach.tes.core.api.exception.ActionExecuteFailedException;
import top.jach.tes.core.impl.domain.relation.PairRelationsInfo;
import top.jach.tes.plugin.jhkt.DataAction;
import top.jach.tes.plugin.jhkt.InfoNameConstant;
import top.jach.tes.plugin.jhkt.analysis.MicroserviceAttr;
import top.jach.tes.plugin.jhkt.analysis.MicroserviceAttrsInfo;
import top.jach.tes.plugin.jhkt.arcsmell.ArcSmell;
import top.jach.tes.plugin.jhkt.arcsmell.ArcSmellAction;
import top.jach.tes.plugin.jhkt.arcsmell.ArcSmellsInfo;
import top.jach.tes.plugin.jhkt.dts.DtssInfo;
import top.jach.tes.plugin.jhkt.git.commit.GitCommitsForMicroserviceInfo;
import top.jach.tes.plugin.jhkt.maintain.MainTain;
import top.jach.tes.plugin.jhkt.maintain.MainTainsInfo;
import top.jach.tes.plugin.jhkt.microservice.Microservice;
import top.jach.tes.plugin.jhkt.microservice.MicroservicesInfo;

import java.beans.Beans;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

public class AnalysisMain extends DevApp {
    public static void main(String[] args) throws ActionExecuteFailedException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException {
        Context context = Environment.contextFactory.createContext(Environment.defaultProject);

        MicroservicesInfo microservices = InfoTool.queryLastInfoByNameAndInfoClass(InfoNameConstant.MicroservicesForRepos, MicroservicesInfo.class);
        microservices = MicroservicesInfo.createInfoByExcludeMicroservice(microservices,
                "x_2b", "x_1b", "x_23", "x_1d/x_6eed",
                "x_39","x_1f","x_27/x_25","c_demo/c_demoa","c_demo/c_demob",
                "x_13/x_ae5", "x_25", "x_21/7103");
        microservices.setName(InfoNameConstant.MicroservicesForReposExcludeSomeHistory);

        PairRelationsInfo pairRelationsInfo = microservices.callRelationsInfoByTopic();
        pairRelationsInfo.setName(InfoNameConstant.MicroserviceExcludeSomeCallRelation);
        InfoTool.saveInputInfos(microservices, pairRelationsInfo);


        DtssInfo dtssInfo = InfoTool.queryLastInfoByNameAndInfoClass(InfoNameConstant.BugDts, DtssInfo.class);
        PairRelationsInfo bugMicroserviceRelations = InfoTool.queryLastInfoByNameAndInfoClass(InfoNameConstant.RelationBugAndMicroservice, PairRelationsInfo.class);
        Map<String, GitCommitsForMicroserviceInfo> gitCommitsForMicroserviceInfoMap = new HashMap<>();
        for (Microservice microservice :
                microservices.getMicroservices()) {
            GitCommitsForMicroserviceInfo gitCommitsForMicroserviceInfo = new GitCommitsForMicroserviceInfo();
            gitCommitsForMicroserviceInfo
                    .setReposId(microservices.getReposId())
                    .setMicroserviceName(microservice.getElementName())
                    .setStatisticDiffFiles(null)
                    .setGitCommits(null);
            List<Info> infos = Environment.infoRepositoryFactory.getRepository(GitCommitsForMicroserviceInfo.class)
                    .queryDetailsByInfoAndProjectId(gitCommitsForMicroserviceInfo, Environment.defaultProject.getId(), PageQueryDto.create(1,1).setSortField("createdTime"));
            if(infos.size()>0) {
                gitCommitsForMicroserviceInfoMap.put(microservice.getElementName(), (GitCommitsForMicroserviceInfo)infos.get(0));
            }
        }

        InputInfoProfiles infoProfileMap = InputInfoProfiles.InputInfoProfiles()
                .addInfoProfile(ArcSmellAction.Elements_INFO, microservices)
                .addInfoProfile(ArcSmellAction.PAIR_RELATIONS_INFO, pairRelationsInfo)
                ;

        Action action = new ArcSmellAction();
        ArcSmellsInfo arcSmellsInfo = action.execute(infoProfileMap.toInputInfos(Environment.infoRepositoryFactory), context)
                .getFirstByInfoClass(ArcSmellsInfo.class);

        List<MicroserviceAttrsInfo> microserviceAttrsInfos = microserviceAttrsInfos(microservices, dtssInfo, bugMicroserviceRelations, gitCommitsForMicroserviceInfoMap, arcSmellsInfo);

        exportCSV(microserviceAttrsInfos, new File("F:\\data\\tes\\analysis\\csv"));
        exportExcel(microserviceAttrsInfos, new File("F:\\data\\tes\\analysis"));
    }

    public static void exportCSV(List<MicroserviceAttrsInfo> microserviceAttrsInfos, File dir) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if(!dir.exists()){
            dir.mkdirs();
        }
        FileUtils.cleanDirectory(dir);
        for (MicroserviceAttrsInfo mai :
                microserviceAttrsInfos) {
            String version = mai.getVersion();
            File file = new File(dir.getAbsolutePath()+"/"+version+".csv");
            StringBuilder sb = new StringBuilder();
            Field[] fields = MicroserviceAttr.class.getDeclaredFields();
            for (Field field:
                    fields) {
                sb.append(field.getName());
                sb.append(',');
            }
            sb.append('\n');
            for (MicroserviceAttr ma :
                    mai.getMicroserviceAttrs()) {
                for (Field field:
                        fields) {
                    Method m = ma.getClass().getMethod("get" + getMethodName(field.getName()));
                    Object val = m.invoke(ma);
                    if (val != null) {
                        sb.append(val);
                    }
                    sb.append(',');
                }
                sb.append('\n');
            }
            FileUtils.write(file, sb.toString(), "utf8");
        }
    }

    private static String getMethodName(String fildeName){
        byte[] items = fildeName.getBytes();
        items[0] = (byte) ((char) items[0] - 'a' + 'A');
        return new String(items);
    }

    public static void exportExcel(List<MicroserviceAttrsInfo> microserviceAttrsInfos, File dir) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, IOException {
        if(!dir.exists()){
            dir.mkdirs();
        }
        FileOutputStream outputStream = null;
        Workbook wb = new XSSFWorkbook();
        Workbook wb_t_hublink_commitcount = new XSSFWorkbook(); //根据hublink划分两个样本
        for (MicroserviceAttrsInfo mai :
                microserviceAttrsInfos) {
            String version = mai.getVersion();
            Sheet sheet = wb.createSheet(version);

            Row row = sheet.createRow(0);
            Field[] fields = MicroserviceAttr.class.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(fields[i].getName());
            }
            List<MicroserviceAttr> mas = mai.getMicroserviceAttrs();
            for (int i = 0; i < mas.size(); i++) {
                MicroserviceAttr ma = mas.get(i);
                Row r = sheet.createRow(i+1);
                for (int j = 0; j < fields.length; j++) {
                    Cell cell = r.createCell(j);

                    Method m = ma.getClass().getMethod("get" + getMethodName(fields[j].getName()));
                    Object val = m.invoke(ma);
                    if (val != null) {
                        if(val instanceof Double){
                            cell.setCellValue((Double)val);
                        }else if(val instanceof Long){
                            cell.setCellValue((Long)val);
                        }else if(val instanceof Integer){
                            cell.setCellValue((Integer)val);
                        }else if(val instanceof Float){
                            cell.setCellValue((Float)val);
                        }else if(val instanceof Date){
                            cell.setCellValue((Date)val);
                        }else {
                            cell.setCellValue(val.toString());
                        }
                    }
                }
            }

            // t 检验 hublink commitcount
            Sheet sheet_t_arc = wb_t_hublink_commitcount.createSheet(version);
            List<Pair<Long, Long>> pairs = new ArrayList<>();
            for (MicroserviceAttr ma :
                    mas) {
                Long hublink = ma.getHublink();
                Long commitCount = ma.getCommitCount();
                if(hublink == null || commitCount == null){
                    continue;
                }
                Pair<Long, Long> pair = Pair.of(hublink, commitCount);
                pairs.add(pair);
            }
            Collections.sort(pairs, (o1, o2) -> {
                /*if(o1.getLeft() == null && o2.getLeft() == null){
                    return 0;
                }
                if(o1.getLeft()==null){
                    return -1;
                }else if(o2.getLeft()==null){
                    return 1;
                }*/
                return o1.getLeft().intValue()-o2.getLeft().intValue();
            });
            for (int i = 0; i < pairs.size() / 2; i++) {
                Row r = sheet_t_arc.getRow(i);
                if(r==null){
                    r = sheet_t_arc.createRow(i);
                }
                r.createCell(0).setCellValue(pairs.get(i).getRight());
            }
            for (int i = pairs.size() / 2; i < pairs.size(); i++) {
                Row r = sheet_t_arc.getRow(i-pairs.size()/2);
                if(r==null){
                    r = sheet_t_arc.createRow(i-pairs.size()/2);
                }
                r.createCell(1).setCellValue(pairs.get(i).getRight());
            }
        }

        File file = new File(dir.getAbsolutePath()+"/"+"analysis.xlsx");
        if(file.exists()){
            FileUtils.forceDelete(file);
        }
        file.createNewFile();
        wb.write(new FileOutputStream(file));

        File file_t_hublink_commitcount = new File(dir.getAbsolutePath()+"/"+"analysis_t_hublink_commitcount.xlsx");
        if(file_t_hublink_commitcount.exists()){
            FileUtils.forceDelete(file_t_hublink_commitcount);
        }
        file_t_hublink_commitcount.createNewFile();
        wb_t_hublink_commitcount.write(new FileOutputStream(file_t_hublink_commitcount));
    }

    private static List<MicroserviceAttrsInfo> microserviceAttrsInfos(MicroservicesInfo microservices,
                                                                      DtssInfo dtssInfo,
                                                                      PairRelationsInfo bugMicroserviceRelations,
                                                                      Map<String, GitCommitsForMicroserviceInfo> gitCommitsForMicroserviceInfoMap,
                                                                      ArcSmellsInfo arcSmellsInfo) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        format.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        List<MicroserviceAttrsInfo> microserviceAttrsInfos = new ArrayList<>();
        int[] ds = {1,2,3,6};
        for (int di = 0; di < ds.length; di++) {
            int d = ds[di];
            for (int i = 0; i+d < 7; i++) {
                Calendar start = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai"));
                start.set(2019, 5+i, 1);
                Calendar end = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai"));
                end.set(2019, 5+i+d, 1);

                MainTainsInfo mainTainsInfo = MainTainsInfo.createInfo(DataAction.DefaultReposId,
                        microservices,
                        gitCommitsForMicroserviceInfoMap,
                        dtssInfo,
                        bugMicroserviceRelations,
                        start.getTimeInMillis(),
                        end.getTimeInMillis()
                );
                Map<String, MainTain> map = mainTainsInfo.nameMainTainMap();

                MicroserviceAttrsInfo microserviceAttrsInfo = MicroserviceAttrsInfo.createInfo();
                microserviceAttrsInfo.setVersion(format.format(start.getTime())+"_"+format.format(end.getTime()));
                microserviceAttrsInfos.add(microserviceAttrsInfo);
                for (Microservice m :
                        microservices) {
                    String name = m.getElementName();
                    Long codeLines = m.getCodeLines();
                    Long annotationLines = m.getAnnotationLines();
                    int pubTopicCount = m.getPubTopics().size();
                    int subTopicCount = m.getSubTopics().size();

                    ArcSmell arcSmell = arcSmellsInfo.find(name);
                    Long cyclic = arcSmell.getCyclic();
                    Long hublink = arcSmell.getHublink();

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
        }
        return microserviceAttrsInfos;
    }
}
