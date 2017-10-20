package com.lsh.atp.core.dao;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huangdong on 16/7/7.
 */
public interface BaseDao<T extends Serializable, ID extends Serializable> {

    int insert(T entity);

    int update(T entity);

    int delete(ID id);

    T get(ID id);

    T findOne(T params);

    List<T> findList(T params);

    int count(T params);
}
