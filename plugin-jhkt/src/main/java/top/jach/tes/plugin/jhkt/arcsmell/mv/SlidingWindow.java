package top.jach.tes.plugin.jhkt.arcsmell.mv;

import top.jach.tes.plugin.tes.code.git.commit.DiffFile;
import top.jach.tes.plugin.tes.code.git.commit.GitCommit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SlidingWindow {
   public int window_length;
   public Set<DiffFile> diffFiles = new HashSet<DiffFile>();

   public SlidingWindow(){

   }

   public SlidingWindow(int window_length,  Set<DiffFile> diffFiles){
       this.window_length=window_length;
       this.diffFiles=diffFiles;
   }

   public SlidingWindow setWindowLength(int window_length){
       this.window_length=window_length;
       return this;
   }


    public SlidingWindow setDiffFiles(Set<DiffFile> diffFiles){
        if(diffFiles == null){
            return this;
        }
        this.diffFiles = diffFiles;
        return this;
    }


}
