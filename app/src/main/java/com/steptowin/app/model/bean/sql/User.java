package com.steptowin.app.model.bean.sql;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.steptowin.core.common.BaseEntity;
import com.steptowin.core.parser.xing.XingParseAlias;
import com.steptowin.core.test.Club;

import java.io.Serializable;
import java.util.List;


/**
 * desc:
 * author：zg
 * date:2016-03-30
 */
@DatabaseTable(tableName = "user")
public class User extends BaseEntity implements Serializable {
//    @XingParseAlias("introduce")
    @DatabaseField(id = true,columnName = "user_id",dataType = DataType.STRING,index = true,useGetSet = true)
    private String user_id;

    @DatabaseField(columnName = "name",dataType = DataType.STRING,index = true,useGetSet = true)
    private String name;

    @DatabaseField(columnName = "sex",dataType = DataType.STRING,index = true,useGetSet = true)
    private String sex;


    private String age;

    @XingParseAlias("user_name")
    private List<String> user_names;

//    @XStreamImplicit(itemFieldName = "club")
    private List<Club> club;

    public User(){}


    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public List<String> getUser_names() {
        return user_names;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
