package com.example.incampus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogIn extends AppCompatActivity {

    private EditText userName, password;
    private Button loginBtn;
    private TextView toggleLogin, forgotPassword;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private String uemail, fname, lname, uname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView btn = findViewById(R.id.txtToggleLogin);

        userName = findViewById(R.id.edtUserNameEmail);
        password = findViewById(R.id.edtPassword);
        loginBtn = findViewById(R.id.btnLogin);
        toggleLogin = findViewById(R.id.txtToggleLogin);
        forgotPassword = findViewById(R.id.txtForgotPassword);
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();

        toggleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LogIn.this, SignUp.class);
                startActivity(i);
                finish();
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("DEBUG68");
                String username = new String("");
                String pswd = new String("");
                System.out.println("DEBUG71");
                username = userName.getText().toString();
                pswd = password.getText().toString();
                System.out.println("DEBUG74");
                if(TextUtils.isEmpty(username))
                    System.out.println("UserName can't be empty");

                else if(TextUtils.isEmpty(pswd))
                    System.out.println("Password can't be empty");
                else{
                    System.out.println("DEBUG81");
                        String finalPswd = pswd;
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference databaseReference = firebaseDatabase.getReference("users");
                    String finalUsername = username;
                    System.out.println("DEBUG86");
                    String finalUsername1 = username;
                    databaseReference.orderByChild("userName").equalTo(finalUsername).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot childDataSnapshot: snapshot.getChildren()){
                                    System.out.println("DEBUG91");
                                    String emailId = (String) childDataSnapshot.child("emailId").getValue();
                                    String user_name = (String) childDataSnapshot.child("userName").getValue();
                                    String first_name = (String) childDataSnapshot.child("firstName").getValue();
                                    String last_name = (String) childDataSnapshot.child("lastName").getValue();
                                    System.out.println("DEBUG96");
                                    System.out.println("username: "+ user_name);
                                    System.out.println("firstname: " + first_name);
                                    System.out.println("lastname: " + last_name);
                                    System.out.println("email: " + emailId);
                                    System.out.println("DEBUG101");
                                    mAuth.signInWithEmailAndPassword(emailId, finalPswd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {

                                            if(finalUsername1.equals("Security")){
                                                Intent i = new Intent(LogIn.this, SecurityActivity.class);
                                                startActivity(i);
                                                finish();
                                            }
                                            else{
                                                Toast.makeText(LogIn.this, "User LoggedIn Successfully", Toast.LENGTH_SHORT).show();
                                                Intent i = new Intent(LogIn.this, MainActivity.class);
                                                startActivity(i);
                                                finish();
                                            }

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                System.out.println("ERROR!!: " + error.toString());
                            }
                        });

                }
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText resetMail = new EditText(view.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
                passwordResetDialog.setTitle("Rest Password ? ");
                passwordResetDialog.setMessage("Enter your Email to receive Reset Link");
                passwordResetDialog.setView(resetMail);


                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String mail = resetMail.getText().toString();
                        mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(LogIn.this, "Reset Link sent to your Email", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LogIn.this, "Error ! Reset Link is not sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                passwordResetDialog.create().show();

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){

            if(firebaseUser.getEmail().equals("nonuser1729@gmail.com")){
                Intent i = new Intent(LogIn.this, SecurityActivity.class);
                startActivity(i);
                finish();
            }
            else{

                Intent i = new Intent(LogIn.this, MainActivity.class);
                startActivity(i);
                finish();
            }

        }
    }
}