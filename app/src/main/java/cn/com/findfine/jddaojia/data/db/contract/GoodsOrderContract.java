package cn.com.findfine.jddaojia.data.db.contract;


public class GoodsOrderContract {

    public static final int ORDER_STATUS_SUCCESS = 1;
    public static final int ORDER_STATUS_CANCEL = 2;
    public static final int ORDER_STATUS_UNPAY = 3;

    public static final String TABLE_NAME = "goods_order";
    public static final String _ID = "_id";
    public static final String USER_ID = "user_id";
    public static final String ORDER_NUMBER = "order_number";
    public static final String CREATE_ORDER_TIME = "create_order_time";
    public static final String ORDER_STATUS = "order_status";
    public static final String SHOP_ID = "shop_id";
    public static final String SHOP_NAME = "shop_name";
    public static final String GOODS_ARRAY = "goods_array";
    public static final String GOODS_PRICE = "goods_price";
    public static final String USER_ADDRESS = "shop_address";

    public static final String SQL_CREATE_ORDER =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    USER_ID + " TEXT," +
                    ORDER_NUMBER + " TEXT," +
                    CREATE_ORDER_TIME + " TEXT," +
                    ORDER_STATUS + " INTEGER," +
                    SHOP_ID + " INTEGER," +
                    SHOP_NAME + " TEXT," +
                    GOODS_ARRAY + " TEXT," +
                    GOODS_PRICE + " TEXT," +
                    USER_ADDRESS + " TEXT)";
}
