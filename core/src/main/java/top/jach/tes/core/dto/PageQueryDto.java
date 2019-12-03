package top.jach.tes.core.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageQueryDto<T> {

    private List<T> result;

    private Integer pageNum;

    private Integer pageSize;

    private Long total;

    private PageQueryDto(Integer pageNum, Integer pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public static PageQueryDto create(Integer pageNum, Integer pageSize){
        return new PageQueryDto(pageNum, pageSize);
    }

    public PageQueryDto addResult(List<T> result, Long total){
        this.result = result;
        this.total = total;
        return this;
    }

    public PageQueryDto resultAdd(T t){
        if(this.result == null){
            result = new ArrayList<>();
        }
        this.result.add(t);
        return this;
    }
}
