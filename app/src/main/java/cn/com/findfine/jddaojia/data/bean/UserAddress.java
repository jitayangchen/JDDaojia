package cn.com.findfine.jddaojia.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yangchen on 2018/4/3.
 */

public class UserAddress implements Parcelable {
    private int id;
    private String userId;
    private String city;
    private String address;
    private String houseNumber;
    private String name;
    private String phoneNumber;

    public UserAddress() {

    }

    protected UserAddress(Parcel in) {
        id = in.readInt();
        userId = in.readString();
        city = in.readString();
        address = in.readString();
        houseNumber = in.readString();
        name = in.readString();
        phoneNumber = in.readString();
    }

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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(userId);
        dest.writeString(city);
        dest.writeString(address);
        dest.writeString(houseNumber);
        dest.writeString(name);
        dest.writeString(phoneNumber);
    }

    public static final Creator<UserAddress> CREATOR = new Creator<UserAddress>() {
        @Override
        public UserAddress createFromParcel(Parcel in) {
            return new UserAddress(in);
        }

        @Override
        public UserAddress[] newArray(int size) {
            return new UserAddress[size];
        }
    };

}
