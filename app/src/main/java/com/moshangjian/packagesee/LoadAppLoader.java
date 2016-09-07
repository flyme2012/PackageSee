package com.moshangjian.packagesee;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by lbe on 16-7-22.
 */
public class LoadAppLoader extends AsyncDataLoader<Map<Integer,List<AppContract.AppInfo>>> {

    public LoadAppLoader(Context context) {
        super(context);
    }

    @Override
    public Map<Integer,List<AppContract.AppInfo>> loadInBackground() {
        return getSystemAppInfo();
    }

    /**
     * 获取系统中的全部包信息 并将预先系统安装的社交软件去掉
     */
    private Map<Integer,List<AppContract.AppInfo>> getSystemAppInfo() {
        PackageManager pm = getContext().getPackageManager();
        List<PackageInfo> packageInfos = pm.getInstalledPackages(0);
        Map<Integer ,List<AppContract.AppInfo>> map = new HashMap<>();
        List<AppContract.AppInfo> systemAppList = new ArrayList<>();
        List<AppContract.AppInfo>perAppList = new ArrayList<>();
        Iterator<PackageInfo> iterator = packageInfos.iterator();
        while (iterator.hasNext()) {
            PackageInfo packageInfo = iterator.next();
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            String packageName = applicationInfo.packageName;
            AppContract.AppInfo appInfo = new AppContract.AppInfo();
            appInfo.packageName = packageName;
            appInfo.title = (String) applicationInfo.loadLabel(pm);
            appInfo.signInco = getSingInfo(packageName);
            appInfo.icon = applicationInfo.loadIcon(pm);
            appInfo.firstInstallTime = packageInfo.firstInstallTime;
            if (isSystemApp(applicationInfo)) {
                systemAppList.add(appInfo);
            } else {
                perAppList.add(appInfo);
            }

        }
        map.put(0,perAppList);
        map.put(1,systemAppList);
        return map;
    }


    public String getSingInfo(String packageName) {
        try {
            PackageInfo packageInfo = getContext().getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];
            return parseSignature(sign.toByteArray());
        } catch (Exception e) {
        }
        return "";
    }

    public String parseSignature(byte[] signature) {
        try {
            CertificateFactory certFactory = CertificateFactory
                    .getInstance("X.509");
            X509Certificate cert = (X509Certificate) certFactory
                    .generateCertificate(new ByteArrayInputStream(signature));
            String pubKey = cert.getPublicKey().toString();
            String signNumber = cert.getSerialNumber().toString();
            Log.d("hb","signName:" + cert.getSigAlgName());
            Log.d("hb","pubKey:" + pubKey);
            Log.d("hb","signNumber:" + signNumber);
            Log.d("hb","subjectDN:"+cert.getSubjectDN().toString());
            return signNumber;
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 判断是不是系统自身的ＡＰＰ
     *
     * @param appInfo
     * @return
     */
    private boolean isSystemApp(ApplicationInfo appInfo) {
        return (appInfo.flags & appInfo.FLAG_SYSTEM) > 0;
    }


}
