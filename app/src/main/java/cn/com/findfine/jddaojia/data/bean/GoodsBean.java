package cn.com.findfine.jddaojia.data.bean;

import android.os.Parcel;
import android.os.Parcelable;


public class GoodsBean implements Parcelable {

    private int id;
    private int goodsId;
    private int shopId;
    private String goodsName;
    private String goodsPhoto;
    private float goodsPrice;
    private String goodsCategory;
    private int goodsSalesVolume;
    private int goodsCartCount = 0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsPhoto() {
        return goodsPhoto;
    }

    public void setGoodsPhoto(String goodsPhoto) {
        this.goodsPhoto = goodsPhoto;
    }

    public float getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(float goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getGoodsCategory() {
        return goodsCategory;
    }

    public void setGoodsCategory(String goodsCategory) {
        this.goodsCategory = goodsCategory;
    }

    public int getGoodsSalesVolume() {
        return goodsSalesVolume;
    }

    public void setGoodsSalesVolume(int goodsSalesVolume) {
        this.goodsSalesVolume = goodsSalesVolume;
    }

    public int getGoodsCartCount() {
        return goodsCartCount;
    }

    public void setGoodsCartCount(int goodsCartCount) {
        this.goodsCartCount = goodsCartCount;
    }

    @Override
    public String toString() {
        return "GoodsBean{" +
                "id=" + id +
                ", goodsId=" + goodsId +
                ", shopId=" + shopId +
                ", goodsName='" + goodsName + '\'' +
                ", goodsPhoto='" + goodsPhoto + '\'' +
                ", goodsPrice=" + goodsPrice +
                ", goodsCategory='" + goodsCategory + '\'' +
                ", goodsSalesVolume=" + goodsSalesVolume +
                ", goodsCartCount=" + goodsCartCount +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(goodsId);
        dest.writeInt(shopId);
        dest.writeString(goodsName);
        dest.writeString(goodsPhoto);
        dest.writeFloat(goodsPrice);
        dest.writeString(goodsCategory);
        dest.writeInt(goodsSalesVolume);
        dest.writeInt(goodsCartCount);
    }

    public static final Creator<GoodsBean> CREATOR = new Creator<GoodsBean>() {
        @Override
        public GoodsBean createFromParcel(Parcel in) {
            GoodsBean goodsBean = new GoodsBean();
            goodsBean.id = in.readInt();
            goodsBean.goodsId = in.readInt();
            goodsBean.shopId = in.readInt();
            goodsBean.goodsName = in.readString();
            goodsBean.goodsPhoto = in.readString();
            goodsBean.goodsPrice = in.readFloat();
            goodsBean.goodsCategory = in.readString();
            goodsBean.goodsSalesVolume = in.readInt();
            goodsBean.goodsCartCount = in.readInt();
            return goodsBean;
        }

        @Override
        public GoodsBean[] newArray(int size) {
            return new GoodsBean[size];
        }
    };
}
