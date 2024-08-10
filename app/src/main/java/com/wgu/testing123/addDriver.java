package com.wgu.testing123;

import static com.wgu.testing123.databaseFragment.driverAdapter;
import static com.wgu.testing123.databaseFragment.driverList;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class addDriver extends AppCompatActivity {
    dbHelper db = new dbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_driver);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);



            return insets;
        });

        //Save Button
        TextView saveAddDriver = findViewById(R.id.saveAddDriver);

        saveAddDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText driverFirstName = findViewById(R.id.addFirstNameDriver);
                EditText driverLastName = findViewById(R.id.addLastNameDriver);
                EditText driverPhoneNumber = findViewById(R.id.addPhoneNumberDriver);
                //EditText workToday = findViewById(R.id.addDriverNote);

                if (driverLastName.getText().toString().isEmpty() || driverLastName.getText().toString().isEmpty() || driverPhoneNumber.getText().toString().isEmpty()){
                    Toast.makeText(addDriver.this, "One or more fields are empty", Toast.LENGTH_LONG).show();
                }else {
                    db.insertDriver(driverFirstName.getText().toString(), driverLastName.getText().toString(), driverPhoneNumber.getText().toString(), 0);
                    ArrayList<Driver> driversListTest = new ArrayList<Driver>();
                    driversListTest = db.getAllDriversArray();

                    Driver driverTest = new Driver(driversListTest.get(driversListTest.size()-1).getId(), driverFirstName.getText().toString(), driverLastName.getText().toString(), driverPhoneNumber.getText().toString(), 0);
                    if (driverList == null){
                        driverList = new ArrayList<Driver>();
                    };

                    driverList.add(driverTest);
                    driverAdapter.setData(generateDriverList());
                    driverAdapter.notifyDataSetChanged();
                    onRestart();
                    finish();
                }

            }
        });

        //Cancel Button
        ImageView exitAddDriver = findViewById(R.id.exitAddDriver);

        exitAddDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onRestart();
                finish();
            }
        });
    }
    public List<Driver> generateDriverList() {
        db = new dbHelper(this);
        Cursor result = db.getAllDrivers();
        List<Driver> drivers = new ArrayList<>();

        if (result.getCount() == 0) {
            Toast.makeText(this, "No driver found", Toast.LENGTH_SHORT).show();
            return null;
        } else {
            while (result.moveToNext()) {
                Driver driver = new Driver(result.getInt(0), result.getString(1), result.getString(2), result.getString(3), result.getInt(4));

                drivers.add(driver);
                //Toast.makeText(getActivity(), result.getString(0), Toast.LENGTH_SHORT).show();
            }
        }
        return drivers;
    }
}