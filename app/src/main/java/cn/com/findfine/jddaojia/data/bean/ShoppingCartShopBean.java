package cn.com.findfine.jddaojia.data.bean;

import java.util.List;

/**
 * Created by yangchen on 2018/4/4.
 */

public class ShoppingCartShopBean {

    private int id;
    private int shopId;
    private String shopName;
    private List<GoodsBean> goodsBeanList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public List<GoodsBean> getGoodsBeanList() {
        return goodsBeanList;
    }

    public void setGoodsBeanList(List<GoodsBean> goodsBeanList) {
        this.goodsBeanList = goodsBeanList;
    }
}
