package com.example.incampus;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
import de.codecrafters.tableview.model.TableColumnPxWidthModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class LogsFragment extends Fragment {

    Context thisContext;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View rootView =  inflater.inflate(R.layout.fragment_logs, container, false);
        Toast.makeText(getContext(), "Fragment reloaded", Toast.LENGTH_SHORT).show();

        thisContext = container.getContext();
//        getLogs(MainActivity.user_Name);
//        System.out.println("DEBUG51/USERNAME: " + MainActivity.user_Name);
//        final String[] logs = {SharedPrefManager.getInstance(thisContext).getKeyLogs()};
        final String[] logs = {""};

        TableView tableView = rootView.findViewById(R.id.userLogs);
        String[] headers = {"S.no", "Date", "Time", "Exit/Entry"};


        TableColumnPxWidthModel columnPxWidthModel = new TableColumnPxWidthModel( 4, 350);
        columnPxWidthModel.setColumnWidth(1, 350);
        columnPxWidthModel.setColumnWidth(0, 139);
        tableView.setColumnModel(columnPxWidthModel);

        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(getActivity().getApplicationContext(), headers));
        getLogs(MainActivity.user_Name);
        System.out.println("DEBUG70");
        System.out.println("USERNAME: " + MainActivity.user_Name);
        logs[0] = SharedPrefManager.getInstance(thisContext).getKeyLogs();


        if(!logs[0].equals(""))
            tableView.setDataAdapter(new SimpleTableDataAdapter(getActivity().getApplicationContext(), addRow(logs[0])));

        rootView.findViewById(R.id.refreshBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LogsFragment()).commit();
//                getLogs(MainActivity.user_Name);
//                System.out.println("DEBUG70");
//                System.out.println("USERNAME: " + MainActivity.user_Name);
//                logs[0] = SharedPrefManager.getInstance(thisContext).getKeyLogs();
//                if(!logs[0].equals(""))
//                    tableView.setDataAdapter(new SimpleTableDataAdapter(getActivity().getApplicationContext(), addRow(logs[0])));
            }
        });


//        refreshCotent(tableView);
        return rootView;
    }




//    private void refreshCotent(TableView tableView) {
//        getLogs(MainActivity.user_Name);
//        String logs = SharedPrefManager.getInstance(getActivity().getApplicationContext()).getKeyLogs();
//        String[] headers = {"S.no", "Date", "Time", "Exit/Entry"};
//        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(getActivity().getApplicationContext(), headers));
//        if(!logs.equals(""))
//            tableView.setDataAdapter(new SimpleTableDataAdapter(getActivity().getApplicationContext(), addRow(logs)));
//    }

    private String[][] addRow(String logs) {

        System.out.println("DEBUG86/last_char: " + logs.charAt(logs.length() - 1));

        StringTokenizer st = new StringTokenizer(logs, "\n");
        ArrayList<String>tempLogs = new ArrayList<String>();
        while(st.hasMoreTokens()){
            tempLogs.add(st.nextToken());
        }

        System.out.println("DEBUG90");
        System.out.println(tempLogs);

        int numRows = tempLogs.size();
        for(int i = 0; i < numRows; i++){
            System.out.println("LOG" + (i + 1) + ": " + tempLogs.get(i));
        }

        String [][] arr = new String[numRows][4];


        for(int i = 0; i < numRows; i++){

            arr[i][0] = String.valueOf(i + 1);
            int j = 1;
            StringTokenizer sT = new StringTokenizer(tempLogs.get(i), " ");
            String tempStr = new String("");
            while(sT.hasMoreTokens() && j <= 4){
                if(j == 1)
                    arr[i][j] = sT.nextToken();

                else if(j == 2)
                    tempStr += sT.nextToken();

                else if(j == 3){
                    tempStr += sT.nextToken();
                    arr[i][j - 1] = tempStr;
                }

                else
                    arr[i][j - 1] = sT.nextToken();

               j++;
            }
            System.out.println("\n");
        }

        System.out.println("DEBUG127");
        for(int i = 0; i < numRows; i++){
            for(int j = 0; j < 4; j++){
                System.out.print(arr[i][j] + " ");
            }
            System.out.println("\n");
        }

        System.out.println("DEBUG135");

        return arr;

    }

    public void getLogs(String username){
        System.out.println("DEBUG142");
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_SHOWLOGS,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println("DEBUG150");
                            System.out.println("DEBUG151/RESPONSE: " + response);
                            JSONObject jsonObject = new JSONObject(response);
                            System.out.println("DEBUG153");
                            JSONArray jsonArray = jsonObject.getJSONArray("logs");
                            System.out.println("DEBUG155");
                            String logs = new String("");
                            int len = jsonArray.length();
                            len -= 1;
                            for(int i = 0; i < jsonArray.length(); i++){
                                System.out.println("LOG" + (i + 1) + jsonArray.get(i));
                                if(i  != len)
                                logs += (jsonArray.get(i) + "\n");
                                else
                                    logs += jsonArray.get(i);
                            }
                            System.out.println("DEBUG161");
                            System.out.println("LOGS: " + logs);
                            SharedPrefManager.getInstance(getActivity().getApplicationContext()).userLogs(logs);
                            System.out.println("DEBUG165");

                        } catch (JSONException e) {
                            System.out.println("DEBUG166: " + "GOTCHA");
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String>params = new HashMap<>();
                params.put("user_name", MainActivity.user_Name);
                System.out.println("user_name: " + MainActivity.user_Name);
                return params;
            }
        };
        RequestHandler.getInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        System.out.println("DEBUG194/ON START");
//        System.out.println("user_name: " + MainActivity.user_Name);
//        getLogs(MainActivity.user_Name);
//    }


}
