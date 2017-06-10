package org.framework.core.common.entity;

import javax.persistence.*;

/**
 * Created by User on 2017/6/5.
 */
@MappedSuperclass
public abstract class IdEntity {

    /**
     * 主键
     */
    private Integer id;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="id",nullable=false,length=20)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
