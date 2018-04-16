package cn.com.findfine.jddaojia.data.db.contract;

/**
 * Created by yangchen on 2017/9/25.
 */

public class GoodsContract {

    public static final String TABLE_NAME = "goods";
    public static final String _ID = "_id";
    public static final String GOODS_ID = "goods_id";
    public static final String SHOP_ID = "shop_id";
    public static final String GOODS_NAME = "goods_name";
    public static final String GOODS_PHOTO = "goods_photo";
    public static final String GOODS_PRICE = "goods_price";
    public static final String GOODS_CATEGORY = "goods_category";
    public static final String GOODS_SALES_VOLUME = "goods_sales_volume";

    public static final String SQL_CREATE_GOODS =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    GOODS_ID + " INTEGER," +
                    SHOP_ID + " INTEGER," +
                    GOODS_NAME + " TEXT," +
                    GOODS_PHOTO + " TEXT," +
                    GOODS_PRICE + " REAL," +
                    GOODS_CATEGORY + " TEXT," +
                    GOODS_SALES_VOLUME + " INTEGER)";
}
