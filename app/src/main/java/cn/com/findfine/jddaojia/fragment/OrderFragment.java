package cn.com.findfine.jddaojia.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.com.findfine.jddaojia.Constant;
import cn.com.findfine.jddaojia.R;
import cn.com.findfine.jddaojia.adapter.OrderAdapter;
import cn.com.findfine.jddaojia.data.bean.GoodsOrderBean;
import cn.com.findfine.jddaojia.data.db.dao.GoodsOrderDao;
import cn.com.findfine.jddaojia.login.LoginActivity;
import cn.com.findfine.jddaojia.utils.SharedPreferencesUtil;


public class OrderFragment extends Fragment implements View.OnClickListener {

    private LocalBroadcastManager localBroadcastManager;
    private MyBroadcastReceiver myBroadcastReceiver;
    private View btnLogin;

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


        btnLogin = view.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);

        RecyclerView rvOrderGoodsList = view.findViewById(R.id.rv_order_goods_list);
        rvOrderGoodsList.setLayoutManager(new LinearLayoutManager(getContext()));
        GoodsOrderDao goodsOrderDao = new GoodsOrderDao();
        List<GoodsOrderBean> goodsOrderBeans = goodsOrderDao.queryAllOrderByUserId(SharedPreferencesUtil.getUserAccount(getContext()));
        rvOrderGoodsList.setAdapter(new OrderAdapter(goodsOrderBeans, getContext()));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean loginStatus = SharedPreferencesUtil.getLoginStatus(getContext());
        setLoginStatus(loginStatus);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                startActivity(new Intent(getContext(), LoginActivity.class));
                break;
        }
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
        } else {
            btnLogin.setVisibility(View.VISIBLE);
        }
    }

}
