package top.jach.tes.core.api.dto;

import lombok.Data;
import top.jach.tes.core.api.domain.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于封装查询的条件和结果
 * @param <T>
 */
@Data
public class PageQueryDto<T> {

    private List<T> result;

    private Integer pageNum = 1; // 从1开始

    private Integer pageSize = -1;

    private Long total;

    private String sortField;
    private SortType sortType = SortType.DESC;

    public enum SortType {
        ASC(),
        DESC()
    }

    private PageQueryDto(Integer pageNum, Integer pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public static PageQueryDto LastInfoQueryDto(){
        return create(1,1).setSortField(Entity.FIELD_CREATED_TIME);
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

    public PageQueryDto<T> setSortField(String sortField) {
        this.sortField = sortField;
        return this;
    }

    public PageQueryDto<T> setSortType(SortType sortType) {
        this.sortType = sortType;
        return this;
    }
}
