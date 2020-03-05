package top.jach.tes.plugin.jhkt.arcsmell.mv;

import org.apache.commons.compress.utils.Lists;
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
    public Map<String,Map<String,Integer>> find(List<Set<String>> w_paths){
        Map<String,Map<String,Integer>> tran=new HashMap<>();//创建一个二维矩阵
        Map<String,Integer> col=new HashMap<>();
        for(Set<String> set:w_paths){
            for(String str:set){
                col.put(str,0);
            }
        }
        List<String> column=new ArrayList<>(new HashSet<>(col.keySet()));//所有文件路径名集合（非重复）
        for(Map.Entry<String, Integer> entry : col.entrySet()){
            tran.put(entry.getKey(),col);//二维矩阵每一行都对应一整个map集合
        }
        //按每个文件路径遍历每一个窗口下的集合
        for(String strs:column){//遍历每个文件路径
            for(int j=0;j<w_paths.size();j++){//遍历每个窗口
                List<String> tlist=new ArrayList<>(new HashSet<>(w_paths.get(j)));//深克隆
                if(tlist.contains(strs)){
                    Map<String,Integer> tmp=new HashMap<>(new HashMap<>(col));
                    for(String che:tlist){
                        if(che.equals(strs)){
                            continue;
                        }
                        else{
                            tmp.put(che,col.get(che)+1);
                        }
                    }
                    //更新tran中原来strs对应的map的values集合
                    Map<String,Integer> check=new HashMap<>(new HashMap<>(tran.get(strs)));
                    for(Map.Entry<String, Integer> ent:tmp.entrySet()){
                        tmp.put(ent.getKey(),check.get(ent.getKey())+ent.getValue());
                    }
                    tran.put(strs,new HashMap<>(tmp));//覆盖上一次的修改
                }
            }
        }
        return tran;
    }
//根据文件名找到对应的微服务
    public String getMicroserviceByPathname(String path,List<Microservice> microservices){
        String mname=null;
        for(Microservice microservice:microservices){
            String mPath=microservice.getAllPath();
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
    //根据filePath找到该文件对应的微服务，处在同一个微服务下的文件合并作为一个元素出现
    public List<Set<String>> fileToMicroservice(List<Set<String>> paths,List<Microservice> microservices){
        List<Set<String>> micros=new ArrayList<>();
        for(Set<String> sets:paths){
            Set<String> tmpset=new HashSet<>();
            for(String str:sets){
                String tmp=getMicroserviceByPathname(str,microservices);
                if(tmpset.contains(tmp)){
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
    //检测算法,目前返回的是null,可返回的数据是一个map矩阵，记录每个文件与其他文件在不同窗口一起出现的次数
    public ElementsValue detect(List<GitCommit> gitCommits,int len,List<Microservice> microservices){
        //List<GitCommit> gitCommits= Lists.newArrayList(gitCommitsInfo.getGitCommits().iterator());
        Collections.sort(gitCommits);//将获取的gitCommits对象集合按照提交时间先后排序（git记录的是秒级的提交时间戳）
        //根据滑动窗口大小设置将gitCommits集合分隔为子集合
        if (gitCommits == null || gitCommits.size() == 0 || len < 1) {
            return null;
        }
        List<List<GitCommit>> windows = new ArrayList<List<GitCommit>>();//窗口的集合
        int size = gitCommits.size(); //传入集合长度
        int count = (size + len - 1) / len;//分隔后的集合个数
        for (int i = 0; i < count; i++) {
            List<GitCommit> subList = gitCommits.subList(i * len, ((i + 1) * len > size ? size : len * (i + 1)));
            windows.add(subList);
        }
        //生成滑动窗口对象列表
        List<SlidingWindow> s_windows=new ArrayList<>();
        for(List<GitCommit> lgc:windows){
            Set<DiffFile> s_diffFile=new HashSet<>();
            for(GitCommit gc:lgc){
                for(DiffFile df:gc.getDiffFiles()){
                    s_diffFile.add(df);
                }
            }
            s_windows.add(new SlidingWindow(len,s_diffFile));
        }

        List<Set<String>> w_paths=new ArrayList<>();//所有窗口中路径集合的集合
        for(int i=0;i<s_windows.size();i++){
            Set<String> s_path=new HashSet<>();//某一个窗口中所有文件路径的集合，多个string形式存储
            for(DiffFile df:s_windows.get(i).getDiffFiles()){
                s_path.addAll(df.getFilePath());
            }
            w_paths.add(s_path);
        }

        //将获取的各个窗口下的路径名集合转换为路径对应微服务的微服务名
        //存在问题，并没有返回正确的文件对应的微服务名
        List<Set<String>> m=fileToMicroservice(w_paths,microservices);
        List<Set<String>> microservs=new ArrayList<>(m);

        //可返回的数据,一个二维矩阵，记录每个file与其他各个file共同出现在不同窗口次数
        Map<String,Map<String,Integer>> resultFiles=find(microservs);
        //统计每个微服务满足mv架构异味的次数
        Map<String,Integer> allmicro=new HashMap<>();
        for(Set<String> set:microservs){
            for(String str:set){
                allmicro.put(str,0);
            }
        }
        List<String> allserves=new ArrayList<>(new HashSet<>(allmicro.keySet()));//所有微服务名不重复
        ElementsValue elmentMv=ElementsValue.createInfo();
        for(String strs:allserves){
            Map<String,Integer> mapItem=resultFiles.get(strs);
            //elment.setName(strs);
            int value=0;
            for(Map.Entry<String,Integer> entry:mapItem.entrySet()){
                value+=entry.getValue();
            }
            elmentMv.put(strs,(double)value);
        }
        return elmentMv;

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
