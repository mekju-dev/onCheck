package com.wgu.testing123;

import static com.wgu.testing123.TodaysDriversFragment.driverAdapter;
import static com.wgu.testing123.TodaysDriversFragment.driverList;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class selectDrivers extends AppCompatActivity implements TodayDriverRecyclerViewInterface, ItemRecyclerViewInterface, View.OnClickListener{
    dbHelper db = new dbHelper(this);
    ArrayList<Driver> allDrivers = new ArrayList<Driver>();
    ArrayList<Driver> allDriverID = new ArrayList<Driver>();
    //ArrayList<String> allItems = new ArrayList<String>();
    ArrayList<Item> allItems = new ArrayList<Item>();
    List<Integer> driversIDs = new ArrayList<>();
    List<String> driverNames  = new ArrayList<>();
    List<String> itemNames  = new ArrayList<>();
    List<Integer> itemIDList;
    ArrayList<Item> itemArrayList = new ArrayList<>();
    AutoCompleteTextView driversAutoComplete, itemsAutoComplete, itemsAutoComplete2, itemsAutoComplete3, itemsAutoComplete4, itemsAutoComplete5;
    DriverAdapter adapter;
    List<Driver> tempList = new ArrayList<Driver>();

    List<Item> tempItemList = new ArrayList<Item>();
    private ArrayAdapter<Driver> driverAdapter1;
    private ArrayAdapter<Item> arrayAdapter;
    private DriverAdapter driverAdapter1Test;
    Item selectedItem;
    int countOfItems;
    Driver selectedDriver;
    Button addItem, addAllItem;
    TextView saveSelectedDriverButton;
    LinearLayout layoutList;
    final ArrayList<Item> items = new ArrayList<Item>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_drivers);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            layoutList = findViewById(R.id.layoutListSD);
            addItem = findViewById(R.id.addItemButtonSelectedDrivers);
            addAllItem = findViewById(R.id.addAllItemButtonSelectedDrivers);
            addItem.setOnClickListener(this);
            addAllItem.setOnClickListener(this);
            saveSelectedDriverButton = findViewById(R.id.saveSelectDriver);
            saveSelectedDriverButton.setOnClickListener(this);
            allItems = db.getAllItemsArray();
            for (Driver driver : allDrivers) {
                driversIDs.add(driver.getId());
            }
            for (Driver driver : allDrivers){
                driverNames.add(driver.toString());
            }//could potentially and an index list to each to keep track later
            for (Item item: allItems){
                itemNames.add(item.getItem());
            }
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            return insets;
        });


        allDrivers = db.getAllDriversArray();;
        List<Driver> driversList = new ArrayList<Driver>();
        driversList = allDrivers;
        countOfItems = 0;

        // driverAdapter1Test.setData(driversList);
        //driverAdapter1Test = new DriverAdapter(allDrivers, this);
        //allDriver = db.getAllDriverNames().toArray(new String[0]);
        driversAutoComplete = findViewById(R.id.selectDriver);
        TextInputLayout selectDriverList = findViewById(R.id.selectDriverList);

        // = new DriverAdapter(allDrivers, this);
        driverAdapter1 = new ArrayAdapter<Driver>(this, android.R.layout.simple_dropdown_item_1line, allDrivers);
        //android.R.layout.simple_dropdown_item_1line
        driversAutoComplete.setThreshold(0);
        driversAutoComplete.setAdapter(driverAdapter1);

/*
driversAutoComplete.addTextChangedListener(new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {filterList(String.valueOf(s));

    }
});

 */


        driversAutoComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                driversAutoComplete.showDropDown();
                /*
                driversAutoComplete.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        List<Driver> filteredList = new ArrayList<>();
                        for (Driver driver : allDrivers){
                            if (driver.getFirstName().toLowerCase().contains(text.toLowerCase()) || driver.getLastName().toLowerCase().contains(text.toLowerCase())){
                                filteredList.add(driver);
                            }
                        }
                        if (filteredList.isEmpty()){

                        }
                        else{
                            driverAdapter1.setFilteredList(filteredList);
                            tempList = filteredList;
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                 */

            }

        });
        driversAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String driverSelected = parent.getItemAtPosition(position).toString();
                //String driverSelected = String.valueOf(parent.getSelectedItemPosition());
                //driverSelectedPos = position


                selectedDriver = driverAdapter1.getItem(position);
                //Toast.makeText(selectDrivers.this, driverSelected, Toast.LENGTH_SHORT).show();
            }
        });

        //Save Button




                ImageView exitAddDriver = findViewById(R.id.exitSelectDriver);

                exitAddDriver.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        onRestart();
                        finish();
                    }

                });

    }

    public String getTodayDate(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        month++;
        return month + "/" + day + "/" + year;
    }


    /**
     * @param driver
     */
    @Override
    public void onDriverClick(Driver driver) {

    }

    /**
     * @param driver
     */
    @Override
    public void onDriverHold(Driver driver) {

    }

    /**
     * @param item
     */
    @Override
    public void onItemClick(Item item) {

    }

    /**
     * @param item
     */
    @Override
    public void onItemHold(Item item) {

    }

    /**
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.addItemButtonSelectedDrivers) {

            if (layoutList.getChildCount() < 5){
            addView();
            }

            else {
                Toast.makeText(this, "Limit of 5 items per person", Toast.LENGTH_SHORT).show();
            }
        }

        else if (v.getId() == R.id.addAllItemButtonSelectedDrivers){
            addAllView();
        }

        else if (v.getId() == R.id.saveSelectDriver) {

            if (checkIfValid()) {
                //arrayAdapter.notifyDataSetChanged();

                //TodaysOutstandingFragment.itemAdapter = loanAdapter;
                //TodaysOutstandingFragment.itemAdapter.notifyDataSetChanged();
                driverAdapter1.notifyDataSetChanged();
                Toast.makeText(this, "Driver added to today", Toast.LENGTH_SHORT).show();
                onRestart();
                finish();
            }
        }

    }

    //Validating select driver with items for the day
    private boolean checkIfValid(){
        Driver driver = new Driver();
        List<Item> itemsVal = new ArrayList<>();
        List<Integer> itemsQuantity = new ArrayList<>();
        Boolean temp = true;
        //items = generateItemsFromDB();
        if (!driverNames.contains(driversAutoComplete.getText().toString())){
            Toast.makeText(this, "Driver Name Invalid", Toast.LENGTH_SHORT).show();
            temp = false;
        }
        else if (countOfItems == 0){
            Toast.makeText(this, "Please add an Item", Toast.LENGTH_SHORT).show();
            temp = false;
        }
        else {
            for (int i = 0; i < layoutList.getChildCount(); i++) {
                View itemView = layoutList.getChildAt(i);
                AutoCompleteTextView editText = (AutoCompleteTextView) itemView.findViewById(R.id.itemSelectedRowText);
                EditText itemQuantity = (EditText) itemView.findViewById(R.id.quantitySelectedRowText);

                if (items.isEmpty()){
                    Toast.makeText(this, "Please add valid item", Toast.LENGTH_SHORT).show();
                    temp = false;
                }
                else if (itemQuantity.getText() == null){
                    Toast.makeText(this, "Please add valid quantity", Toast.LENGTH_SHORT).show();
                    temp = false;
                }

                    else if (!itemNames.contains(editText.getText().toString())) {
                        Toast.makeText(this, "Item Invalid", Toast.LENGTH_SHORT).show();
                        temp = false;
                    }
                    else if (itemQuantity.getText().toString().isEmpty()){
                    Toast.makeText(this, "Please fill item quantity", Toast.LENGTH_SHORT).show();
                    temp = false;
                }
                    else if (itemQuantity.getText().toString() == "0"){
                    Toast.makeText(this, "Item quantity cannot be 0", Toast.LENGTH_SHORT).show();
                    temp = false;
                }
                    else if (items.get(i).getQuantity() < Integer.parseInt(itemQuantity.getText().toString())) {
                        Toast.makeText(this, "Not enough items in inventory", Toast.LENGTH_SHORT).show();
                        temp = false;
                    }
                    else {

                    itemsVal.add(i, items.get(i));
                    itemsQuantity.add(i, Integer.parseInt(itemQuantity.getText().toString()));
                    temp = true;
                    //HERE ADD TODAYOUTSTANDINGADAPTER , TODAYDRIVERFRAGMENTADAPTER NOTIFYCHANGE
                }
            }
        }
        List<Loan> loanList = new ArrayList<Loan>();
        if (temp){
            Driver theDriver = new Driver(selectedDriver.getId(),selectedDriver.getFirstName(),selectedDriver.getLastName(),selectedDriver.getPhone(),1);
            db.updateDriver(selectedDriver.getId(),selectedDriver.getFirstName(),selectedDriver.getLastName(),selectedDriver.getPhone(),1);
            for (int index  = 0; index < itemsVal.size(); index++) {
                Item ite = itemsVal.get(index);
                String filler = "ite.getItem(), ite.getID(), itemsQuantity.get(index), selectedDriver.getId(), selectedDriver.firstName + ' ' + selectedDriver.lastName, getTodayDate()";

                db.insertLoan(ite.getItem(), ite.getID(), itemsQuantity.get(index), selectedDriver.getId(), selectedDriver.firstName + " " + selectedDriver.lastName, getTodayDate());
                //Loan loanTest = db.getLoanFromData(ite.getItem(), itemsQuantity.get(index), selectedDriver.firstName + " " + selectedDriver.lastName, getTodayDate());
                //loanList.add(loanTest);

                db.updateItemQuantity(ite.getID(), ite.getQuantity() - itemsQuantity.get(index));

                arrayAdapter.add(ite);
                arrayAdapter.notifyDataSetChanged();
            }
            if (driverList == null){
                driverList = new ArrayList<Driver>();
            }

           //itemAdapter.setData(loanList);
           //itemAdapter.notifyDataSetChanged();
           driverList.add(theDriver);
           driverAdapter.setData(driverList);
           driverAdapter.notifyDataSetChanged();


        }
        return temp;
    }


private void addView() {
        final View itemView = getLayoutInflater().inflate(R.layout.row_add_selected_item, null, false);

        AutoCompleteTextView itemName = (AutoCompleteTextView) itemView.findViewById(R.id.itemSelectedRowText);
        EditText itemQuantity = (EditText) itemView.findViewById(R.id.quantitySelectedRowText);
        EditText removeItem = (EditText) itemView.findViewById(R.id.removeItemSelectRow);

        /*
        for (Item item:allItems){
            items.add(item);
        }

         */

    //THE onItemClickListener is only being applied once, to the first element
        arrayAdapter = new ArrayAdapter<Item>(this, android.R.layout.simple_dropdown_item_1line, allItems);
        //itemName.setAdapter(arrayAdapter);
        //itemName = findViewById(R.id.selectItem);
        itemName.setThreshold(0);
        itemName.setAdapter(arrayAdapter);

        itemName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                itemName.showDropDown();

            }
        });
        itemName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = arrayAdapter.getItem(position);
                items.add(selectedItem);
            }
        });

        removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView(itemView);
            }
        });

        Item test = new Item();
        layoutList.addView(itemView);
       // itemArrayList.add(countOfItems, test);
        countOfItems++;
    }
    private void removeView(View view){
        layoutList.removeView(view);
        //itemArrayList.remove(countOfItems);
        countOfItems--;
    }
    private void addAllView() {

        for (Item item : allItems) {
            final View itemView = getLayoutInflater().inflate(R.layout.row_add_selected_item, null, false);

            AutoCompleteTextView itemName = (AutoCompleteTextView) itemView.findViewById(R.id.itemSelectedRowText);
            EditText itemQuantity = (EditText) itemView.findViewById(R.id.quantitySelectedRowText);
            EditText removeItem = (EditText) itemView.findViewById(R.id.removeItemSelectRow);

            arrayAdapter = new ArrayAdapter<Item>(this, android.R.layout.simple_dropdown_item_1line, allItems);
            itemName.setThreshold(0);
            itemName.setAdapter(arrayAdapter);

            itemName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    itemName.showDropDown();

                }
            });
            itemName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    selectedItem = arrayAdapter.getItem(position);
                    items.add(selectedItem);
                }
            });

            //AutoFill All Items
            itemName.setText(item.getItem());
            if (item.getQuantity() < 1){
                itemQuantity.setText("0");
                Toast.makeText(this, "Not enough " + item.getItem(), Toast.LENGTH_SHORT).show();
            }
            else {
                itemQuantity.setText("1");
            }
            items.add(item);

            removeItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeView(itemView);
                    items.remove(item);
                }
            });
            Item test = new Item();
            layoutList.addView(itemView);
            // itemArrayList.add(countOfItems, test);
            countOfItems++;
        }
    }
}