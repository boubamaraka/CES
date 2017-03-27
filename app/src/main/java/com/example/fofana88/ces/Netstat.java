package com.example.fofana88.ces;

/**
 * Created by fofana88 on 9.2.2017.
 */

import android.app.Activity;
import android.app.ActivityManager;
//import android.app.Fragment;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import java.io.BufferedReader;
import java.io.FileReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.Fragment;

import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

public class Netstat extends Fragment {
    public Netstat() {
    }
    private static final String ARG_SECTION_NUMBER ="" ;

    public static Netstat myinstance(int sectionNumber) {
        Netstat fragment = new Netstat();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    String output="";
    private ListView mainview;
    private ArrayAdapter mainadp;
    JSONObject appsJs = new JSONObject();
    JSONArray app_list = new JSONArray();
    final ArrayList<String> txtstr = new ArrayList<String>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.netstat, container,
                false);
        //setContentView(R.layout.netstat);

        //final TextView textmpid = (TextView) findViewById(R.id.textView2);
        mainview = (ListView) rootView.findViewById(R.id.list);


        // Button buttonLoadTrafficStats = (Button) findViewById(R.id.button1);
        // buttonLoadTrafficStats.setOnClickListener(new OnClickListener() {

        //public void onClick(View v) {
        output="";
        ActivityManager mActivityManager = (ActivityManager) getActivity().getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> mRunningProcess = mActivityManager.getRunningAppProcesses();
        int i = 1;
        int a;
        String b;
        int c;
        for (ActivityManager.RunningAppProcessInfo amProcess : mRunningProcess){
            //Get name, Pid, Uid of all running process
            System.out.println("Application: " +i+ " PID: " + amProcess.pid+ " (processName= " + amProcess.processName + " UID= "+amProcess.uid+")");
            Log.i("Application", (i++) + "PID: " + amProcess.pid + "(processName=" + amProcess.processName + "UID="+amProcess.uid+")");
            i++;
            //textmpid.setText("PID: "+ String.valueOf(amProcess.pid));
            a = amProcess.pid;
            b = String.valueOf(amProcess.pid);

            ArrayList<Connection> connections = new ArrayList<Connection>();
            //BufferedReader in = new BufferedReader(new FileReader("/proc/" + a + "/net/tcp6"));
            try {
                BufferedReader in = new BufferedReader(new FileReader("/proc/" + a + "/net/tcp"));
                String line = b;
                int z;
                while((line = in.readLine()) != null) {
                    System.out.println(" tcp Netstat line: " + line);
                    line = line.trim();
                    String[] fields = line.split("\\s+", 10);
                    int fieldn = 0;
                    for(String field : fields) {
                        System.out.println(" tcp Field " + (fieldn++) + ": [" + field + "]");
                        int m = fieldn;
                        String n = field;
                    }

                    if(fields[0].equals("sl")) {
                        continue;
                    }

                    Connection connection = new Connection();

                    String src[] = fields[1].split(":", 2);
                    String dst[] = fields[2].split(":", 2);
                    connection.src = getAddress(src[0]);
                    connection.spt = String.valueOf(getInt16(src[1]));

                    connection.dst = getAddress(dst[0]);

                    connection.dpt = String.valueOf(getInt16(dst[1]));

                    connection.uid = fields[7];

                    String appName= getAppName(Integer.parseInt(connection.uid));

                    connection.state = getstate(fields[3]);
                    JSONObject appsJ = new JSONObject();
                    appsJ.put("protocol","TCP4");
                    appsJ.put("source_ip",connection.src);
                    appsJ.put("source_port",connection.spt);
                    appsJ.put("remote_source",connection.dst);
                    appsJ.put("remote_port",connection.dpt);
                    appsJ.put("uid",connection.uid);
                    appsJ.put("appname",appName);
                    appsJ.put("state",connection.state);
                    
                    app_list.put(appsJ);
                    System.out.println("--------------TCP4 Json Array-------------------");
                    System.out.println(connection.uid);
                    System.out.println(app_list);


                    connections.add(connection);




                    output = "TCP4- Source IP : " + connection.src + " Source Port: " + connection.spt + " Remote IP : "+ connection.dst + " Remote Port: " + connection.dpt + " App UID: " + connection.uid + " App : " + appName + " State: " + connection.state +"\n";
                    txtstr.add(output);
                    //System.out.println(app_list);
                    Log.v("TakeAttendance","App list "+app_list);
                    System.out.println(appsJ);
                }
                in.close();
                //= connections;

                //textmpid.setText(output);
                in = new BufferedReader(new FileReader("/proc/" + a + "/net/tcp6"));
                //String line = b;
                //int z;
                while((line = in.readLine()) != null) {
                    System.out.println(" tcp6 Netstat line: " + line);
                    line = line.trim();
                    String[] fields = line.split("\\s+", 10);
                    int fieldn = 0;
                    for(String field : fields) {
                        System.out.println(" tcp6 Field " + (fieldn++) + ": [" + field + "]");
                        int m = fieldn;
                        String n = field;
                    }

                    if(fields[0].equals("sl")) {
                        continue;
                    }
                    Connection connection = new Connection();

                    String src[] = fields[1].split(":", 2);
                    String dst[] = fields[2].split(":", 2);
                    connection.src = getAddress6(src[0]);
                    connection.spt = String.valueOf(getInt16(src[1]));

                    connection.dst = getAddress6(dst[0]);

                    connection.dpt = String.valueOf(getInt16(dst[1]));

                    connection.uid = fields[7];

                    String appName= getAppName(Integer.parseInt(connection.uid));

                    connection.state = getstate(fields[3]);
                    JSONObject appsJ = new JSONObject();
                    appsJ.put("protocol","TCP6");
                    appsJ.put("source_ip",connection.src);
                    appsJ.put("source_port",connection.spt);
                    appsJ.put("remote_source",connection.dst);
                    appsJ.put("remote_port",connection.dpt);
                    appsJ.put("uid",connection.uid);
                    appsJ.put("appname",appName);
                    appsJ.put("state",connection.state);
                    app_list.put(appsJ);
                    System.out.println("--------------TCP6 Json Array-------------------");
                    System.out.println(connection.uid);
                    System.out.println(app_list);

                    connections.add(connection);




                    output = "TCP6- Source IP : " + connection.src + " Source Port: " + connection.spt + " Remote IP : "+ connection.dst + " Remote Port: " + connection.dpt + " App UID: " + connection.uid + " App : " + appName + " State: " + connection.state +"\n";
                    txtstr.add(output);
                }
                in.close();
                in = new BufferedReader(new FileReader("/proc/" + a + "/net/udp"));
                //String line = b;
                //int z;
                while((line = in.readLine()) != null) {
                    System.out.println(" udp4 Netstat line: " + line);
                    line = line.trim();
                    String[] fields = line.split("\\s+", 10);
                    int fieldn = 0;
                    for(String field : fields) {
                        System.out.println(" udp4 Field " + (fieldn++) + ": [" + field + "]");
                        int m = fieldn;
                        String n = field;
                    }

                    if(fields[0].equals("sl")) {
                        continue;
                    }
                    Connection connection = new Connection();

                    String src[] = fields[1].split(":", 2);
                    String dst[] = fields[2].split(":", 2);
                    connection.src = getAddress(src[0]);
                    connection.spt = String.valueOf(getInt16(src[1]));

                    connection.dst = getAddress(dst[0]);

                    connection.dpt = String.valueOf(getInt16(dst[1]));

                    connection.uid = fields[7];

                    String appName= getAppName(Integer.parseInt(connection.uid));

                    connection.state = getstate(fields[3]);
                    JSONObject appsJ = new JSONObject();
                    appsJ.put("protocol","UDP4");
                    appsJ.put("source_ip",connection.src);
                    appsJ.put("source_port",connection.spt);
                    appsJ.put("remote_source",connection.dst);
                    appsJ.put("remote_port",connection.dpt);
                    appsJ.put("uid",connection.uid);
                    appsJ.put("appname",appName);
                    appsJ.put("state",connection.state);
                    app_list.put(appsJ);
                    System.out.println("-------------- UDP4 Json Array-------------------");
                    System.out.println(connection.uid);
                    System.out.println(app_list);
                    connections.add(connection);




                    output = "UDP4- Source IP : " + connection.src + " Source Port: " + connection.spt + " Remote IP : "+ connection.dst + " Remote Port: " + connection.dpt + " App UID: " + connection.uid + " App : " + appName + " State: " + connection.state +"\n";
                    txtstr.add(output);
                }
                in.close();
                in = new BufferedReader(new FileReader("/proc/" + a + "/net/udp6"));
                //String line = b;
                //int z;
                while((line = in.readLine()) != null) {
                    System.out.println(" udp6 Netstat line: " + line);
                    line = line.trim();
                    String[] fields = line.split("\\s+", 10);
                    int fieldn = 0;
                    for(String field : fields) {
                        System.out.println(" udp6 Field " + (fieldn++) + ": [" + field + "]");
                        int m = fieldn;
                        String n = field;
                    }

                    if(fields[0].equals("sl")) {
                        continue;
                    }
                    Connection connection = new Connection();

                    String src[] = fields[1].split(":", 2);
                    String dst[] = fields[2].split(":", 2);
                    connection.src = getAddress6(src[0]);
                    connection.spt = String.valueOf(getInt16(src[1]));

                    connection.dst = getAddress6(dst[0]);

                    connection.dpt = String.valueOf(getInt16(dst[1]));

                    connection.uid = fields[7];

                    String appName= getAppName(Integer.parseInt(connection.uid));

                    connection.state = getstate(fields[3]);
                    JSONObject appsJ = new JSONObject();
                    appsJ.put("protocol","UDP6");
                    appsJ.put("source_ip",connection.src);
                    appsJ.put("source_port",connection.spt);
                    appsJ.put("remote_source",connection.dst);
                    appsJ.put("remote_port",connection.dpt);
                    appsJ.put("uid",connection.uid);
                    appsJ.put("appname",appName);
                    appsJ.put("state",connection.state);
                    app_list.put(appsJ);
                    System.out.println("--------------UDP6 Json Array-------------------");
                    System.out.println(connection.uid);
                    System.out.println(app_list);

                    connections.add(connection);




                    output = "UDP6- Source IP : " + connection.src + " Source Port: " + connection.spt + " Remote IP : "+ connection.dst + " Remote Port: " + connection.dpt + " App UID: " + connection.uid + " App : " + appName + " State: " + connection.state +"\n";
                    txtstr.add(output);
                }
                in.close();
                String deviceId = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                //appsJs.put("android "+ deviceId,app_list);
                appsJs.put("",app_list);
            }

            catch(Exception e) {
                System.out.println(" checknetlog() Exception: " + e.toString());
            }

        }

        //}

        //});
        mainadp =new ArrayAdapter<String>(getActivity(),R.layout.listelement,R.id.txt,txtstr);
        mainview.setAdapter(mainadp);
        //Log.v("TakeAttendance","JSONpARENT "+appsJs);
        System.out.println("------ Json---------------");
        //send data to mysql local server


        System.out.println(appsJs.toString());
        //save json to external file
        /*String content = appsJs.toString();
        FileWriter fw = null;
        try {
            fw = new FileWriter("sdcard/json.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/


        return rootView;

    }
    private String getAppName(int uid)
    {
        String processName = "";
        final PackageManager pm = getActivity().getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        for(ApplicationInfo info: packages){
            if(info.uid==uid){
                processName =info.loadLabel(pm).toString();
                break;
            }
        }
        return processName;
    }
    public class Connection {
        String src;
        String spt;
        String dst;
        String dpt;
        String uid;
        String state;
    }

    final String states[] = { "ESTABLISHED",   "SYN_SENT",   "SYN_RECV",   "F_WAIT1",   "F_WAIT2",   "TIME_WAIT",
            "CLOSED",    "CLOSED_WAIT",   "LAST_ACK",   "LISTEN",   "CLOSING",  "UNKNOWN"
    };
    //Mapping state from proc filesystem
    private final String getstate(String state) {
        try {
            if(state.equals("01")){
                return states[0];
            }
            else if(state.equals("02")){
                return states[1];
            }
            else if(state.equals("03")){
                return states[2];
            }
            else if(state.equals("04")){
                return states[3];
            }
            else if(state.equals("05")){
                return states[4];
            }
            else if(state.equals("06")){
                return states[5];
            }
            else if(state.equals("07")){
                return states[6];
            }
            else if(state.equals("08")){
                return states[7];
            }
            else if(state.equals("09")){
                return states[8];
            }
            else if(state.equals("0A")){
                return states[9];
            }else if(state.equals("0B")){
                return states[10];
            }else{
                return states[11];
            }
        }catch (Exception e){
            return "states Error";
        }

    }
    // IPv4 hexadecimal convert to decimal format
    private final String getAddress(final String hexa) {
        try {
            final long v = Long.parseLong(hexa, 16);
            final long adr = (v >>> 24) | (v << 24) |
                    ((v << 8) & 0x00FF0000) | ((v >> 8) & 0x0000FF00);
            return ((adr >> 24) & 0xff) + "." + ((adr >> 16) & 0xff) + "." + ((adr >> 8) & 0xff) + "." + (adr & 0xff);
        } catch(Exception e) {
            Log.w("NetworkLog", e.toString(), e);
            return "-1.-1.-1.-1";
        }
    }
    // IPv6 hexadecimal convert to decimal format
    private final String getAddress6(final String hexa) {
        try {
            final String ip4[] = hexa.split("0000000000000000FFFF0000");

            if(ip4.length == 2) {
                final long v = Long.parseLong(ip4[1], 16);
                final long adr = (v >>> 24) | (v << 24) |
                        ((v << 8) & 0x00FF0000) | ((v >> 8) & 0x0000FF00);
                return ((adr >> 24) & 0xff) + "." + ((adr >> 16) & 0xff) + "." + ((adr >> 8) & 0xff) + "." + (adr & 0xff);
            } else {
                return "-2.-2.-2.-2";
            }
        } catch(Exception e) {
            Log.w("NetworkLog", e.toString(), e);
            return "-1.-1.-1.-1";
        }
    }
    // port number hexadecimal convert to decimal format
    private final int getInt16(final String hexa) {
        try {
            return Integer.parseInt(hexa, 16);
        } catch(Exception e) {
            Log.w("NetworkLog", e.toString(), e);
            return -1;
        }
    }
}