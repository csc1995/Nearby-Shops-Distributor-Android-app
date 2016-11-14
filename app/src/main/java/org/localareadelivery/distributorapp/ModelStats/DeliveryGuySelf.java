package org.localareadelivery.distributorapp.ModelStats;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sumeet on 14/6/16.
 */
public class DeliveryGuySelf implements Parcelable{

    private int ID;
    private String vehicleName;
    private int shopID;


    protected DeliveryGuySelf(Parcel in) {
        ID = in.readInt();
        vehicleName = in.readString();
        shopID = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeString(vehicleName);
        dest.writeInt(shopID);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DeliveryGuySelf> CREATOR = new Creator<DeliveryGuySelf>() {
        @Override
        public DeliveryGuySelf createFromParcel(Parcel in) {
            return new DeliveryGuySelf(in);
        }

        @Override
        public DeliveryGuySelf[] newArray(int size) {
            return new DeliveryGuySelf[size];
        }
    };

    public DeliveryGuySelf() {
    }


    // Getter and setter methods

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }
}
