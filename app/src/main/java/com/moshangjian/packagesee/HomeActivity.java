package com.moshangjian.packagesee;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private AppContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        TabLayout.Tab preTab = tabLayout.newTab();
        preTab.setText("个人应用");
        tabLayout.addTab(preTab);
        TabLayout.Tab systemTab = tabLayout.newTab();
        systemTab.setText("系统应用");
        tabLayout.addTab(systemTab);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        viewPager.setAdapter(new MyFragmentStatePagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        fab.setOnClickListener(this);

        Loader loader = new LoadAppLoader(HomeActivity.this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                performSendMailAction(HomeActivity.this,"446810035@qq.com","技术交流");
                break;
        }
    }

    public void performSendMailAction(Context context, String mail, String title) {
        Uri uri = Uri.parse("mailto:" + mail);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_TEXT, "");
        context.startActivity(Intent.createChooser(intent, title));
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
