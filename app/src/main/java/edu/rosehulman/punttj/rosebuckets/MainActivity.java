package edu.rosehulman.punttj.rosebuckets;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import edu.rosehulman.punttj.rosebuckets.fragments.BucketListFragment;
import edu.rosehulman.punttj.rosebuckets.fragments.BucketListItemFragment;
import edu.rosehulman.punttj.rosebuckets.fragments.BucketListSubItemFragment;
import edu.rosehulman.punttj.rosebuckets.fragments.LoginFragment;
import edu.rosehulman.punttj.rosebuckets.fragments.SubItemDetailFragment;
import edu.rosehulman.punttj.rosebuckets.model.BucketList;
import edu.rosehulman.punttj.rosebuckets.model.BucketListItem;
import edu.rosehulman.punttj.rosebuckets.model.SubItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LoginFragment.OnLoginListener,
        BucketListFragment.OnBLSelectedListener, BucketListItemFragment.OnBLItemSelectedListener,
        BucketListSubItemFragment.OnSubItemSelectedListener{

    private BucketListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment fragment = new LoginFragment();
            ft.add(R.id.content_main, fragment);
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onLoginPressed() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = new BucketListFragment();
        ft.replace(R.id.content_main, fragment);
        ft.addToBackStack("bl");
        ft.commit();
    }

    @Override
    public void onBLSelected(BucketList bl) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = new BucketListItemFragment();
        ft.replace(R.id.content_main, fragment);
        ft.addToBackStack("item");
        ft.commit();
    }

    @Override
    public void onBLItemSelected(BucketListItem item) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = new BucketListSubItemFragment();
        ft.replace(R.id.content_main, fragment);
        ft.addToBackStack("subitem");
        ft.commit();
    }

    @Override
    public void onSubItemSelected(SubItem subItem) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = SubItemDetailFragment.newInstance(subItem);
        ft.replace(R.id.content_main, fragment);
        ft.addToBackStack("detail");
        ft.commit();
    }
}
