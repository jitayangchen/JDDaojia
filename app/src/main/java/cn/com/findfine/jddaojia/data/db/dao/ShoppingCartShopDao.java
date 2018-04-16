package cn.com.findfine.jddaojia.data.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.com.findfine.jddaojia.data.bean.ShoppingCartShopBean;
import cn.com.findfine.jddaojia.data.db.JdDaojiaDbHelper;
import cn.com.findfine.jddaojia.data.db.contract.ShoppingCartShopContract;


public class ShoppingCartShopDao {

    private JdDaojiaDbHelper dbHelper;

    public ShoppingCartShopDao() {
        dbHelper = JdDaojiaDbHelper.getInstance();
    }

    /**
     * 把商家插入购物车
     * @param shoppingCartShopBean
     * @param userId
     * @return
     */
    public boolean insertShopCart(ShoppingCartShopBean shoppingCartShopBean, String userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ShoppingCartShopContract.USER_ID, userId);
        values.put(ShoppingCartShopContract.SHOP_ID, shoppingCartShopBean.getShopId());
        values.put(ShoppingCartShopContract.SHOP_NAME, shoppingCartShopBean.getShopName());
        long insert = db.insert(ShoppingCartShopContract.TABLE_NAME, null, values);
        return insert != -1;
    }

    public int deleteShopCartById(String userId, int shopId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(ShoppingCartShopContract.TABLE_NAME,
                ShoppingCartShopContract.USER_ID + "=? and "
                        + ShoppingCartShopContract.SHOP_ID + "=?", new String[]{userId, String.valueOf(shopId)});
    }

    public boolean updateShopCart(ShoppingCartShopBean shoppingCartShopBean, String userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ShoppingCartShopContract.USER_ID, userId);
        values.put(ShoppingCartShopContract.SHOP_ID, shoppingCartShopBean.getShopId());
        values.put(ShoppingCartShopContract.SHOP_NAME, shoppingCartShopBean.getShopName());
        int update = db.update(ShoppingCartShopContract.TABLE_NAME, values, ShoppingCartShopContract.SHOP_ID + "=? and " + ShoppingCartShopContract.USER_ID + "=?", new String[]{String.valueOf(shoppingCartShopBean.getShopId()), userId});
        return update >= 0;
    }

    public List<ShoppingCartShopBean> queryShopCartByUserId(String userId) {
        List<ShoppingCartShopBean> shoppingCartShopBeans = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(ShoppingCartShopContract.TABLE_NAME, null, ShoppingCartShopContract.USER_ID + "=?", new String[]{userId}, null, null, ShoppingCartShopContract._ID + " DESC");
        while (cursor.moveToNext()) {
            ShoppingCartShopBean shoppingCartShopBean = new ShoppingCartShopBean();
            shoppingCartShopBean.setId(cursor.getInt(cursor.getColumnIndex(ShoppingCartShopContract._ID)));
            shoppingCartShopBean.setShopId(cursor.getInt(cursor.getColumnIndex(ShoppingCartShopContract.SHOP_ID)));
            shoppingCartShopBean.setShopName(cursor.getString(cursor.getColumnIndex(ShoppingCartShopContract.SHOP_NAME)));
            shoppingCartShopBeans.add(shoppingCartShopBean);
        }
        cursor.close();
        return shoppingCartShopBeans;
    }

    public ShoppingCartShopBean queryShopCartByUserIdAndShopId(String userId, int shopId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(ShoppingCartShopContract.TABLE_NAME, null, ShoppingCartShopContract.USER_ID + "=? and " + ShoppingCartShopContract.SHOP_ID + "=?", new String[]{userId, String.valueOf(shopId)}, null, null, ShoppingCartShopContract._ID + " DESC");
        ShoppingCartShopBean shoppingCartShopBean = null;
        if (cursor.moveToNext()) {
            shoppingCartShopBean = new ShoppingCartShopBean();
            shoppingCartShopBean.setId(cursor.getInt(cursor.getColumnIndex(ShoppingCartShopContract._ID)));
            shoppingCartShopBean.setShopId(cursor.getInt(cursor.getColumnIndex(ShoppingCartShopContract.SHOP_ID)));
            shoppingCartShopBean.setShopName(cursor.getString(cursor.getColumnIndex(ShoppingCartShopContract.SHOP_NAME)));
        }
        cursor.close();
        return shoppingCartShopBean;
    }
}
