package com.steptowin.core.db.dao;

import com.j256.ormlite.stmt.PreparedQuery;

import java.util.List;

/**
 * @Desc:
 * @Author: zg
 * @Time:
 */
public interface IDao<T> {
    /**
     * @param t
     * @return int The number of rows updated in the database. This should be 1.
     * @throws
     * @Title: insert
     * @Description: 将数据插入到数据库当中 Create a new row in the database from an
     * object. If the object being created uses
     * DatabaseField.generatedId() then the data parameter will be
     * modified and set with the corresponding id from the
     * database.
     */
    Long insert(T t);

    /**
     * This is a convenience method to creating a data item but only if the ID does not already exist in the table.
     * This extracts the id from the data parameter, does a queryForId(Object) on it, returning the data if it exists.
     * If it does not exist create(Object) will be called with the parameter.
     *
     * @param t
     * @return Either the data parameter if it was inserted (now with the ID field set via the create method) or the data element that existed already in the database
     */
    T insertIfNotExists(T t);

    /**
     * This is a convenience method for creating an item in the database if it does not exist.
     * The id is extracted from the data parameter and a query-by-id is made on the database.
     * If a row in the database with the same id exists then all of the columns in the database will be updated from the fields in the data parameter.
     * If the id is null (or 0 or some other default value) or doesn't exist in the database then the object will be created in the database.
     * This also means that your data item must have an id field defined.
     * @param t
     * @return
     */
    Long insertOrUpdate(T t);

    /**
     * @param t
     * @return int
     * @throws
     * @Title: delete
     * @Description: 删除数据库当中数据，以id为准 Delete the database row corresponding to
     * the id from the data parameter.
     */
    Long delete(T t);

    /**
     * 删除表中所有数据,不建议做这个处理,可能需要较长的时间
     * @param clazz
     * @return  是否删除成功
     */
    Boolean deleteAll(Class<T> clazz);

    /**
     * @param t
     * @return int
     * @throws
     * @Title: update
     * @Description: 将数据更新到数据库当中 Store the fields from an object to the database
     * row corresponding to the id from the data parameter. If you
     * have made changes to an object, this is how you persist
     * those changes to the database.
     */
    Long update(T t);


    @SuppressWarnings("unchecked")
    List<T> query(PreparedQuery<T> preparedQuery);

    /**
     * @param t 接收新数据库数据的变量
     * @return int The number of rows found in the database that correspond to
     * the data id. This should be 1.
     * @throws
     * @Title: refresh
     * @Description: 数据库数据修改后，调用此方法可把数据库新数据更新到该变量对象当中
     */
    Long refresh(T t);


    /**
     * @return long
     * @throws
     * @Title: count
     * @Description: 返回表数据条数
     */
    Long count();


}
