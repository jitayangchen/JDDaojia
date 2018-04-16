package cn.com.findfine.jddaojia.shop;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;

import cn.com.findfine.jddaojia.BaseActivity;
import cn.com.findfine.jddaojia.R;
import cn.com.findfine.jddaojia.data.bean.GoodsBean;
import cn.com.findfine.jddaojia.utils.FileUtil;

public class GoodsDetialActivity extends BaseActivity {

    private GoodsBean goodsBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detial);

        setStatusBarColor(Color.WHITE);

        Intent intent = getIntent();
        goodsBean = intent.getParcelableExtra("goods_info");

        init();


    }

    private void init() {
        ImageView ivGoodsPhoto = findViewById(R.id.iv_goods_photo);
        TextView tvGoodsName = findViewById(R.id.tv_goods_name);
        TextView tvGoodsPrice = findViewById(R.id.tv_goods_price);
        TextView tvShopName = findViewById(R.id.tv_shop_name);
        File file = new File(FileUtil.getCacheFilePath() + goodsBean.getGoodsPhoto());
        Glide.with(this).load(file).into(ivGoodsPhoto);

        tvGoodsName.setText(goodsBean.getGoodsName());
        tvGoodsPrice.setText("￥" + String.valueOf(goodsBean.getGoodsPrice()));
    }
}
