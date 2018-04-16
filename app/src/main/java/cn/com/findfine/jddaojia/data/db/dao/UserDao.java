package cn.com.findfine.jddaojia.data.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import cn.com.findfine.jddaojia.data.bean.UserInfo;
import cn.com.findfine.jddaojia.data.db.JdDaojiaDbHelper;
import cn.com.findfine.jddaojia.data.db.contract.UserContract;

/**
 * Created by yangchen on 2017/9/25.
 */

public class UserDao {

    private JdDaojiaDbHelper dbHelper;

    public UserDao() {
        dbHelper = JdDaojiaDbHelper.getInstance();
    }

    public boolean insertUser(UserInfo userInfo) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserContract.USER_ID, userInfo.getUserId());
        values.put(UserContract.NICK_NAME, userInfo.getNickName());
        values.put(UserContract.PASSWORD, userInfo.getPassword());
        long insert = db.insert(UserContract.TABLE_NAME, null, values);
        return insert != -1;
    }

    public int deleteUserById(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(UserContract.TABLE_NAME, UserContract._ID + "=?", new String[]{String.valueOf(id)});
    }

    public boolean updateUser(UserInfo userInfo) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserContract.USER_ID, userInfo.getUserId());
        values.put(UserContract.NICK_NAME, userInfo.getNickName());
        values.put(UserContract.PASSWORD, userInfo.getPassword());
        int update = db.update(UserContract.TABLE_NAME, values, UserContract._ID + "=?", new String[]{userInfo.getUserId()});
        return update >= 0;
    }

    public UserInfo queryUserById(String userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(UserContract.TABLE_NAME, null, UserContract.USER_ID + "=?", new String[]{userId}, null, null, null);
        UserInfo userInfo = null;
        while (cursor.moveToNext()) {
            userInfo = new UserInfo();
            userInfo.setUserId(cursor.getString(cursor.getColumnIndex(UserContract.USER_ID)));
            userInfo.setNickName(cursor.getString(cursor.getColumnIndex(UserContract.NICK_NAME)));
            userInfo.setPassword(cursor.getString(cursor.getColumnIndex(UserContract.PASSWORD)));
        }
        cursor.close();
        return userInfo;
    }
}
