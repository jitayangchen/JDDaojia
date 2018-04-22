package cn.com.findfine.jddaojia.data.db.contract;


public class ShoppingCartGoodsContract {

    public static final String TABLE_NAME = "shopping_cart_goods";
    public static final String _ID = "_id";
    public static final String USER_ID = "user_id";
    public static final String SHOP_ID = "shop_id";
    public static final String GOODS_COUNT = "goods_count";
    public static final String GOODS_ID = "goods_ID";

    public static final String SQL_CREATE_SHOPPING_CART =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    USER_ID + " TEXT," +
                    SHOP_ID + " INTEGER," +
                    GOODS_COUNT + " INTEGER," +
                    GOODS_ID + " INTEGER)";
}
