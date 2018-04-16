package cn.com.findfine.jddaojia.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

import cn.com.findfine.jddaojia.R;
import cn.com.findfine.jddaojia.data.bean.GoodsBean;
import cn.com.findfine.jddaojia.data.bean.ShoppingCartShopBean;
import cn.com.findfine.jddaojia.shopcart.ShopCartActivity;
import cn.com.findfine.jddaojia.utils.FileUtil;

/**
 * Created by yangchen on 2018/4/5.
 */

public class ShoppingCartShopAdapter extends RecyclerView.Adapter<ShoppingCartShopAdapter.ViewHolder> {

    private List<ShoppingCartShopBean> shoppingCartShopBeans;
    private Context context;

    public ShoppingCartShopAdapter(List<ShoppingCartShopBean> shoppingCartShopBeans, Context context) {
        this.shoppingCartShopBeans = shoppingCartShopBeans;
        this.context = context;
    }

    public void setShoppingCartShopBeans(List<ShoppingCartShopBean> shoppingCartShopBeans) {
        this.shoppingCartShopBeans = shoppingCartShopBeans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shopping_cart_shop, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShoppingCartShopBean shoppingCartShopBean = shoppingCartShopBeans.get(position);
        holder.shopName.setText(shoppingCartShopBean.getShopName());

        List<GoodsBean> goodsBeanList = shoppingCartShopBean.getGoodsBeanList();
        GoodsBean goodsBean = goodsBeanList.get(0);
        File file = new File(FileUtil.getCacheFilePath() + goodsBean.getGoodsPhoto());
        Glide.with(context).load(file).into(holder.ivGoodsPhoto_1);
        holder.tvGoodsPrice_1.setText(String.valueOf(goodsBean.getGoodsPrice()));

        if (goodsBeanList.size() > 1) {
            holder.ivGoodsPhoto_2.setVisibility(View.VISIBLE);
            holder.tvGoodsPrice_2.setVisibility(View.VISIBLE);

            goodsBean = goodsBeanList.get(1);
            file = new File(FileUtil.getCacheFilePath() + goodsBean.getGoodsPhoto());
            Glide.with(context).load(file).into(holder.ivGoodsPhoto_2);
            holder.tvGoodsPrice_2.setText(String.valueOf(goodsBean.getGoodsPrice()));
        } else {
            holder.ivGoodsPhoto_2.setVisibility(View.INVISIBLE);
            holder.tvGoodsPrice_2.setVisibility(View.INVISIBLE);
        }

        if (goodsBeanList.size() > 2) {
            holder.ivGoodsPhoto_3.setVisibility(View.VISIBLE);
            holder.tvGoodsPrice_3.setVisibility(View.VISIBLE);

            goodsBean = goodsBeanList.get(2);
            file = new File(FileUtil.getCacheFilePath() + goodsBean.getGoodsPhoto());
            Glide.with(context).load(file).into(holder.ivGoodsPhoto_3);
            holder.tvGoodsPrice_3.setText(String.valueOf(goodsBean.getGoodsPrice()));
        } else {
            holder.ivGoodsPhoto_3.setVisibility(View.INVISIBLE);
            holder.tvGoodsPrice_3.setVisibility(View.INVISIBLE);
        }

        if (goodsBeanList.size() > 3) {
            holder.ivGoodsPhoto_4.setVisibility(View.VISIBLE);
            holder.tvGoodsPrice_4.setVisibility(View.VISIBLE);

            goodsBean = goodsBeanList.get(3);
            file = new File(FileUtil.getCacheFilePath() + goodsBean.getGoodsPhoto());
            Glide.with(context).load(file).into(holder.ivGoodsPhoto_4);
            holder.tvGoodsPrice_4.setText(String.valueOf(goodsBean.getGoodsPrice()));
        } else {
            holder.ivGoodsPhoto_4.setVisibility(View.INVISIBLE);
            holder.tvGoodsPrice_4.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return shoppingCartShopBeans.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView shopName;
        ImageView ivGoodsPhoto_1;
        ImageView ivGoodsPhoto_2;
        ImageView ivGoodsPhoto_3;
        ImageView ivGoodsPhoto_4;

        TextView tvGoodsPrice_1;
        TextView tvGoodsPrice_2;
        TextView tvGoodsPrice_3;
        TextView tvGoodsPrice_4;

        public ViewHolder(View itemView) {
            super(itemView);

            shopName = itemView.findViewById(R.id.tv_shop_name);
            ivGoodsPhoto_1 = itemView.findViewById(R.id.iv_goods_photo_1);
            ivGoodsPhoto_2 = itemView.findViewById(R.id.iv_goods_photo_2);
            ivGoodsPhoto_3 = itemView.findViewById(R.id.iv_goods_photo_3);
            ivGoodsPhoto_4 = itemView.findViewById(R.id.iv_goods_photo_4);

            tvGoodsPrice_1 = itemView.findViewById(R.id.tv_goods_price_1);
            tvGoodsPrice_2 = itemView.findViewById(R.id.tv_goods_price_2);
            tvGoodsPrice_3 = itemView.findViewById(R.id.tv_goods_price_3);
            tvGoodsPrice_4 = itemView.findViewById(R.id.tv_goods_price_4);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShoppingCartShopBean shoppingCartShopBean = shoppingCartShopBeans.get(getLayoutPosition());
                    Intent intent = new Intent(context, ShopCartActivity.class);
                    intent.putExtra("shop_id", shoppingCartShopBean.getShopId());
                    context.startActivity(intent);
                }
            });
        }
    }
}


