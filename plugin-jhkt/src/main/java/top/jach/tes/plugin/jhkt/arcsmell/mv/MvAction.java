package top.jach.tes.plugin.jhkt.arcsmell.mv;

import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import top.jach.tes.core.api.domain.action.Action;
import top.jach.tes.core.api.domain.action.DefaultOutputInfos;
import top.jach.tes.core.api.domain.action.InputInfos;
import top.jach.tes.core.api.domain.action.OutputInfos;
import top.jach.tes.core.api.domain.context.Context;
import top.jach.tes.core.api.domain.meta.Meta;
import top.jach.tes.core.api.exception.ActionExecuteFailedException;
import top.jach.tes.core.impl.domain.element.ElementsValue;
import top.jach.tes.plugin.jhkt.microservice.Microservice;
import top.jach.tes.plugin.jhkt.microservice.MicroservicesInfo;
import top.jach.tes.plugin.tes.code.git.commit.DiffFile;
import top.jach.tes.plugin.tes.code.git.commit.GitCommit;
import top.jach.tes.plugin.tes.code.git.commit.GitCommitsInfo;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class MvAction implements Action {
    public static final String MICROSERVICE_INFO = "MicroserviceInfo";
    public static final String GIT_COMMITS_INFO = "GitCommitsInfo";


    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getDesc() {
        return null;
    }

    @Override
    public Meta getInputMeta() {
        return null;
    }

    //寻找多个滑动窗口中重复出现的提交文件及出现次数
    public Map<String,Map<String,Integer>> find(List<Set<String>> w_paths, List<String> microservices){
        Map<String,Map<String,Integer>> tran=new HashMap<>();//创建一个二维矩阵
        Map<String,Integer> col=new HashMap<>();
        for(Set<String> set:w_paths){
            for(String str:set){
                col.put(str,0);
            }
        }
        List<String> column=new ArrayList<>(new HashSet<>(col.keySet()));//所有文件路径名集合（非重复）
        for(Map.Entry<String, Integer> entry : col.entrySet()){
            tran.put(entry.getKey(),new HashMap<>());//二维矩阵每一行都对应一整个map集合
        }
        //按每个文件路径遍历每一个窗口下的集合
        for(String strs:column){//遍历每个文件路径
            String m = getMicroservicePathByPathname(strs, microservices);
            Map<String,Integer> tmp = tran.get(strs);
            for(int j=0;j<w_paths.size();j++){//遍历每个窗口
                Set<String> tlist = w_paths.get(j);
                if(tlist.contains(strs)){
                    for(String che:tlist){
                        if(che.startsWith(m)){
                            continue;
                        }
                        else{
                            Integer count = tmp.get(che);
                            if (count == null){
                                count = 0;
                            }
                            tmp.put(che,count+1);
                        }
                    }
                    //更新tran中原来strs对应的map的values集合
                    /*Map<String,Integer> check=new HashMap<>(new HashMap<>(tran.get(strs)));
                    for(Map.Entry<String, Integer> ent:tmp.entrySet()){
                        tmp.put(ent.getKey(),check.get(ent.getKey())+ent.getValue());
                    }*/
                }
            }
        }
        return tran;
    }
//根据文件名找到对应的微服务
    public static String getMicroserviceByPathname(String path,List<Microservice> microservices){
        String mname=null;
        for(Microservice microservice:microservices){
            String mPath=microservice.getAllPath();
//            String mPath=microservice.getPath();
            if(mPath==null){
                continue;
            }
            if(path.startsWith(mPath)){
                mname=microservice.getElementName();
                break;
            }
        }
        return mname;
    }
    public static String getMicroservicePathByPathnameAndMs(String path,List<Microservice> microservices){
        String mname=null;
        for(Microservice microservice:microservices){
            String mPath=microservice.getAllPath();
//            String mPath=microservice.getPath();
            if(mPath==null){
                continue;
            }
            if(path.startsWith(mPath)){
                mname=mPath;
                break;
            }
        }
        return mname;
    }
    public static String getMicroservicePathByPathname(String path,List<String> microservices){
        String mname=null;
        for(String mPath:microservices){
//            String mPath=microservice.getPath();
            if(mPath==null){
                continue;
            }
            if(path.startsWith(mPath)){
                mname=mPath;
                break;
            }
        }
        return mname;
    }
    //根据filePath找到该文件对应的微服务，处在同一个微服务下的文件合并作为一个元素出现
    public static List<Set<String>> fileToMicroservice(List<Set<String>> paths,List<Microservice> microservices){
        List<Set<String>> micros=new ArrayList<>();
        for(Set<String> sets:paths){
            Set<String> tmpset=new HashSet<>();
            for(String str:sets){
                String tmp=getMicroserviceByPathname(str,microservices);
                if(tmpset.contains(tmp) || StringUtils.isBlank(tmp)){
                    continue;//若当前路径所在微服务在之前的遍历中已经出现过，则不重复添加
                }
                else{
                    tmpset.add(tmp);
                }
            }
            micros.add(tmpset);
        }
        return micros;
    }
    public static MvResult detectMvResult(List<GitCommit> gitCommits,int len,List<Microservice> microservices){
        if (gitCommits == null || gitCommits.size() == 0 || len < 1) {
            return null;
        }
        //将获取的gitCommits对象集合按照提交时间先后排序（git记录的是秒级的提交时间戳）
        Collections.sort(gitCommits);
        //生成从提交信息中提取出文件集合
        Queue<Set<String>> blocks = generateFileSetBlocks(gitCommits, microservices);
        //获取每个文件提交的次数
        Map<String, Integer> fileCount = findFileCommitCount(blocks);
        // 初始化一个len长度的滑动窗口
        SlidingWindow slidingWindow = generateSlidingWindow(len, blocks);
        Map<String,Map<String,Integer>> resultFiles = slidingWindow.slideBlocks(blocks, path -> getMicroservicePathByPathnameAndMs(path, microservices));
        return new MvResult(len, resultFiles, fileCount, microservices);
    }
    //检测算法,目前返回的是null,可返回的数据是一个map矩阵，记录每个文件与其他文件在不同窗口一起出现的次数
    public static ElementsValue detect(List<GitCommit> gitCommits,int len, int minCommitCount, double minPer,List<Microservice> microservices){
        if (gitCommits == null || gitCommits.size() == 0 || len < 1) {
            return null;
        }
        MvResult  mvResult = detectMvResult(gitCommits, len, microservices);
        Map<String, MvValue> mmvs = mvResult.calculateMvValues(minCommitCount, minPer);
        Map<String, Double> ms = new HashMap<>();
        for (Map.Entry<String,MvValue> entry:
            mmvs.entrySet()){
            ms.put(entry.getKey(), Double.valueOf(entry.getValue().getFile()));
        }
        ElementsValue elmentMv=ElementsValue.createInfo();
        elmentMv.setValueMap(ms);
        return elmentMv;
    }


    private static SlidingWindow generateSlidingWindow(int len, Queue<Set<String>> blocks) {
        List<Set<String>> bs = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            bs.add(blocks.poll());
        }
        return new SlidingWindow(bs);
    }

    private static Map<String, Integer> findFileCommitCount(Queue<Set<String>> blocks) {
        Map<String, Integer> fileCount = new HashMap<>();
        for (Set<String> block :
                blocks) {
            for (String file :
                    block) {
                Integer c = fileCount.get(file);
                if (c == null){
                    c = 0;
                }
                fileCount.put(file, c+1);
            }
        }
        return fileCount;
    }

    private static Queue<Set<String>> generateFileSetBlocks(List<GitCommit> gitCommits, List<Microservice> microservices) {
        Queue<Set<String>> blocks=new LinkedBlockingQueue<>();
        for(GitCommit gc:gitCommits){
            Set<String> block=new HashSet<>();
            for(DiffFile df:gc.getDiffFiles()){
                for (String p :
                        df.getFilePath()) {
                    String m = getMicroserviceByPathname(gc.getRepoName()+"/"+p,microservices);
                    if (m!=null){
                        block.add(gc.getRepoName()+"/"+p);
                    }
                }
            }
            if(block.size()>0) {
                blocks.add(block);
            }
        }
        return blocks;
    }

    @Override
    public OutputInfos execute(InputInfos inputInfos, Context context) throws ActionExecuteFailedException {
       /* GitCommitsInfo gitCommitsInfo=inputInfos.getInfo(GIT_COMMITS_INFO,GitCommitsInfo.class);
        MicroservicesInfo microservicesInfo=inputInfos.getInfo(MICROSERVICE_INFO,MicroservicesInfo.class);
        List<Microservice> microservices=Lists.newArrayList(microservicesInfo.getMicroservices().iterator());*/

        //计算出各个微服务同时在一个窗口下出现次数,返回所有微服务满足该AS的次数
        //参数6表示设置6个gitCommits组成一个窗口
        //ElementsValue elementMv=detect(gitCommitsInfo,6,microservices);
        return null;
       // return DefaultOutputInfos.WithSaveFlag(elementMv);
    }
}
