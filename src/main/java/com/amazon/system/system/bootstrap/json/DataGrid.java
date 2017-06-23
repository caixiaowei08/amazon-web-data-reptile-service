package com.amazon.system.system.bootstrap.json;

import com.amazon.system.system.bootstrap.hibernate.SortDirection;

import java.io.Serializable;

/**
 * Created by User on 2017/6/18.
 */
public class DataGrid implements Serializable{

    private Integer limit = 10;

    private SortDirection order;

    private String sort = "id";

    private Integer offset = 0;

    private String search;

    private String field;

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public SortDirection getOrder() {
        return order;
    }

    public void setOrder(SortDirection order) {
        this.order = order;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
