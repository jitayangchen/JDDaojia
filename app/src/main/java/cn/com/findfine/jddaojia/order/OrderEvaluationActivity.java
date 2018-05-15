package cn.com.findfine.jddaojia.order;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.com.findfine.jddaojia.BaseActivity;
import cn.com.findfine.jddaojia.R;
import cn.com.findfine.jddaojia.data.bean.GoodsOrderBean;
import cn.com.findfine.jddaojia.http.HttpRequest;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class OrderEvaluationActivity extends BaseActivity implements View.OnClickListener {

    private List<ImageView> imageViews;
    private TextView tvEvaluationResult;
    private int orderEvaluation = 0;
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


        Intent intent = getIntent();
        goodsOrderBean = intent.getParcelableExtra("order_bean");

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
                    Toast.makeText(OrderEvaluationActivity.this, "你已经评价了", Toast.LENGTH_SHORT).show();
                    return ;
                }

                if (orderEvaluation != 0) {
                    commitOrderEvaluation(orderEvaluation);
                }
                break;
        }
    }

    private void commitOrderEvaluation(int orderEvaluation) {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("order_id", goodsOrderBean.getOrderId());
        builder.add("order_evalution", String.valueOf(orderEvaluation));
        builder.add("evalution_content", "非常满意");

        HttpRequest.requestPost("order_evalution.php", builder, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.i("Response", result);
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    String success = jsonObj.getString("success");
                    if ("true".equals(success)) {
                        handler.sendEmptyMessage(1);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setStarStatus(int evaluation) {
        for (int i = 0; i < evaluation; i++) {
            imageViews.get(i).setBackgroundResource(R.mipmap.order_star_light);
        }

        for (int i = evaluation; i < 5; i++) {
            imageViews.get(i).setBackgroundResource(R.mipmap.order_star);
        }
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Toast.makeText(OrderEvaluationActivity.this, "订单评价提交成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("is_evaluation", true);
                setResult(1212, intent);
                finish();
            }
        }
    };
}
