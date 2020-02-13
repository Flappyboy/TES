package top.jach.tes.plugin.jhkt.arcsmell.mv;

import org.bouncycastle.math.ec.custom.sec.SecT113R1Curve;
import org.junit.Test;
import top.jach.tes.plugin.tes.code.git.commit.DiffFile;

import java.util.*;

import static org.junit.Assert.*;

public class MvActionTest {
/*List<Set<String>> tmp_paths=new ArrayList<>();//备份，并保持两个list顺序也一致
        for(int k=0;k<w_paths.size();k++){
            tmp_paths.add(w_paths.get(k));
        }*/
 /* List<Set<String>> w_paths=new ArrayList<>();//所有窗口中路径集合的集合
        for(int i=0;i<slidingWindows.size();i++){
            Set<String> s_path=new HashSet<>();//某一个窗口中所有文件路径的集合，多个string形式存储
            for(DiffFile df:slidingWindows.get(i).diffFiles){
                s_path.addAll(df.getFilePath());
            }
            w_paths.add(s_path);
        }*/

    @Test
    public void excute(){
        List<Set<String>> paths=new ArrayList<>();
        List<String> l1 = Arrays.asList("f1","f2","f3");
        List<String> l2= Arrays.asList("f1","f2","f5");
        List<String> l3= Arrays.asList("f4","f5","f6");
        List<String> l4= Arrays.asList("f3","f5","f6");
        List<String> l5= Arrays.asList("f1","f3","f5");
        List<String> l6= Arrays.asList("f2","f3","f6");
        Set<String> s1=new HashSet<>();
        Set<String> s2=new HashSet<>();
        Set<String> s3=new HashSet<>();
        Set<String> s4=new HashSet<>();
        Set<String> s5=new HashSet<>();
        Set<String> s6=new HashSet<>();
        s1.addAll(l1);
        s2.addAll(l2);
        s3.addAll(l3);
        s4.addAll(l4);
        s5.addAll(l5);
        s6.addAll(l6);
        paths.add(s1);
        paths.add(s2);
        paths.add(s3);
        paths.add(s4);
        paths.add(s5);
        paths.add(s6);

       // List<Map<Set<String>,Integer>> result=find(paths);
        Map<String,Map<String,Integer>> tran=new HashMap<>();//创建一个二维矩阵
        Map<String,Integer> col=new HashMap<>();
        for(Set<String> set:paths){
            for(String str:set){
                col.put(str,0);
               // column.add(str);
            }
        }
        List<String> column=new ArrayList<>(new HashSet<>(col.keySet()));//所有文件路径名集合（非重复）
        for(Map.Entry<String, Integer> entry : col.entrySet()){
            tran.put(entry.getKey(),col);//二维矩阵每一行都对应一整个map集合
        }
        //遍历每一个窗口下的集合
       //按每个文件路径遍历
        for(String strs:column){//遍历每个文件路径
            for(int j=0;j<paths.size();j++){//遍历每个窗口
                List<String> tlist=new ArrayList<>(new HashSet<>(paths.get(j)));//深克隆
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
                   /* for(int i=1;i<tlist.size();i++){
                        tmp.put(tlist.get(i),col.get(tlist.get(i))+1);
                    }*/
                    //更新tran中原来strs对应的map的values集合
                        Map<String,Integer> check=new HashMap<>(new HashMap<>(tran.get(strs)));
                        for(Map.Entry<String, Integer> ent:tmp.entrySet()){
                            tmp.put(ent.getKey(),check.get(ent.getKey())+ent.getValue());
                        }
                    tran.put(strs,new HashMap<>(tmp));//覆盖了上一次的修改
                }
            }
        }
        //输出乱七八糟，肯定是算法哪里出错了
        for(String cols:column){
            System.out.print(cols+":");
            System.out.println("");
            Map<String,Integer> mapItem=tran.get(cols);
            for(Map.Entry<String,Integer> entry:mapItem.entrySet()){
                System.out.print(" "+entry.getKey()+"---"+entry.getValue());
            }
            System.out.println("");
        }

        /*for(Map<String,Map<String,Integer>> tes:tran){
            System.out.println(tran);
        }*/

    }


}