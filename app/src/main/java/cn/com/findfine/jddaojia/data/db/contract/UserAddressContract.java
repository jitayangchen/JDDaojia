package cn.com.findfine.jddaojia.data.db.contract;

/**
 * Created by yangchen on 2017/9/25.
 */

public class UserAddressContract {

    public static final String TABLE_NAME = "user_address";
    public static final String _ID = "_id";
    public static final String USER_ID = "user_id";
    public static final String CITY = "city";
    public static final String ADDRESS = "address";
    public static final String HOUSE_NUMBER = "house_number";
    public static final String NAME = "name";
    public static final String PHONE_NUMBER = "phone_number";

    public static final String SQL_CREATE_USER_ADDRESS =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    USER_ID + " TEXT," +
                    CITY + " TEXT," +
                    ADDRESS + " TEXT," +
                    HOUSE_NUMBER + " TEXT," +
                    NAME + " TEXT," +
                    PHONE_NUMBER + " TEXT)";
}
