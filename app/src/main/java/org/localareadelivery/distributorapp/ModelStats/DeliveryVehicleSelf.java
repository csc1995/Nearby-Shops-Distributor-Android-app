package org.localareadelivery.distributorapp.ModelStats;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sumeet on 14/6/16.
 */
public class DeliveryVehicleSelf implements Parcelable{

    int ID;
    String vehicleName;
    int shopID;


    protected DeliveryVehicleSelf(Parcel in) {
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

    public static final Creator<DeliveryVehicleSelf> CREATOR = new Creator<DeliveryVehicleSelf>() {
        @Override
        public DeliveryVehicleSelf createFromParcel(Parcel in) {
            return new DeliveryVehicleSelf(in);
        }

        @Override
        public DeliveryVehicleSelf[] newArray(int size) {
            return new DeliveryVehicleSelf[size];
        }
    };

    public DeliveryVehicleSelf() {
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
