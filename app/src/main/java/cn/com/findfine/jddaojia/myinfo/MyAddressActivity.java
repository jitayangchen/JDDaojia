package cn.com.findfine.jddaojia.myinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.com.findfine.jddaojia.BaseActivity;
import cn.com.findfine.jddaojia.R;
import cn.com.findfine.jddaojia.data.bean.UserAddress;
import cn.com.findfine.jddaojia.data.db.dao.UserAddressDao;
import cn.com.findfine.jddaojia.utils.SharedPreferencesUtil;

public class MyAddressActivity extends BaseActivity implements View.OnClickListener {

    private List<UserAddress> userAddresses;
    private AddressAdapter addressAdapter;
    private UserAddressDao userAddressDao;
    private String userId;
    private String source;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("管理收货地址");
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
        source = intent.getStringExtra("source");

        init();
    }

    private void init() {
        View btnCreateAddress = findViewById(R.id.btn_create_address);
        btnCreateAddress.setOnClickListener(this);

        userAddresses = userAddressDao.queryAllUserAddressByUserId(userId);

        RecyclerView rvAddress = findViewById(R.id.rv_address);
        rvAddress.setLayoutManager(new LinearLayoutManager(this));
        addressAdapter = new AddressAdapter();
        rvAddress.setAdapter(addressAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_create_address:
                startActivityForResult(new Intent(this, CreateAddressActivity.class), 1000);
                break;
        }
    }

    class AddressAdapter extends RecyclerView.Adapter<ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_address , parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            UserAddress userAddress = userAddresses.get(position);
            holder.tvName.setText(userAddress.getName());
            holder.tvPhoneNumber.setText(userAddress.getPhoneNumber());
            holder.tvAddress.setText(userAddress.getAddress());
        }

        @Override
        public int getItemCount() {
            return userAddresses.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvPhoneNumber;
        TextView tvAddress;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPhoneNumber = itemView.findViewById(R.id.tv_phone_number);
            tvAddress = itemView.findViewById(R.id.tv_address);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("new_order".equals(source)) {
                        Intent intent = new Intent();
                        intent.putExtra("user_address", userAddresses.get(getLayoutPosition()));
                        setResult(3001, intent);
                        finish();
                    } else {
                        Intent intent = new Intent(MyAddressActivity.this, CreateAddressActivity.class);
                        intent.putExtra("is_edit_address", true);
                        intent.putExtra("user_address", userAddresses.get(getLayoutPosition()));
                        startActivityForResult(intent, 1000);
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000 && resultCode == 2000) {
            userAddresses = userAddressDao.queryAllUserAddressByUserId(userId);
            addressAdapter.notifyDataSetChanged();
        }
    }
}
