package cn.com.findfine.jddaojia.login;

import android.content.Intent;
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

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private EditText etUserAccount;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("注册");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setNavigationIcon(R.drawable.ic_launcher_background);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        init();
    }

    private void init() {
        etUserAccount = findViewById(R.id.et_user_account);
        etPassword = findViewById(R.id.et_password);
        View btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
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
                if (userInfo != null) {
                    Toast.makeText(this, "此账号已经注册了", Toast.LENGTH_SHORT).show();
                    return ;
                }

                userInfo = new UserInfo();
                userInfo.setUserId(userAccount);
                userInfo.setPassword(password);
                userDao.insertUser(userInfo);

                Intent intent = new Intent(Constant.LOGIN_STATUS);
                intent.putExtra(Constant.LOGIN_STATUS, true);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                SharedPreferencesUtil.setLoginStatus(this, true);
                SharedPreferencesUtil.saveUserAccount(this, userAccount);
                SharedPreferencesUtil.saveUserPassword(this, password);
                Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }
}
