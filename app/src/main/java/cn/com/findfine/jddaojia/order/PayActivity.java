package cn.com.findfine.jddaojia.order;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.com.findfine.jddaojia.BaseActivity;
import cn.com.findfine.jddaojia.MainActivity;
import cn.com.findfine.jddaojia.R;
import cn.com.findfine.jddaojia.http.HttpRequest;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class PayActivity extends BaseActivity {

    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("支付收银台");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        float price = intent.getFloatExtra("price", 0.0f);
        orderId = intent.getStringExtra("order_id");

        TextView tvPrice = findViewById(R.id.tv_price);
        tvPrice.setText("￥" + String.valueOf(price));


        Button btnPay = findViewById(R.id.btn_pay);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                GoodsOrderDao goodsOrderDao = new GoodsOrderDao();
//                goodsOrderDao.updateOrder(orderNumber, GoodsOrderContract.ORDER_STATUS_SUCCESS);
//
//                Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
//
//                Intent orderFragmentIntent = new Intent(PayActivity.this, MainActivity.class);
//                orderFragmentIntent.putExtra("is_refresh_order", true);
//                startActivity(orderFragmentIntent);

                payOrder();
            }
        });

    }

    private void payOrder() {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("order_id", orderId);
        builder.add("order_status", "1");

        HttpRequest.requestPost("order_status.php", builder, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.i("Response", result);
//                    {"success":true,"message":"添加成功"}
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

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                Intent orderFragmentIntent = new Intent(PayActivity.this, MainActivity.class);
                orderFragmentIntent.putExtra("is_refresh_order", true);
                startActivity(orderFragmentIntent);
            }
        }
    };
}
