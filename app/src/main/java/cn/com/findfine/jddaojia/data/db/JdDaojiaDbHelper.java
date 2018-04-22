package cn.com.findfine.jddaojia.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cn.com.findfine.jddaojia.JDApplication;
import cn.com.findfine.jddaojia.data.db.contract.GoodsContract;
import cn.com.findfine.jddaojia.data.db.contract.GoodsOrderContract;
import cn.com.findfine.jddaojia.data.db.contract.ShopContract;
import cn.com.findfine.jddaojia.data.db.contract.ShoppingCartGoodsContract;
import cn.com.findfine.jddaojia.data.db.contract.ShoppingCartShopContract;
import cn.com.findfine.jddaojia.data.db.contract.UserAddressContract;
import cn.com.findfine.jddaojia.data.db.contract.UserContract;


public class JdDaojiaDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "jddaojia.db";
    private static final int DB_VERSION = 1;


    public static JdDaojiaDbHelper getInstance() {
        return SingletonProvider.instance;
    }

    private static class SingletonProvider {
        private static JdDaojiaDbHelper instance = new JdDaojiaDbHelper(JDApplication.getApplication());
    }

    private JdDaojiaDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        onUpgrade(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (int version = oldVersion + 1; version <= newVersion; version++) {
            upgradeTo(db, version);
        }
    }

    /**
     * Upgrade database from (version - 1) to version.
     */
    private void upgradeTo(SQLiteDatabase db, int version) {
        switch (version) {
            case 1:
                createFirstTable(db);
                break;
            default:
                throw new IllegalStateException("Don't know how to upgrade to " + version);
        }
    }

    private void createFirstTable(SQLiteDatabase db) {
        db.execSQL(UserAddressContract.SQL_CREATE_USER_ADDRESS);
        db.execSQL(ShopContract.SQL_CREATE_SHOP);
        db.execSQL(GoodsContract.SQL_CREATE_GOODS);
        db.execSQL(ShoppingCartShopContract.SQL_CREATE_SHOPPING_CART);
        db.execSQL(ShoppingCartGoodsContract.SQL_CREATE_SHOPPING_CART);
        db.execSQL(UserContract.SQL_CREATE_USER_INFO);
        db.execSQL(GoodsOrderContract.SQL_CREATE_ORDER);
    }

}
