package com.wgu.testing123;

import static com.wgu.testing123.databaseFragment.itemAdapter;
import static com.wgu.testing123.databaseFragment.itemList;

import android.database.Cursor;
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

import java.util.ArrayList;
import java.util.List;

public class addItem extends AppCompatActivity {

    dbHelper db = new dbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_item);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;


        });

        //Save Button
        TextView saveAdditem = findViewById(R.id.saveAddItem);

        saveAdditem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText itemName = findViewById(R.id.addItemName);
                EditText itemDescription = findViewById(R.id.addItemDescription);
                EditText itemQuantity = findViewById(R.id.addItemQuantity);

                //int quantity = Integer.parseInt(itemQuantity.getText().toString());
                if (itemName.getText().toString().isEmpty() || itemDescription.getText().toString().isEmpty() || itemQuantity.getText().toString().isEmpty()){
                    Toast.makeText(addItem.this, "One or more fields are empty", Toast.LENGTH_LONG).show();
                }else {

                    String text = db.insertItem(itemName.getText().toString(), itemDescription.getText().toString(), Integer.parseInt(itemQuantity.getText().toString()));
                        // if (!(db.getDispatchIdFromUser(dispatcherUsername.getText().toString())== -1)){
                        //Dispatch dispatch = new Dispatch(db.getDispatchIdFromUser(dispatcherUsername.getText().toString()),dispatcherFirstName.getText().toString(), dispatcherLastName.getText().toString(), dispatcherUsername.getText().toString(), dispatcherPass.getText().toString());
                        ArrayList<Item> items = new ArrayList<Item>();
                        items = (db.getAllItemsArray());

                        //ISSUE WITH LAST INDEX OF
                        //int indexOfLast = items.lastIndexOf(items);
                        Item itemX = new Item(items.get(items.size()-1).getID(), itemName.getText().toString(), itemDescription.getText().toString(), Integer.parseInt(itemQuantity.getText().toString()));
                        if (itemList == null) {
                            itemList = new ArrayList<Item>();

                        }
                        itemList.add(itemX);
                        itemAdapter.setData(generateItemList());
                        itemAdapter.notifyDataSetChanged();
                        //}


                    onRestart();
                    finish();
                }
            }
        });

        //Cancel Button
        ImageView exitAddItem = findViewById(R.id.exitAddItem);

        exitAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRestart();
                finish();
            }
        });
    }
        public List<Item> generateItemList() {
            db = new dbHelper(this);
            Cursor result = db.getAllItems();
            List<Item> items = new ArrayList<>();

            if (result.getCount() == 0) {
                Toast.makeText(this, "No Item Found", Toast.LENGTH_SHORT).show();
                return null;
            } else {
                while (result.moveToNext()) {
                    Item item = new Item(result.getInt(0), result.getString(1), result.getString(2), result.getInt(3));

                    items.add(item);
                    //Toast.makeText(getActivity(), result.getString(0), Toast.LENGTH_SHORT).show();
                }
            }
            return items;
        }
}