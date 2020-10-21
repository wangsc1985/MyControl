package com.wang17.mycontrol.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 阿弥陀佛 on 2015/11/18.
 */
public class DataContext {

    private DatabaseHelper dbHelper;
    private Context context;

    public DataContext(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    //region Setting
    public Setting getSetting(Object name) {

        //获取数据库对象
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //查询获得游标
        Cursor cursor = db.query("setting", null, "name=?", new String[]{name.toString()}, null, null, null);
        //判断游标是否为空
        while (cursor.moveToNext()) {
            Setting setting = new Setting(name.toString(), cursor.getString(1), cursor.getInt(2));
            cursor.close();
            db.close();
            return setting;
        }
        return null;
    }

    public Setting getSetting(Object name, Object defaultValue) {
        Setting setting = getSetting(name);
        if (setting == null) {
            this.addSetting(name, defaultValue);
            setting = new Setting(name.toString(), defaultValue.toString(), 100);
            return setting;
        }
        return setting;
    }

    /**
     * 修改制定key配置，如果不存在则创建。
     *
     * @param name
     * @param value
     */
    public void editSetting(Object name, Object value) {
        //获取数据库对象
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //使用update方法更新表中的数据
        ContentValues values = new ContentValues();
        values.put("value", value.toString());
        if (db.update("setting", values, "name=?", new String[]{name.toString()}) == 0) {
            this.addSetting(name, value.toString());
        }
        db.close();
    }

    public void editSettingLevel(Object name, int level) {
        //获取数据库对象
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //使用update方法更新表中的数据
        ContentValues values = new ContentValues();
        values.put("level", level + "");
        db.update("setting", values, "name=?", new String[]{name.toString()});
        db.close();
    }

    public void deleteSetting(Object name) {
        //获取数据库对象
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("setting", "name=?", new String[]{name.toString()});
//        String sql = "DELETE FROM setting WHERE userId="+userId.toString()+" AND name="+name;
//        addLog(new Log(sql,userId),db);
        //关闭SQLiteDatabase对象
        db.close();
    }

    public void deleteSetting(String name) {
        //获取数据库对象
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("setting", "name=?", new String[]{name});
//        String sql = "DELETE FROM setting WHERE userId="+userId.toString()+" AND name="+name;
//        addLog(new Log(sql,userId),db);
        //关闭SQLiteDatabase对象
        db.close();
    }

    public void addSetting(Object name, Object value) {
        //获取数据库对象
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //使用insert方法向表中插入数据
        ContentValues values = new ContentValues();
        values.put("name", name.toString());
        values.put("value", value.toString());
        //调用方法插入数据
        db.insert("setting", "name", values);
        //关闭SQLiteDatabase对象
        db.close();
    }

    public List<Setting> getSettings() {
        List<Setting> result = new ArrayList<>();
        //获取数据库对象
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //查询获得游标
        Cursor cursor = db.query("setting", null, null, null, null, null, "level,name");
        //判断游标是否为空
        while (cursor.moveToNext()) {
            Setting setting = new Setting(cursor.getString(0), cursor.getString(1), cursor.getInt(2));
            result.add(setting);
        }
        cursor.close();
        db.close();
        return result;
    }

    public void clearSetting() {
        //获取数据库对象
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("setting", null, null);
        //        String sql = "DELETE FROM setting WHERE userId="+userId.toString()+" AND key="+key;
//        addLog(new Log(sql,userId),db);
        //关闭SQLiteDatabase对象
        db.close();
    }
    //endregion
}
