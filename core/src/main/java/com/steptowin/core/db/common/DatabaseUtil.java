package com.steptowin.core.db.common;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.misc.JavaxPersistence;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * desc: 基于ormlite三方框架的数据库工具类，包括：数据库升级==
 * author：zg
 * date:2016/4/6 0006
 * time:下午 2:58
 */
public class DatabaseUtil {
    public static final String TAG = "DatabaseUtil";
    /**数据库表操作类型*/
    public enum OPERATION_TYPE{
        /**表新增字段*/
        ADD,
        /**表删除字段*/
        DELETE,
        /**
         * 表既有新增，又有删除字段。如果确认只有新增或者删除，建议使用对应类型。
         */
        UPDATE
    }
    /**
     * 升级表，增加字段
     * @param db
     * @param clazz
     */
    public static <t> void upgradeTable(SQLiteDatabase db,ConnectionSource cs,Class<t> clazz,OPERATION_TYPE type){
        String tableName = extractTableName(clazz);

        db.beginTransaction();
        try {

            //Rename table
            String tempTableName = tableName + "_temp";
            String sql = "ALTER TABLE "+tableName+" RENAME TO " +tempTableName;
            db.execSQL(sql);

            //Create table
            try {
                sql = TableUtils.getCreateTableStatements(cs, clazz).get(0);
                db.execSQL(sql);
            } catch (Exception e) {
                e.printStackTrace();
                TableUtils.createTable(cs, clazz);
            }

            //Load data
            String columns;
            switch (type){
                case ADD:
                    columns = Arrays.toString(getColumnNames(db, tempTableName)).replace("[", "").replace("]","");
                    break;
                case DELETE:
                    columns = Arrays.toString(getColumnNames(db,tableName)).replace("[", "").replace("]", "");
                    break;
                case UPDATE:
                    columns = Arrays.toString(getSameColumnNames(db, tableName, tempTableName)).replace("[", "").replace("]", "");
                    break;
                default:
                    throw new IllegalArgumentException("OPERATION_TYPE error");
            }

            sql = "INSERT INTO "+tableName +
                    "("+ columns+") "+
                    "SELECT "+ columns+" FROM "+tempTableName;
            db.execSQL(sql);

            //Drop temp table
            sql = "DROP TABLE IF EXISTS "+tempTableName;
            db.execSQL(sql);

            db.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.endTransaction();
        }
    }

    /**
     * 获取两个表相同列名
     * @param db
     * @param tableName1
     * @param tableName2
     * @return
     */
    private static String[] getSameColumnNames(SQLiteDatabase db,String tableName1,String tableName2){
        String[] columnNames1 = getColumnNames(db, tableName1);
        String[] columnNames2 = getColumnNames(db,tableName2);
        ArrayList<String> result = new ArrayList<>();
        for (String column1:columnNames1
             ) {
            for (String column2:columnNames2
                 ) {
                if(column1.equals(column2))
                    result.add(column1);
            }
        }
        String[] columnNames = result.toArray(new String[result.size()]);

        return columnNames;
    }


    /**
     * 获取表名(ormlite DatabaseTableConfig.java)
     * @param clazz
     * @param <t>
     * @return
     */
    private static <t> String extractTableName(Class<t> clazz) {
        DatabaseTable databaseTable = clazz.getAnnotation(DatabaseTable.class);
        String name ;
        if (databaseTable != null && databaseTable.tableName() != null && databaseTable.tableName().length() > 0) {
            name = databaseTable.tableName();
        } else {
            /*
             * NOTE: to remove javax.persistence usage, comment the following line out
             */
            name = JavaxPersistence.getEntityName(clazz);
            if (name == null) {
                // if the name isn't specified, it is the class name lowercased
                name = clazz.getSimpleName().toLowerCase();
            }
        }
        return name;
    }

    /**
     * 获取表的列名
     * @param db
     * @param tableName
     * @return
     */
    private static String[] getColumnNames(SQLiteDatabase db,String tableName){
        String[] columnNames = null;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("PRAGMA table_info("+tableName+")",null);
            if(cursor != null){
                int columnIndex = cursor.getColumnIndex("name");
                if(columnIndex == -1){
                    return null;
                }

                int index = 0;
                columnNames = new String[cursor.getCount()];
                for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
                    columnNames[index] = cursor.getString(columnIndex);
                    index++;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(cursor != null) {
                cursor.close();
            }
        }
        return columnNames;
    }
}
