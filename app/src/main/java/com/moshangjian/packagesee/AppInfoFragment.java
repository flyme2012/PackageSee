package com.moshangjian.packagesee;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

/**
 * Created by lbe on 16-7-22.
 */
public class AppInfoFragment extends Fragment implements AppContract.View {

    private ProgressBar loadView;
    private int type = 0;
    private RecyclerView recyclerView;
    private ClipboardManager myClipboard;

    public static AppInfoFragment newInstance(Bundle bundle) {
        AppInfoFragment feedbackFragment = new AppInfoFragment();
        feedbackFragment.setArguments(bundle);
        return feedbackFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_info, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        loadView = (ProgressBar) view.findViewById(R.id.load);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        type = bundle.getInt("key");
    }

    private ListAdapter adapter;

    @Override
    public void showData(Map<Integer, List<AppContract.AppInfo>> map) {
        loadView.setVisibility(View.GONE);
        List<AppContract.AppInfo> appInfos = map.get(type);
        if (adapter == null) {
            adapter = new ListAdapter(appInfos);
        }
        recyclerView.setAdapter(adapter);
        adapter.reload(appInfos);
    }

    private void showDialog(final AppContract.AppInfo appInfo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(appInfo.title);
        builder.setMessage("包名：" + appInfo.packageName + "\n");
        builder.setCancelable(true);
        builder.setPositiveButton("复制", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                copy(appInfo.packageName);
                toast("已复制到剪贴板");
                dialog.cancel();
            }
        });
        builder.setNegativeButton("详情", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                goDetail(appInfo.packageName);
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    private void toast(String des){
        Toast.makeText(getActivity(),des,Toast.LENGTH_SHORT).show();
    }

    private void copy(String packageName) {
        if (myClipboard == null) {
            myClipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        }
        ClipData myClip = ClipData.newPlainText("text", packageName);
        myClipboard.setPrimaryClip(myClip);
    }

    private void goDetail(String packageName) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", packageName, null));
        startActivity(intent);
    }

    public class ListAdapter extends RecyclerView.Adapter<ItemViewHolder> {
        List<AppContract.AppInfo> appInfos;

        public ListAdapter(List<AppContract.AppInfo> appInfos) {
            this.appInfos = appInfos;
        }

        public void reload(List<AppContract.AppInfo> appInfos) {
            this.appInfos = appInfos;
            notifyDataSetChanged();
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ItemViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.app_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            final AppContract.AppInfo appInfo = appInfos.get(position);
            holder.titleView.setText(appInfo.title);
            holder.packageView.setText(appInfo.packageName);
            holder.iconView.setImageDrawable(appInfo.icon);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(appInfo);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    copy(appInfo.packageName);
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return appInfos.size();
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView packageView;
        public TextView titleView;
        public ImageView iconView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            packageView = (TextView) itemView.findViewById(R.id.des);
            titleView = (TextView) itemView.findViewById(R.id.title);
            iconView = (ImageView) itemView.findViewById(R.id.icon);

        }
    }


}
