package cn.com.findfine.jddaojia.data.bean;

/**
 * Created by yangchen on 2018/4/4.
 */

public class ShoppingCartGoodsBean {

    private int id;
    private String userId;
    private int shopId;
    private String shopName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
}
