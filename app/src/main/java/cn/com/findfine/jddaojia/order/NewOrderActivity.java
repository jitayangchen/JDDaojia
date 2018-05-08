package cn.com.findfine.jddaojia.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.com.findfine.jddaojia.BaseActivity;
import cn.com.findfine.jddaojia.Constant;
import cn.com.findfine.jddaojia.R;
import cn.com.findfine.jddaojia.data.bean.GoodsBean;
import cn.com.findfine.jddaojia.data.bean.UserAddress;
import cn.com.findfine.jddaojia.data.db.dao.ShoppingCartGoodsDao;
import cn.com.findfine.jddaojia.data.db.dao.ShoppingCartShopDao;
import cn.com.findfine.jddaojia.data.db.dao.UserAddressDao;
import cn.com.findfine.jddaojia.http.HttpRequest;
import cn.com.findfine.jddaojia.http.HttpUrl;
import cn.com.findfine.jddaojia.myinfo.MyAddressActivity;
import cn.com.findfine.jddaojia.utils.SharedPreferencesUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class NewOrderActivity extends BaseActivity implements View.OnClickListener {

    private List<GoodsBean> goodsBeans;
    private int shopId;
    private float cartGoodsPrice = 0.0f;
    private UserAddress userAddress;
    private String goodsArrayJson;
    private boolean isCreateOrder = false;
    private String orderNumber;
    private String userId;
    private String shopName;
    private UserAddressDao userAddressDao;
    private TextView tvUserAddress;
    private TextView tvUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("订单配送至");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        shopId = intent.getIntExtra("shop_id", 0);
        shopName = intent.getStringExtra("shop_name");
        userId = SharedPreferencesUtil.getUserAccount(this);
        init();
    }

    private void init() {
        tvUserAddress = findViewById(R.id.tv_user_address);
        tvUserInfo = findViewById(R.id.tv_user_info);
        tvUserAddress.setOnClickListener(this);
        tvUserInfo.setOnClickListener(this);

        userAddressDao = new UserAddressDao();
        initUserAddress();

        ShoppingCartGoodsDao shoppingCartGoodsDao = new ShoppingCartGoodsDao();
        goodsBeans = shoppingCartGoodsDao.queryGoodsCartByUserIdAndShopId(SharedPreferencesUtil.getUserAccount(this), shopId);

        TextView tvPayPrice = findViewById(R.id.tv_pay_price);

        JSONArray jsonArray = new JSONArray();
        for (GoodsBean goodsBean : goodsBeans) {
            cartGoodsPrice += goodsBean.getGoodsPrice() * goodsBean.getGoodsCartCount();
            jsonArray.put(goodsBean.getGoodsId());
        }
        goodsArrayJson = jsonArray.toString();
        tvPayPrice.setText("￥" + String.valueOf(cartGoodsPrice));

        RecyclerView rvOrderGoodsList = findViewById(R.id.rv_order_goods_list);
        rvOrderGoodsList.setLayoutManager(new LinearLayoutManager(this));
        rvOrderGoodsList.setAdapter(new ShopCartAdapter());


        Button btnCommitOrder = findViewById(R.id.btn_commit_order);
        btnCommitOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createOrder();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_user_address:
            case R.id.tv_user_info:
                Intent intent = new Intent(this, MyAddressActivity.class);
                intent.putExtra("source", "new_order");
                startActivityForResult(intent, 3000);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3000 && resultCode == 3001) {
            userAddress = data.getParcelableExtra("user_address");
            initUserAddress();
        }
    }

    private void initUserAddress() {
        if (userAddress != null) {
            tvUserAddress.setText(userAddress.getAddress());

            tvUserInfo.setVisibility(View.VISIBLE);
            tvUserInfo.setText(userAddress.getName() + "        " + userAddress.getPhoneNumber());
        } else {
            tvUserAddress.setText("请选择收货地址");
            tvUserInfo.setVisibility(View.GONE);
        }
    }

    private void createOrder() {
        if (userAddress == null) {
            Toast.makeText(this, "请选择收货地址", Toast.LENGTH_SHORT).show();
            return ;
        }
        if (!isCreateOrder) {
//            GoodsOrderDao goodsOrderDao = new GoodsOrderDao();
//            GoodsOrderBean goodsOrderBean = new GoodsOrderBean();
//            goodsOrderBean.setUserId(userId);
            orderNumber = String.valueOf(System.currentTimeMillis());
//            goodsOrderBean.setOrderNumber(orderNumber);

            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            goodsOrderBean.setCreateOrderTime(sdf.format(date));
//            goodsOrderBean.setOrderStatus(GoodsOrderContract.ORDER_STATUS_UNPAY);
//            goodsOrderBean.setShopId(shopId);
//            goodsOrderBean.setShopName(shopName);
//            goodsOrderBean.setGoodsArray(goodsArrayJson);
//            goodsOrderBean.setGoodsPrice(String.valueOf(cartGoodsPrice));
//            goodsOrderBean.setUserAddress(userAddress.getName() + "  " + userAddress.getPhoneNumber() + '\n' + userAddress.getAddress());
//            goodsOrderDao.insertOrder(goodsOrderBean);








            FormBody.Builder builder = new FormBody.Builder();
            builder.add("shop_id", String.valueOf(shopId));
            builder.add("goods_id", goodsArrayJson);
            builder.add("create_time", sdf.format(date));
            builder.add("user_address", userAddress.getName() + "  " + userAddress.getPhoneNumber() + '\n' + userAddress.getAddress());
            builder.add("user_id", SharedPreferencesUtil.getUserAccount(this));
            builder.add("user_phone", SharedPreferencesUtil.getUserAccount(this));
            Log.i("params", goodsArrayJson);

            HttpRequest.requestPost("http://115.28.17.184/order_add.php", builder, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    Log.i("Response", result);
//                    {"success":true,"message":"添加成功"}
                    try {
                        JSONObject jsonObj = new JSONObject(result);
                        String success = jsonObj.getString("success");
                        if ("true".equals(success)) {
                            ShoppingCartShopDao shoppingCartShopDao = new ShoppingCartShopDao();
                            shoppingCartShopDao.deleteShopCartById(userId, shopId);
                            ShoppingCartGoodsDao shoppingCartGoodsDao = new ShoppingCartGoodsDao();
                            shoppingCartGoodsDao.deleteGoodsCartByUserIdAndShopId(userId, shopId);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            Intent intent = new Intent();
            intent.putExtra(Constant.REFRESH_SHOP_CART, true);
            setResult(2001, intent);

            isCreateOrder = true;
        }

        Intent intent = new Intent(this, PayActivity.class);
        intent.putExtra("price", cartGoodsPrice);
        intent.putExtra("order_number", orderNumber);
        startActivity(intent);
    }

    class ShopCartAdapter extends RecyclerView.Adapter<ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_goods, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            GoodsBean goodsBean = goodsBeans.get(position);
            Glide.with(NewOrderActivity.this).load(HttpUrl.BASE_URL + goodsBean.getGoodsPhoto()).into(holder.ivGoodsPhoto);
            holder.tvGoodsName.setText(goodsBean.getGoodsName());
            holder.tvGoodsPrice.setText("￥" + String.valueOf(goodsBean.getGoodsPrice()));
            holder.tvGoodsPriceAndCount.setText("￥" + String.valueOf(goodsBean.getGoodsPrice()) + " X " + String.valueOf(goodsBean.getGoodsCartCount()));
        }

        @Override
        public int getItemCount() {
            return goodsBeans.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivGoodsPhoto;
        TextView tvGoodsName;
        TextView tvGoodsPriceAndCount;
        TextView tvGoodsPrice;

        public ViewHolder(View itemView) {
            super(itemView);

            ivGoodsPhoto = itemView.findViewById(R.id.iv_goods_photo);
            tvGoodsName = itemView.findViewById(R.id.tv_goods_name);
            tvGoodsPriceAndCount = itemView.findViewById(R.id.tv_goods_price_and_count);
            tvGoodsPrice = itemView.findViewById(R.id.tv_goods_price);

        }

    }
}
