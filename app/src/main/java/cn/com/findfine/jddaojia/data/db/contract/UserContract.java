package cn.com.findfine.jddaojia.data.db.contract;


public class UserContract {

    public static final String TABLE_NAME = "user_info";
    public static final String _ID = "_id";
    public static final String USER_ID = "user_id";
    public static final String NICK_NAME = "nick_name";
    public static final String PASSWORD = "password";

    public static final String SQL_CREATE_USER_INFO =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    USER_ID + " TEXT," +
                    NICK_NAME + " TEXT," +
                    PASSWORD + " TEXT)";
}
