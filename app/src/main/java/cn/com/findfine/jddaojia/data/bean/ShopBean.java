package cn.com.findfine.jddaojia.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


public class ShopBean implements Parcelable {

    private int id;
    private int shopId;
    private String shopName;
    private String shopPhoto;
    private String shopAddress;
    private ArrayList<GoodsBean> goodsBeans;

    public ShopBean() {

    }

    private ShopBean(Parcel in) {
        id = in.readInt();
        shopId = in.readInt();
        shopName = in.readString();
        shopPhoto = in.readString();
        shopAddress = in.readString();
        goodsBeans = in.createTypedArrayList(GoodsBean.CREATOR);
    }

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

    public String getShopPhoto() {
        return shopPhoto;
    }

    public void setShopPhoto(String shopPhoto) {
        this.shopPhoto = shopPhoto;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public ArrayList<GoodsBean> getGoodsBeans() {
        return goodsBeans;
    }

    public void setGoodsBeans(ArrayList<GoodsBean> goodsBeans) {
        this.goodsBeans = goodsBeans;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(shopId);
        dest.writeString(shopName);
        dest.writeString(shopPhoto);
        dest.writeString(shopAddress);
        dest.writeTypedList(goodsBeans);
    }

    public static final Creator<ShopBean> CREATOR = new Creator<ShopBean>() {
        @Override
        public ShopBean createFromParcel(Parcel in) {
            return new ShopBean(in);
        }

        @Override
        public ShopBean[] newArray(int size) {
            return new ShopBean[size];
        }
    };
}
