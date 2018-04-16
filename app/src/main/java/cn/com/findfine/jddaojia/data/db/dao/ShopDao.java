package cn.com.findfine.jddaojia.data.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.com.findfine.jddaojia.data.bean.ShopBean;
import cn.com.findfine.jddaojia.data.db.JdDaojiaDbHelper;
import cn.com.findfine.jddaojia.data.db.contract.ShopContract;

/**
 * Created by yangchen on 2017/9/25.
 */

public class ShopDao {

    private JdDaojiaDbHelper dbHelper;

    public ShopDao() {
        dbHelper = JdDaojiaDbHelper.getInstance();
    }

    public boolean insertShop(ShopBean shopBean) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ShopContract.SHOP_ID, shopBean.getShopId());
        values.put(ShopContract.SHOP_NAME, shopBean.getShopName());
        values.put(ShopContract.SHOP_PHOTO, shopBean.getShopPhoto());
        values.put(ShopContract.SHOP_ADDRESS, shopBean.getShopAddress());
        long insert = db.insert(ShopContract.TABLE_NAME, null, values);
        return insert != -1;
    }

    public int deleteShopById(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(ShopContract.TABLE_NAME, ShopContract._ID + "=?", new String[]{String.valueOf(id)});
    }

    public boolean updateShop(ShopBean shopBean) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ShopContract.SHOP_ID, shopBean.getShopId());
        values.put(ShopContract.SHOP_NAME, shopBean.getShopName());
        values.put(ShopContract.SHOP_PHOTO, shopBean.getShopPhoto());
        values.put(ShopContract.SHOP_ADDRESS, shopBean.getShopAddress());
        int update = db.update(ShopContract.TABLE_NAME, values, ShopContract._ID + "=?", new String[]{String.valueOf(shopBean.getId())});
        return update >= 0;
    }

    public List<ShopBean> queryAllShop() {
        List<ShopBean> shopBeans = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(ShopContract.TABLE_NAME, null, null, null, null, null, ShopContract._ID + " DESC");
        while (cursor.moveToNext()) {
            ShopBean shopBean = new ShopBean();
            shopBean.setId(cursor.getInt(cursor.getColumnIndex(ShopContract._ID)));
            shopBean.setShopId(cursor.getInt(cursor.getColumnIndex(ShopContract.SHOP_ID)));
            shopBean.setShopName(cursor.getString(cursor.getColumnIndex(ShopContract.SHOP_NAME)));
            shopBean.setShopPhoto(cursor.getString(cursor.getColumnIndex(ShopContract.SHOP_PHOTO)));
            shopBean.setShopAddress(cursor.getString(cursor.getColumnIndex(ShopContract.SHOP_ADDRESS)));
            shopBeans.add(shopBean);
        }
        cursor.close();
        return shopBeans;
    }
}
