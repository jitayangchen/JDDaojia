package cn.com.findfine.jddaojia.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

import cn.com.findfine.jddaojia.Constant;
import cn.com.findfine.jddaojia.R;
import cn.com.findfine.jddaojia.data.bean.GoodsBean;
import cn.com.findfine.jddaojia.data.bean.ShoppingCartShopBean;
import cn.com.findfine.jddaojia.data.db.dao.ShoppingCartGoodsDao;
import cn.com.findfine.jddaojia.data.db.dao.ShoppingCartShopDao;
import cn.com.findfine.jddaojia.login.LoginActivity;
import cn.com.findfine.jddaojia.shop.GoodsDetialActivity;
import cn.com.findfine.jddaojia.utils.FileUtil;
import cn.com.findfine.jddaojia.utils.SharedPreferencesUtil;

public class GoodsListAdapter extends RecyclerView.Adapter<GoodsListAdapter.ViewHolder> {

    private Context context;
    private List<GoodsBean> goodsBeans;
    private String shopName;
    private String shopAddress;
    private String userId;
    private ShoppingCartShopDao shoppingCartShopDao = null;
    private ShoppingCartGoodsDao shoppingCartGoodsDao = null;

    public GoodsListAdapter(Context context, String shopName, String shopAddress) {
        this.context = context;
        this.shopName = shopName;
        this.shopAddress = shopAddress;
        userId = SharedPreferencesUtil.getUserAccount(context);
    }

    public void setGoodsBeans(List<GoodsBean> goodsBeans) {
        this.goodsBeans = goodsBeans;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = View.inflate(parent.getContext(), R.layout.item_home_page, parent);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goods, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GoodsBean goodsBean = goodsBeans.get(position);
        File file = new File(FileUtil.getCacheFilePath() + goodsBean.getGoodsPhoto());
        Glide.with(context).load(file).into(holder.ivGoodsPhoto);
        holder.tvGoodsName.setText(goodsBean.getGoodsName());
        holder.tvGoodsPrice.setText(String.valueOf(goodsBean.getGoodsPrice()));
        holder.tvGoodsCount.setText(String.valueOf(goodsBean.getGoodsCartCount()));
    }

    @Override
    public int getItemCount() {
        return goodsBeans.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View btnSubtractGoods;
        View btnAddGoods;
        TextView tvGoodsCount;
        ImageView ivGoodsPhoto;
        TextView tvGoodsName;
        TextView tvGoodsPrice;

        public ViewHolder(final View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GoodsBean goodsBean = goodsBeans.get(getLayoutPosition());

                    Intent intent = new Intent(context, GoodsDetialActivity.class);
                    intent.putExtra("goods_info", goodsBean);
                    context.startActivity(intent);
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
            boolean loginStatus = SharedPreferencesUtil.getLoginStatus(context);
            if (!loginStatus) {
                context.startActivity(new Intent(context, LoginActivity.class));
                return ;
            } else if (TextUtils.isEmpty(userId)) {
                userId = SharedPreferencesUtil.getUserAccount(context);
            }


            switch (v.getId()) {
                case R.id.btn_subtract_goods:
                    try {
                        subtractGoods();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.btn_add_goods:
                    try {
                        addGoods();
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

            if (shoppingCartShopDao == null) {
                shoppingCartShopDao = new ShoppingCartShopDao();
            }

            if (shoppingCartGoodsDao == null) {
                shoppingCartGoodsDao = new ShoppingCartGoodsDao();
            }

            String shopCartStatus = null;
            if (goodsBean.getGoodsCartCount() == 1) {
                goodsBean.setGoodsCartCount(0);
                shoppingCartGoodsDao.deleteGoodsCartById(goodsBean, userId);
                Log.i("DATA", "删除了goods cart");

                List<GoodsBean> goodsBeanList = shoppingCartGoodsDao.queryGoodsCartByUserIdAndShopId(userId, goodsBean.getShopId());
                if (goodsBeanList.size() <= 0) {
                    shoppingCartShopDao.deleteShopCartById(userId, goodsBean.getShopId());
                    Log.i("DATA", "删除了shop cart");

//                    Intent intent = new Intent(Constant.SHOP_CART_STATUS);
//                    intent.putExtra(Constant.SHOP_CART_STATUS, "gone");
//                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                    shopCartStatus = "gone";
                }
            } else {
                goodsBean.setGoodsCartCount(goodsBean.getGoodsCartCount() - 1);
                shoppingCartGoodsDao.updateGoodsCart(goodsBean, userId);
            }
            tvGoodsCount.setText(String.valueOf(goodsBean.getGoodsCartCount()));

            Intent intent = new Intent(Constant.SHOP_CART_STATUS);
            if (!TextUtils.isEmpty(shopCartStatus)) {
                intent.putExtra(Constant.SHOP_CART_STATUS, shopCartStatus);
            }
            intent.putExtra(Constant.SHOP_CART_COUNT, "subtract");
            intent.putExtra(Constant.SHOP_CART_GOODS_PRICE, goodsBean.getGoodsPrice());
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }

        private void addGoods() {
            if (shoppingCartShopDao == null) {
                shoppingCartShopDao = new ShoppingCartShopDao();
            }
            GoodsBean goodsBean = goodsBeans.get(getLayoutPosition());

            String shopCartStatus = null;
            ShoppingCartShopBean shoppingCartShopBean = shoppingCartShopDao.queryShopCartByUserIdAndShopId(userId, goodsBean.getShopId());
            if (shoppingCartShopBean == null) {
                shoppingCartShopBean = new ShoppingCartShopBean();
                shoppingCartShopBean.setShopId(goodsBean.getShopId());
                shoppingCartShopBean.setShopName(shopName);
                shoppingCartShopBean.setShopAddress(shopAddress);
                shoppingCartShopDao.insertShopCart(shoppingCartShopBean, userId);
                Log.i("DATA", "插入了shop cart");

//                Intent intent = new Intent(Constant.SHOP_CART_STATUS);
//                intent.putExtra(Constant.SHOP_CART_STATUS, "visible");
//                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                shopCartStatus = "visible";
            }

            if (shoppingCartGoodsDao == null) {
                shoppingCartGoodsDao = new ShoppingCartGoodsDao();
            }
            GoodsBean goodsBeanTemp = shoppingCartGoodsDao.queryGoodsCartByUserIdAndShopIdAndGoodsId(userId, goodsBean.getShopId(), goodsBean.getGoodsId());
            if (goodsBeanTemp == null) {
                goodsBean.setGoodsCartCount(1);
                shoppingCartGoodsDao.insertGoodsCart(goodsBean, userId);
                Log.i("DATA", "插入了goods cart");
            } else {
                goodsBean.setGoodsCartCount(goodsBeanTemp.getGoodsCartCount() + 1);
                shoppingCartGoodsDao.updateGoodsCart(goodsBean, userId);
            }
            tvGoodsCount.setText(String.valueOf(goodsBean.getGoodsCartCount()));

            Intent intent = new Intent(Constant.SHOP_CART_STATUS);
            if (!TextUtils.isEmpty(shopCartStatus)) {
                intent.putExtra(Constant.SHOP_CART_STATUS, shopCartStatus);
            }
            intent.putExtra(Constant.SHOP_CART_COUNT, "add");
            intent.putExtra(Constant.SHOP_CART_GOODS_PRICE, goodsBean.getGoodsPrice());
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }
    }
}
