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

public class update_item extends AppCompatActivity {
    dbHelper db = new dbHelper(this);
    private int ID;
    private String item;
    private String description;
    private int quantity;
    private int workToday;
    EditText itemEditText;
    EditText descriptionEditText;
    EditText quantityEditText;
    DriverAdapter driverAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_item);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            itemEditText = findViewById(R.id.updateItemName);
            descriptionEditText = findViewById(R.id.updateItemDescription);
            quantityEditText = findViewById(R.id.updateItemQuantity);

            ID = extras.getInt("ID");
            item = extras.getString("Item");
            description = extras.getString("Description");
            quantity = extras.getInt("Quantity");


            itemEditText.setText(item);
            descriptionEditText.setText(description);
            quantityEditText.setText(String.valueOf(quantity));

            return insets;
        });

        //Save Button
        TextView saveAddDriver = findViewById(R.id.saveUpdateItem);

        saveAddDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //EditText workToday = findViewById(R.id.addDriverNote);


                if (itemEditText.getText().toString().isEmpty() || descriptionEditText.getText().toString().isEmpty() || quantityEditText.getText().toString().isEmpty()){
                    Toast.makeText(update_item.this, "One or more fields are empty", Toast.LENGTH_LONG).show();
                }else {
                    db.updateItem(ID, itemEditText.getText().toString(), descriptionEditText.getText().toString(), Integer.valueOf(quantityEditText.getText().toString()));
                    onRestart();
                    finish();
                }
                //db.insertDriver(driverFirstName.getText().toString(), driverLastName.getText().toString(), driverPhoneNumber.getText().toString(), 0);
                //driverAdapter.notifyDataSetChanged();

            }
        });

        //Cancel Button
        ImageView exitAddDriver = findViewById(R.id.exitUpdateItem);

        exitAddDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRestart();
                finish();
            }
        });
    }
}