package cn.com.findfine.jddaojia.shop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

import cn.com.findfine.jddaojia.BaseActivity;
import cn.com.findfine.jddaojia.Constant;
import cn.com.findfine.jddaojia.R;
import cn.com.findfine.jddaojia.adapter.GoodsListAdapter;
import cn.com.findfine.jddaojia.data.bean.GoodsBean;
import cn.com.findfine.jddaojia.data.bean.ShopBean;
import cn.com.findfine.jddaojia.data.db.dao.GoodsDao;
import cn.com.findfine.jddaojia.data.db.dao.ShoppingCartGoodsDao;
import cn.com.findfine.jddaojia.order.NewOrderActivity;
import cn.com.findfine.jddaojia.shopcart.ShopCartActivity;
import cn.com.findfine.jddaojia.utils.FileUtil;
import cn.com.findfine.jddaojia.utils.SharedPreferencesUtil;

public class ShopDetialActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView rvShopGoodsList;
    private ShopBean shopBean;
    private ImageView ivShopPhoto;
    private TextView tvShopName;
    private LocalBroadcastManager localBroadcastManager;
    private MyBroadcastReceiver myBroadcastReceiver;
    private ImageButton btnCart;
    private TextView tvGotoPay;
    private TextView tvCartInfo;
    private TextView tvCartCount;
    private TextView tvCartGoodsPrice;
    private int cartCount = 0;
    private float cartGoodsPrice = 0;
    private GoodsListAdapter goodsListAdapter;
    private GoodsDao goodsDao;
    private List<GoodsBean> goodsBeans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detial);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        myBroadcastReceiver = new MyBroadcastReceiver();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.SHOP_CART_STATUS);
        localBroadcastManager.registerReceiver(myBroadcastReceiver, intentFilter);

        Intent intent = getIntent();
        shopBean = intent.getParcelableExtra("shop_info");

        init();
    }

    private void init() {
        rvShopGoodsList = findViewById(R.id.rv_shop_goods_list);
        btnCart = findViewById(R.id.btn_cart);
        tvGotoPay = findViewById(R.id.tv_goto_pay);
        tvCartInfo = findViewById(R.id.tv_cart_info);
        tvCartCount = findViewById(R.id.tv_cart_count);
        tvCartGoodsPrice = findViewById(R.id.tv_cart_goods_price);

        btnCart.setOnClickListener(this);
        tvGotoPay.setOnClickListener(this);

        rvShopGoodsList.setLayoutManager(new LinearLayoutManager(this));
        goodsDao = new GoodsDao();

        loadData();

        goodsListAdapter = new GoodsListAdapter(this, shopBean.getShopName(), shopBean.getShopAddress());
        goodsListAdapter.setGoodsBeans(goodsBeans);
        rvShopGoodsList.setAdapter(goodsListAdapter);

        ivShopPhoto = findViewById(R.id.iv_shop_photo);
        tvShopName = findViewById(R.id.tv_shop_name);

        File file = new File(FileUtil.getCacheFilePath() + shopBean.getShopPhoto());
        Glide.with(this).load(file).into(ivShopPhoto);
        tvShopName.setText(shopBean.getShopName());

    }

    private void loadData() {
        cartCount = 0;
        cartGoodsPrice = 0.0f;

        goodsBeans = goodsDao.queryGoodsByShopId(shopBean.getShopId());
        boolean loginStatus = SharedPreferencesUtil.getLoginStatus(this);
        if (loginStatus) {
            ShoppingCartGoodsDao shoppingCartGoodsDao = new ShoppingCartGoodsDao();
            List<GoodsBean> goodsBeansCart = shoppingCartGoodsDao.queryGoodsCartByUserIdAndShopId(SharedPreferencesUtil.getUserAccount(this), shopBean.getShopId());
            if (goodsBeansCart.size() > 0) {
                setBottomStatus(true);

                for (GoodsBean goodsBean : goodsBeans) {
                    for (GoodsBean goodsBeanTemp : goodsBeansCart) {
                        if (goodsBean.getGoodsId() == goodsBeanTemp.getGoodsId()) {
                            cartCount += goodsBeanTemp.getGoodsCartCount();
                            cartGoodsPrice += goodsBeanTemp.getGoodsPrice() * goodsBeanTemp.getGoodsCartCount();
                            goodsBean.setGoodsCartCount(goodsBeanTemp.getGoodsCartCount());
                            break;
                        }
                    }
                }
                tvCartCount.setText(String.valueOf(cartCount));
                tvCartGoodsPrice.setText("￥" + String.valueOf(cartGoodsPrice));
            } else {
                setBottomStatus(false);
            }
        } else {
            setBottomStatus(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cart:
                Intent intent = new Intent(this, ShopCartActivity.class);
                intent.putExtra("shop_id", shopBean.getShopId());
                intent.putExtra("shop_name", shopBean.getShopName() + "(" + shopBean.getShopAddress() + ")");
                startActivityForResult(intent, 2000);
                break;
            case R.id.tv_goto_pay:
                Intent gotoPayIntent = new Intent(this, NewOrderActivity.class);
                gotoPayIntent.putExtra("shop_id", shopBean.getShopId());
                gotoPayIntent.putExtra("shop_name", shopBean.getShopName() + "(" + shopBean.getShopAddress() + ")");
                startActivityForResult(gotoPayIntent, 2000);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2000 && resultCode == 2001) {
            boolean refreshShopCart = data.getBooleanExtra(Constant.REFRESH_SHOP_CART, false);
            if (refreshShopCart) {
                loadData();

                goodsListAdapter.setGoodsBeans(goodsBeans);
                goodsListAdapter.notifyDataSetChanged();
            }
        }
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Constant.SHOP_CART_STATUS.equals(action)) {
                String shopCartStatus = intent.getStringExtra(Constant.SHOP_CART_STATUS);
                String shopCartCount = intent.getStringExtra(Constant.SHOP_CART_COUNT);
                float goodsPrice = intent.getFloatExtra(Constant.SHOP_CART_GOODS_PRICE, 0);
                if (!TextUtils.isEmpty(shopCartStatus)) {
                    if ("gone".equals(shopCartStatus)) {
                        setBottomStatus(false);
                    } else {
                        setBottomStatus(true);
                    }
                }

                if (!TextUtils.isEmpty(shopCartCount)) {
                    if ("add".equals(shopCartCount)) {
                        cartCount++;
                        cartGoodsPrice += goodsPrice;
                    } else {
                        cartCount--;
                        cartGoodsPrice -= goodsPrice;
                    }
                    tvCartCount.setText(String.valueOf(cartCount));
                    tvCartGoodsPrice.setText("￥" + String.valueOf(cartGoodsPrice));
                }
            }

        }
    }

    private void setBottomStatus(boolean shopCartStatus) {
        if (shopCartStatus) {
            btnCart.setEnabled(true);
            tvGotoPay.setEnabled(true);
            btnCart.setBackgroundResource(R.mipmap.goto_cart_enable);
            tvGotoPay.setBackgroundColor(getResources().getColor(R.color.color_main_selected));
            tvCartInfo.setVisibility(View.GONE);
            tvCartCount.setVisibility(View.VISIBLE);
            tvCartGoodsPrice.setVisibility(View.VISIBLE);
        } else {
            btnCart.setEnabled(false);
            tvGotoPay.setEnabled(false);
            btnCart.setBackgroundResource(R.mipmap.goto_cart_disable);
            tvGotoPay.setBackgroundColor(getResources().getColor(R.color.color_shop_deteil_gray));
            tvCartInfo.setVisibility(View.VISIBLE);
            tvCartCount.setVisibility(View.GONE);
            tvCartGoodsPrice.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(myBroadcastReceiver);
    }
}
