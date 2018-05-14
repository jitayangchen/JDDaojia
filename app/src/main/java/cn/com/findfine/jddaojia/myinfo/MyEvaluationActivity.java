package cn.com.findfine.jddaojia.myinfo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.com.findfine.jddaojia.BaseActivity;
import cn.com.findfine.jddaojia.R;
import cn.com.findfine.jddaojia.data.bean.GoodsBean;
import cn.com.findfine.jddaojia.data.bean.GoodsOrderBean;
import cn.com.findfine.jddaojia.http.HttpRequest;
import cn.com.findfine.jddaojia.utils.SharedPreferencesUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class MyEvaluationActivity extends BaseActivity {

    private List<GoodsOrderBean> goodsOrderBeans;
    private MyEvaluationAdapter myEvaluationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_evaluation);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("我的评价");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        init();
    }

    private void init() {
        getOrderData();

        RecyclerView rvMyEvaluation = findViewById(R.id.rv_my_evaluation);
        rvMyEvaluation.setLayoutManager(new LinearLayoutManager(this));
        myEvaluationAdapter = new MyEvaluationAdapter();
        rvMyEvaluation.setAdapter(myEvaluationAdapter);

    }

    public void getOrderData() {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("user_id", SharedPreferencesUtil.getUserId(this));

        HttpRequest.requestPost("order_list.php", builder, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.i("Response", result);
//                result = JsonData.ORDER_DATA;
                try {
                    if (goodsOrderBeans == null) {
                        goodsOrderBeans = new ArrayList<>();
                    }
                    goodsOrderBeans.clear();

                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        GoodsOrderBean goodsOrderBean = new GoodsOrderBean();
                        goodsOrderBean.setOrderId(jsonObject.getString("order_id"));
                        goodsOrderBean.setOrderNumber(jsonObject.getString("order_number"));
                        goodsOrderBean.setCreateOrderTime(jsonObject.getString("create_order_time"));
                        goodsOrderBean.setOrderStatus(Integer.valueOf(jsonObject.getString("order_status")));
                        goodsOrderBean.setShopId(Integer.valueOf(jsonObject.getString("shop_id")));
                        goodsOrderBean.setShopName(jsonObject.getString("shop_name"));
                        goodsOrderBean.setUserAddress(jsonObject.getString("user_address"));
                        String orderEvalution = jsonObject.getString("order_evalution");
                        if ("null".equals(orderEvalution)) {
                            goodsOrderBean.setOrderEvaluation(0);
                        } else {
                            goodsOrderBean.setOrderEvaluation(Integer.valueOf(orderEvalution));

                            goodsOrderBean.setEvalutionContent(jsonObject.getString("evalution_content"));


                            List<GoodsBean> goodsBeans = new ArrayList<>();
                            JSONArray goodsListArray = jsonObject.getJSONArray("goods_list");
                            for (int j = 0; j < goodsListArray.length(); j++) {
                                JSONObject goodsObj = goodsListArray.getJSONObject(j);
                                GoodsBean goodsBean = new GoodsBean();
                                goodsBean.setGoodsId(Integer.valueOf(goodsObj.getString("goods_id")));
                                goodsBean.setGoodsName(goodsObj.getString("goods_name"));
                                goodsBean.setGoodsPhoto(goodsObj.getString("goods_photo"));
                                goodsBean.setGoodsPrice(Float.valueOf(goodsObj.getString("goods_price")));
                                goodsBeans.add(goodsBean);
                            }

                            goodsOrderBean.setGoodsArray(goodsBeans);

                            goodsOrderBeans.add(0, goodsOrderBean);
                        }

                    }


                    handler.sendEmptyMessage(1);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                myEvaluationAdapter.notifyDataSetChanged();
            }
        }
    };

    class MyEvaluationAdapter extends RecyclerView.Adapter<ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_evaluation, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            GoodsOrderBean goodsOrderBean = goodsOrderBeans.get(position);
            holder.tvShopName.setText(goodsOrderBean.getShopName());

            switch (goodsOrderBean.getOrderEvaluation()) {
                case 1:
                    holder.ivStar_1.setBackgroundResource(R.mipmap.order_star_light);
                    break;
                case 2:
                    holder.ivStar_1.setBackgroundResource(R.mipmap.order_star_light);
                    holder.ivStar_2.setBackgroundResource(R.mipmap.order_star_light);
                    break;
                case 3:
                    holder.ivStar_1.setBackgroundResource(R.mipmap.order_star_light);
                    holder.ivStar_2.setBackgroundResource(R.mipmap.order_star_light);
                    holder.ivStar_3.setBackgroundResource(R.mipmap.order_star_light);
                    break;
                case 4:
                    holder.ivStar_1.setBackgroundResource(R.mipmap.order_star_light);
                    holder.ivStar_2.setBackgroundResource(R.mipmap.order_star_light);
                    holder.ivStar_3.setBackgroundResource(R.mipmap.order_star_light);
                    holder.ivStar_4.setBackgroundResource(R.mipmap.order_star_light);
                    break;
                case 5:
                    holder.ivStar_1.setBackgroundResource(R.mipmap.order_star_light);
                    holder.ivStar_2.setBackgroundResource(R.mipmap.order_star_light);
                    holder.ivStar_3.setBackgroundResource(R.mipmap.order_star_light);
                    holder.ivStar_4.setBackgroundResource(R.mipmap.order_star_light);
                    holder.ivStar_5.setBackgroundResource(R.mipmap.order_star_light);
                    break;
            }
        }

        @Override
        public int getItemCount() {
            if (goodsOrderBeans == null) {
                return 0;
            }
            return goodsOrderBeans.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvShopName;
        private final ImageView ivStar_1;
        private final ImageView ivStar_2;
        private final ImageView ivStar_3;
        private final ImageView ivStar_4;
        private final ImageView ivStar_5;

        public ViewHolder(View itemView) {
            super(itemView);
            tvShopName = itemView.findViewById(R.id.tv_shop_name);
            ivStar_1 = itemView.findViewById(R.id.iv_star_1);
            ivStar_2 = itemView.findViewById(R.id.iv_star_2);
            ivStar_3 = itemView.findViewById(R.id.iv_star_3);
            ivStar_4 = itemView.findViewById(R.id.iv_star_4);
            ivStar_5 = itemView.findViewById(R.id.iv_star_5);
        }
    }
}
