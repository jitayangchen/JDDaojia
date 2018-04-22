package cn.com.findfine.jddaojia.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.com.findfine.jddaojia.R;
import cn.com.findfine.jddaojia.data.JsonData;
import cn.com.findfine.jddaojia.data.bean.GoodsBean;
import cn.com.findfine.jddaojia.data.bean.ShopBean;
import cn.com.findfine.jddaojia.shop.ShopDetialActivity;
import cn.com.findfine.jddaojia.utils.FileUtil;


public class HomePageAdapter extends RecyclerView.Adapter<HomePageAdapter.ViewHolder> {

    private Context context;
    private List<ShopBean> shopBeans;


    public HomePageAdapter(Context context, List<ShopBean> shopBeans) {
        this.context = context;
        this.shopBeans = shopBeans;
    }

    private void initData() {
        try {
            JSONArray jsonArray = new JSONArray(JsonData.SHOP_GOODS);
            for (int i = 0; i < jsonArray.length(); i++) {
                ShopBean shopBean = new ShopBean();
                JSONObject shopObject = jsonArray.getJSONObject(i);
                shopBean.setShopName(shopObject.getString("shop_name"));
                shopBean.setShopPhoto(shopObject.getString("shop_photo"));

                JSONArray goodsList = shopObject.getJSONArray("goods_list");
                ArrayList<GoodsBean> goodsBeans = new ArrayList<>();
                for (int j = 0; j < goodsList.length(); j++) {
                    JSONObject goodsObject = goodsList.getJSONObject(j);
                    GoodsBean goodsBean = new GoodsBean();
                    goodsBean.setGoodsName(goodsObject.getString("goods_name"));
                    goodsBean.setGoodsPhoto(goodsObject.getString("goods_photo"));
//                    goodsBean.setGoodsPrice((float) goodsObject.getDouble("goods_price"));
                    goodsBeans.add(goodsBean);
                }
                shopBean.setGoodsBeans(goodsBeans);
                shopBeans.add(shopBean);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = View.inflate(parent.getContext(), R.layout.item_home_page, parent);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_page, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ShopBean shopBean = shopBeans.get(position);
        File file = new File(FileUtil.getCacheFilePath() + shopBean.getShopPhoto());
        Glide.with(context).load(file).into(holder.ivShopPhoto);
        holder.tvShopName.setText(shopBean.getShopName());
    }

    @Override
    public int getItemCount() {
        return shopBeans.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivShopPhoto;
        TextView tvShopName;

        public ViewHolder(final View itemView) {
            super(itemView);
            ivShopPhoto = itemView.findViewById(R.id.iv_shop_photo);
            tvShopName = itemView.findViewById(R.id.tv_shop_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ShopDetialActivity.class);
                    intent.putExtra("shop_info", shopBeans.get(getLayoutPosition()));
                    context.startActivity(intent);



                    JSONArray jsonArray = new JSONArray();
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("shop_id", 100001);
                        jsonObject.put("shop_name", "周黑鸭");
                        jsonObject.put("shop_photo", "100.jpg");
                        jsonObject.put("shop_address", "青岛市城阳区村里");

                        JSONArray goodsArray = new JSONArray();
                        JSONObject goodsObject = new JSONObject();
                        goodsObject.put("goods_id", 100);
                        goodsObject.put("shop_id", 100001);
                        goodsObject.put("goods_name", "鸭脖");
                        goodsObject.put("goods_photo", "100001.jpg");
                        goodsObject.put("goods_price", 6.6);
                        goodsObject.put("goods_category", "category");
                        goodsObject.put("goods_sales_volume", 10);
                        goodsArray.put(goodsObject);

                        jsonObject.put("goods_list", goodsArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(jsonObject);

                    Log.i("YYY", jsonArray.toString());
                }
            });
        }
    }
}
