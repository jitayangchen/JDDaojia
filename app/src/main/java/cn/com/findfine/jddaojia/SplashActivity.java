package cn.com.findfine.jddaojia;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import cn.com.findfine.jddaojia.data.JsonData;
import cn.com.findfine.jddaojia.data.bean.GoodsBean;
import cn.com.findfine.jddaojia.data.bean.ShopBean;
import cn.com.findfine.jddaojia.data.db.dao.GoodsDao;
import cn.com.findfine.jddaojia.data.db.dao.ShopDao;
import cn.com.findfine.jddaojia.utils.FileUtil;
import cn.com.findfine.jddaojia.utils.SharedPreferencesUtil;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        } else {
            initData();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode ==100 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            initData();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void initData() {

        boolean isInitData = SharedPreferencesUtil.getIsInitData(this);
        if (isInitData)
            return ;

        copyAssetsToSdcard();

        try {
            JSONArray jsonArray = new JSONArray(JsonData.SHOP_GOODS);
            for (int i = 0; i < jsonArray.length(); i++) {
                ShopBean shopBean = new ShopBean();
                JSONObject shopObject = jsonArray.getJSONObject(i);
                shopBean.setShopId(shopObject.getInt("shop_id"));
                shopBean.setShopName(shopObject.getString("shop_name"));
                shopBean.setShopPhoto(shopObject.getString("shop_photo"));
                shopBean.setShopAddress(shopObject.getString("shop_address"));


                ShopDao shopDao = new ShopDao();
                shopDao.insertShop(shopBean);

                JSONArray goodsList = shopObject.getJSONArray("goods_list");
                for (int j = 0; j < goodsList.length(); j++) {
                    JSONObject goodsObject = goodsList.getJSONObject(j);
                    GoodsBean goodsBean = new GoodsBean();
                    goodsBean.setGoodsId(goodsObject.getInt("goods_id"));
                    goodsBean.setShopId(goodsObject.getInt("shop_id"));
                    goodsBean.setGoodsName(goodsObject.getString("goods_name"));
                    goodsBean.setGoodsPhoto(goodsObject.getString("goods_photo"));
                    goodsBean.setGoodsPrice((float) goodsObject.getDouble("goods_price"));
                    goodsBean.setGoodsCategory(goodsObject.getString("goods_category"));
                    goodsBean.setGoodsSalesVolume(goodsObject.getInt("goods_sales_volume"));


                    GoodsDao goodsDao = new GoodsDao();
                    goodsDao.insertGoods(goodsBean);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SharedPreferencesUtil.setIsInitData(this, true);
    }

    private void copyAssetsToSdcard() {
        String goodsDirectory = Environment.getExternalStorageDirectory() + "/jddaojia";
        File goodsDirectoryFile = new File(goodsDirectory);
        if (!goodsDirectoryFile.exists()) {
            goodsDirectoryFile.mkdirs();
        }
        AssetManager assetManager = getAssets();
        String[] list = new String[0];
        try {
            list = assetManager.list("goods");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String fileName : list) {
            FileUtil.copyAssertFile(getAssets(), "goods/" + fileName, goodsDirectory + "/" + fileName);
            Log.i("FileName", fileName);
        }
    }
}
