package cn.com.findfine.jddaojia.data.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.com.findfine.jddaojia.data.bean.GoodsOrderBean;
import cn.com.findfine.jddaojia.data.db.JdDaojiaDbHelper;
import cn.com.findfine.jddaojia.data.db.contract.GoodsOrderContract;


public class GoodsOrderDao {

    private JdDaojiaDbHelper dbHelper;

    public GoodsOrderDao() {
        dbHelper = JdDaojiaDbHelper.getInstance();
    }

    public boolean insertOrder(GoodsOrderBean goodsOrderBean) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GoodsOrderContract.USER_ID, goodsOrderBean.getUserId());
        values.put(GoodsOrderContract.ORDER_NUMBER, goodsOrderBean.getOrderNumber());
        values.put(GoodsOrderContract.CREATE_ORDER_TIME, goodsOrderBean.getCreateOrderTime());
        values.put(GoodsOrderContract.ORDER_STATUS, goodsOrderBean.getOrderStatus());
        values.put(GoodsOrderContract.SHOP_ID, goodsOrderBean.getShopId());
        values.put(GoodsOrderContract.SHOP_NAME, goodsOrderBean.getShopName());
        values.put(GoodsOrderContract.GOODS_ARRAY, goodsOrderBean.getGoodsArray());
        values.put(GoodsOrderContract.GOODS_PRICE, goodsOrderBean.getGoodsPrice());
        values.put(GoodsOrderContract.USER_ADDRESS, goodsOrderBean.getUserAddress());
        long insert = db.insert(GoodsOrderContract.TABLE_NAME, null, values);
        return insert != -1;
    }

    public int deleteOrderById(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(GoodsOrderContract.TABLE_NAME, GoodsOrderContract._ID + "=?", new String[]{String.valueOf(id)});
    }

    public boolean updateOrder(String orderNumber, int orderStatus) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GoodsOrderContract.ORDER_STATUS, orderStatus);
        int update = db.update(GoodsOrderContract.TABLE_NAME, values, GoodsOrderContract.ORDER_NUMBER + "=?", new String[]{orderNumber});
        return update >= 0;
    }

    public List<GoodsOrderBean> queryAllOrderByUserId(String userId) {
        List<GoodsOrderBean> goodsOrderBeans = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(GoodsOrderContract.TABLE_NAME, null, GoodsOrderContract.USER_ID + "=?", new String[]{userId}, null, null, GoodsOrderContract._ID + " DESC");
        while (cursor.moveToNext()) {
            GoodsOrderBean goodsOrderBean = new GoodsOrderBean();
            goodsOrderBean.setId(cursor.getInt(cursor.getColumnIndex(GoodsOrderContract._ID)));
            goodsOrderBean.setOrderNumber(cursor.getString(cursor.getColumnIndex(GoodsOrderContract.ORDER_NUMBER)));
            goodsOrderBean.setCreateOrderTime(cursor.getString(cursor.getColumnIndex(GoodsOrderContract.CREATE_ORDER_TIME)));
            goodsOrderBean.setOrderStatus(cursor.getInt(cursor.getColumnIndex(GoodsOrderContract.ORDER_STATUS)));
            goodsOrderBean.setShopId(cursor.getInt(cursor.getColumnIndex(GoodsOrderContract.SHOP_ID)));
            goodsOrderBean.setShopName(cursor.getString(cursor.getColumnIndex(GoodsOrderContract.SHOP_NAME)));
            goodsOrderBean.setGoodsArray(cursor.getString(cursor.getColumnIndex(GoodsOrderContract.GOODS_ARRAY)));
            goodsOrderBean.setGoodsPrice(cursor.getString(cursor.getColumnIndex(GoodsOrderContract.GOODS_PRICE)));
            goodsOrderBean.setUserAddress(cursor.getString(cursor.getColumnIndex(GoodsOrderContract.USER_ADDRESS)));
            goodsOrderBeans.add(goodsOrderBean);
        }
        cursor.close();
        return goodsOrderBeans;
    }
}
