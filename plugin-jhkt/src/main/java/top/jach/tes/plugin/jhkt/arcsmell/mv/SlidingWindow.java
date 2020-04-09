package top.jach.tes.plugin.jhkt.arcsmell.mv;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class SlidingWindow {

   private Queue<Set<String>> blocks = new LinkedBlockingQueue<>();
   private Map<String, Map<String, Integer>> matrix = new HashMap<>();

   public SlidingWindow(List<Set<String>> blocks){
       for (Set<String> block :
               blocks) {
           this.blocks.add(block);
       }
   }
   public Map<String, Map<String, Integer>> slideBlocks(Iterable<Set<String>> blocks, FilePrefix filePrefix){
       for (Set<String> block:
            blocks) {
           slide(block, filePrefix);
       }
       while (this.blocks.size()>0){
           slide(null, filePrefix);
       }
       return matrix;
   }

   public void slide(Set<String> newBlock, FilePrefix filePrefix){
       Set<String> target = blocks.peek();
       for (String tfile :
               target) {
           String prefix = filePrefix.getPrefix(tfile);
           Map<String, Integer> trelationCount = getRelationCount(tfile);
           for (Set<String> block:
                blocks){
               for (String file :
                       block) {
                   if(file.startsWith(prefix)){
                       continue;
                   }
                   // tfile->file
                   Integer count = trelationCount.get(file);
                   if (count == null){
                       count = 0;
                   }
                   trelationCount.put(file,count+1);

                   // file->tfile
                   Map<String, Integer> relationCount = getRelationCount(file);
                   count = relationCount.get(tfile);
                   if (count == null){
                       count = 0;
                   }
                   relationCount.put(tfile,count+1);
               }
           }
       }

       blocks.poll();
       if (newBlock != null) {
           blocks.add(newBlock);
       }
   }

   public interface FilePrefix {
       String getPrefix(String path);
   }

   private Map<String, Integer> getRelationCount(String file){
       Map<String, Integer> relationCount = matrix.get(file);
       if(relationCount == null){
           relationCount = new HashMap<>();
           matrix.put(file, relationCount);
       }
       return relationCount;
   }

}
