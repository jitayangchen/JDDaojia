package cn.com.findfine.jddaojia.myinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import cn.com.findfine.jddaojia.BaseActivity;
import cn.com.findfine.jddaojia.R;
import cn.com.findfine.jddaojia.data.bean.UserAddress;
import cn.com.findfine.jddaojia.data.db.dao.UserAddressDao;
import cn.com.findfine.jddaojia.utils.SharedPreferencesUtil;

public class CreateAddressActivity extends BaseActivity implements View.OnClickListener {

    private EditText etCity;
    private EditText etAddress;
    private EditText etHouseNumber;
    private EditText etName;
    private EditText etPhoneNumber;
    private View btnSaveAddress;
    private boolean isEditAddress;
    private UserAddress userAddress;
    private View btnDeleteAddress;
    private UserAddressDao userAddressDao;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_address);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("新建收货地址");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        userAddressDao = new UserAddressDao();

        userId = SharedPreferencesUtil.getUserAccount(this);

        Intent intent = getIntent();
        isEditAddress = intent.getBooleanExtra("is_edit_address", false);
        if (isEditAddress) {
            userAddress = intent.getParcelableExtra("user_address");
        }

        init();
    }

    private void init() {
        etCity = findViewById(R.id.et_city);
        etAddress = findViewById(R.id.et_address);
        etHouseNumber = findViewById(R.id.et_house_number);
        etName = findViewById(R.id.et_name);
        etPhoneNumber = findViewById(R.id.et_phone_number);

        btnSaveAddress = findViewById(R.id.btn_save_address);
        btnSaveAddress.setOnClickListener(this);

        btnDeleteAddress = findViewById(R.id.btn_delete_address);
        btnDeleteAddress.setOnClickListener(this);
        if (isEditAddress) {
            btnDeleteAddress.setVisibility(View.VISIBLE);

            if (userAddress != null) {
                etCity.setText(userAddress.getCity());
                etAddress.setText(userAddress.getAddress());
                etHouseNumber.setText(userAddress.getHouseNumber());
                etName.setText(userAddress.getName());
                etPhoneNumber.setText(userAddress.getPhoneNumber());
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_delete_address:
                userAddressDao.deleteUserAddressById(userAddress.getId());
                setResult(2000);
                finish();
                break;
            case R.id.btn_save_address:
                String city = etCity.getText().toString();
                String address = etAddress.getText().toString();
                String houseNumber = etHouseNumber.getText().toString();
                String name = etName.getText().toString();
                String phoneNumber = etPhoneNumber.getText().toString();

                if (TextUtils.isEmpty(city)) {
                    Toast.makeText(this, "所在城市不能为空", Toast.LENGTH_SHORT).show();
                    return ;
                }

                if (TextUtils.isEmpty(address)) {
                    Toast.makeText(this, "小区、大夏、学校不能为空", Toast.LENGTH_SHORT).show();
                    return ;
                }

                if (TextUtils.isEmpty(houseNumber)) {
                    Toast.makeText(this, "楼号-门牌号不能为空", Toast.LENGTH_SHORT).show();
                    return ;
                }

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(this, "收货人不能为空", Toast.LENGTH_SHORT).show();
                    return ;
                }

                if (TextUtils.isEmpty(phoneNumber)) {
                    Toast.makeText(this, "联系电话不能为空", Toast.LENGTH_SHORT).show();
                    return ;
                }

                if (isEditAddress) {
                    userAddress.setCity(city);
                    userAddress.setAddress(address);
                    userAddress.setHouseNumber(houseNumber);
                    userAddress.setName(name);
                    userAddress.setPhoneNumber(phoneNumber);
                    userAddressDao.updateUserAddress(userAddress);
                } else {
                    userAddress = new UserAddress();
                    userAddress.setUserId(userId);
                    userAddress.setCity(city);
                    userAddress.setAddress(address);
                    userAddress.setHouseNumber(houseNumber);
                    userAddress.setName(name);
                    userAddress.setPhoneNumber(phoneNumber);
                    userAddressDao.insertUserAddress(userAddress);
                }

                setResult(2000);
                finish();
                break;
        }
    }
}
