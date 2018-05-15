package cn.com.findfine.jddaojia.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.com.findfine.jddaojia.BaseActivity;
import cn.com.findfine.jddaojia.R;
import cn.com.findfine.jddaojia.data.bean.GoodsBean;
import cn.com.findfine.jddaojia.data.bean.GoodsOrderBean;
import cn.com.findfine.jddaojia.http.HttpUrl;

public class OrderDetailActivity extends BaseActivity {

    private GoodsOrderBean goodsOrderBean;
    private float goodsPrice = 0.0f;
    private Button btnOrderEvaluation;

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

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sdf.parse(goodsOrderBean.getCreateOrderTime());
            tvOrderNumber.setText(String.valueOf(date.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tvOrderTime.setText(goodsOrderBean.getCreateOrderTime());

        initGoodsList(goodsOrderBean.getGoodsArray(), llOrderGoods);

        tvGoodsPriceRight.setText("￥" + String.valueOf(goodsPrice));
        tvAllPriceRight.setText("￥" + String.valueOf(goodsPrice + 10.0f));

        btnOrderEvaluation = findViewById(R.id.btn_order_evaluation);
        btnOrderEvaluation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetailActivity.this, OrderEvaluationActivity.class);
                intent.putExtra("order_bean", goodsOrderBean);
                startActivityForResult(intent, 1211);
            }
        });

        if (goodsOrderBean.getOrderEvaluation() > 0) {
            btnOrderEvaluation.setVisibility(View.GONE);
        }


        Button btnOrderPay = findViewById(R.id.btn_order_pay);
        if (goodsOrderBean.getOrderStatus() != 1) {
            btnOrderPay.setVisibility(View.VISIBLE);
        }
        btnOrderPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetailActivity.this, PayActivity.class);
                intent.putExtra("price", goodsPrice + 10f);
                intent.putExtra("order_id", goodsOrderBean.getOrderId());
                startActivity(intent);
            }
        });
    }

    private void initGoodsList(List<GoodsBean> goodsBeans, LinearLayout llOrderGoods) {
        for (int i = 0; i < goodsBeans.size(); i++) {
            GoodsBean goodsBean = goodsBeans.get(i);

            goodsPrice += goodsBean.getGoodsPrice() * goodsBean.getGoodsCartCount();

            View view = LayoutInflater.from(this).inflate(R.layout.item_order_detail_goods, null, false);
            ImageView ivGoodsPhoto = view.findViewById(R.id.iv_goods_photo);
            TextView tvGoodsName = view.findViewById(R.id.tv_goods_name);
            TextView tvGoodsPriceAndCount = view.findViewById(R.id.tv_goods_price_and_count);
            TextView tvGoodsPrice = view.findViewById(R.id.tv_goods_price);
            Glide.with(this).load(HttpUrl.BASE_URL + goodsBean.getGoodsPhoto()).into(ivGoodsPhoto);
            tvGoodsName.setText(goodsBean.getGoodsName());
            tvGoodsPrice.setText("￥" + String.valueOf(goodsBean.getGoodsPrice()));
            tvGoodsPriceAndCount.setText("￥" + String.valueOf(goodsBean.getGoodsPrice()) + " X " + String.valueOf(goodsBean.getGoodsCartCount()));
            llOrderGoods.addView(view);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1211 && resultCode == 1212) {
            boolean isEvaluation = data.getBooleanExtra("is_evaluation", false);
            if (isEvaluation) {
                btnOrderEvaluation.setVisibility(View.GONE);
            }
        }
    }
}
