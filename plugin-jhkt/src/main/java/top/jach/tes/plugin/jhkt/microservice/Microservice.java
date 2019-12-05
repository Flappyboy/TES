package top.jach.tes.plugin.jhkt.microservice;

import lombok.Data;

import java.util.List;

@Data
public class Microservice {
    String repoName;
    String path;

    Long annotationLines;
    Long codeLines;
    List<String> pubTopics;
    List<String> subTopics;

    public Microservice setRepoName(String repoName) {
        this.repoName = repoName;
        return this;
    }

    public Microservice setPath(String path) {
        this.path = path;
        return this;
    }

    public Microservice setAnnotationLines(Long annotationLines) {
        this.annotationLines = annotationLines;
        return this;
    }

    public Microservice setCodeLines(Long codeLines) {
        this.codeLines = codeLines;
        return this;
    }

    public Microservice setPubTopics(List<String> pubTopics) {
        this.pubTopics = pubTopics;
        return this;
    }

    public Microservice setSubTopics(List<String> subTopics) {
        this.subTopics = subTopics;
        return this;
    }
}
