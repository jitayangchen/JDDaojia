package cn.com.findfine.jddaojia.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;

import cn.com.findfine.jddaojia.R;
import cn.com.findfine.jddaojia.data.bean.GoodsBean;
import cn.com.findfine.jddaojia.data.bean.ShopBean;
import cn.com.findfine.jddaojia.http.HttpUrl;
import cn.com.findfine.jddaojia.shop.ShopDetialActivity;

public class SearchGoodsListAdapter extends RecyclerView.Adapter<SearchGoodsListAdapter.ViewHolder> {

    private Context context;
    private List<GoodsBean> goodsBeans;
    private HashMap<Integer, ShopBean> shopBeanHashMap;

    public SearchGoodsListAdapter(Context context) {
        this.context = context;
    }

    public void setGoodsBeans(List<GoodsBean> goodsBeans, HashMap<Integer, ShopBean> shopBeanHashMap) {
        this.goodsBeans = goodsBeans;
        this.shopBeanHashMap = shopBeanHashMap;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_goods, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GoodsBean goodsBean = goodsBeans.get(position);
        Glide.with(context).load(HttpUrl.BASE_URL + goodsBean.getGoodsPhoto()).into(holder.ivGoodsPhoto);
        holder.tvGoodsName.setText(goodsBean.getGoodsName());
        holder.tvGoodsPrice.setText(String.valueOf(goodsBean.getGoodsPrice()));
    }

    @Override
    public int getItemCount() {
        if (goodsBeans == null)
            return 0;

        return goodsBeans.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivGoodsPhoto;
        TextView tvGoodsName;
        TextView tvGoodsPrice;

        public ViewHolder(final View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GoodsBean goodsBean = goodsBeans.get(getLayoutPosition());

                    ShopBean shopBean = shopBeanHashMap.get(goodsBean.getShopId());

                    Intent intent = new Intent(context, ShopDetialActivity.class);
                    intent.putExtra("shop_info", shopBean);
                    context.startActivity(intent);
                }
            });

            ivGoodsPhoto = itemView.findViewById(R.id.iv_goods_photo);
            tvGoodsName = itemView.findViewById(R.id.tv_goods_name);
            tvGoodsPrice = itemView.findViewById(R.id.tv_goods_price);

        }

    }
}
