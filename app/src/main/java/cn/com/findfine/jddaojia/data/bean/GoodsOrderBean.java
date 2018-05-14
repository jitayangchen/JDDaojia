package cn.com.findfine.jddaojia.data.bean;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class GoodsOrderBean implements Parcelable {
    private String orderId;
    private String userId;
    private String orderNumber;
    private String createOrderTime;
    private int orderStatus;
    private int shopId;
    private String shopName;
    private List<GoodsBean> goodsArray;
    private String goodsPrice;
    private String userAddress;
    private int orderEvaluation = 0;
    private String evalutionContent;

    public GoodsOrderBean() {

    }

    protected GoodsOrderBean(Parcel in) {
        orderId = in.readString();
        userId = in.readString();
        orderNumber = in.readString();
        createOrderTime = in.readString();
        orderStatus = in.readInt();
        shopId = in.readInt();
        shopName = in.readString();
        goodsArray = in.createTypedArrayList(GoodsBean.CREATOR);
        goodsPrice = in.readString();
        userAddress = in.readString();
        orderEvaluation = in.readInt();
        evalutionContent = in.readString();
    }

    public static final Creator<GoodsOrderBean> CREATOR = new Creator<GoodsOrderBean>() {
        @Override
        public GoodsOrderBean createFromParcel(Parcel in) {
            return new GoodsOrderBean(in);
        }

        @Override
        public GoodsOrderBean[] newArray(int size) {
            return new GoodsOrderBean[size];
        }
    };

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCreateOrderTime() {
        return createOrderTime;
    }

    public void setCreateOrderTime(String createOrderTime) {
        this.createOrderTime = createOrderTime;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
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

    public List<GoodsBean> getGoodsArray() {
        return goodsArray;
    }

    public void setGoodsArray(List<GoodsBean> goodsArray) {
        this.goodsArray = goodsArray;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public int getOrderEvaluation() {
        return orderEvaluation;
    }

    public void setOrderEvaluation(int orderEvaluation) {
        this.orderEvaluation = orderEvaluation;
    }

    public String getEvalutionContent() {
        return evalutionContent;
    }

    public void setEvalutionContent(String evalutionContent) {
        this.evalutionContent = evalutionContent;
    }

    @Override
    public String toString() {
        return "GoodsOrderBean{" +
                "orderId=" + orderId +
                ", userId='" + userId + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                ", createOrderTime='" + createOrderTime + '\'' +
                ", orderStatus=" + orderStatus +
                ", shopId=" + shopId +
                ", shopName='" + shopName + '\'' +
                ", goodsArray='" + goodsArray + '\'' +
                ", goodsPrice='" + goodsPrice + '\'' +
                ", userAddress='" + userAddress + '\'' +
                ", orderEvaluation=" + orderEvaluation +
                ", evalutionContent=" + evalutionContent +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(orderId);
        dest.writeString(userId);
        dest.writeString(orderNumber);
        dest.writeString(createOrderTime);
        dest.writeInt(orderStatus);
        dest.writeInt(shopId);
        dest.writeString(shopName);
        dest.writeTypedList(goodsArray);
        dest.writeString(goodsPrice);
        dest.writeString(userAddress);
        dest.writeInt(orderEvaluation);
        dest.writeString(evalutionContent);
    }
}
