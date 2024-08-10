package com.wgu.testing123;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class update_driver extends AppCompatActivity{
    dbHelper db = new dbHelper(this);
    private int ID;
    private String firstName;
    private String lastName;
    private String phone;
    private int workToday;
    EditText driverFirstName;
    EditText driverLastName;
    EditText driverPhoneNumber;
    DriverAdapter driverAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_driver);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            driverFirstName = findViewById(R.id.updateFirstNameDriver);
            driverLastName = findViewById(R.id.updateLastNameDriver);
            driverPhoneNumber = findViewById(R.id.updatePhoneNumberDriver);

            ID = extras.getInt("DriverID");
            firstName = extras.getString("DriverFirst");
            lastName = extras.getString("DriverLast");
            phone = extras.getString("DriverPhone");
            workToday = extras.getInt("DriverWorkToday");

            driverFirstName.setText(firstName);
            driverLastName.setText(lastName);
            driverPhoneNumber.setText(phone);

            return insets;
        });

        //Save Button
        TextView saveAddDriver = findViewById(R.id.saveUpdateDriver);

        saveAddDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //EditText workToday = findViewById(R.id.addDriverNote);


                if (driverFirstName.getText().toString().isEmpty() || driverLastName.getText().toString().isEmpty() || driverPhoneNumber.getText().toString().isEmpty()){
                    Toast.makeText(update_driver.this, "One or more fields are empty", Toast.LENGTH_LONG).show();
                }else {
                    db.updateDriver(ID, driverFirstName.getText().toString(), driverLastName.getText().toString(), driverPhoneNumber.getText().toString(), workToday);
                    onRestart();
                    finish();
                }
                //db.insertDriver(driverFirstName.getText().toString(), driverLastName.getText().toString(), driverPhoneNumber.getText().toString(), 0);
                //driverAdapter.notifyDataSetChanged();

            }
        });

        //Cancel Button
        ImageView exitAddDriver = findViewById(R.id.exitUpdateDriver);

        exitAddDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRestart();
                finish();
            }
        });

    }
}