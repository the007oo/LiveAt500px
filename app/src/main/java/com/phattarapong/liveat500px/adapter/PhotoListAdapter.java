package com.phattarapong.liveat500px.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;

import com.phattarapong.liveat500px.Dao.PhotoItemCollectionDao;
import com.phattarapong.liveat500px.Dao.PhotoItemDao;
import com.phattarapong.liveat500px.R;
import com.phattarapong.liveat500px.dataType.MutableInteger;
import com.phattarapong.liveat500px.view.PhotoListItem;

/**
 * Created by Phattarapong on 13-Jul-17.
 */

public class PhotoListAdapter extends BaseAdapter {
    MutableInteger lastPostionInteger;

    public PhotoListAdapter(MutableInteger lastPostionInteger) {
        this.lastPostionInteger = lastPostionInteger;
    }

    public void setDao(PhotoItemCollectionDao dao) {
        this.dao = dao;
    }

    PhotoItemCollectionDao dao;

    @Override
    public int getCount() {
        if (dao == null) {
            return 0;
        }
        if (dao.getData() == null) {
            return 0;
        }
        return dao.getData().size();
    }

    @Override
    public Object getItem(int position) {
        return dao.getData().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

//    @Override
//    public int getItemViewType(int position) {
//        return position % 2 == 0 ? 0:1 ;
//    }
//
//    @Override
//    public int getViewTypeCount() {
//        return 2;
//    }

    @Override
    public int getItemViewType(int position) {
        return position == getCount() - 1 ? 1 : 0;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == getCount() - 1) {
            ProgressBar item;
            if (convertView != null) {
                item = (ProgressBar) convertView;
            } else
                item = new ProgressBar(parent.getContext());
            return item;
        }
        PhotoListItem photoListItem;
        if (convertView != null)
            photoListItem = (PhotoListItem) convertView;
        else
            photoListItem = new PhotoListItem(parent.getContext());

        PhotoItemDao dao = (PhotoItemDao) getItem(position);
        photoListItem.setNameText(dao.getCaption());
        photoListItem.setDescription(dao.getUsername() + "\n" + dao.getCamera());
        photoListItem.setImageUrl(dao.getImageUrl());

        if (lastPostionInteger.getValue() < position) {
            Animation animation = AnimationUtils.loadAnimation(parent.getContext(), R.anim.up_form_bottom);
            photoListItem.startAnimation(animation);
            lastPostionInteger.setValue(position);
        }
        return photoListItem;
//        }
//        else {
//            TextView photoListItem;
//            if(convertView != null)
//                photoListItem = (TextView) convertView;
//            else
//                photoListItem = new TextView(parent.getContext());
//            photoListItem.setText("Position" + position);
//            return photoListItem;
//        }

    }

    public void increaseLastPosition(int amount) {
        lastPostionInteger.setValue(lastPostionInteger.getValue() + amount);
    }
}
