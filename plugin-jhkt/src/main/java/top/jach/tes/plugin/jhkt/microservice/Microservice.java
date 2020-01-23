package top.jach.tes.plugin.jhkt.microservice;

import lombok.Data;
import top.jach.tes.core.impl.domain.element.Element;

import java.util.*;

@Data
public class Microservice extends Element {
    String path;
    String repoName;
    Long annotationLines;
    Long codeLines;
    Set<String> pubTopics = new HashSet<>();
    Set<String> subTopics = new HashSet<>();
    Map<String, Integer> subTopicOneOf = new HashMap<>();

    public Microservice setSubTopicOneOf(Map<String, Integer> subTopicOneOf) {
        this.subTopicOneOf = subTopicOneOf;
        return this;
    }

    public Microservice setElementName(String elementName){
        this.elementName = elementName;
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

    public Microservice setPubTopics(Set<String> pubTopics) {
        this.pubTopics = pubTopics;
        return this;
    }

    public Microservice setSubTopics(Set<String> subTopics) {
        this.subTopics = subTopics;
        return this;
    }
}
