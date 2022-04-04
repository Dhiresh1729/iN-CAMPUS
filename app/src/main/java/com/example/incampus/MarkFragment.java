package com.example.incampus;

import static com.example.incampus.MainActivity.email_Id;
import static com.example.incampus.MainActivity.first_Name;
import static com.example.incampus.MainActivity.last_Name;
import static com.example.incampus.MainActivity.user_Name;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class MarkFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_mark_exit_entry, container, false);



        String userName = user_Name;
        String firstName = first_Name;
        String lastName = last_Name;
        String email = email_Id;
        ImageView qrImage = rootView.findViewById(R.id.qrCode2);

//        Connection connect;
//        String ConnectionResult = new String("");
//        int cnt = 0;
//
//        MySQLConnection mySQLConnection = new MySQLConnection();
//        connect = mySQLConnection.getConnection();
//
//        if(connect != null){
//            String query1 = String.format("SELECT * FROM user WHERE user_id = %1$s;", userName);
//
//            try (Statement st1 = connect.createStatement()) {
//                ResultSet rs1 = st1.executeQuery(query1);
//
//                int count = 0;
//                while(rs1.next()){
//                    count++;
//                }
//
//                if(count == 0){
//                    System.out.println("INSERTING NEW USER TO THE DATABASE");
//
//                    String content = new String("");
//                    content = String.format("[ Username: %1$s\nFirst name: %2$s\nLast name: %3$s\nEmail: %4$s]", user_Name, first_Name, last_Name, email_Id);
//                    QRCodeWriter writer = new QRCodeWriter();
//                    try{
//
//
//                        String query2 = String.format("INSERT INTO user(user_id, first_name, last_name, email) VALUES(%1$s, %2$s, %3$s, %4$s)", userName, firstName, lastName, email);
//                        Statement st2 = connect.createStatement();
//                        ResultSet rs2 = st2.executeQuery(query2);
//                    } catch(WriterException e){
//                        e.printStackTrace();
//                    }
//
//                }
//            } catch (SQLException throwables) {
//                throwables.printStackTrace();
//            }
//
//        }
//        Connection con;

        String content = new String("");
        content = String.format("[\tUsername: %1$s,\n\tFirst Name: %2$s,\n\tLast Name: %3$s,\n\tEmail: %4$s\t]",userName, firstName, lastName, email);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 512, 512);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        qrImage.setImageBitmap(bmp);
//        Calendar c = Calendar.getInstance();
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String formattedDate = df.format(c.getTime());
//        System.out.println("CURRENT DATE TIME: " + formattedDate);

        return rootView;
    }

}
