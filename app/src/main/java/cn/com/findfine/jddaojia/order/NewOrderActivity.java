package cn.com.findfine.jddaojia.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.com.findfine.jddaojia.BaseActivity;
import cn.com.findfine.jddaojia.R;
import cn.com.findfine.jddaojia.data.bean.GoodsBean;
import cn.com.findfine.jddaojia.data.bean.GoodsOrderBean;
import cn.com.findfine.jddaojia.data.bean.UserAddress;
import cn.com.findfine.jddaojia.data.db.contract.GoodsOrderContract;
import cn.com.findfine.jddaojia.data.db.dao.GoodsOrderDao;
import cn.com.findfine.jddaojia.data.db.dao.ShoppingCartGoodsDao;
import cn.com.findfine.jddaojia.data.db.dao.ShoppingCartShopDao;
import cn.com.findfine.jddaojia.data.db.dao.UserAddressDao;
import cn.com.findfine.jddaojia.utils.FileUtil;
import cn.com.findfine.jddaojia.utils.SharedPreferencesUtil;

public class NewOrderActivity extends BaseActivity {

    private List<GoodsBean> goodsBeans;
    private int shopId;
    private float cartGoodsPrice = 0.0f;
    private UserAddress userAddress;
    private String goodsArrayJson;
    private boolean isCreateOrder = false;
    private String orderNumber;
    private String userId;

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
        userId = SharedPreferencesUtil.getUserAccount(this);
        init();
    }

    private void init() {
        TextView tvUserAddress = findViewById(R.id.tv_user_address);
        TextView tvUserInfo = findViewById(R.id.tv_user_info);

        UserAddressDao userAddressDao = new UserAddressDao();
        List<UserAddress> userAddressList = userAddressDao.queryAllUserAddressByUserId(SharedPreferencesUtil.getUserAccount(this));
        if (userAddressList.size() > 0) {
            userAddress = userAddressList.get(0);
            tvUserAddress.setText(userAddress.getAddress());

            tvUserInfo.setText(userAddress.getName() + "        " + userAddress.getPhoneNumber());
        }

        ShoppingCartGoodsDao shoppingCartGoodsDao = new ShoppingCartGoodsDao();
        goodsBeans = shoppingCartGoodsDao.queryGoodsCartByUserIdAndShopId(SharedPreferencesUtil.getUserAccount(this), shopId);

        TextView tvPayPrice = findViewById(R.id.tv_pay_price);

        JSONArray jsonArray = new JSONArray();
        for (GoodsBean goodsBean : goodsBeans) {
            cartGoodsPrice += goodsBean.getGoodsPrice() * goodsBean.getGoodsCartCount();

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("goods_id", goodsBean.getGoodsId());
                jsonObject.put("goods_name", goodsBean.getGoodsName());
                jsonObject.put("goods_photo", goodsBean.getGoodsPhoto());
                jsonObject.put("goods_price", goodsBean.getGoodsPrice());
                jsonObject.put("goods_count", goodsBean.getGoodsCartCount());

                jsonArray.put(jsonObject);
                goodsArrayJson = jsonArray.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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

    private void createOrder() {
        if (userAddress == null) {
            Toast.makeText(this, "请选择收货地址", Toast.LENGTH_SHORT).show();
            return ;
        }
        if (!isCreateOrder) {
            GoodsOrderDao goodsOrderDao = new GoodsOrderDao();
            GoodsOrderBean goodsOrderBean = new GoodsOrderBean();
            goodsOrderBean.setUserId(userId);
            orderNumber = String.valueOf(System.currentTimeMillis());
            goodsOrderBean.setOrderNumber(orderNumber);

            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            goodsOrderBean.setCreateOrderTime(sdf.format(date));
            goodsOrderBean.setOrderStatus(GoodsOrderContract.ORDER_STATUS_UNPAY);
            goodsOrderBean.setShopId(shopId);
            goodsOrderBean.setGoodsArray(goodsArrayJson);
            goodsOrderBean.setGoodsPrice(String.valueOf(cartGoodsPrice));
            goodsOrderBean.setUserAddress(userAddress.getName() + "  " + userAddress.getPhoneNumber() + '\n' + userAddress.getAddress());
            goodsOrderDao.insertOrder(goodsOrderBean);



            ShoppingCartShopDao shoppingCartShopDao = new ShoppingCartShopDao();
            shoppingCartShopDao.deleteShopCartById(userId, shopId);
            ShoppingCartGoodsDao shoppingCartGoodsDao = new ShoppingCartGoodsDao();
            shoppingCartGoodsDao.deleteGoodsCartByUserIdAndShopId(userId, shopId);
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
            File file = new File(FileUtil.getCacheFilePath() + goodsBean.getGoodsPhoto());
            Glide.with(NewOrderActivity.this).load(file).into(holder.ivGoodsPhoto);
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
