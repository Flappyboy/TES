package top.jach.tes.plugin.jhkt.microservice;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class Microservice {
    String path;

    Long annotationLines;
    Long codeLines;
    Set<String> pubTopics;
    Set<String> subTopics;

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

    public Microservice setPubTopics(Set<String> pubTopics) {
        this.pubTopics = pubTopics;
        return this;
    }

    public Microservice setSubTopics(Set<String> subTopics) {
        this.subTopics = subTopics;
        return this;
    }
}
