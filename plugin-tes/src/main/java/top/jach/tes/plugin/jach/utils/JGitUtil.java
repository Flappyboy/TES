package top.jach.tes.plugin.jach.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.ArchiveCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.RmCommand;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.archive.TarFormat;
import org.eclipse.jgit.archive.TgzFormat;
import org.eclipse.jgit.archive.ZipFormat;
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.filter.RevFilter;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.FetchResult;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

public class JGitUtil {
    public static final String HEAD = "HEAD";
    // git 锁超时时间，单位秒
    public static final String GITKEEP = ".gitkeep";
    private static final String REFS_TAGS_PREFIX = "refs/tags/";

    /**
     * "zip","tar","tar.gz"
     */
    public interface ArchiveFormatName {
        String zip = "zip";
        String tar = "tar";
        String tar_gz = "tar.gz";
    }

    public static final String GIT_SUFFIX = ".git";
    public static final String GITIGNORE = ".gitignore";

    static {
        ArchiveCommand
                .registerFormat(ArchiveFormatName.tar_gz, new TgzFormat());
        ArchiveCommand.registerFormat(ArchiveFormatName.tar, new TarFormat());
        ArchiveCommand.registerFormat(ArchiveFormatName.zip, new ZipFormat());
    }

    public static void doInit(File gitDir) throws GitAPIException {
        if (!gitDir.exists())
            gitDir.mkdirs();
        try (Git git = Git.init().setDirectory(gitDir).setBare(false).call()) {
        }
    }

    public static void doClone(File gitDir, String gituri,
                               CredentialsProvider credentialsProvider, String branch)
            throws GitAPIException {
        Git.cloneRepository().setURI(gituri)
                .setCredentialsProvider(credentialsProvider)
                .setDirectory(gitDir).setCloneAllBranches(false)
                .setBranch(branch).call().close();
    }

    public static PullResult doPull(File gitDir, String url,
                                    CredentialsProvider credentialsProvider) throws IOException,
            GitAPIException {
        try (Git git = Git.open(gitDir)) {
            return git.pull().setCredentialsProvider(credentialsProvider)
                    .call();
        }
    }

    public static FetchResult doForceUpdate(File gitDir, CredentialsProvider credentialsProvider) throws IOException,
            GitAPIException {
        Git git = null;
        try {
            git = Git.open(gitDir);
            FetchResult ret = git.fetch().setCredentialsProvider(credentialsProvider).call();
            git.reset().setMode(ResetType.HARD).setRef(Constants.FETCH_HEAD).call();
            return ret;
        } finally {
            if (git != null)
                git.close();
        }
    }

    public static void doAdd(File gitDir, String... files)
            throws GitAPIException, IOException {
        try (Git git = Git.open(gitDir)) {
            AddCommand add = git.add();
            if (ArrayUtils.isEmpty(files))
                add.addFilepattern(".");
            else
                for (String file : files) {
                    add.addFilepattern(file);
                }
            add.call();
        }
    }

    public static void doRm(File gitDir, String... filepatterns)
            throws GitAPIException, IOException {
        if (ArrayUtils.isEmpty(filepatterns)
                && ArrayUtils
                .isEmpty(filepatterns = listFileNamesExceptGit(gitDir)))
            return;
        try (Git git = Git.open(gitDir)) {
            RmCommand rm = git.rm().setCached(false);
            for (String filepattern : filepatterns) {
                rm.addFilepattern(filepattern);
            }
            rm.call();
        }
    }

    private static String[] listFileNamesExceptGit(File gitDir) {
        return gitDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return !GIT_SUFFIX.equals(name) && !GITIGNORE.equals(name);
            }
        });
    }

    public static void doTag(File gitDir, String tagname, String message)
            throws GitAPIException, IOException {
        try (Git git = Git.open(gitDir)) {
            git.tag().setName(tagname).setMessage(message).call();
        }
    }

    public static List<String> doTagDelete(File gitDir, String... tags)
            throws IOException, GitAPIException {
        try (Git git = Git.open(gitDir)) {
            return git.tagDelete().setTags(tags).call();
        }
    }

    public static List<Ref> doTagList(File gitDir) throws IOException,
            GitAPIException {
        try (Git git = Git.open(gitDir)) {
            return git.tagList().call();
        }
    }

    public static void doCommit(File gitDir, String message)
            throws GitAPIException, IOException {
        try (Git git = Git.open(gitDir)) {
            // commit
            CommitCommand commit = git.commit().setAll(false)
                    .setMessage(message);
            commit.call();
        }
    }

    public static UsernamePasswordCredentialsProvider getCredentialsProvider(
            String gituser, String gitpasswd) {
        UsernamePasswordCredentialsProvider credentialsProvider = null;
        if (StringUtils.isNotEmpty(gituser)
                && StringUtils.isNotEmpty(gitpasswd)) {
            credentialsProvider = new UsernamePasswordCredentialsProvider(
                    gituser, gitpasswd);
        }
        return credentialsProvider;
    }

    public static Iterable<PushResult> doPush(File gitDir,
                                              CredentialsProvider credentialsProvider) throws IOException,
            GitAPIException {
        try (Git git = Git.open(gitDir)) {
            PushCommand push = git.push().setCredentialsProvider(
                    credentialsProvider);
            return push.call();
        }
    }

    public static Iterable<PushResult> doPushTags(File gitDir,
                                                  CredentialsProvider credentialsProvider) throws IOException,
            GitAPIException {
        try (Git git = Git.open(gitDir)) {
            PushCommand push = git.push().setCredentialsProvider(
                    credentialsProvider);
            return push.setPushTags().call();
        }
    }

    public static RevCommit doGetCommit(File gitdir, String branchOrSha)
            throws IOException {
        try (Git git = Git.open(gitdir)) {
            Repository repo = git.getRepository();
            ObjectId resolve = repo.resolve(branchOrSha);
            return GitLowLevelAPI.buildRevCommit(repo, resolve.getName());
        }
    }

    public static Iterable<RevCommit> doLog(File gitdir, String path,
                                            int maxCount, AnyObjectId not, AnyObjectId add, RevFilter revFilter)
            throws IOException, GitAPIException {
        try (Git git = Git.open(gitdir)) {
            LogCommand log = git.log();
            if (StringUtils.isNotEmpty(path))
                log.addPath(path);
            if (maxCount > 0)
                log.setMaxCount(maxCount);
            if (revFilter != null)
                log.setRevFilter(revFilter);
            if (not != null) {
                log.not(not);
            }
            if (add != null)
                log.add(add);
            return log.call();
        }
    }

    /**
     * 获取仓库分支路径下的文件列表
     *
     * @param gitdir
     * @param branchOrSha
     * @param path
     * @param handler
     * @throws Exception
     */
    public static void dolsTree(File gitdir, String branchOrSha, String path,
                                boolean recursive, IGitTreeWorkHandler handler) throws Exception {
        try (Repository repository = GitLowLevelAPI.openRepository(gitdir)) {
            final ObjectId testbranch = repository.resolve(branchOrSha);
            GitLowLevelAPI.readElementsAt(repository, testbranch.getName(),
                    path, recursive, handler);

        }
    }

    public static String getShortRefName(String refName) {
        if (StringUtils.startsWith(refName, REFS_TAGS_PREFIX))
            return StringUtils.substring(refName, REFS_TAGS_PREFIX.length());
        return refName;
    }

    /**
     * @param fmt
     *            {@link ArchiveFormatName} "zip","tar","tar.gz"
     */
    public static void doArchive(File gitdir, OutputStream out,
                                 String branchOrSha, String fmt, String... paths)
            throws IOException, GitAPIException {
        try (Git git = Git.open(gitdir)) {
            ArchiveCommand archive = git.archive().setOutputStream(out)
                    .setTree(git.getRepository().resolve(branchOrSha));
            if (fmt != null)
                archive.setFormat(fmt);
            if (paths != null)
                archive.setPaths(paths);
            archive.call();
        }
    }

    public static void doGetFileContent(File gitdir, final OutputStream out,
                                        String branchOrSha, String blobId, String path) throws Exception {
        GitLowLevelAPI.doGetFileContent(gitdir, out, branchOrSha, blobId, path);
    }

    public abstract static class GitLowLevelAPI {
        private GitLowLevelAPI() {
        }

        static void readElementsAt(Repository repository, String commit,
                                   String path, boolean recursive, IGitTreeWorkHandler handler)
                throws Exception {
            RevCommit revCommit = buildRevCommit(repository, commit);
            RevTree tree = revCommit.getTree();
            if (StringUtils.isEmpty(path)) {
                try (TreeWalk treeWalk = new TreeWalk(repository)) {
                    treeWalk.addTree(tree);
                    treeWalk.setRecursive(recursive);
                    treeWalk.setPostOrderTraversal(recursive);

                    while (treeWalk.next()) {
                        if (handler != null)
                            handler.handle(repository, treeWalk);
                    }
                }
            } else {
                try (TreeWalk treeWalk = buildTreeWalk(repository, tree, path)) {
                    if ((treeWalk.getFileMode(0).getBits() & FileMode.TYPE_TREE) == 0) {
                        throw new IllegalStateException(
                                "Tried to read the elements of a non-tree for commit '"
                                        + commit + "' and path '" + path
                                        + "', had filemode "
                                        + treeWalk.getFileMode(0).getBits());
                    }

                    try (TreeWalk dirWalk = new TreeWalk(repository)) {
                        dirWalk.addTree(treeWalk.getObjectId(0));
                        dirWalk.setRecursive(recursive);
                        dirWalk.setPostOrderTraversal(recursive);
                        while (dirWalk.next()) {
                            if (handler != null)
                                handler.handle(repository, dirWalk);
                        }
                    }
                }
            }
        }

        static Repository openRepository(File gitdir) throws IOException {
            FileRepositoryBuilder builder = new FileRepositoryBuilder();
            return builder.setGitDir(new File(gitdir, JGitUtil.GIT_SUFFIX))
                    .readEnvironment().findGitDir().build();
        }

        static RevCommit buildRevCommit(Repository repository, String commit)
                throws IOException {
            try (RevWalk revWalk = new RevWalk(repository)) {
                return revWalk.parseCommit(ObjectId.fromString(commit));
            }
        }

        private static TreeWalk buildTreeWalk(Repository repository,
                                              RevTree tree, final String path) throws IOException {
            TreeWalk treeWalk = TreeWalk.forPath(repository, path, tree);

            if (treeWalk == null) {
                throw new FileNotFoundException("Did not find expected file '"
                        + path + "' in tree '" + tree.getName() + "'");
            }

            return treeWalk;
        }

        static void doGetFileContent(File gitdir, OutputStream out,
                                     String branchOrSha, String blobId, String path)
                throws IOException {
            if (StringUtils.isEmpty(branchOrSha))
                branchOrSha = "HEAD";
            try (Repository repository = openRepository(gitdir)) {
                if (StringUtils.isNotEmpty(blobId)) {
                    ObjectLoader loader = repository.open(repository
                            .resolve(blobId));
                    loader.copyTo(out);
                } else
                    try (RevWalk revWalk = new RevWalk(repository)) {
                        RevCommit commit = revWalk.parseCommit(repository
                                .resolve(branchOrSha));
                        RevTree tree = commit.getTree();

                        try (TreeWalk treeWalk = new TreeWalk(repository)) {
                            treeWalk.addTree(tree);
                            treeWalk.setRecursive(true);
                            treeWalk.setFilter(PathFilter.create(path));
                            if (!treeWalk.next()) {
                                throw new IllegalStateException(
                                        "Did not find expected file " + path);
                            }

                            ObjectId objectId = treeWalk.getObjectId(0);
                            ObjectLoader loader = repository.open(objectId);
                            loader.copyTo(out);
                        }
                    }
            }
        }
    }

    public interface IGitTreeWorkHandler {
        void handle(Repository repository, TreeWalk treeWalk) throws Exception;
    }
}