package com.example.incampus;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Scanner#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Scanner extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Scanner() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Scanner.
     */
    // TODO: Rename and change types and number of parameters
    public static Scanner newInstance(String param1, String param2) {
        Scanner fragment = new Scanner();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_scanner, container, false);

        Button scanBtn = root.findViewById(R.id.scanBtn);
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanBarcode();
//                IntentIntegrator intentIntegrator = new IntentIntegrator(getActivity());
//                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
//                intentIntegrator.setCameraId(0);
//                intentIntegrator.setOrientationLocked(false);
//                intentIntegrator.setPrompt("scanning");
//                intentIntegrator.setBeepEnabled(true);
//                intentIntegrator.setBarcodeImageEnabled(true);
//                intentIntegrator.initiateScan();
            }
        });

        return root;
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        System.out.println("DEBUG93");
//        InsertLogs insertLogs = new InsertLogs();
//        System.out.println("DEBUG95");
//        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//        System.out.println("DEBUG97");
//
//        if(result != null && result.getContents() != null){
//            System.out.println("DEBUG100");
//            String username = result.getContents();
//            System.out.println("user_name: "  +  username);
//            username = username.replace("http://", "");
//            String finalUsername = username;
//            new AlertDialog.Builder(getContext())
//                    .setTitle("USERNAME SCANNED")
//                    .setMessage(username)
//                    .setPositiveButton("Entry", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            insertLogs.insert_log(finalUsername, "Entry", getContext());
//                            dialogInterface.dismiss();
//                        }
//                    }).setNegativeButton("Exit", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    insertLogs.insert_log(finalUsername, "Exit", getContext());
//                    dialogInterface.dismiss();
//                }
//            }).create().show();
//            System.out.println("DEBUG121");
//        }
//        System.out.println("DEBUG123");
//        super.onActivityResult(requestCode, resultCode, data);
//        System.out.println("DEBUG125");
//    }

    public void scanBarcode(){
    }

}