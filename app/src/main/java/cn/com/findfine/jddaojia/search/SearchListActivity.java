package cn.com.findfine.jddaojia.search;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.com.findfine.jddaojia.BaseActivity;
import cn.com.findfine.jddaojia.R;
import cn.com.findfine.jddaojia.adapter.SearchGoodsListAdapter;
import cn.com.findfine.jddaojia.data.bean.GoodsBean;
import cn.com.findfine.jddaojia.http.HttpRequest;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class SearchListActivity extends BaseActivity implements View.OnClickListener {

    private EditText etSearch;
    private SearchGoodsListAdapter searchGoodsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        init();
    }

    private void init() {
        Button btnSearch = findViewById(R.id.btn_search);
        etSearch = findViewById(R.id.et_search);
        btnSearch.setOnClickListener(this);

        RecyclerView rvSearchGoods = findViewById(R.id.rv_search_goods);
        rvSearchGoods.setLayoutManager(new LinearLayoutManager(this));
        searchGoodsListAdapter = new SearchGoodsListAdapter(this);
        rvSearchGoods.setAdapter(searchGoodsListAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:
                String keyWords = etSearch.getText().toString();
                if (!TextUtils.isEmpty(keyWords)) {
//                    GoodsDao goodsDao = new GoodsDao();
//                    List<GoodsBean> goodsBeans = goodsDao.queryGoodsByKeyWords(keyWords);
//
//                    for (GoodsBean goodsBean : goodsBeans) {
//                        Log.i("KeyWords", goodsBean.toString());
//                    }
//
//                    searchGoodsListAdapter.setGoodsBeans(goodsBeans);
//                    searchGoodsListAdapter.notifyDataSetChanged();

                    getGoodsByKeyWords(keyWords);
                }
                break;
        }
    }

    private void getGoodsByKeyWords(String keyWords) {
        // good_search.php
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("key_words", keyWords);

        HttpRequest.requestPost("good_search.php", builder, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.i("Response", result);
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    List<GoodsBean> goodsBeans = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        JSONArray goodInfoArray = jsonObject.getJSONArray("good_info");
                        for (int j = 0; j < goodInfoArray.length(); j++) {
                            JSONObject goodsObj = goodInfoArray.getJSONObject(j);

                            GoodsBean goodsBean = new GoodsBean();
                            goodsBean.setShopId(Integer.valueOf(goodsObj.getString("shop_id")));
                            goodsBean.setGoodsId(Integer.valueOf(goodsObj.getString("goods_id")));
                            goodsBean.setGoodsName(goodsObj.getString("goods_name"));
                            goodsBean.setGoodsPhoto(goodsObj.getString("goods_photo"));
                            goodsBean.setGoodsPrice(Float.valueOf(goodsObj.getString("goods_price")));

                            goodsBeans.add(goodsBean);
                        }

                    }
                    searchGoodsListAdapter.setGoodsBeans(goodsBeans);
                    handler.sendEmptyMessage(1);

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
                searchGoodsListAdapter.notifyDataSetChanged();
            }
        }
    };
}
