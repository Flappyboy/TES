package top.jach.tes.plugin.tes.code.git.commit;

import lombok.Getter;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.*;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.patch.FileHeader;
import org.eclipse.jgit.patch.HunkHeader;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
public class GitCommit {
    private String sha;
    private String message;
    private String author;
    private List<DiffFile> diffFiles = new ArrayList<>();

    public static GitCommit createByRevCommit(RevCommit commit, Git git, RevWalk revWalk, DiffFormatter df) throws IOException, GitAPIException {
        GitCommit gitCommit = new GitCommit();
        gitCommit.setSha(commit.getName());
        gitCommit.setAuthor(commit.getAuthorIdent().getEmailAddress());
        gitCommit.setMessage(commit.getFullMessage());

        System.out.println(commit.getName());
                /*commit.getName();
                commit.getAuthorIdent().getEmailAddress();
                commit.getAuthorIdent().getName();
                commit.getFullMessage();*/

        // 忽略Merge
        if(commit.getParentCount()>=2 || commit.getParentCount()<=0){
            return null;
        }
        RevCommit parent = commit.getParent(0);
        if(parent == null){
            return null;
        }

        AbstractTreeIterator treeParser = new CanonicalTreeParser(null, git.getRepository().newObjectReader(), commit.getTree());

//        RevWalk revWalk = new RevWalk(git.getRepository());
        RevTree parentTree = revWalk.parseTree(parent);
        AbstractTreeIterator parentTreeParser = new CanonicalTreeParser(null, git.getRepository().newObjectReader(), parentTree);
        List<DiffEntry> diffEntries = git.diff().setOldTree(parentTreeParser).setNewTree(treeParser).call();

        // 每个DiffEntry就是一个文件的修改
        for (DiffEntry diffEntry :
                diffEntries) {
            // 相似度
            diffEntry.getScore();

            FileHeader fileHeader = df.toFileHeader(diffEntry);
            List<HunkHeader> hunks = (List<HunkHeader>) fileHeader.getHunks();
            int addSize = 0, subSize = 0, modifyAddSize = 0, modifySubSize = 0;

            for (HunkHeader hunkHeader :
                    hunks) {
                EditList editList = hunkHeader.toEditList();
                for (Edit edit :
                        editList) {
                    edit.getType().name();
                    switch (edit.getType()){
                        case INSERT:
                            addSize += edit.getLengthB();
                            break;
                        case DELETE:
                            subSize += edit.getLengthA();
                        case REPLACE:
                            modifyAddSize += edit.getLengthB();
                            modifySubSize += edit.getLengthA();
                            break;
                        case EMPTY:
                            break;
                    }
                }
            }
            gitCommit.addDiffFiles(new DiffFile().setChangeType(diffEntry.getChangeType().name())
                    .setOldPath(diffEntry.getOldPath())
                    .setNewPath(diffEntry.getNewPath())
                    .setAddSize(addSize)
                    .setSubSize(subSize)
                    .setModifyAddSize(modifyAddSize)
                    .setModifySubSize(modifySubSize)
            );
                    /*diffEntry.getChangeType().name();
                    diffEntry.getNewPath();
                    diffEntry.getOldPath();*/
        }
        return gitCommit;
    }

    public static GitCommit createByRef(Ref ref, Git git) throws IOException, GitAPIException {
        RevWalk revWalk = new RevWalk(git.getRepository());
        RevCommit revCommit = revWalk.parseCommit(ref.getObjectId());
        return createByRevCommit(revCommit, git);
    }

    public static GitCommit createByRevCommit(RevCommit commit, Git git) throws IOException, GitAPIException {
        DiffFormatter df = new DiffFormatter(new ByteArrayOutputStream());
        df.setDiffComparator(RawTextComparator.WS_IGNORE_ALL);
        df.setRepository(git.getRepository());
        return createByRevCommit(commit, git, new RevWalk(git.getRepository()),  df);
    }

    public GitCommit addDiffFiles(DiffFile... diffFiles){
        for (DiffFile diffFile:
                diffFiles) {
            if (diffFile == null){
                continue;
            }
            this.diffFiles.add(diffFile);
        }
        return this;
    }

    public GitCommit setDiffFiles(List<DiffFile> diffFiles) {
        if(diffFiles == null){
            return this;
        }
        this.diffFiles = diffFiles;
        return this;
    }

    public GitCommit setSha(String sha) {
        this.sha = sha;
        return this;
    }

    public GitCommit setMessage(String message) {
        this.message = message;
        return this;
    }

    public GitCommit setAuthor(String author) {
        this.author = author;
        return this;
    }
}
