package cn.com.findfine.jddaojia.shop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.com.findfine.jddaojia.BaseActivity;
import cn.com.findfine.jddaojia.Constant;
import cn.com.findfine.jddaojia.R;
import cn.com.findfine.jddaojia.adapter.GoodsListAdapter;
import cn.com.findfine.jddaojia.data.bean.GoodsBean;
import cn.com.findfine.jddaojia.data.bean.ShopBean;
import cn.com.findfine.jddaojia.data.db.dao.ShoppingCartGoodsDao;
import cn.com.findfine.jddaojia.http.HttpRequest;
import cn.com.findfine.jddaojia.http.HttpUrl;
import cn.com.findfine.jddaojia.order.NewOrderActivity;
import cn.com.findfine.jddaojia.shopcart.ShopCartActivity;
import cn.com.findfine.jddaojia.utils.SharedPreferencesUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

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
//    private GoodsDao goodsDao;
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
//        goodsDao = new GoodsDao();

        loadData();

        goodsListAdapter = new GoodsListAdapter(this, shopBean.getShopName(), shopBean.getShopAddress());

        rvShopGoodsList.setAdapter(goodsListAdapter);

        ivShopPhoto = findViewById(R.id.iv_shop_photo);
        tvShopName = findViewById(R.id.tv_shop_name);

        Glide.with(this).load(HttpUrl.BASE_URL + shopBean.getShopPhoto()).into(ivShopPhoto);
        tvShopName.setText(shopBean.getShopName());

    }

    private void loadData() {
        cartCount = 0;
        cartGoodsPrice = 0.0f;

        FormBody.Builder builder = new FormBody.Builder();
        builder.add("shop_id", String.valueOf(shopBean.getShopId()));

        HttpRequest.requestPost("goods_list.php", builder, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.i("Response", result);
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    JSONArray shopList = jsonObj.getJSONArray("goods_list");
                    goodsBeans = new ArrayList<>();
                    for (int i = 0; i < shopList.length(); i++) {
                        JSONObject goodsObj = shopList.getJSONObject(i);
                        GoodsBean goodsBean = new GoodsBean();
                        goodsBean.setShopId(Integer.valueOf(goodsObj.getString("shop_id")));
                        goodsBean.setGoodsId(Integer.valueOf(goodsObj.getString("goods_id")));
                        goodsBean.setGoodsName(goodsObj.getString("goods_name"));
                        goodsBean.setGoodsPhoto(goodsObj.getString("goods_photo"));
                        goodsBean.setGoodsPrice(Float.valueOf(goodsObj.getString("goods_price")));

                        goodsBeans.add(goodsBean);
                    }

                    handler.sendEmptyMessage(1);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void initData() {
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

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                initData();
                goodsListAdapter.setGoodsBeans(goodsBeans);
                goodsListAdapter.notifyDataSetChanged();
            }
        }
    };

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
