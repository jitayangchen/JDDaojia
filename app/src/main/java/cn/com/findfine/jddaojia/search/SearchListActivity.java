package cn.com.findfine.jddaojia.search;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import cn.com.findfine.jddaojia.BaseActivity;
import cn.com.findfine.jddaojia.R;
import cn.com.findfine.jddaojia.adapter.SearchGoodsListAdapter;
import cn.com.findfine.jddaojia.data.bean.GoodsBean;
import cn.com.findfine.jddaojia.data.db.dao.GoodsDao;

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
                    GoodsDao goodsDao = new GoodsDao();
                    List<GoodsBean> goodsBeans = goodsDao.queryGoodsByKeyWords(keyWords);

                    for (GoodsBean goodsBean : goodsBeans) {
                        Log.i("KeyWords", goodsBean.toString());
                    }

                    searchGoodsListAdapter.setGoodsBeans(goodsBeans);
                    searchGoodsListAdapter.notifyDataSetChanged();
                }
                break;
        }
    }
}
