package edu.rosehulman.punttj.rosebuckets;

import android.support.v4.app.FragmentManager;
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

import edu.rosehulman.punttj.rosebuckets.fragments.AboutFragment;
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

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);


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


            FragmentManager fm = getSupportFragmentManager();
            if(fm.getBackStackEntryCount() > 1) {
                if (fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1).getName().equals("login")) {
                    fab.setVisibility(View.GONE);
                }
                if (fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1).getName().equals("subItem")) {
                    fab.setVisibility(View.VISIBLE);
                }
            }

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

        Fragment switchTo = null;

        switch(id){
            case R.id.nav_about:
                switchTo = new AboutFragment();
                fab.setVisibility(View.GONE);
                break;
            case R.id.nav_home:
                switchTo = new BucketListFragment();
                fab.setVisibility(View.VISIBLE);
                break;
        }

        if(switchTo != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            for(int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i ++){
                getSupportFragmentManager().popBackStackImmediate();
            }

            ft.replace(R.id.content_main, switchTo);
            ft.addToBackStack(null);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onLoginPressed() {
        fab.setVisibility(View.VISIBLE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = new BucketListFragment();
        ft.replace(R.id.content_main, fragment);
        ft.addToBackStack("login");
        ft.commit();
    }

    @Override
    public void onBLSelected(BucketList bl) {
        fab.setVisibility(View.VISIBLE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = new BucketListItemFragment();
        ft.replace(R.id.content_main, fragment);
        ft.addToBackStack("bl");
        ft.commit();
    }

    @Override
    public void onBLItemSelected(BucketListItem item) {
        fab.setVisibility(View.VISIBLE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = new BucketListSubItemFragment();
        ft.replace(R.id.content_main, fragment);
        ft.addToBackStack("blItem");
        ft.commit();
    }

    @Override
    public void onSubItemSelected(SubItem subItem) {
        fab.setVisibility(View.GONE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = SubItemDetailFragment.newInstance(subItem);
        ft.replace(R.id.content_main, fragment);
        ft.addToBackStack("subItem");
        ft.commit();
    }
}
