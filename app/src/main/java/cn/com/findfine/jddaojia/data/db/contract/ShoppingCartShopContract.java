package cn.com.findfine.jddaojia.data.db.contract;

/**
 * Created by yangchen on 2017/9/25.
 */

public class ShoppingCartShopContract {

    public static final String TABLE_NAME = "shopping_cart_shop";
    public static final String _ID = "_id";
    public static final String USER_ID = "user_id";
    public static final String SHOP_ID = "shop_id";
    public static final String SHOP_NAME = "shop_name";

    public static final String SQL_CREATE_SHOPPING_CART =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    USER_ID + " TEXT," +
                    SHOP_ID + " INTEGER," +
                    SHOP_NAME + " TEXT)";
}
