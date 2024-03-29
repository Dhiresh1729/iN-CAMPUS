package com.example.incampus;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class StudentLogs extends AppCompatActivity {

    private Button refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_logs);

        refresh = findViewById(R.id.refreshBtn);

        String logs;
        getAllLogs();
        logs = SharedPrefManager.getInstance(this).getKeyAllLogs();

        TableView tableView = findViewById(R.id.studentLogs);
        String[] headers = {"Username ", "Date", "Time", "Exit/Entry"};

        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(getApplicationContext(), headers));
        if(!logs.equals(""))
            tableView.setDataAdapter(new SimpleTableDataAdapter(getApplicationContext(), addRow(logs)));


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAllLogs();
                String logs = SharedPrefManager.getInstance(StudentLogs.this).getKeyAllLogs();
                if(!logs.equals(""))
                    tableView.setDataAdapter(new SimpleTableDataAdapter(getApplicationContext(), addRow(logs)));

            }
        });
    }

    public void getAllLogs(){
        System.out.println("DEBUG142");
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_SHOW_ALL_USER_LOGS,
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
                            SharedPrefManager1.getInstance(getApplicationContext()).allUserLogs(logs);
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
                return params;
            }
        };
        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private String[][] addRow(String logs) {

        System.out.println("DEBUG86/last_char: " + logs.charAt(logs.length() - 1));

        StringTokenizer st = new StringTokenizer(logs, "\n");
        ArrayList<String> tempLogs = new ArrayList<String>();
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

            StringTokenizer sT = new StringTokenizer(tempLogs.get(i), " ");
            int j = 0;
            String tempStr = new String("");
            while(sT.hasMoreTokens() && j <= 4){

                if(j == 0 || j == 1)
                    arr[i][j] = sT.nextToken();

                else if(j == 2 || j == 3)
                    tempStr += sT.nextToken();

                else if(j == 4){
                    arr[i][j - 1] = sT.nextToken();
                    arr[i][j - 2] = tempStr;
                }

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

    @Override
    public void onBackPressed() {
        // do something on back.
        Intent i = new Intent(StudentLogs.this, StudentCard.class);
        startActivity(i);
        finish();
    }


}