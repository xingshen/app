package com.steptowin.core.test.db;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @Desc:
 * @Author: zg
 * @Time:
 */

@DatabaseTable(tableName = "users")
public class User {
    @DatabaseField(generatedId = true, columnName = "local_id", dataType = DataType.INTEGER, index = true)
    private int user_id;// 本地id,自增
    @DatabaseField( columnName = "name", dataType = DataType.STRING, index = true, canBeNull = false)
    private String name;

    public void setName(String name) {
        this.name = name;
    }

}
