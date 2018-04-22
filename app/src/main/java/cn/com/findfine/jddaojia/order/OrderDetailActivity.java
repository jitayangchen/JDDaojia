package cn.com.findfine.jddaojia.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import cn.com.findfine.jddaojia.BaseActivity;
import cn.com.findfine.jddaojia.R;
import cn.com.findfine.jddaojia.data.bean.GoodsBean;
import cn.com.findfine.jddaojia.data.bean.GoodsOrderBean;
import cn.com.findfine.jddaojia.utils.FileUtil;

public class OrderDetailActivity extends BaseActivity {

    private GoodsOrderBean goodsOrderBean;
    private float goodsPrice = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("订单详情");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        goodsOrderBean = intent.getParcelableExtra("goods_order_bean");
        init();
    }

    private void init() {
        TextView tvShopNameAddress = findViewById(R.id.tv_shop_name_address);
        TextView tvGoodsPriceRight = findViewById(R.id.tv_goods_price_right);
        TextView tvAllPriceRight = findViewById(R.id.tv_all_price_right);
        TextView tvUserAddress = findViewById(R.id.tv_user_address);
        TextView tvOrderNumber = findViewById(R.id.tv_order_number);
        TextView tvOrderTime = findViewById(R.id.tv_order_time);
        LinearLayout llOrderGoods = findViewById(R.id.ll_order_goods);

        tvShopNameAddress.setText(goodsOrderBean.getShopName());
        tvUserAddress.setText(goodsOrderBean.getUserAddress());
        tvOrderNumber.setText(goodsOrderBean.getOrderNumber());
        tvOrderTime.setText(goodsOrderBean.getCreateOrderTime());

        initGoodsList(goodsOrderBean.getGoodsArray(), llOrderGoods);

        tvGoodsPriceRight.setText("￥" + String.valueOf(goodsPrice));
        tvAllPriceRight.setText("￥" + String.valueOf(goodsPrice + 10.0f));
    }

    private void initGoodsList(String goodsJson, LinearLayout llOrderGoods) {
        try {
            JSONArray jsonArray = new JSONArray(goodsJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject goodsObject = jsonArray.getJSONObject(i);
                GoodsBean goodsBean = new GoodsBean();
                goodsBean.setGoodsId(goodsObject.getInt("goods_id"));
                goodsBean.setGoodsName(goodsObject.getString("goods_name"));
                goodsBean.setGoodsPhoto(goodsObject.getString("goods_photo"));
                goodsBean.setGoodsPrice((float) goodsObject.getDouble("goods_price"));
                goodsBean.setGoodsCartCount(goodsObject.getInt("goods_count"));

                goodsPrice += goodsBean.getGoodsPrice() * goodsBean.getGoodsCartCount();

                View view = LayoutInflater.from(this).inflate(R.layout.item_order_detail_goods, null, false);
                ImageView ivGoodsPhoto = view.findViewById(R.id.iv_goods_photo);
                TextView tvGoodsName = view.findViewById(R.id.tv_goods_name);
                TextView tvGoodsPriceAndCount = view.findViewById(R.id.tv_goods_price_and_count);
                TextView tvGoodsPrice = view.findViewById(R.id.tv_goods_price);
                File file = new File(FileUtil.getCacheFilePath() + goodsBean.getGoodsPhoto());
                Glide.with(this).load(file).into(ivGoodsPhoto);
                tvGoodsName.setText(goodsBean.getGoodsName());
                tvGoodsPrice.setText("￥" + String.valueOf(goodsBean.getGoodsPrice()));
                tvGoodsPriceAndCount.setText("￥" + String.valueOf(goodsBean.getGoodsPrice()) + " X " + String.valueOf(goodsBean.getGoodsCartCount()));
                llOrderGoods.addView(view);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
