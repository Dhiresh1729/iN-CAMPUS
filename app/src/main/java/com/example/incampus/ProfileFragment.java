package com.example.incampus;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View rootView  = inflater.inflate(R.layout.fragment_profile, container, false);

        ImageView imageView = rootView.findViewById(R.id.profilePic);
        TextView userName = rootView.findViewById(R.id.userName);
        TextView firstName = rootView.findViewById(R.id.firstName);
        TextView lastName = rootView.findViewById(R.id.lastName);
        TextView emailId = rootView.findViewById(R.id.emailId);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference("uploads/" + MainActivity.user_Name);
        try {
            File localFile = File.createTempFile("tempfile", "jpeg");
            storageReference.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            imageView.setImageBitmap(bitmap);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            imageView.setImageBitmap(bitmap);

                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        userName.setText(MainActivity.user_Name);
        firstName.setText(MainActivity.first_Name);
        lastName.setText(MainActivity.last_Name);
        emailId.setText(MainActivity.email_Id);


        return rootView;
    }

}
