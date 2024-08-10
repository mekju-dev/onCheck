package com.wgu.testing123;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class databaseFragment extends Fragment implements TodayDriverRecyclerViewInterface, ItemRecyclerViewInterface, DispatchRecyclerViewInterface, LoanRecyclerViewInterface, SwipeRefreshLayout.OnRefreshListener {

    RadioButton driverRadio, itemRadio, dispatchRadio, loanRadio;
    TextView result;
    Button btn;
    //driver recycler view variables
    String string = "";
    RecyclerView dbRecyclerView;
    dbHelper db;
    public static List<Driver> driverList;
    public static DriverAdapter driverAdapter;
    //item recycler view variables
    public static List<Item> itemList;
    public static ItemAdapter itemAdapter;
    //dispatch recycler view variables
    public static List<Dispatch> dispatchList;
    public static DispatchAdapter dispatchAdapter;

    //loan recycler view variables
    private List<Loan> loanList;
    private LoanAdapter loanAdapter;
    Button buttonAdd;
    Button buttonCanceldb;
    private SearchView searchView;
    SwipeRefreshLayout swipeRefreshLayout;
    Dispatch tempDispatch;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_database_drivers, container, false);

        //line divider for recycler views
        DividerItemDecoration divider =
                new DividerItemDecoration(this.getContext(),
                        DividerItemDecoration.VERTICAL);

        divider.setDrawable(ContextCompat.getDrawable(getContext(),
                R.drawable.line_divider));


        searchView = rootView.findViewById(R.id.dbSearchView);
        searchView.setIconifiedByDefault(true);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (driverRadio.isChecked()){
                    filteredDriverList(newText);}
                else if (itemRadio.isChecked()){
                    filteredItemList(newText);
                }
                else if (dispatchRadio.isChecked()){
                    filteredDispatchList(newText);
                }
                else if (loanRadio.isChecked()){
                    filteredLoanList(newText);
                }
                else {
                    Toast.makeText(rootView.getContext(), "ERROR", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });


        //radio work
        driverRadio = rootView.findViewById(R.id.driverRadioDB);
        itemRadio = rootView.findViewById(R.id.itemRadioDB);
        dispatchRadio = rootView.findViewById(R.id.dispatchRadioDB);
        loanRadio = rootView.findViewById(R.id.loanRadioDB);

        driverRadio.setChecked(true);
        //data for recycler views
        driverList = generateDriverList();
        itemList = generateItemList();
        dispatchList = generateDispatchList();
        loanList = generateLoanList();


        //setup recycler view
        dbRecyclerView = rootView.findViewById(R.id.driverRecyclerViewDB);
        dbRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dbRecyclerView.addItemDecoration(divider);
        driverAdapter = new DriverAdapter(driverList, this);
        itemAdapter = new ItemAdapter(itemList, this);
        dispatchAdapter = new DispatchAdapter(dispatchList, this);
        loanAdapter = new LoanAdapter(loanList, this);

        dbRecyclerView.setAdapter(driverAdapter);

        //on click listeners for radio buttons
        driverRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                driverList = generateDriverList();

                dbRecyclerView.setAdapter(driverAdapter);
                driverRadio.setChecked(true);
            }
        });
        itemRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemList = generateItemList();
                dbRecyclerView.setAdapter(itemAdapter);
                itemRadio.setChecked(true);
            }
        });
        dispatchRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchList = generateDispatchList();

                dbRecyclerView.setAdapter(dispatchAdapter);
                dispatchRadio.setChecked(true);
            }
        });
        loanRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loanList = generateLoanList();

                dbRecyclerView.setAdapter(loanAdapter);
                loanRadio.setChecked(true);
            }
        });

        buttonAdd = rootView.findViewById(R.id.addButtonDB);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialog();
            }
        });

        return rootView;

        //driverRadio  = (RadioButton) findViewById(R.id.driverRadioDB);
    }

    //Lists for searching for inv table
    private void filteredItemList(String text) {
        List<Item> filteredItemList = new ArrayList<>();
        for (Item item : itemList){
            if (item.getItem().toLowerCase().contains(text.toLowerCase()) || item.getDescription().toLowerCase().contains(text.toLowerCase())){
                filteredItemList.add(item);
            }
        }
        if (filteredItemList.isEmpty()){
            Toast.makeText(this.getContext(), "No data found", Toast.LENGTH_SHORT).show();
        }else{
            itemAdapter.setFilteredList(filteredItemList);
        }
    }
    //Lists for searching for driver table
    private void filteredDriverList(String text) {
        List<Driver> filteredDriverList = new ArrayList<>();
        for (Driver driver : driverList){
            if (driver.getFirstName().toLowerCase().contains(text.toLowerCase()) || driver.getLastName().toLowerCase().contains(text.toLowerCase()) || driver.getPhone().toLowerCase().contains(text.toLowerCase())){
                filteredDriverList.add(driver);
            }
        }
        if (filteredDriverList.isEmpty()){
            Toast.makeText(this.getContext(), "No data found", Toast.LENGTH_SHORT).show();
        }else{
            driverAdapter.setFilteredList(filteredDriverList);
        }
    }
    //Lists for searching for dispatch table
    private void filteredDispatchList(String text) {
        List<Dispatch> filteredDispatchList = new ArrayList<>();
        for (Dispatch dispatch : dispatchList){
            if (dispatch.getFirstName().toLowerCase().contains(text.toLowerCase()) || dispatch.getLastName().toLowerCase().contains(text.toLowerCase()) || dispatch.getUsername().toLowerCase().contains(text.toLowerCase())){
                filteredDispatchList.add(dispatch);
            }
        }
        if (filteredDispatchList.isEmpty()){
            Toast.makeText(this.getContext(), "No data found", Toast.LENGTH_SHORT).show();
        }else{
            dispatchAdapter.setFilteredList(filteredDispatchList);
        }
    }
    //Lists for searching for loan table
    private void filteredLoanList(String text) {
        List<Loan> filteredLoanList = new ArrayList<>();
        for (Loan loan : loanList){
            if (loan.getItem().toLowerCase().contains(text.toLowerCase()) || loan.getDriver().toLowerCase().contains(text.toLowerCase()) || loan.getDate().toLowerCase().contains(text.toLowerCase())){
                filteredLoanList.add(loan);
            }
        }
        if (filteredLoanList.isEmpty()){
            Toast.makeText(this.getContext(), "No data found", Toast.LENGTH_SHORT).show();
        }else{
            loanAdapter.setFilteredList(filteredLoanList);
        }
    }

    public List<Driver> generateDriverList() {
        db = new dbHelper(getActivity());
        Cursor result = db.getAllDrivers();
        List<Driver> drivers = new ArrayList<>();

        if (result.getCount() == 0) {
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

    public List<Item> generateItemList() {
        db = new dbHelper(getActivity());
        Cursor result = db.getAllItems();
        List<Item> items = new ArrayList<>();

        if (result.getCount() == 0) {
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

    public List<Dispatch> generateDispatchList() {
        db = new dbHelper(getActivity());
        Cursor result = db.getAllDispatchers();
        List<Dispatch> dispatchers = new ArrayList<>();

        if (result.getCount() == 0) {
            return null;
        } else {
            while (result.moveToNext()) {
                Dispatch dispatch = new Dispatch(result.getInt(0), result.getString(1), result.getString(2), result.getString(3), result.getString(4));

                dispatchers.add(dispatch);
                //Toast.makeText(getActivity(), result.getString(0), Toast.LENGTH_SHORT).show();
            }
        }
        return dispatchers;
    }

    private List<Loan> generateLoanList() {
        db = new dbHelper(getActivity());
        Cursor result = db.getAllLoans();
        List<Loan> loans = new ArrayList<>();

        if (result.getCount() == 0) {
            return null;
        } else {
            while (result.moveToNext()) {
                Loan loan = new Loan(result.getInt(0), result.getString(1), result.getInt(2), result.getInt(3), result.getString(4), result.getInt(5), result.getString(6));

                loans.add(loan);
                //Toast.makeText(getActivity(), result.getString(0), Toast.LENGTH_SHORT).show();
            }
        }
        return loans;
    }

    public void displayMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    private void showBottomDialog() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout_add);

        LinearLayout layoutAddItem = dialog.findViewById(R.id.layoutAddItem);
        LinearLayout layoutAddDriver = dialog.findViewById(R.id.layoutAddDriver);
        LinearLayout layoutAddDispatcher = dialog.findViewById(R.id.layoutAddDispatcher);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        layoutAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Intent intent = new Intent(getActivity(), addItem.class);
                startActivity(intent);
            }
        });

        layoutAddDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Intent intent = new Intent(getActivity(), addDriver.class);
                startActivity(intent);
            }
        });

        layoutAddDispatcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Intent intent = new Intent(getActivity(), addDispatcher.class);
                startActivity(intent);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }



    /**
     * @param driver
     */
    @Override
    public void onDriverClick(Driver driver) {

        buildDeleteDialog(driver);
    }

    /**
     * @param driver
     */
    @Override
    public void onDriverHold(Driver driver) {
        Intent intent = new Intent(getActivity(), update_driver.class);
        intent.putExtra("DriverID", driver.getId());
        intent.putExtra("DriverFirst", driver.getFirstName());
        intent.putExtra("DriverLast", driver.getLastName());
        intent.putExtra("DriverPhone", driver.getPhone());
        intent.putExtra("DriverWorkToday", driver.getWorkToday());

        startActivity(intent);
        //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ItemsFragment()).commit();
    }

    public void buildDeleteDialog(Driver driver){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);

        builder.setTitle("Delete driver");
        builder.setMessage("Are you sure you want to delete: " + driver.toString() + " from the database?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                int id = driver.getId();

                db.removeLoan(id);
                db.deleteDriver(id);

                driverList.remove(driver);
                driverAdapter.setData(driverList);
                driverAdapter.notifyDataSetChanged();

                dialogInterface.dismiss();
                Toast.makeText(getActivity(), driver.toString() + " and their belonging loans have been deleted", Toast.LENGTH_LONG).show();

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
            }
        });

        dialog.show();}

    /**
     * @param item
     */
    @Override
    public void onItemClick(Item item) {
        buildItemDeleteDialog(item);
    }

    /**
     * @param item
     */
    @Override
    public void onItemHold(Item item) {
        Intent intent = new Intent(getActivity(), update_item.class);
        intent.putExtra("ID", item.getID());
        intent.putExtra("Item", item.getItem());
        intent.putExtra("Description", item.getDescription());
        intent.putExtra("Quantity", item.getQuantity());

        startActivity(intent);
    }
    public void buildItemDeleteDialog(Item item){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        itemList = generateItemList();
        builder.setTitle("Delete item");
        builder.setMessage("Are you sure you want to delete: " + item.getItem() + " from the database?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                int id = item.getID();
                String name = item.getItem();

                db.deleteLoansFromItem(id);
                db.deleteItem(id);
                itemList.remove(item);
                itemAdapter.setData(generateItemList());
                itemAdapter.notifyDataSetChanged();
                dialogInterface.dismiss();
                Toast.makeText(getActivity(), name + " and loans with this item have been deleted", Toast.LENGTH_LONG).show();

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
            }
        });

        dialog.show();}

    /**
     * @param dispatch
     */
    @Override
    public void onDispatchClick(Dispatch dispatch) {
        buildDispatchDeleteDialog(dispatch);
    }
    public void buildDispatchDeleteDialog(Dispatch dispatch){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setTitle("Delete dispatcher:");
        String name = dispatch.getFirstName() + "   " + dispatch.getLastName();
        builder.setMessage("Are you sure you want to delete: " + name + " from the database?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                int id = dispatch.getId();

                db.deleteDispatcher(id);
                int checkIfLoggedIntoDispatcher = db.deleteAuthWhere(dispatch.getUsername());



                dispatchList.remove(dispatch);
                dispatchAdapter.setData(dispatchList);
                dispatchAdapter.notifyDataSetChanged();

                dialogInterface.dismiss();
                if (checkIfLoggedIntoDispatcher == 1){
                    Intent intent = new Intent(getActivity(), Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    Toast.makeText(getActivity(), "You have been logged out since your account has been deleted ", Toast.LENGTH_SHORT).show();
                }
                else {Toast.makeText(getActivity(), name + " removed from database", Toast.LENGTH_LONG).show();}
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
            }
        });

        dialog.show();}

    /**
     * @param loan
     */
    @Override
    public void onLoanClick(Loan loan) {
        buildLoanDeleteDialog(loan);
    }

    public void buildLoanDeleteDialog(Loan loan){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setTitle("Delete loan:");
        String loanString = loan.getItem() + "   " + loan.getQuantity();
        builder.setMessage("Are you sure you want to delete: " + loanString + " from the database?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                int id = loan.getId();

                db.removeLoanID(id);

                if (loanList == null){
                    loanList = new ArrayList<Loan>();
                }
                loanList.remove(loan);
                loanAdapter.setData(loanList);
                loanAdapter.notifyDataSetChanged();

                dialogInterface.dismiss();
                Toast.makeText(getActivity(), loanString + " removed from database ", Toast.LENGTH_LONG).show();

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
            }
        });

        dialog.show();}


    @Override
    public void onRefresh() {
        if(!(generateDispatchList() == null)) {
            dispatchAdapter.setData(generateDispatchList());
            dispatchAdapter.notifyDataSetChanged();
        }
        if (!(generateItemList() == null)) {
            itemAdapter.setData(generateItemList());
            itemAdapter.notifyDataSetChanged();
        }
        if ((!(generateDriverList() == null))){
            driverAdapter.setData(generateDriverList());
            driverAdapter.notifyDataSetChanged();
        }
        if (!(generateLoanList() == null)){
            loanAdapter.setData(generateLoanList());
            loanAdapter.notifyDataSetChanged();
        }
        swipeRefreshLayout.setRefreshing(false);
    }

}