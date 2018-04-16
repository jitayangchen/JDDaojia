package cn.com.findfine.jddaojia.data.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.com.findfine.jddaojia.data.bean.GoodsBean;
import cn.com.findfine.jddaojia.data.db.JdDaojiaDbHelper;
import cn.com.findfine.jddaojia.data.db.contract.ShoppingCartGoodsContract;


public class ShoppingCartGoodsDao {

    private JdDaojiaDbHelper dbHelper;

    public ShoppingCartGoodsDao() {
        dbHelper = JdDaojiaDbHelper.getInstance();
    }

    public boolean insertGoodsCart(GoodsBean goodsBean, String userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ShoppingCartGoodsContract.USER_ID, userId);
        values.put(ShoppingCartGoodsContract.SHOP_ID, goodsBean.getShopId());
        values.put(ShoppingCartGoodsContract.GOODS_ID, goodsBean.getGoodsId());
        values.put(ShoppingCartGoodsContract.GOODS_COUNT, goodsBean.getGoodsCartCount());
        long insert = db.insert(ShoppingCartGoodsContract.TABLE_NAME, null, values);
        return insert != -1;
    }

    public int deleteGoodsCartById(GoodsBean goodsBean, String userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(ShoppingCartGoodsContract.TABLE_NAME,
                ShoppingCartGoodsContract.USER_ID + "=? and "
                        + ShoppingCartGoodsContract.GOODS_ID + "=?", new String[]{userId, String.valueOf(goodsBean.getGoodsId())});
    }

    public int deleteGoodsCartByUserIdAndShopId(String userId, int shopId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(ShoppingCartGoodsContract.TABLE_NAME,
                ShoppingCartGoodsContract.USER_ID + "=? and "
                        + ShoppingCartGoodsContract.SHOP_ID + "=?", new String[]{userId, String.valueOf(shopId)});
    }

    public boolean updateGoodsCart(GoodsBean goodsBean, String userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
//        values.put(ShoppingCartGoodsContract.USER_ID, userId);
//        values.put(ShoppingCartGoodsContract.SHOP_ID, goodsBean.getShopId());
//        values.put(ShoppingCartGoodsContract.GOODS_ID, goodsBean.getGoodsId());
        values.put(ShoppingCartGoodsContract.GOODS_COUNT, goodsBean.getGoodsCartCount());
        int update = db.update(ShoppingCartGoodsContract.TABLE_NAME, values,
                ShoppingCartGoodsContract.SHOP_ID + "=? and "
                        + ShoppingCartGoodsContract.USER_ID + "=? and "
                        + ShoppingCartGoodsContract.GOODS_ID + "=?",
                new String[]{String.valueOf(goodsBean.getShopId()), userId, String.valueOf(goodsBean.getGoodsId())});
        return update >= 0;
    }

    public List<GoodsBean> queryGoodsCartByUserId(String userId) {
        List<GoodsBean> goodsBeans = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(ShoppingCartGoodsContract.TABLE_NAME, null, ShoppingCartGoodsContract.USER_ID, new String[]{userId}, null, null, ShoppingCartGoodsContract._ID + " DESC");
        while (cursor.moveToNext()) {
            GoodsBean goodsBean = new GoodsBean();
            goodsBean.setId(cursor.getInt(cursor.getColumnIndex(ShoppingCartGoodsContract._ID)));
            goodsBean.setShopId(cursor.getInt(cursor.getColumnIndex(ShoppingCartGoodsContract.SHOP_ID)));
            goodsBean.setGoodsId(cursor.getInt(cursor.getColumnIndex(ShoppingCartGoodsContract.GOODS_ID)));
            goodsBean.setGoodsCartCount(cursor.getInt(cursor.getColumnIndex(ShoppingCartGoodsContract.GOODS_COUNT)));
            goodsBeans.add(goodsBean);
        }
        cursor.close();
        return goodsBeans;
    }

    public List<GoodsBean> queryGoodsCartByUserIdAndShopId(String userId, int shopId) {
        List<GoodsBean> goodsBeans = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(ShoppingCartGoodsContract.TABLE_NAME, null,
                ShoppingCartGoodsContract.USER_ID + "=? and "
                        + ShoppingCartGoodsContract.SHOP_ID + "=?", new String[]{userId, String.valueOf(shopId)}, null, null, ShoppingCartGoodsContract._ID + " DESC");

        GoodsDao goodsDao = new GoodsDao();
        while (cursor.moveToNext()) {
//            GoodsBean goodsBean = new GoodsBean();
            int goodsId = cursor.getInt(cursor.getColumnIndex(ShoppingCartGoodsContract.GOODS_ID));
            GoodsBean goodsBean = goodsDao.queryGoodsByGoodsId(goodsId);
            goodsBean.setId(cursor.getInt(cursor.getColumnIndex(ShoppingCartGoodsContract._ID)));
//            goodsBean.setShopId(cursor.getInt(cursor.getColumnIndex(ShoppingCartGoodsContract.SHOP_ID)));
//            goodsBean.setGoodsId(cursor.getInt(cursor.getColumnIndex(ShoppingCartGoodsContract.GOODS_ID)));
            goodsBean.setGoodsCartCount(cursor.getInt(cursor.getColumnIndex(ShoppingCartGoodsContract.GOODS_COUNT)));
            goodsBeans.add(goodsBean);
        }
        cursor.close();
        return goodsBeans;
    }

    public GoodsBean queryGoodsCartByUserIdAndShopIdAndGoodsId(String userId, int shopId, int goodsId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(ShoppingCartGoodsContract.TABLE_NAME, null,
                ShoppingCartGoodsContract.USER_ID + "=? and "
                + ShoppingCartGoodsContract.SHOP_ID + "=? and "
                + ShoppingCartGoodsContract.GOODS_ID + "=?",
                new String[]{userId, String.valueOf(shopId), String.valueOf(goodsId)}, null, null, ShoppingCartGoodsContract._ID + " DESC");
        GoodsBean goodsBean = null;
        if (cursor.moveToNext()) {
            goodsBean = new GoodsBean();
            goodsBean.setId(cursor.getInt(cursor.getColumnIndex(ShoppingCartGoodsContract._ID)));
            goodsBean.setShopId(cursor.getInt(cursor.getColumnIndex(ShoppingCartGoodsContract.SHOP_ID)));
            goodsBean.setGoodsId(cursor.getInt(cursor.getColumnIndex(ShoppingCartGoodsContract.GOODS_ID)));
            goodsBean.setGoodsCartCount(cursor.getInt(cursor.getColumnIndex(ShoppingCartGoodsContract.GOODS_COUNT)));
        }
        cursor.close();
        return goodsBean;
    }
}
