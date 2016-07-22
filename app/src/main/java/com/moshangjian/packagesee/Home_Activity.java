package com.moshangjian.packagesee;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class Home_Activity extends AppCompatActivity {

    private AppContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        TabLayout.Tab preTab = tabLayout.newTab();
        preTab.setText("个人应用");
        tabLayout.addTab(preTab);
        TabLayout.Tab systemTab = tabLayout.newTab();
        systemTab.setText("系统应用");
        tabLayout.addTab(systemTab);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        viewPager.setAdapter(new MyFragmentStatePagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        Loader loader = new LoadAppLoader(Home_Activity.this);
        presenter = new AppPresenter(loader,getSupportLoaderManager());
        presenter.start();

        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#f8f8f8"));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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

    /**
     * 定义自己的ViewPager适配器。
     * 也可以使用FragmentPagerAdapter。关于这两者之间的区别，可以自己去搜一下。
     */
    public class MyFragmentStatePagerAdapter extends FragmentPagerAdapter {

        public MyFragmentStatePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            bundle.putInt("key",position);
            AppInfoFragment fragment = AppInfoFragment.newInstance(bundle);
            Log.d("hb","position = " + position);
            if (presenter != null){
                presenter.addView(fragment);
            }
            return fragment ;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0 ){
                return "个人应用";
            }else{
                return "系统应用";
            }
        }
    }
}
