package com.phattarapong.liveat500px.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.phattarapong.liveat500px.Dao.PhotoItemDao;
import com.phattarapong.liveat500px.R;
import com.phattarapong.liveat500px.fragment.MoreInfoFragment;

import org.parceler.Parcels;

public class MoreInfoActivity extends AppCompatActivity {
    FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);
        bindView();

        PhotoItemDao dao = Parcels.unwrap(getIntent().getParcelableExtra("dao"));
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.content,
                    MoreInfoFragment.newInstance(dao),"MoreInfo").commit();
        }
         frameLayout = (FrameLayout) findViewById(R.id.content);
    }

    private void bindView() {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
