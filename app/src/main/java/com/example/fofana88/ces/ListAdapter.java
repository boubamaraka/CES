package com.example.fofana88.ces;

import android.app.Activity;
import android.support.v4.app.Fragment;
        import android.content.Context;
        import android.content.pm.ApplicationInfo;
        import android.content.pm.PackageInfo;
        import android.content.pm.PackageManager;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;

        import java.util.List;

public class ListAdapter extends ArrayAdapter<ApplicationInfo>{

    private List<ApplicationInfo> appsList = null;
    private Context context;
    private PackageManager packageManager;
    public ListAdapter(Context context, int txtid,
                       List<ApplicationInfo> appsList) {
        super(context, txtid, appsList);
        this.context = context;
        this.appsList = appsList;
        packageManager = context.getPackageManager();
    }


    @Override
    public View getView(int position, View newview, ViewGroup parent) {

        View view = newview;
        if (null == view) {
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_single, null);
        }

        ApplicationInfo applicationInfo = appsList.get(position);
        if (null != applicationInfo) {
            TextView appName = (TextView) view.findViewById(R.id.app_name);
            TextView packageName = (TextView) view.findViewById(R.id.app_paackage);
            ImageView iconview = (ImageView) view.findViewById(R.id.app_icon);
            TextView appVersion = (TextView) view.findViewById(R.id.app_version);
            TextView AppCode = (TextView) view.findViewById(R.id.app_code);
            TextView AppUid = (TextView) view.findViewById(R.id.app_uid);

            appName.setText(applicationInfo.loadLabel(packageManager));
            packageName.setText(applicationInfo.packageName);
            iconview.setImageDrawable(applicationInfo.loadIcon(packageManager));
            PackageInfo pinfo = null;
            try {
                pinfo = packageManager.getPackageInfo(applicationInfo.packageName,0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            String VersionName = pinfo.versionName;
            int VersionCode = pinfo.versionCode;
            int Appid=applicationInfo.uid;
            appVersion.setText(VersionName);
            AppCode.setText(""+VersionCode);
            AppUid.setText(""+Appid);

           // int versionNumber = pinfo.versionCode;
           // String versionName = pinfo.versionName;

            //System.out.println(applicationInfo.loadLabel(packageManager));
        }
        return view;
    }
}