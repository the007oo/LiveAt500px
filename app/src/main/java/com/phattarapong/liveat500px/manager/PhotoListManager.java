package com.phattarapong.liveat500px.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.gson.Gson;
import com.phattarapong.liveat500px.Dao.PhotoItemCollectionDao;
import com.phattarapong.liveat500px.Dao.PhotoItemDao;

import org.parceler.Parcels;

import java.util.ArrayList;

/**
 * Created by nuuneoi on 11/16/2014.
 */

public class PhotoListManager {
    public PhotoItemCollectionDao getDao() {
        return dao;
    }

    public void setDao(PhotoItemCollectionDao dao) {
        this.dao = dao;
        saveCache();
    }

    public void insertDaoAtTopPosition(PhotoItemCollectionDao Dao) {
        if (dao == null) {
            dao = new PhotoItemCollectionDao();
        }
        if (dao.getData() == null) {
            dao.setData(new ArrayList<PhotoItemDao>());
        }
        dao.getData().addAll(0, Dao.getData());
        saveCache();
    }

    public void appendDaoAtBottomPosition(PhotoItemCollectionDao Dao) {
        if (dao == null) {
            dao = new PhotoItemCollectionDao();
        }
        if (dao.getData() == null) {
            dao.setData(new ArrayList<PhotoItemDao>());
        }
        dao.getData().addAll(dao.getData().size(), Dao.getData());
        saveCache();
    }

    private PhotoItemCollectionDao dao;


    private Context mContext;

    public PhotoListManager() {
        mContext = Contextor.getInstance().getContext();
        loadCacge();
    }

    public int getMinimumId() {
        if (dao == null) {
            return 0;
        }
        if (dao.getData() == null) {
            return 0;
        }
        if (dao.getData().size() == 0) {
            return 0;
        }
        int minId = dao.getData().get(0).getId();
        for (int i = 1; i < dao.getData().size(); i++) {
            minId = Math.min(minId, dao.getData().get(i).getId());
        }
        return minId;
    }

    public int getMaximumId() {
        if (dao == null) {
            return 0;
        }
        if (dao.getData() == null) {
            return 0;
        }
        if (dao.getData().size() == 0) {
            return 0;
        }
        int maxId = dao.getData().get(0).getId();
        for (int i = 1; i < dao.getData().size(); i++) {
            maxId = Math.max(maxId, dao.getData().get(i).getId());
        }
        return maxId;
    }

    public int getCount() {
        if (dao == null) {
            return 1;
        }
        if (dao.getData() == null) {
            return 1;
        }
        return dao.getData().size() + 1;

    }

    public Bundle onSaveInstanceState() {

        Bundle bundle = new Bundle();
        bundle.putParcelable("dao", Parcels.wrap(dao));
        return bundle;
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        dao = Parcels.unwrap(savedInstanceState.getParcelable("dao"));
    }

    private void saveCache() {
        PhotoItemCollectionDao cacheDao = new PhotoItemCollectionDao();
        if(dao !=null){
            if (dao.getData() != null){
                cacheDao.setData(dao.getData().subList(0,Math.min(20,dao.getData().size())));
            }
        }
        String jsonString = new Gson().toJson(cacheDao);

        SharedPreferences preferences = mContext.getSharedPreferences("photos",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("photocache",jsonString);
        editor.apply();
    }

    private void loadCacge() {
        SharedPreferences preferences = mContext.getSharedPreferences("photos",Context.MODE_PRIVATE);
       String json = preferences.getString("photocache",null);
        if(json == null){
            return;
        }
        dao = new Gson().fromJson(json,PhotoItemCollectionDao.class);
    }
}

