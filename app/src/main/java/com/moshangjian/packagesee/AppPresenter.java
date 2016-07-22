package com.moshangjian.packagesee;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lbe on 16-7-22.
 */
public class AppPresenter implements AppContract.Presenter, LoaderManager.LoaderCallbacks<Map<Integer,List<AppContract.AppInfo>>> {

    private Map<Integer,List<AppContract.AppInfo>> map;
    private final static int LOADER_ID = 101;
    private List<AppContract.View> viewList;
    private Loader loader;
    private LoaderManager loaderManager;
    public AppPresenter(Loader loader, LoaderManager loaderManager) {
        this.loader = loader;
        this.loaderManager = loaderManager;
        viewList = new ArrayList<>();
    }

    @Override
    public void addView(AppContract.View view){
        viewList.add(view);
    }

    @Override
    public void start(){
        loaderManager.initLoader(LOADER_ID,null,this);
    }

    private void showData(){
        if (viewList != null && viewList.size() > 0 ){
            for (AppContract.View view :viewList){
                view.showData(map);
            }
        }
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Map<Integer, List<AppContract.AppInfo>>> loader, Map<Integer, List<AppContract.AppInfo>> data) {
        if (data != null){
            map = data;
            showData();
        }
    }

    @Override
    public void onLoaderReset(Loader<Map<Integer, List<AppContract.AppInfo>>> loader) {

    }

}
