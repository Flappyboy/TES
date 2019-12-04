package top.jach.tes.plugin.tes.code.git.commit;

public class Commit {
    String sha;
    String message;
    String author;

    public String getSha() {
        return sha;
    }

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }

    public Commit setSha(String sha) {
        this.sha = sha;
        return this;
    }

    public Commit setMessage(String message) {
        this.message = message;
        return this;
    }

    public Commit setAuthor(String author) {
        this.author = author;
        return this;
    }
}
