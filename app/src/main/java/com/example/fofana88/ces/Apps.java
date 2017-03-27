package com.example.fofana88.ces;

/**
 * Created by fofana88 on 9.2.2017.
 */
import android.support.v4.app.Fragment;
//import android.app.Fragment;
import android.app.ListActivity;
import android.support.v4.app.ListFragment;
//import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Apps extends ListFragment {
    View rootView;
    public Apps() {
    }
    private static final String ARG_SECTION_NUMBER ="" ;

    public static Apps myinstance(int sectionNumber) {
        Apps fragment = new Apps();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    private PackageManager packageManager = null;
    private List<ApplicationInfo> myapplist = null;
    private ListAdapter listadaptor = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.apps, container,
                false);

        packageManager = getActivity().getPackageManager();

        new ApplicationsLoading().execute();
        return rootView;
    }
    private List<ApplicationInfo> Launchingcheck(List<ApplicationInfo> applist) {
        ArrayList<ApplicationInfo> applicationlist = new ArrayList<ApplicationInfo>();
        for (ApplicationInfo appinfo : applist) {
            try {
                if (null != packageManager.getLaunchIntentForPackage(appinfo.packageName)) {
                    applicationlist.add(appinfo);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return applicationlist;
    }

    private class ApplicationsLoading extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progressApp = null;

        @Override
        protected Void doInBackground(Void... params) {
            myapplist = Launchingcheck(packageManager.getInstalledApplications(PackageManager.GET_META_DATA));
            listadaptor = new ListAdapter(getActivity(),R.layout.list_single, myapplist);

            return null;
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void result) {
            setListAdapter(listadaptor);
            progressApp.dismiss();
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            progressApp = ProgressDialog.show(getActivity(), null,
                    "CES application list...");
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }



}