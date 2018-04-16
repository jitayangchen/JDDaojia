package cn.com.findfine.jddaojia.myinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;

import cn.com.findfine.jddaojia.BaseActivity;
import cn.com.findfine.jddaojia.Constant;
import cn.com.findfine.jddaojia.R;
import cn.com.findfine.jddaojia.utils.SharedPreferencesUtil;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        View btnLogout = findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(this);

        boolean loginStatus = SharedPreferencesUtil.getLoginStatus(this);
        if (loginStatus) {
            btnLogout.setVisibility(View.VISIBLE);
        } else {
            btnLogout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_logout:
                Intent intent = new Intent(Constant.LOGIN_STATUS);
                intent.putExtra(Constant.LOGIN_STATUS, false);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

                SharedPreferencesUtil.setLoginStatus(this, false);

                finish();
                break;
        }
    }
}
