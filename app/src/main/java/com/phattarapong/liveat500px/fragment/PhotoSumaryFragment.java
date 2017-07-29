package com.phattarapong.liveat500px.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.phattarapong.liveat500px.Dao.PhotoItemDao;
import com.phattarapong.liveat500px.R;

import org.parceler.Parcels;

public class PhotoSumaryFragment extends Fragment {
    PhotoItemDao dao;
    private ImageView imageView;
    private TextView textName;
    private TextView textDescription;

    public PhotoSumaryFragment() {
        super();
    }

    public static PhotoSumaryFragment newInstance(PhotoItemDao dao) {
        PhotoSumaryFragment fragment = new PhotoSumaryFragment();
        Bundle args = new Bundle();
        args.putParcelable("dao", Parcels.wrap(dao));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
        dao = Parcels.unwrap(getArguments().getParcelable("dao"));
        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_photo_sumary, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    @SuppressWarnings("UnusedParameters")
    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here
        // Note: State of variable initialized here could not be saved
        //       in onSavedInstanceState
        imageView = (ImageView) rootView.findViewById(R.id.image);
        textName = (TextView) rootView.findViewById(R.id.textView1);
        textDescription = (TextView) rootView.findViewById(R.id.textView2);

        textName.setText(dao.getCaption());
        textDescription.setText(dao.getUsername()+ "\n" + dao.getCamera());
        Glide.with(PhotoSumaryFragment.this)
                .load(dao.getImageUrl())
                .into(imageView);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance (Fragment level's variables) State here
    }

    @SuppressWarnings("UnusedParameters")
    private void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore Instance (Fragment level's variables) State here
    }

}