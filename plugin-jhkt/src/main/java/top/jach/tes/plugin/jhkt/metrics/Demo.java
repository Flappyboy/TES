package top.jach.tes.plugin.jhkt.metrics;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author: qiming
 * @date: 2020/2/21 0:03
 * @description:
 */
public class Demo {
    public static void main(String[] args){
        //0->1, 0->2, 1->2, 1->3
        int[][] correlations=new int[][]{
            {1,0},
            {2,0},
            {2,1},
            {3,1}
        };
        int[] indegree=new int[4];
//        for(int i=0;i<4;i++){
            for(int[] relation: correlations){
                indegree[relation[0]]++;
            }
//        }
        Queue<Integer> queue=new LinkedList<>();
        for(int i=0;i<4;i++){
            if(indegree[i]==0){
                queue.add(i);
            }
        }
        int count=4;
        while(!queue.isEmpty()){
            int first=queue.poll();
            for(int[] relation: correlations){
                if(relation[1]!=first){continue;}
                indegree[relation[0]]--;
                if(indegree[relation[0]]==0){
                    queue.add(relation[0]);
                }
            }
            count--;
        }
        System.out.println(count==0);
    }
}
