package com.phattarapong.liveat500px.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.phattarapong.liveat500px.Dao.PhotoItemDao;
import com.phattarapong.liveat500px.R;
import com.phattarapong.liveat500px.fragment.MainFragment;
import com.phattarapong.liveat500px.fragment.MoreInfoFragment;
import com.phattarapong.liveat500px.manager.Contextor;

import org.parceler.Parcels;

public class MainActivity extends AppCompatActivity implements MainFragment.FragmentListener {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initInstance();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentContainer, MainFragment.newInstance(), "MainFragment")
                    .commit();
        }
    }

    private void initInstance() {
        toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                MainActivity.this,drawerLayout,R.string.open_drawer,R.string.close_drawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.second_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle Humberger Menu
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        if(item.getItemId() == R.id.action_setting){
            Toast.makeText(Contextor.getInstance().getContext(),"Click Setting",Toast.LENGTH_SHORT)
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPhotoItemClicked(PhotoItemDao dao) {
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.moreInfoContainer);
        if(frameLayout == null){
            Intent intent = new Intent(MainActivity.this,MoreInfoActivity.class);
            intent.putExtra("dao", Parcels.wrap(dao));
            startActivity(intent);
        }
        else{
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.moreInfoContainer, MoreInfoFragment.newInstance(dao))
                    .commit();
        }


    }
}
