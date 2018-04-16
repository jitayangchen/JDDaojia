package cn.com.findfine.jddaojia.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cn.com.findfine.jddaojia.BaseActivity;
import cn.com.findfine.jddaojia.R;
import cn.com.findfine.jddaojia.data.db.contract.GoodsOrderContract;
import cn.com.findfine.jddaojia.data.db.dao.GoodsOrderDao;

public class PayActivity extends BaseActivity {

    private String orderNumber;

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
        orderNumber = intent.getStringExtra("order_number");

        TextView tvPrice = findViewById(R.id.tv_price);
        tvPrice.setText("￥" + String.valueOf(price));


        Button btnPay = findViewById(R.id.btn_pay);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoodsOrderDao goodsOrderDao = new GoodsOrderDao();
                goodsOrderDao.updateOrder(orderNumber, GoodsOrderContract.ORDER_STATUS_SUCCESS);

                Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
