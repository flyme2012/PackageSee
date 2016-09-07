package com.moshangjian.packagesee;

import android.graphics.drawable.Drawable;

import java.util.List;
import java.util.Map;

/**
 * Created by lbe on 16-7-22.
 */
public class AppContract {

    interface View{

        public void showData(Map<Integer,List<AppInfo>> map);

    }

    interface Presenter{
        void start();
        void addView(View view);

    }

    public static class AppInfo {
        public String title;
        public String signInco;
        public String packageName;
        public Drawable icon;
        public long firstInstallTime;
    }
}
