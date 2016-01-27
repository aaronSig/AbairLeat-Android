package com.superpixel.lurgan.abairleat.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.superpixel.lurgan.abairleat.R;
import com.superpixel.lurgan.abairleat.adapters.DashboardPagerAdapter;
import com.superpixel.lurgan.abairleat.api.API;
import com.superpixel.lurgan.abairleat.util.ProfileCache;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Martin on 1/20/16.
 */
@EActivity(R.layout.activity_dashboard)
public class DashboardActivity extends BaseActivity {

    private static final String LOG_TAG = "DashboardActivity";

    @Bean
    protected DashboardPagerAdapter dashboardPagerAdapter;
    @Bean
    protected API api;

    @ViewById(R.id.toolbar)
    protected Toolbar toolbarView;
    @ViewById(R.id.tabs)
    protected TabLayout tabLayoutView;
    @ViewById(R.id.pager)
    protected ViewPager viewPager;

    @AfterViews
    protected void afterViews() {
        toolbarSetup();
        contentSetup();

        ProfileCache.init(api);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void toolbarSetup() {
        setSupportActionBar(toolbarView);
    }

    private void contentSetup() {
        viewPager.setAdapter(dashboardPagerAdapter);
        tabLayoutView.setupWithViewPager(viewPager);
    }
}
