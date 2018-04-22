package cn.com.findfine.jddaojia.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.com.findfine.jddaojia.R;
import cn.com.findfine.jddaojia.adapter.HomePageAdapter;
import cn.com.findfine.jddaojia.data.bean.UserAddress;
import cn.com.findfine.jddaojia.data.db.dao.ShopDao;
import cn.com.findfine.jddaojia.data.db.dao.UserAddressDao;
import cn.com.findfine.jddaojia.search.SearchListActivity;
import cn.com.findfine.jddaojia.utils.SharedPreferencesUtil;


public class HomePageFragment extends Fragment {

    public HomePageFragment() {
    }

    public static HomePageFragment newInstance() {
        HomePageFragment fragment = new HomePageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        TextView tvLocation = view.findViewById(R.id.tv_location);
        if (SharedPreferencesUtil.getLoginStatus(getContext())) {
            UserAddressDao userAddressDao = new UserAddressDao();
            List<UserAddress> userAddressList = userAddressDao.queryAllUserAddressByUserId(SharedPreferencesUtil.getUserAccount(getContext()));
            if (userAddressList.size() > 0) {
                UserAddress userAddress = userAddressList.get(0);
                tvLocation.setText(userAddress.getAddress());
            }
        }

        RecyclerView rvHomePage = view.findViewById(R.id.rv_home_page);
        rvHomePage.setLayoutManager(new LinearLayoutManager(getContext()));

        ShopDao shopDao = new ShopDao();
        rvHomePage.setAdapter(new HomePageAdapter(getContext(), shopDao.queryAllShop()));


        ImageView ivHomeSearch = view.findViewById(R.id.iv_home_search);
        ivHomeSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SearchListActivity.class));
            }
        });
        return view;
    }
}
