package com.example.incampus;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnPxWidthModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class StudentCard extends AppCompatActivity {

    private Button logoutBtn, markBtn, scanBtn;
    public static EditText userName;
    private EditText exitEntry;
    private Button refreshBtn;
    public  static String [][]result;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_card);

        Toast.makeText(StudentCard.this, "Toamst", Toast.LENGTH_SHORT).show();

        userName = findViewById(R.id.userNameEdtTxt);
        exitEntry = findViewById(R.id.exitEntryEdtTxt);
        markBtn = findViewById(R.id.markBtn);
        scanBtn = findViewById(R.id.scanBtn);
        refreshBtn = findViewById(R.id.refreshBtn);



        markBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = userName.getText().toString();
                String exitentry = exitEntry.getText().toString();
               insert_log(username, exitentry);
        }});


        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(AdminActivity.this, QRScanner.class);
//                startActivity(i);
//                finish();
                IntentIntegrator intentIntegrator = new IntentIntegrator(StudentCard.this);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                intentIntegrator.setCameraId(0);
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.setPrompt("scanning");
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setBarcodeImageEnabled(true);
                intentIntegrator.initiateScan();


            }
        });

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAllLogs();
                String logs = SharedPrefManager.getInstance(getApplicationContext()).getKeyAllLogs();
                result = addRow(logs);
                Intent i = new Intent(StudentCard.this, StudentLogs.class);
                startActivity(i);
                finish();
            }
        });



    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if(result != null && result.getContents() != null){
            String username = result.getContents();
            username = username.replace("http://", "");
            String finalUsername = username;
            new AlertDialog.Builder(StudentCard.this)
                    .setTitle("USERNAME SCANNED")
                    .setMessage(username)
                    .setPositiveButton("Entry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            insert_log(finalUsername, "Entry");
                            dialogInterface.dismiss();
                        }
                    }).setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    insert_log(finalUsername, "Exit");
                    dialogInterface.dismiss();
                }
            }).create().show();

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void insert_log(String userName, String exitEntry) {
        String username = userName;
        String exit_entry = exitEntry;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(StudentCard.this, "user " + exit_entry + " marked", Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(StudentCard.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(StudentCard.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("time", formattedDate);
                params.put("exit_entry", exit_entry);
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }

    private String[][] addRow(String logs) {

//        System.out.println("DEBUG86/last_char: " + logs.charAt(logs.length() - 1));

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

    @Override
    public void onBackPressed() {
        // do something on back.
        Intent i = new Intent(StudentCard.this, Security.class);
        startActivity(i);
        finish();
    }

}
