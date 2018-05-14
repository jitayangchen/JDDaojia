package cn.com.findfine.jddaojia.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.com.findfine.jddaojia.BaseActivity;
import cn.com.findfine.jddaojia.Constant;
import cn.com.findfine.jddaojia.R;
import cn.com.findfine.jddaojia.http.HttpRequest;
import cn.com.findfine.jddaojia.utils.SharedPreferencesUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private LocalBroadcastManager localBroadcastManager;
    private MyBroadcastReceiver myBroadcastReceiver;
    private EditText etUserAccount;
    private EditText etPassword;
    private String userAccount;
    private String password;

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
                userAccount = etUserAccount.getText().toString();
                password = etPassword.getText().toString();

                if (TextUtils.isEmpty(userAccount)) {
                    Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                    return ;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return ;
                }

//                UserDao userDao = new UserDao();
//                UserInfo userInfo = userDao.queryUserById(userAccount);
//                if (userInfo == null) {
//                    Toast.makeText(this, "用户名错误", Toast.LENGTH_SHORT).show();
//                    return ;
//                }
//
//                if (!password.equals(userInfo.getPassword())) {
//                    Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show();
//                    return ;
//                }

                login(userAccount, password);

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


    private void login(String userAccount, String password) {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("user_account", userAccount);
        builder.add("password", password);

        HttpRequest.requestPost("user_login.php", builder, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
//                {"success":true,"message":"登陆成功","data":{"id":"5","username":"15501005429","password":"123456"}}
                Log.i("Response", result);
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    String success = jsonObj.getString("success");
                    if ("true".equals(success)) {

                        JSONObject dataObj = jsonObj.getJSONObject("data");
                        String userId = dataObj.getString("id");
                        String userAccount = dataObj.getString("username");
                        String password = dataObj.getString("password");

                        Intent intent = new Intent(Constant.LOGIN_STATUS);
                        intent.putExtra(Constant.LOGIN_STATUS, true);
                        LocalBroadcastManager.getInstance(LoginActivity.this).sendBroadcast(intent);
                        SharedPreferencesUtil.setLoginStatus(LoginActivity.this, true);
                        SharedPreferencesUtil.saveUserAccount(LoginActivity.this, userAccount);
                        SharedPreferencesUtil.saveUserPassword(LoginActivity.this, password);
                        SharedPreferencesUtil.saveUserId(LoginActivity.this, userId);

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
//                Intent intent = new Intent(Constant.LOGIN_STATUS);
//                intent.putExtra(Constant.LOGIN_STATUS, true);
//                LocalBroadcastManager.getInstance(LoginActivity.this).sendBroadcast(intent);
//                SharedPreferencesUtil.setLoginStatus(LoginActivity.this, true);
//                SharedPreferencesUtil.saveUserAccount(LoginActivity.this, userAccount);
//                SharedPreferencesUtil.saveUserPassword(LoginActivity.this, password);
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
