package cn.com.findfine.jddaojia.data.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.com.findfine.jddaojia.data.bean.UserAddress;
import cn.com.findfine.jddaojia.data.db.JdDaojiaDbHelper;
import cn.com.findfine.jddaojia.data.db.contract.UserAddressContract;


public class UserAddressDao {

    private JdDaojiaDbHelper dbHelper;

    public UserAddressDao() {
        dbHelper = JdDaojiaDbHelper.getInstance();
    }

//    public static UserAddressDao getInstance() {
//        return SingletonProvider.instance;
//    }
//
//    private static class SingletonProvider {
//        private static UserAddressDao instance = new UserAddressDao();
//    }

    /**
     * 插入地址
     * @param userAddress
     * @return
     */
    public boolean insertUserAddress(UserAddress userAddress) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserAddressContract.CITY, userAddress.getCity());
        values.put(UserAddressContract.USER_ID, userAddress.getUserId());
        values.put(UserAddressContract.ADDRESS, userAddress.getAddress());
        values.put(UserAddressContract.HOUSE_NUMBER, userAddress.getHouseNumber());
        values.put(UserAddressContract.NAME, userAddress.getName());
        values.put(UserAddressContract.PHONE_NUMBER, userAddress.getPhoneNumber());
        long insert = db.insert(UserAddressContract.TABLE_NAME, null, values);
        return insert != -1;
    }

    /**
     * 通过ID删除地址
     * @param id
     * @return
     */
    public int deleteUserAddressById(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(UserAddressContract.TABLE_NAME, UserAddressContract._ID + "=?", new String[]{String.valueOf(id)});
    }

    /**
     * 更新地址
     * @param userAddress
     * @return
     */
    public boolean updateUserAddress(UserAddress userAddress) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserAddressContract.CITY, userAddress.getCity());
        values.put(UserAddressContract.ADDRESS, userAddress.getAddress());
        values.put(UserAddressContract.HOUSE_NUMBER, userAddress.getHouseNumber());
        values.put(UserAddressContract.NAME, userAddress.getName());
        values.put(UserAddressContract.PHONE_NUMBER, userAddress.getPhoneNumber());
        int update = db.update(UserAddressContract.TABLE_NAME, values, UserAddressContract._ID + "=?", new String[]{String.valueOf(userAddress.getId())});
        return update >= 0;
    }

    /**
     * 查询所有地址
     * @return
     */
    public List<UserAddress> queryAllUserAddressByUserId(String userId) {
        List<UserAddress> userAddressList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(UserAddressContract.TABLE_NAME, null, UserAddressContract.USER_ID + "=?", new String[]{userId}, null, null, UserAddressContract._ID + " DESC");
        while (cursor.moveToNext()) {
            UserAddress userAddress = new UserAddress();
            userAddress.setId(cursor.getInt(cursor.getColumnIndex(UserAddressContract._ID)));
            userAddress.setUserId(cursor.getString(cursor.getColumnIndex(UserAddressContract.USER_ID)));
            userAddress.setCity(cursor.getString(cursor.getColumnIndex(UserAddressContract.CITY)));
            userAddress.setAddress(cursor.getString(cursor.getColumnIndex(UserAddressContract.ADDRESS)));
            userAddress.setHouseNumber(cursor.getString(cursor.getColumnIndex(UserAddressContract.HOUSE_NUMBER)));
            userAddress.setName(cursor.getString(cursor.getColumnIndex(UserAddressContract.NAME)));
            userAddress.setPhoneNumber(cursor.getString(cursor.getColumnIndex(UserAddressContract.PHONE_NUMBER)));
            userAddressList.add(userAddress);
        }
        cursor.close();
        return userAddressList;
    }

    public List<UserAddress> queryUserAddressByUserId(String userId) {
        List<UserAddress> userAddressList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(UserAddressContract.TABLE_NAME, null, UserAddressContract.USER_ID + "=?", new String[]{userId}, null, null, UserAddressContract._ID + " DESC");
        while (cursor.moveToNext()) {
            UserAddress userAddress = new UserAddress();
            userAddress.setId(cursor.getInt(cursor.getColumnIndex(UserAddressContract._ID)));
            userAddress.setUserId(cursor.getString(cursor.getColumnIndex(UserAddressContract.USER_ID)));
            userAddress.setCity(cursor.getString(cursor.getColumnIndex(UserAddressContract.CITY)));
            userAddress.setAddress(cursor.getString(cursor.getColumnIndex(UserAddressContract.ADDRESS)));
            userAddress.setHouseNumber(cursor.getString(cursor.getColumnIndex(UserAddressContract.HOUSE_NUMBER)));
            userAddress.setName(cursor.getString(cursor.getColumnIndex(UserAddressContract.NAME)));
            userAddress.setPhoneNumber(cursor.getString(cursor.getColumnIndex(UserAddressContract.PHONE_NUMBER)));
            userAddressList.add(userAddress);
        }
        cursor.close();
        return userAddressList;
    }
}
