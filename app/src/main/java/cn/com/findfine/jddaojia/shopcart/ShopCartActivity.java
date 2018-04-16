package cn.com.findfine.jddaojia.shopcart;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

import cn.com.findfine.jddaojia.BaseActivity;
import cn.com.findfine.jddaojia.Constant;
import cn.com.findfine.jddaojia.R;
import cn.com.findfine.jddaojia.data.bean.GoodsBean;
import cn.com.findfine.jddaojia.data.db.dao.ShoppingCartGoodsDao;
import cn.com.findfine.jddaojia.data.db.dao.ShoppingCartShopDao;
import cn.com.findfine.jddaojia.order.NewOrderActivity;
import cn.com.findfine.jddaojia.shop.GoodsDetialActivity;
import cn.com.findfine.jddaojia.utils.FileUtil;
import cn.com.findfine.jddaojia.utils.SharedPreferencesUtil;

public class ShopCartActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView rvShopCart;
    private List<GoodsBean> goodsBeans;
    private String userId;
    private ShoppingCartShopDao shoppingCartShopDao = null;
    private ShoppingCartGoodsDao shoppingCartGoodsDao = null;
    private ShopCartAdapter shopCartAdapter;
    private View tvGotoPay;
    private TextView tvCartGoodsPrice;
    private int shopId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_cart);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("购物车");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        userId = SharedPreferencesUtil.getUserAccount(this);
        init();
    }

    private void init() {
        Intent intent = getIntent();
        shopId = intent.getIntExtra("shop_id", 0);

        tvGotoPay = findViewById(R.id.tv_goto_pay);
        tvCartGoodsPrice = findViewById(R.id.tv_cart_goods_price);

        tvGotoPay.setOnClickListener(this);

        shoppingCartGoodsDao = new ShoppingCartGoodsDao();
        goodsBeans = shoppingCartGoodsDao.queryGoodsCartByUserIdAndShopId(SharedPreferencesUtil.getUserAccount(this), shopId);
        setBottomStatus();
        rvShopCart = findViewById(R.id.rv_shop_cart);
        rvShopCart.setLayoutManager(new LinearLayoutManager(this));
        shopCartAdapter = new ShopCartAdapter();
        rvShopCart.setAdapter(shopCartAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_goto_pay:
                Intent gotoPayIntent = new Intent(this, NewOrderActivity.class);
                gotoPayIntent.putExtra("shop_id", shopId);
                startActivity(gotoPayIntent);
                break;
        }
    }

    private void setBottomStatus() {
        if (goodsBeans.size() <= 0) {
            tvGotoPay.setEnabled(false);
            tvGotoPay.setBackgroundColor(getResources().getColor(R.color.color_shop_deteil_gray));
            tvCartGoodsPrice.setText("￥0");
            return ;
        }

        tvGotoPay.setEnabled(true);
        tvGotoPay.setBackgroundColor(getResources().getColor(R.color.color_main_selected));
        float cartGoodsPrice = 0.0f;
        for (GoodsBean goodsBean : goodsBeans) {
            cartGoodsPrice += goodsBean.getGoodsPrice() * goodsBean.getGoodsCartCount();
        }

        tvCartGoodsPrice.setText("￥" + String.valueOf(cartGoodsPrice));
    }

    class ShopCartAdapter extends RecyclerView.Adapter<ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goods, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            GoodsBean goodsBean = goodsBeans.get(position);
            File file = new File(FileUtil.getCacheFilePath() + goodsBean.getGoodsPhoto());
            Glide.with(ShopCartActivity.this).load(file).into(holder.ivGoodsPhoto);
            holder.tvGoodsName.setText(goodsBean.getGoodsName());
            holder.tvGoodsPrice.setText(String.valueOf(goodsBean.getGoodsPrice()));
            holder.tvGoodsCount.setText(String.valueOf(goodsBean.getGoodsCartCount()));
        }

        @Override
        public int getItemCount() {
            return goodsBeans.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View btnSubtractGoods;
        View btnAddGoods;
        TextView tvGoodsCount;
        ImageView ivGoodsPhoto;
        TextView tvGoodsName;
        TextView tvGoodsPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GoodsBean goodsBean = goodsBeans.get(getLayoutPosition());
                    Intent intent = new Intent(ShopCartActivity.this, GoodsDetialActivity.class);
                    intent.putExtra("goods_info", goodsBean);
                    startActivity(intent);
                }
            });

            btnSubtractGoods = itemView.findViewById(R.id.btn_subtract_goods);
            btnAddGoods = itemView.findViewById(R.id.btn_add_goods);
            tvGoodsCount = itemView.findViewById(R.id.tv_goods_count);
            ivGoodsPhoto = itemView.findViewById(R.id.iv_goods_photo);
            tvGoodsName = itemView.findViewById(R.id.tv_goods_name);
            tvGoodsPrice = itemView.findViewById(R.id.tv_goods_price);

            btnSubtractGoods.setOnClickListener(this);
            btnAddGoods.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_subtract_goods:
                    try {
                        subtractGoods();
                        setBottomStatus();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.btn_add_goods:
                    try {
                        addGoods();
                        setBottomStatus();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }

        private void subtractGoods() {
            GoodsBean goodsBean = goodsBeans.get(getLayoutPosition());
            if (goodsBean.getGoodsCartCount() <= 0) {
//                Toast.makeText(context, "已经为0了", Toast.LENGTH_SHORT).show();
                return ;
            }
            Intent intent = new Intent();
            intent.putExtra(Constant.REFRESH_SHOP_CART, true);
            setResult(2001, intent);

            if (shoppingCartShopDao == null) {
                shoppingCartShopDao = new ShoppingCartShopDao();
            }

            if (shoppingCartGoodsDao == null) {
                shoppingCartGoodsDao = new ShoppingCartGoodsDao();
            }

            if (goodsBean.getGoodsCartCount() == 1) {
                goodsBean.setGoodsCartCount(0);
                shoppingCartGoodsDao.deleteGoodsCartById(goodsBean, userId);
                Log.i("DATA", "删除了goods cart");

                List<GoodsBean> goodsBeanList = shoppingCartGoodsDao.queryGoodsCartByUserIdAndShopId(userId, goodsBean.getShopId());
                if (goodsBeanList.size() <= 0) {
                    shoppingCartShopDao.deleteShopCartById(userId, goodsBean.getShopId());
                    Log.i("DATA", "删除了shop cart");

                }

                goodsBeans.remove(getLayoutPosition());
                shopCartAdapter.notifyDataSetChanged();
            } else {
                goodsBean.setGoodsCartCount(goodsBean.getGoodsCartCount() - 1);
                shoppingCartGoodsDao.updateGoodsCart(goodsBean, userId);
            }
            tvGoodsCount.setText(String.valueOf(goodsBean.getGoodsCartCount()));

        }

        private void addGoods() {
            Intent intent = new Intent();
            intent.putExtra(Constant.REFRESH_SHOP_CART, true);
            setResult(2001, intent);

            if (shoppingCartShopDao == null) {
                shoppingCartShopDao = new ShoppingCartShopDao();
            }
            GoodsBean goodsBean = goodsBeans.get(getLayoutPosition());

            if (shoppingCartGoodsDao == null) {
                shoppingCartGoodsDao = new ShoppingCartGoodsDao();
            }
            goodsBean.setGoodsCartCount(goodsBean.getGoodsCartCount() + 1);
            shoppingCartGoodsDao.updateGoodsCart(goodsBean, userId);
            tvGoodsCount.setText(String.valueOf(goodsBean.getGoodsCartCount()));

        }
    }
}
