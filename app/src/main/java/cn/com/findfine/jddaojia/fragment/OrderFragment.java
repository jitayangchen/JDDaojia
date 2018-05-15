package cn.com.findfine.jddaojia.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.com.findfine.jddaojia.Constant;
import cn.com.findfine.jddaojia.R;
import cn.com.findfine.jddaojia.adapter.OrderAdapter;
import cn.com.findfine.jddaojia.data.bean.GoodsBean;
import cn.com.findfine.jddaojia.data.bean.GoodsOrderBean;
import cn.com.findfine.jddaojia.http.HttpRequest;
import cn.com.findfine.jddaojia.login.LoginActivity;
import cn.com.findfine.jddaojia.utils.SharedPreferencesUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;


public class OrderFragment extends Fragment implements View.OnClickListener {

    private LocalBroadcastManager localBroadcastManager;
    private MyBroadcastReceiver myBroadcastReceiver;
    private View btnLogin;
    private OrderAdapter orderAdapter;
    private boolean isRefreshOrder = false;
    private RecyclerView rvOrderGoodsList;
    private SwipeRefreshLayout srlOrderPage;

    public OrderFragment() {
    }

    public static OrderFragment newInstance() {
        OrderFragment fragment = new OrderFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myBroadcastReceiver = new MyBroadcastReceiver();
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(myBroadcastReceiver, new IntentFilter(Constant.LOGIN_STATUS));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("我的订单");


        srlOrderPage = view.findViewById(R.id.srl_order_page);
        srlOrderPage.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i("SRL", "=== onRefresh ===");
                refreshOrder();
            }
        });

        btnLogin = view.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);

        rvOrderGoodsList = view.findViewById(R.id.rv_order_goods_list);
        rvOrderGoodsList.setLayoutManager(new LinearLayoutManager(getContext()));
        orderAdapter = new OrderAdapter(getContext());
        refreshOrder();
        rvOrderGoodsList.setAdapter(orderAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean loginStatus = SharedPreferencesUtil.getLoginStatus(getContext());
        setLoginStatus(loginStatus);

        if (isRefreshOrder && loginStatus) {
            refreshOrder();
        }
        isRefreshOrder = true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                startActivity(new Intent(getContext(), LoginActivity.class));
                break;
        }
    }

    private void refreshOrder() {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("user_id", SharedPreferencesUtil.getUserId(getContext()));

        HttpRequest.requestPost("order_list.php", builder, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.i("Response", result);

                if ("null".equals(result)) {
                    handler.sendEmptyMessage(1);
                    return ;
                }
//                result = JsonData.ORDER_DATA;
                try {
                    List<GoodsOrderBean> goodsOrderBeans = new ArrayList<>();
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
                        String goodsCountStr = jsonObject.getString("goods_count");
                        JSONArray jsonArrayGoodsCount = new JSONArray(goodsCountStr);

                        String orderEvalution = jsonObject.getString("order_evalution");
                        if ("null".equals(orderEvalution)) {
                            goodsOrderBean.setOrderEvaluation(0);
                        } else {
                            goodsOrderBean.setOrderEvaluation(Integer.valueOf(orderEvalution));
                        }
                        goodsOrderBean.setEvalutionContent(jsonObject.getString("evalution_content"));


                        List<GoodsBean> goodsBeans = new ArrayList<>();
                        JSONArray goodsListArray = jsonObject.getJSONArray("goods_list");
                        for (int j = 0; j < goodsListArray.length(); j++) {
                            int goodsCount = jsonArrayGoodsCount.getInt(j);
                            JSONObject goodsObj = goodsListArray.getJSONObject(j);
                            GoodsBean goodsBean = new GoodsBean();
                            goodsBean.setGoodsId(Integer.valueOf(goodsObj.getString("goods_id")));
                            goodsBean.setGoodsName(goodsObj.getString("goods_name"));
                            goodsBean.setGoodsPhoto(goodsObj.getString("goods_photo"));
                            goodsBean.setGoodsPrice(Float.valueOf(goodsObj.getString("goods_price")));
                            goodsBean.setGoodsCartCount(goodsCount);
                            goodsBeans.add(goodsBean);
                        }

                        goodsOrderBean.setGoodsArray(goodsBeans);

                        goodsOrderBeans.add(0, goodsOrderBean);
                    }

                    orderAdapter.setGoodsOrderBeans(goodsOrderBeans);

                    handler.sendEmptyMessage(1);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean loginStatus = intent.getBooleanExtra(Constant.LOGIN_STATUS, false);
            setLoginStatus(loginStatus);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(myBroadcastReceiver);
    }

    private void setLoginStatus(boolean loginStatus) {
        if (loginStatus) {
            Log.i("Login", "========= Login Success ========");
            btnLogin.setVisibility(View.GONE);
            rvOrderGoodsList.setVisibility(View.VISIBLE);
        } else {
            btnLogin.setVisibility(View.VISIBLE);
            rvOrderGoodsList.setVisibility(View.GONE);
        }
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                srlOrderPage.setRefreshing(false);
                orderAdapter.notifyDataSetChanged();
            }
        }
    };
}
