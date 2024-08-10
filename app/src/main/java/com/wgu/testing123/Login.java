package com.wgu.testing123;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Login extends AppCompatActivity {
    dbHelper dbHelper;
    EditText username;
    EditText password;
    Button loginButton;
    TextView signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.loginMain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            dbHelper = new dbHelper(this);

            if (!(dbHelper.getAuthCount() == null) && (dbHelper.checkIfLoggedIn())){
                Intent intent = new Intent(Login.this, MainActivity.class);
                intent.putExtra("Username", dbHelper.getAuthCount());
                startActivity(intent);
            }
            username = findViewById(R.id.editTextUsername);
            password = findViewById(R.id.editTextTextPassword);
            loginButton = findViewById(R.id.loginButton);
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    validateUserAndPass(username.getText().toString(), password.getText().toString());

                }
            });

            signUp = findViewById(R.id.addDispatchSignUp);
            signUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Load add dispatch page
                    Intent intent = new Intent(Login.this, addDispatcher.class);
                    startActivity(intent);
                }
            });
            return insets;
        });


    }
    public void validateUserAndPass(String user, String pass){
        if (dbHelper.validateCredentials(user, pass)){
           Intent intent = new Intent(Login.this, MainActivity.class);
           dbHelper.updateAuth(user, 1);
           intent.putExtra("Username", user);

           intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
           startActivity(intent);
        }
        else{
            Toast.makeText(this, "Failed Login Attempt", Toast.LENGTH_SHORT).show();
        }
    }
}