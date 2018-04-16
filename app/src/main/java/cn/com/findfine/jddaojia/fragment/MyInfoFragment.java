package cn.com.findfine.jddaojia.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.com.findfine.jddaojia.Constant;
import cn.com.findfine.jddaojia.R;
import cn.com.findfine.jddaojia.myinfo.MyAddressActivity;
import cn.com.findfine.jddaojia.myinfo.SettingActivity;
import cn.com.findfine.jddaojia.login.LoginActivity;
import cn.com.findfine.jddaojia.utils.SharedPreferencesUtil;


public class MyInfoFragment extends Fragment implements View.OnClickListener {

    private LocalBroadcastManager localBroadcastManager;
    private MyBroadcastReceiver myBroadcastReceiver;
    private View btnLogin;

    public MyInfoFragment() {
    }

    public static MyInfoFragment newInstance() {
        MyInfoFragment fragment = new MyInfoFragment();
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
        View view = inflater.inflate(R.layout.fragment_my_info, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        btnLogin = view.findViewById(R.id.btn_login);
        View btnSetting = view.findViewById(R.id.btn_setting);
        View llMyFollow = view.findViewById(R.id.ll_my_follow);
        View llMyOpinion = view.findViewById(R.id.ll_my_opinion);
        View llMyAddress = view.findViewById(R.id.ll_my_address);

        btnLogin.setOnClickListener(this);
        btnSetting.setOnClickListener(this);
        llMyFollow.setOnClickListener(this);
        llMyOpinion.setOnClickListener(this);
        llMyAddress.setOnClickListener(this);
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
            case R.id.btn_setting:
                startActivity(new Intent(getContext(), SettingActivity.class));
                break;
            case R.id.ll_my_follow:

                break;
            case R.id.ll_my_opinion:

                break;
            case R.id.ll_my_address:
                boolean loginStatus = SharedPreferencesUtil.getLoginStatus(getContext());
                if (loginStatus) {
                    startActivity(new Intent(getContext(), MyAddressActivity.class));
                } else {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }
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
