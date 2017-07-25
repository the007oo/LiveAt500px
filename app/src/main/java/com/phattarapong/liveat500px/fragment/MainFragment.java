package com.phattarapong.liveat500px.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.phattarapong.liveat500px.Dao.PhotoItemCollectionDao;
import com.phattarapong.liveat500px.R;
import com.phattarapong.liveat500px.adapter.PhotoListAdapter;
import com.phattarapong.liveat500px.dataType.MutableInteger;
import com.phattarapong.liveat500px.manager.Contextor;
import com.phattarapong.liveat500px.manager.HttpManager;
import com.phattarapong.liveat500px.manager.PhotoListManager;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    /**************
     * Variable
     **************/
    PhotoListManager photoListManager;
    ListView listView;
    SwipeRefreshLayout swipRefresh;
    PhotoListAdapter listAdapter;
    Button btnNewPhoto;
    boolean isLoadinmore = false;
    MutableInteger lastPositionInteger;

    /**************
     * Methods
     **************/
    public MainFragment() {
        super();
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init() {
        photoListManager = new PhotoListManager();
        lastPositionInteger = new MutableInteger(-1);
    }

    private void initInstances(View rootView, Bundle savedInstancestate) {
        // Init 'View' instance(s) with rootView.findViewById here

        btnNewPhoto = (Button) rootView.findViewById(R.id.btnNewPhotos);
        btnNewPhoto.setOnClickListener(btnListenderNewPhoto);

        listView = (ListView) rootView.findViewById(R.id.list_item);
        listAdapter = new PhotoListAdapter(lastPositionInteger);
        listAdapter.setDao(photoListManager.getDao());
        listView.setAdapter(listAdapter);

        swipRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipRefresh);
        swipRefresh.setOnRefreshListener(pullToRefreshData);

        // แก้บัค Scroll ต้องอยุ่บนสนถึงจะ Refreah ได้
        listView.setOnScrollListener(listViewScrollListener);
        if (savedInstancestate == null) {
            refreshData();
        }


    }

    private void refreshData() {
        if (photoListManager.getCount() == 0) {
            reloadData();
        } else {
            reloadDataNewwer();
        }
    }


    private void reloadDataNewwer() {
        int maxId = photoListManager.getMaximumId();
        Call<PhotoItemCollectionDao> call = HttpManager.getInstance()
                .getService()
                .loadPhotoListAfterId(maxId);
        call.enqueue(new PhotoListLoadCallBack(PhotoListLoadCallBack.MODE_RELOAD_NEWWER));
    }


    private void loadMoreData() {
        if (isLoadinmore)
            return;
        isLoadinmore = true;
        int minId = photoListManager.getMinimumId();
        Call<PhotoItemCollectionDao> call = HttpManager.getInstance()
                .getService()
                .loadPhotoListBeforeId(minId);
        call.enqueue(new PhotoListLoadCallBack(PhotoListLoadCallBack.MODE_LOAD_MORE));
    }

    private void reloadData() {
        Call<PhotoItemCollectionDao> call = HttpManager.getInstance().getService().loadPhotoList();
        call.enqueue(new PhotoListLoadCallBack(PhotoListLoadCallBack.MODE_RELOAD));
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /*
     * Save Instance State Here
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here
        outState.putBundle("photoListManager",
                photoListManager.onSaveInstanceState());
        outState.putBundle("lastPositionInteger", lastPositionInteger.onSavedInstanceState());

    }

    /*
   * Restore Instance State Here
   */
    private void onRestoreInstanceState(Bundle savedInstanceState) {

        photoListManager.onRestoreInstanceState(
                savedInstanceState.getBundle("photoListManager"));
        lastPositionInteger.onRestoreInstanceState(savedInstanceState
                .getBundle("lastPositionInteger"));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void showBtnNewPhoto() {
        btnNewPhoto.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(Contextor.getInstance().getContext(), R.anim.zoom_fade_in);
        btnNewPhoto.startAnimation(animation);
    }

    private void hideBtnNewPhoto() {
        btnNewPhoto.setVisibility(View.GONE);
        Animation animation = AnimationUtils.loadAnimation(Contextor.getInstance().getContext(), R.anim.zoom_fade_out);
        btnNewPhoto.startAnimation(animation);
    }

    private void showToast(String text) {
        Toast.makeText(Contextor.getInstance().getContext()
                , text, Toast.LENGTH_SHORT).show();
    }

    /**************
     * Listener
     **************/
    View.OnClickListener btnListenderNewPhoto = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == btnNewPhoto) {
                hideBtnNewPhoto();
                listView.smoothScrollToPosition(0);
            }
        }
    };
    SwipeRefreshLayout.OnRefreshListener pullToRefreshData = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            refreshData();
        }
    };
    AbsListView.OnScrollListener listViewScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (view == listView) {
                swipRefresh.setEnabled(firstVisibleItem == 0);
                if (firstVisibleItem + visibleItemCount >= totalItemCount) {
                    if (photoListManager.getCount() > 0) {
                        //Load More
                        loadMoreData();
                    }
                }
            }
        }
    };

    /**************
     * Inner Class
     **************/
    class PhotoListLoadCallBack implements Callback<PhotoItemCollectionDao> {
        public static final int MODE_RELOAD = 1;
        public static final int MODE_RELOAD_NEWWER = 2;
        public static final int MODE_LOAD_MORE = 3;
        int getModeReload;

        public PhotoListLoadCallBack(int Mode) {
            getModeReload = Mode;
        }

        @Override
        public void onResponse(Call<PhotoItemCollectionDao> call, Response<PhotoItemCollectionDao> response) {
            swipRefresh.setRefreshing(false);
            if (response.isSuccessful()) {
                PhotoItemCollectionDao dao = response.body();

                int fristVisivlePostion = listView.getFirstVisiblePosition();
                View v = listView.getChildAt(0);
                int top = v == null ? 0 : v.getTop();
                if (getModeReload == MODE_RELOAD_NEWWER) {
                    photoListManager.insertDaoAtTopPosition(dao);

                    int additionalSiz =
                            (dao != null && dao.getData() != null) ? dao.getData().size() : 0;
                    listAdapter.increaseLastPosition(additionalSiz);
                    listView.setSelectionFromTop(fristVisivlePostion + additionalSiz, top);
                    if (additionalSiz > 0) {
                        showBtnNewPhoto();
                    }
                } else if (getModeReload == MODE_LOAD_MORE) {
                    photoListManager.appendDaoAtBottomPosition(dao);

                } else {
                    photoListManager.setDao(dao);
                }
                clearLoadingMoreFlagIfCapable(getModeReload);
                listAdapter.setDao(photoListManager.getDao());
                listAdapter.notifyDataSetChanged();
                showToast("Load Complated");
            } else {
                clearLoadingMoreFlagIfCapable(getModeReload);
                try {
                    showToast(response.errorBody().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(Call<PhotoItemCollectionDao> call, Throwable t) {
            clearLoadingMoreFlagIfCapable(getModeReload);
            swipRefresh.setRefreshing(false);
            showToast(t.toString());
        }

        private void clearLoadingMoreFlagIfCapable(int getModeReload) {
            if (getModeReload == MODE_LOAD_MORE) {
                isLoadinmore = false;
            }
        }
    }
}
