package cn.com.findfine.jddaojia.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.com.findfine.jddaojia.R;
import cn.com.findfine.jddaojia.adapter.HomePageAdapter;
import cn.com.findfine.jddaojia.data.bean.ShopBean;
import cn.com.findfine.jddaojia.data.bean.UserAddress;
import cn.com.findfine.jddaojia.data.db.dao.UserAddressDao;
import cn.com.findfine.jddaojia.http.HttpRequest;
import cn.com.findfine.jddaojia.search.SearchListActivity;
import cn.com.findfine.jddaojia.utils.SharedPreferencesUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;


public class HomePageFragment extends Fragment {

    private HomePageAdapter homePageAdapter;

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

//        ShopDao shopDao = new ShopDao();
        homePageAdapter = new HomePageAdapter(getContext());
        rvHomePage.setAdapter(homePageAdapter);


        ImageView ivHomeSearch = view.findViewById(R.id.iv_home_search);
        ivHomeSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SearchListActivity.class));
            }
        });


        final SwipeRefreshLayout srlHomePage = view.findViewById(R.id.srl_home_page);
        srlHomePage.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i("SRL", "=== onRefresh ===");
                srlHomePage.setRefreshing(false);
            }
        });



        tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                HttpRequest.requestGet("https://www.baidu.com/", new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//
//                    }
//                });

                FormBody.Builder builder = new FormBody.Builder();
//                    .add("token", "7562901a1807841e42b8fb60fc0cad9f5af037411aa662.35496722")
//                    .add("m", "search")
//                    .add("a", "getrankuser")
//                    .add("c", "rank")
//                    .add("uid", "119834721")
//                    .add("isJailbreak", "0");

                HttpRequest.requestPost("http://115.28.17.184/shop_list.php", builder, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        Log.i("Response", result);
                        try {
                            JSONObject jsonObj = new JSONObject(result);
                            JSONArray shopList = jsonObj.getJSONArray("shop_list");
                            for (int i = 0; i < shopList.length(); i++) {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });




        FormBody.Builder builder = new FormBody.Builder();
//                    .add("token", "7562901a1807841e42b8fb60fc0cad9f5af037411aa662.35496722")
//                    .add("m", "search")
//                    .add("a", "getrankuser")
//                    .add("c", "rank")
//                    .add("uid", "119834721")
//                    .add("isJailbreak", "0");

        HttpRequest.requestPost("http://115.28.17.184/shop_list.php", builder, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.i("Response", result);
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    JSONArray shopList = jsonObj.getJSONArray("shop_list");
                    List<ShopBean> shopBeans = new ArrayList<>();
                    for (int i = 0; i < shopList.length(); i++) {
                        JSONObject shopObj = shopList.getJSONObject(i);
                        ShopBean shopBean = new ShopBean();
                        shopBean.setShopId(Integer.valueOf(shopObj.getString("id")));
                        shopBean.setShopName(shopObj.getString("shop_name"));
                        shopBean.setShopPhoto(shopObj.getString("shop_logo"));
                        shopBean.setShopAddress(shopObj.getString("shop_address"));

                        shopBeans.add(shopBean);
                    }
                    homePageAdapter.setShopBeans(shopBeans);
                    handler.sendEmptyMessage(1);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                homePageAdapter.notifyDataSetChanged();
            }
        }
    };
}
