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

import java.util.List;

import cn.com.findfine.jddaojia.R;
import cn.com.findfine.jddaojia.data.bean.GoodsBean;
import cn.com.findfine.jddaojia.data.bean.GoodsOrderBean;
import cn.com.findfine.jddaojia.http.HttpUrl;
import cn.com.findfine.jddaojia.order.OrderDetailActivity;


public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private List<GoodsOrderBean> goodsOrderBeans;
    private Context context;

    public OrderAdapter(Context context) {
        this.context = context;
    }

    public void setGoodsOrderBeans(List<GoodsOrderBean> goodsOrderBeans) {
        this.goodsOrderBeans = goodsOrderBeans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GoodsOrderBean goodsOrderBean = goodsOrderBeans.get(position);
        holder.shopName.setText(goodsOrderBean.getShopName());

        List<GoodsBean> goodsBeanList = goodsOrderBean.getGoodsArray();
        GoodsBean goodsBean = goodsBeanList.get(0);
        Glide.with(context).load(HttpUrl.BASE_URL + goodsBean.getGoodsPhoto()).into(holder.ivGoodsPhoto_1);
        holder.tvGoodsPrice_1.setText(String.valueOf(goodsBean.getGoodsPrice()));

        if (goodsBeanList.size() > 1) {
            holder.ivGoodsPhoto_2.setVisibility(View.VISIBLE);
            holder.tvGoodsPrice_2.setVisibility(View.VISIBLE);

            goodsBean = goodsBeanList.get(1);
            Glide.with(context).load(HttpUrl.BASE_URL + goodsBean.getGoodsPhoto()).into(holder.ivGoodsPhoto_2);
            holder.tvGoodsPrice_2.setText(String.valueOf(goodsBean.getGoodsPrice()));
        } else {
            holder.ivGoodsPhoto_2.setVisibility(View.INVISIBLE);
            holder.tvGoodsPrice_2.setVisibility(View.INVISIBLE);
        }

        if (goodsBeanList.size() > 2) {
            holder.ivGoodsPhoto_3.setVisibility(View.VISIBLE);
            holder.tvGoodsPrice_3.setVisibility(View.VISIBLE);

            goodsBean = goodsBeanList.get(2);
            Glide.with(context).load(HttpUrl.BASE_URL + goodsBean.getGoodsPhoto()).into(holder.ivGoodsPhoto_3);
            holder.tvGoodsPrice_3.setText(String.valueOf(goodsBean.getGoodsPrice()));
        } else {
            holder.ivGoodsPhoto_3.setVisibility(View.INVISIBLE);
            holder.tvGoodsPrice_3.setVisibility(View.INVISIBLE);
        }

        if (goodsBeanList.size() > 3) {
            holder.ivGoodsPhoto_4.setVisibility(View.VISIBLE);
            holder.tvGoodsPrice_4.setVisibility(View.VISIBLE);

            goodsBean = goodsBeanList.get(3);
            Glide.with(context).load(HttpUrl.BASE_URL + goodsBean.getGoodsPhoto()).into(holder.ivGoodsPhoto_4);
            holder.tvGoodsPrice_4.setText(String.valueOf(goodsBean.getGoodsPrice()));
        } else {
            holder.ivGoodsPhoto_4.setVisibility(View.INVISIBLE);
            holder.tvGoodsPrice_4.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if (goodsOrderBeans == null) {
            return 0;
        }
        return goodsOrderBeans.size();
    }

//    private List<GoodsBean> getGoodsBeanList(String goodsJson) {
//        List<GoodsBean> goodsBeans = new ArrayList<>();
//        try {
//            JSONArray jsonArray = new JSONArray(goodsJson);
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject goodsObject = jsonArray.getJSONObject(i);
//                GoodsBean goodsBean = new GoodsBean();
//                goodsBean.setGoodsId(goodsObject.getInt("goods_id"));
//                goodsBean.setGoodsName(goodsObject.getString("goods_name"));
//                goodsBean.setGoodsPhoto(goodsObject.getString("goods_photo"));
//                goodsBean.setGoodsPrice((float) goodsObject.getDouble("goods_price"));
//                goodsBean.setGoodsCartCount(goodsObject.getInt("goods_count"));
//
//                goodsBeans.add(goodsBean);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return goodsBeans;
//    }

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
                    Intent intent = new Intent(context, OrderDetailActivity.class);
                    GoodsOrderBean goodsOrderBean = goodsOrderBeans.get(getLayoutPosition());
                    intent.putExtra("goods_order_bean", goodsOrderBean);
                    context.startActivity(intent);
                }
            });
        }
    }
}
