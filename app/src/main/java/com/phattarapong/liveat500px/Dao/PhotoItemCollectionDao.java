package com.phattarapong.liveat500px.Dao;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Phattarapong on 14-Jul-17.
 */
@Parcel
public class PhotoItemCollectionDao{
    @SerializedName("success")
    private Boolean success;
    @SerializedName("data")
    private List<PhotoItemDao> data;

    public Boolean getSuccess() {
        return success;
    }
    public PhotoItemCollectionDao(){

    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<PhotoItemDao> getData() {
        return data;
    }

    public void setData(List<PhotoItemDao> data) {
        this.data = data;
    }
}
