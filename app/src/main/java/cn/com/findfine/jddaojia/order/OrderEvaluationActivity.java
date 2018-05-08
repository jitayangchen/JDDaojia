package cn.com.findfine.jddaojia.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.com.findfine.jddaojia.BaseActivity;
import cn.com.findfine.jddaojia.R;
import cn.com.findfine.jddaojia.data.bean.GoodsOrderBean;
import cn.com.findfine.jddaojia.data.db.dao.GoodsOrderDao;

public class OrderEvaluationActivity extends BaseActivity implements View.OnClickListener {

    private List<ImageView> imageViews;
    private TextView tvEvaluationResult;
    private String orderNumber;
    private int orderEvaluation = 0;
    private GoodsOrderDao goodsOrderDao;
    private GoodsOrderBean goodsOrderBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_evaluation);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("订单评价");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        goodsOrderDao = new GoodsOrderDao();

        Intent intent = getIntent();
        orderNumber = intent.getStringExtra("order_number");
//        goodsOrderBean = goodsOrderDao.queryOrderByOrderNumber(orderNumber);

        init();
    }

    private void init() {
        ImageView ivStar_1 = findViewById(R.id.iv_star_1);
        ImageView ivStar_2 = findViewById(R.id.iv_star_2);
        ImageView ivStar_3 = findViewById(R.id.iv_star_3);
        ImageView ivStar_4 = findViewById(R.id.iv_star_4);
        ImageView ivStar_5 = findViewById(R.id.iv_star_5);

        tvEvaluationResult = findViewById(R.id.tv_evaluation_result);
        Button btnCommitEvaluation = findViewById(R.id.btn_commit_evaluation);

        btnCommitEvaluation.setOnClickListener(this);

        imageViews = new ArrayList<>();
        imageViews.add(ivStar_1);
        imageViews.add(ivStar_2);
        imageViews.add(ivStar_3);
        imageViews.add(ivStar_4);
        imageViews.add(ivStar_5);

        if (goodsOrderBean.getOrderEvaluation() > 0) {
            setStarStatus(goodsOrderBean.getOrderEvaluation());
        } else {
            for (ImageView imageView : imageViews) {
                imageView.setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_star_1:
                setStarStatus(1);
                tvEvaluationResult.setText("非常不满意");
                orderEvaluation = 1;
                break;
            case R.id.iv_star_2:
                setStarStatus(2);
                tvEvaluationResult.setText("不满意");
                orderEvaluation = 2;
                break;
            case R.id.iv_star_3:
                setStarStatus(3);
                tvEvaluationResult.setText("一般");
                orderEvaluation = 3;
                break;
            case R.id.iv_star_4:
                setStarStatus(4);
                tvEvaluationResult.setText("满意");
                orderEvaluation = 4;
                break;
            case R.id.iv_star_5:
                setStarStatus(5);
                tvEvaluationResult.setText("非常满意");
                orderEvaluation = 5;
                break;
            case R.id.btn_commit_evaluation:
                if (goodsOrderBean.getOrderEvaluation() > 0) {
                    return ;
                }

                if (orderEvaluation != 0) {
//                    goodsOrderDao.updateOrderEvaluation(orderNumber, orderEvaluation);

                    Toast.makeText(this, "订单评价提交成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    private void setStarStatus(int evaluation) {
        for (int i = 0; i < evaluation; i++) {
            imageViews.get(i).setBackgroundResource(R.mipmap.order_star_light);
        }

        for (int i = evaluation; i < 5; i++) {
            imageViews.get(i).setBackgroundResource(R.mipmap.order_star);
        }
    }
}
