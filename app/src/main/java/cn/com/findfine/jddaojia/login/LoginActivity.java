package cn.com.findfine.jddaojia.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import cn.com.findfine.jddaojia.BaseActivity;
import cn.com.findfine.jddaojia.Constant;
import cn.com.findfine.jddaojia.R;
import cn.com.findfine.jddaojia.data.bean.UserInfo;
import cn.com.findfine.jddaojia.data.db.dao.UserDao;
import cn.com.findfine.jddaojia.utils.SharedPreferencesUtil;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private LocalBroadcastManager localBroadcastManager;
    private MyBroadcastReceiver myBroadcastReceiver;
    private EditText etUserAccount;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("登录");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        myBroadcastReceiver = new MyBroadcastReceiver();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(myBroadcastReceiver, new IntentFilter(Constant.LOGIN_STATUS));


        init();

    }

    private void init() {
        etUserAccount = findViewById(R.id.et_user_account);
        etPassword = findViewById(R.id.et_password);
        View btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);

        View btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                String userAccount = etUserAccount.getText().toString();
                String password = etPassword.getText().toString();

                if (TextUtils.isEmpty(userAccount)) {
                    Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                    return ;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return ;
                }

                UserDao userDao = new UserDao();
                UserInfo userInfo = userDao.queryUserById(userAccount);
                if (userInfo == null) {
                    Toast.makeText(this, "用户名错误", Toast.LENGTH_SHORT).show();
                    return ;
                }

                if (!password.equals(userInfo.getPassword())) {
                    Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show();
                    return ;
                }

                Intent intent = new Intent(Constant.LOGIN_STATUS);
                intent.putExtra(Constant.LOGIN_STATUS, true);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                SharedPreferencesUtil.setLoginStatus(this, true);
                SharedPreferencesUtil.saveUserAccount(this, userAccount);
                SharedPreferencesUtil.saveUserPassword(this, password);
                Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean loginStatus = intent.getBooleanExtra(Constant.LOGIN_STATUS, false);
            if (loginStatus) {
                finish();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(myBroadcastReceiver);
    }
}
