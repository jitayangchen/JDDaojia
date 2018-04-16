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
import cn.com.findfine.jddaojia.adapter.ShoppingCartShopAdapter;
import cn.com.findfine.jddaojia.data.bean.GoodsBean;
import cn.com.findfine.jddaojia.data.bean.ShoppingCartShopBean;
import cn.com.findfine.jddaojia.data.db.dao.ShoppingCartGoodsDao;
import cn.com.findfine.jddaojia.data.db.dao.ShoppingCartShopDao;
import cn.com.findfine.jddaojia.login.LoginActivity;
import cn.com.findfine.jddaojia.utils.SharedPreferencesUtil;


public class ShoppingCartFragment extends Fragment implements View.OnClickListener {

    private LocalBroadcastManager localBroadcastManager;
    private MyBroadcastReceiver myBroadcastReceiver;
    private View btnLogin;
    private ShoppingCartShopAdapter shoppingCartShopAdapter;
    private ShoppingCartShopDao shoppingCartShopDao;
    private ShoppingCartGoodsDao shoppingCartGoodsDao;
    private boolean isRefreshShopCart = false;

    public ShoppingCartFragment() {
    }

    public static ShoppingCartFragment newInstance() {
        ShoppingCartFragment fragment = new ShoppingCartFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myBroadcastReceiver = new MyBroadcastReceiver();
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        IntentFilter intentFilter = new IntentFilter(Constant.LOGIN_STATUS);
        intentFilter.addAction(Constant.LOGIN_STATUS);
        localBroadcastManager.registerReceiver(myBroadcastReceiver, intentFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_cart, container, false);
        btnLogin = view.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("我的购物车");

        RecyclerView rvShoppingCartShop = view.findViewById(R.id.rv_shopping_cart_shop);
        rvShoppingCartShop.setLayoutManager(new LinearLayoutManager(getContext()));

        shoppingCartShopAdapter = new ShoppingCartShopAdapter(getData(), getContext());
        rvShoppingCartShop.setAdapter(shoppingCartShopAdapter);
        return view;
    }

    private List<ShoppingCartShopBean> getData() {
        if (shoppingCartShopDao == null) {
            shoppingCartShopDao = new ShoppingCartShopDao();
        }
        if (shoppingCartGoodsDao == null) {
            shoppingCartGoodsDao = new ShoppingCartGoodsDao();
        }

        String userId = SharedPreferencesUtil.getUserAccount(getContext());
        List<ShoppingCartShopBean> shoppingCartShopBeans = shoppingCartShopDao.queryShopCartByUserId(userId);
        for (ShoppingCartShopBean shoppingCartShopBean : shoppingCartShopBeans) {
            List<GoodsBean> goodsBeans = shoppingCartGoodsDao.queryGoodsCartByUserIdAndShopId(userId, shoppingCartShopBean.getShopId());
            shoppingCartShopBean.setGoodsBeanList(goodsBeans);
        }

        return shoppingCartShopBeans;
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean loginStatus = SharedPreferencesUtil.getLoginStatus(getContext());
        setLoginStatus(loginStatus);

        if (isRefreshShopCart) {
            shoppingCartShopAdapter.setShoppingCartShopBeans(getData());
            shoppingCartShopAdapter.notifyDataSetChanged();
        }

        isRefreshShopCart = true;
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
            String action = intent.getAction();
            if (Constant.LOGIN_STATUS.equals(action)) {
                boolean loginStatus = intent.getBooleanExtra(Constant.LOGIN_STATUS, false);
                setLoginStatus(loginStatus);
            }
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
