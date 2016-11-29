package com.caco3.orca.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.caco3.orca.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A base activity class which performs some routine work
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Nullable
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Nullable
    @BindView(R.id.nv_view)
    NavigationView navigationView;

    @Nullable
    @BindView(R.id.nav_drawer_layout)
    DrawerLayout drawerLayout;

    private ActionBarDrawerToggle drawerToggle;

    /**
     * Constant indicating that current activity has no item in navigation drawer
     * and it should not be checked
     * @see #getNavDrawerItemId()
     */
    private static final int NAV_DRAWER_NO_ITEM = -1;


    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);

        setupToolbar();
        setupNavDrawer();
    }


    /**
     * Subclasses may override it to return a menu resource id associated with this activity.
     * Then the item will be checked in nav drawer.
     * If subclass doesn't override it no item will be checked in the nav drawer
     *
     * @return menu resource id of item associated with this activity
     */
    @IdRes
    protected int getNavDrawerItemId(){
        return NAV_DRAWER_NO_ITEM;
    }

    /**
     * Subclasses may override it to indicate whether current activity has parent activity.
     * If it returns true then back arrow is shown on toolbar instead of hamburger icon
     * @return boolean indicating whether the current activity has parent activity. Default false
     */
    protected boolean hasParentActivity(){
        return false;
    }

    protected void setupToolbar(){
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();

            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    @Override
    public void onBackPressed(){
        if (isNavDrawerOpened()) {
            closeNavDrawer();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Returns boolean indicating whether navigation drawer is opened
     * @return true if nav drawer is opened, false if closed or there is no navigation drawer in this activity
     */
    private boolean isNavDrawerOpened(){
        /**
         * Simplified from
         * {@code   if (drawerLayout != null) {
         *              return drawerLayout.isDrawerOpen(GravityCompat.START);
         *          } else {
         *               return false;
         *          }
         * }
         */
        return drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START);
    }

    /**
     * Closes navigation drawer if it's present in this activity
     */
    private void closeNavDrawer(){
        if (drawerLayout != null) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    private void setupNavigationView(){
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if (item.getItemId() != getNavDrawerItemId()) {
                        switch (item.getItemId()){
                            case R.id.nav_learning:
                                // TODO: 11/25/16 navigate to learning activity
                                break;
                        }
                    }

                    if (drawerLayout != null) {
                        drawerLayout.closeDrawers();
                    }


                    return true;
                }
            });
            // if no parent activity, show hamburger icon in toolbar
            drawerToggle.setDrawerIndicatorEnabled(!hasParentActivity());

            if (getNavDrawerItemId() != NAV_DRAWER_NO_ITEM) {
                navigationView.getMenu().findItem(getNavDrawerItemId()).setChecked(true);
            }
        }
    }

    private void setupNavDrawer(){
        if (drawerLayout != null &&  toolbar != null) {
            drawerToggle = new ActionBarDrawerToggle(this,
                    drawerLayout,
                    toolbar,
                    R.string.nav_drawer_open,
                    R.string.nav_drawer_close);

            drawerLayout.addDrawerListener(drawerToggle);

            setupNavigationView();
        }
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (drawerToggle != null) {
            drawerToggle.syncState();
        }
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (drawerToggle != null) {
            drawerToggle.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
}
