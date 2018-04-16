package cn.com.findfine.jddaojia.data.db.contract;

/**
 * Created by yangchen on 2017/9/25.
 */

public class ShopContract {

    public static final String TABLE_NAME = "shop";
    public static final String _ID = "_id";
    public static final String SHOP_ID = "shop_id";
    public static final String SHOP_NAME = "shop_name";
    public static final String SHOP_PHOTO = "shop_photo";
    public static final String SHOP_ADDRESS = "shop_address";

    public static final String SQL_CREATE_SHOP =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    SHOP_ID + " INTEGER," +
                    SHOP_NAME + " TEXT," +
                    SHOP_PHOTO + " TEXT," +
                    SHOP_ADDRESS + " TEXT)";
}
