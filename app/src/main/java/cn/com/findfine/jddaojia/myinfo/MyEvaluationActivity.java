package cn.com.findfine.jddaojia.myinfo;

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

import java.util.List;

import cn.com.findfine.jddaojia.BaseActivity;
import cn.com.findfine.jddaojia.R;
import cn.com.findfine.jddaojia.data.bean.GoodsOrderBean;
import cn.com.findfine.jddaojia.data.db.dao.GoodsOrderDao;

public class MyEvaluationActivity extends BaseActivity {

    private List<GoodsOrderBean> goodsOrderBeans;

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
        GoodsOrderDao goodsOrderDao = new GoodsOrderDao();
//        goodsOrderBeans = goodsOrderDao.queryEvaluationOrder();
        for (GoodsOrderBean goodsOrderBean : goodsOrderBeans) {
            Log.i("evaluation", goodsOrderBean.toString());
        }


        RecyclerView rvMyEvaluation = findViewById(R.id.rv_my_evaluation);
        rvMyEvaluation.setLayoutManager(new LinearLayoutManager(this));
        rvMyEvaluation.setAdapter(new MyEvaluationAdapter());

    }

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
