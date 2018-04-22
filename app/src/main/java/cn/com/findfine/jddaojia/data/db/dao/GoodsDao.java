package cn.com.findfine.jddaojia.data.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.com.findfine.jddaojia.data.bean.GoodsBean;
import cn.com.findfine.jddaojia.data.db.JdDaojiaDbHelper;
import cn.com.findfine.jddaojia.data.db.contract.GoodsContract;


public class GoodsDao {

    private JdDaojiaDbHelper dbHelper;

    public GoodsDao() {
        dbHelper = JdDaojiaDbHelper.getInstance();
    }

    public boolean insertGoods(GoodsBean goodsBean) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GoodsContract.GOODS_ID, goodsBean.getGoodsId());
        values.put(GoodsContract.SHOP_ID, goodsBean.getShopId());
        values.put(GoodsContract.GOODS_NAME, goodsBean.getGoodsName());
        values.put(GoodsContract.GOODS_PHOTO, goodsBean.getGoodsPhoto());
        values.put(GoodsContract.GOODS_PRICE, goodsBean.getGoodsPrice());
        values.put(GoodsContract.GOODS_CATEGORY, goodsBean.getGoodsCategory());
        values.put(GoodsContract.GOODS_SALES_VOLUME, goodsBean.getGoodsSalesVolume());
        long insert = db.insert(GoodsContract.TABLE_NAME, null, values);
        return insert != -1;
    }

    public int deleteGoodsById(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(GoodsContract.TABLE_NAME, GoodsContract._ID + "=?", new String[]{String.valueOf(id)});
    }

    public boolean updateGoods(GoodsBean goodsBean) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GoodsContract.GOODS_ID, goodsBean.getGoodsId());
        values.put(GoodsContract.SHOP_ID, goodsBean.getShopId());
        values.put(GoodsContract.GOODS_NAME, goodsBean.getGoodsName());
        values.put(GoodsContract.GOODS_PHOTO, goodsBean.getGoodsPhoto());
        values.put(GoodsContract.GOODS_PRICE, goodsBean.getGoodsPrice());
        values.put(GoodsContract.GOODS_CATEGORY, goodsBean.getGoodsCategory());
        values.put(GoodsContract.GOODS_SALES_VOLUME, goodsBean.getGoodsSalesVolume());
        int update = db.update(GoodsContract.TABLE_NAME, values, GoodsContract._ID + "=?", new String[]{String.valueOf(goodsBean.getId())});
        return update >= 0;
    }

    public List<GoodsBean> queryAllGoods() {
        List<GoodsBean> goodsBeans = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(GoodsContract.TABLE_NAME, null, null, null, null, null, GoodsContract._ID + " DESC");
        while (cursor.moveToNext()) {
            GoodsBean goodsBean = new GoodsBean();
            goodsBean.setId(cursor.getInt(cursor.getColumnIndex(GoodsContract._ID)));
            goodsBean.setGoodsId(cursor.getInt(cursor.getColumnIndex(GoodsContract.GOODS_ID)));
            goodsBean.setShopId(cursor.getInt(cursor.getColumnIndex(GoodsContract.SHOP_ID)));
            goodsBean.setGoodsName(cursor.getString(cursor.getColumnIndex(GoodsContract.GOODS_NAME)));
            goodsBean.setGoodsPhoto(cursor.getString(cursor.getColumnIndex(GoodsContract.GOODS_PHOTO)));
            goodsBean.setGoodsPrice(cursor.getFloat(cursor.getColumnIndex(GoodsContract.GOODS_PRICE)));
            goodsBean.setGoodsCategory(cursor.getString(cursor.getColumnIndex(GoodsContract.GOODS_CATEGORY)));
            goodsBean.setGoodsSalesVolume(cursor.getInt(cursor.getColumnIndex(GoodsContract.GOODS_SALES_VOLUME)));
            goodsBeans.add(goodsBean);
        }
        cursor.close();
        return goodsBeans;
    }

    public List<GoodsBean> queryGoodsByShopId(int shopId) {
        List<GoodsBean> goodsBeans = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(GoodsContract.TABLE_NAME, null, GoodsContract.SHOP_ID + "=?", new String[]{String.valueOf(shopId)}, null, null, GoodsContract._ID + " DESC");
        while (cursor.moveToNext()) {
            GoodsBean goodsBean = new GoodsBean();
            goodsBean.setId(cursor.getInt(cursor.getColumnIndex(GoodsContract._ID)));
            goodsBean.setGoodsId(cursor.getInt(cursor.getColumnIndex(GoodsContract.GOODS_ID)));
            goodsBean.setShopId(cursor.getInt(cursor.getColumnIndex(GoodsContract.SHOP_ID)));
            goodsBean.setGoodsName(cursor.getString(cursor.getColumnIndex(GoodsContract.GOODS_NAME)));
            goodsBean.setGoodsPhoto(cursor.getString(cursor.getColumnIndex(GoodsContract.GOODS_PHOTO)));
            goodsBean.setGoodsPrice(cursor.getFloat(cursor.getColumnIndex(GoodsContract.GOODS_PRICE)));
            goodsBean.setGoodsCategory(cursor.getString(cursor.getColumnIndex(GoodsContract.GOODS_CATEGORY)));
            goodsBean.setGoodsSalesVolume(cursor.getInt(cursor.getColumnIndex(GoodsContract.GOODS_SALES_VOLUME)));
            goodsBeans.add(goodsBean);
        }
        cursor.close();
        return goodsBeans;
    }

    public GoodsBean queryGoodsByGoodsId(int goodsId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(GoodsContract.TABLE_NAME, null, GoodsContract.GOODS_ID + "=?", new String[]{String.valueOf(goodsId)}, null, null, GoodsContract._ID + " DESC");
        GoodsBean goodsBean = null;
        if (cursor.moveToNext()) {
            goodsBean = new GoodsBean();
            goodsBean.setId(cursor.getInt(cursor.getColumnIndex(GoodsContract._ID)));
            goodsBean.setGoodsId(cursor.getInt(cursor.getColumnIndex(GoodsContract.GOODS_ID)));
            goodsBean.setShopId(cursor.getInt(cursor.getColumnIndex(GoodsContract.SHOP_ID)));
            goodsBean.setGoodsName(cursor.getString(cursor.getColumnIndex(GoodsContract.GOODS_NAME)));
            goodsBean.setGoodsPhoto(cursor.getString(cursor.getColumnIndex(GoodsContract.GOODS_PHOTO)));
            goodsBean.setGoodsPrice(cursor.getFloat(cursor.getColumnIndex(GoodsContract.GOODS_PRICE)));
            goodsBean.setGoodsCategory(cursor.getString(cursor.getColumnIndex(GoodsContract.GOODS_CATEGORY)));
            goodsBean.setGoodsSalesVolume(cursor.getInt(cursor.getColumnIndex(GoodsContract.GOODS_SALES_VOLUME)));
        }
        cursor.close();
        return goodsBean;
    }

    public List<GoodsBean> queryGoodsByKeyWords(String keyWords) {
        List<GoodsBean> goodsBeans = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT  * FROM " + GoodsContract.TABLE_NAME + " where " + GoodsContract.GOODS_NAME + " like '%" + keyWords + "%'";
        Log.i("KeyWords", sql);
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            GoodsBean goodsBean = new GoodsBean();
            goodsBean.setId(cursor.getInt(cursor.getColumnIndex(GoodsContract._ID)));
            goodsBean.setGoodsId(cursor.getInt(cursor.getColumnIndex(GoodsContract.GOODS_ID)));
            goodsBean.setShopId(cursor.getInt(cursor.getColumnIndex(GoodsContract.SHOP_ID)));
            goodsBean.setGoodsName(cursor.getString(cursor.getColumnIndex(GoodsContract.GOODS_NAME)));
            goodsBean.setGoodsPhoto(cursor.getString(cursor.getColumnIndex(GoodsContract.GOODS_PHOTO)));
            goodsBean.setGoodsPrice(cursor.getFloat(cursor.getColumnIndex(GoodsContract.GOODS_PRICE)));
            goodsBean.setGoodsCategory(cursor.getString(cursor.getColumnIndex(GoodsContract.GOODS_CATEGORY)));
            goodsBean.setGoodsSalesVolume(cursor.getInt(cursor.getColumnIndex(GoodsContract.GOODS_SALES_VOLUME)));
            goodsBeans.add(goodsBean);
        }
        cursor.close();
        return goodsBeans;
    }
}
