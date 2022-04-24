package com.example.incampus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {

    private EditText firstName, lastName, userName, emailId, password, confirmPassword;
    private Button signUpBtn;
    private TextView toggleSignUp;
//    private Uri mImageUri;
//    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        userName = findViewById(R.id.editName);
        firstName = findViewById(R.id.edtFirstName);
        lastName = findViewById(R.id.edtLastName);
        emailId = findViewById(R.id.editCollegeEmail);
        password = findViewById(R.id.editPassword);
        confirmPassword = findViewById(R.id.editConfirmPassword);
        toggleSignUp = findViewById(R.id.toggleSignUp);
        signUpBtn = findViewById(R.id.btnSignUp);
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
//        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
//        mImageUri = Uri.parse("android.resource://com.example.incampus/drawable-anydpi/ic_dp.xml");


        toggleSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignUp.this, LogIn.class);
                startActivity(i);
                finish();
            }
        });

     signUpBtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             String username = new String("");
             String firstname = new String("");
             String lastname = new String("");
             String email = new String("");
             String pswd = new String("");
             String cnfPswd = new String("");
             boolean isValidEmail = false;

             username = userName.getText().toString();
             firstname = firstName.getText().toString();
             lastname = lastName.getText().toString();
             email = emailId.getText().toString().trim();
             pswd = password.getText().toString();
             cnfPswd = confirmPassword.getText().toString();



             if(!TextUtils.isEmpty(email)){

                 String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                         "[a-zA-Z0-9_+&*-]+)*@" +
                         "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                         "A-Z]{2,7}$";

                 Pattern pat = Pattern.compile(emailRegex);
                 isValidEmail =  pat.matcher(email).matches();

             }
             if(TextUtils.isEmpty(firstname)){
                 Toast.makeText(SignUp.this, "FirstName can't be empty", Toast.LENGTH_SHORT).show();
             }
             else if(TextUtils.isEmpty(lastname)){
                 Toast.makeText(SignUp.this, "LastName can't be empty", Toast.LENGTH_SHORT).show();
             }
             else if(TextUtils.isEmpty(username)){
                 Toast.makeText(SignUp.this, "username can't be empty", Toast.LENGTH_SHORT).show();
             }
             else if(TextUtils.isEmpty(email)){
                 Toast.makeText(SignUp.this, "email can't be empty", Toast.LENGTH_SHORT).show();
             }
             else if(!isValidEmail){
                 Toast.makeText(SignUp.this, "Not a valid username", Toast.LENGTH_SHORT).show();
             }
             else if(TextUtils.isEmpty(pswd)){
                 Toast.makeText(SignUp.this, "Password can't be empty", Toast.LENGTH_SHORT).show();
             }
             else if(!pswd.equals(cnfPswd)){
                 Toast.makeText(SignUp.this, "Password and Confirm password doesn't matches", Toast.LENGTH_SHORT).show();
             }
             else if(pswd.length() < 6){
                 Toast.makeText(SignUp.this, "Password length can't be less than 6 characters", Toast.LENGTH_SHORT).show();
             }
             else{

                 String finalFirstname = firstname;
                 String finalLastname = lastname;
                 String finalUsername = username;
                 String finalEmail = email;
                 String finalPswd = pswd;
                 String finalUsername1 = username;
                 mAuth.createUserWithEmailAndPassword(email, pswd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                         if(task.isSuccessful()){
                             User user = new User(finalFirstname, finalLastname, finalUsername, finalEmail);

                             FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                 @Override
                                 public void onComplete(@NonNull Task<Void> task) {

                                     if(finalUsername1.equals("Security")){
                                         Toast.makeText(SignUp.this, "Account created!!", Toast.LENGTH_SHORT).show();
                                         Intent i = new Intent(SignUp.this, StudentCard.class);
                                         startActivity(i);
                                         finish();
                                     }

//                                     uploadFile(finalUsername);
                                     Toast.makeText(SignUp.this, "Account created!!", Toast.LENGTH_SHORT).show();
                                     Intent i = new Intent(SignUp.this, MainActivity.class);
                                     startActivity(i);
                                     finish();

                                 }
                             });
                         }
                         else{
                             Log.i(String.valueOf(SignUp.this), "createUserWithEmail:failure", task.getException());
                             Toast.makeText(SignUp.this, "Error: cannot create user account:( " + task.getException() + " )", Toast.LENGTH_SHORT).show();
                         }
                     }
                 });
//
             }

         }
     });
    }

//    private void uploadFile(String username){
//        if (mImageUri != null){
//            StorageReference fileReference = mStorageRef.child(username);
//            fileReference.putFile(Uri.parse(mImageUri))
//                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            Toast.makeText(SignUp.this, "Upload Successful", Toast.LENGTH_SHORT).show();
//
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(SignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//
//                }
//            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                    double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
//
//                }
//            });
//        }
//
//        else{
//            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private String getFileExtension(Uri uri){
//        ContentResolver cR = getContentResolver();
//        MimeTypeMap mime = MimeTypeMap.getSingleton();
//        return mime.getExtensionFromMimeType(cR.getType(uri));
//    }
}

