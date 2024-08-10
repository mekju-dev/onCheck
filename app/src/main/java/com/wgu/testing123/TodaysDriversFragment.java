package com.wgu.testing123;

import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TodaysDriversFragment extends Fragment implements TodayDriverRecyclerViewInterface{
    RecyclerView todaysDriverRecyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    dbHelper db;
    public static List<Driver> driverList, tempList;
    private List<Item> itemList, tempItemList;

    ArrayList <Loan> loansNotReturning;
    public static DriverAdapter driverAdapter;

    public static LoanAdapter loanAdapter;
    List<String> loanedItemList;
    ArrayList<Loan> loanedItems;
    private final int pass = R.drawable.baseline_check_24;
    private final int fail = R.drawable.baseline_close_24;
    private SearchView searchView;

    public TodaysDriversFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_driver_home, container, false);

        db = new dbHelper(getActivity());
        loanedItems = new ArrayList<Loan>();
        //line divider for recycler view
        DividerItemDecoration divider =
                new DividerItemDecoration(this.getContext(),
                        DividerItemDecoration.VERTICAL);

        divider.setDrawable(ContextCompat.getDrawable(getContext(),
                R.drawable.line_divider));

        //Search functionality
        searchView = rootView.findViewById(R.id.homeDriverSearchView);
        searchView.setIconifiedByDefault(true);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });





        driverList = generateDriverList();
        tempList = driverList;
        itemList = generateItemList();
        tempItemList = itemList;
        todaysDriverRecyclerView = rootView.findViewById(R.id.driverRecyclerView);
        todaysDriverRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        driverAdapter = new DriverAdapter(driverList, this);
        todaysDriverRecyclerView.setAdapter(driverAdapter);

        todaysDriverRecyclerView.addItemDecoration(divider);
         /*
        swipeRefreshLayout = swipeRefreshLayout.findViewById(R.id.swipeRefreshLayoutDrivers);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                driverAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
          */

        return rootView;
    }

    private void filterList(String text) {
        List<Driver> filteredList = new ArrayList<>();
        for (Driver driver : driverList){
            if (driver.getFirstName().toLowerCase().contains(text.toLowerCase()) || driver.getLastName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(driver);
            }
        }
        if (filteredList.isEmpty()){
            //No data found
        }
        else{
            driverAdapter.setFilteredList(filteredList);
            tempList = filteredList;
        }
    }


    private List<Driver> generateDriverList()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        month++;
        String dateToday = month + "/" + day + "/" + year;
        db = new dbHelper(getActivity());
        Cursor result = db.getAllDriversToday();
        List<Driver> drivers = new ArrayList<>();
        if (result.getCount() == 0) {
            return null;
        } else {
            while (result.moveToNext()) {
                Driver driver = new Driver(result.getInt(0), result.getString(1), result.getString(2), result.getString(3), result.getInt(4));

                drivers.add(driver);
                //Toast.makeText(getActivity(), result.getString(0), Toast.LENGTH_SHORT).show();
            }
        } return drivers;
    }

    private List<Item> generateItemList()
    {
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
        } return items;
    }




    public void displayMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    /**
     * @param driver
     */
    @Override
    public void onDriverClick(Driver driver) {
        confirmDeleteDialog(driver);
    }

    public void confirmDeleteDialog(Driver selectDriver){
        ArrayList<Item> itemArrayList = new ArrayList<>();

        loanedItems = new ArrayList<Loan>();
        tempList = generateDriverList();
        loanedItems = db.getAllLoans(selectDriver.getId());
        String loansToStringList[] = new String[loanedItems.size()];

        int index = 0;
        for (Loan loan:loanedItems){
            Item itemTemp = new Item(loan.getItemID(), loan.getItem(), "temp desc", loan.getQuantity());

            loansToStringList[index] = itemTemp.getItem() + "   " + itemTemp.getQuantity();
            itemArrayList.add(itemTemp);
            index++;
        }
        //String driverName = driverList.get(pos).getFirstName() + " " + driverList.get(pos).getLastName();
        String driverName = selectDriver.getFirstName() + " " + selectDriver.getLastName();

        boolean[] checkItemsBool = new boolean[loanedItems.size()];
        int i = 0;
        while (loanedItems.size() > i){
            checkItemsBool[i] = true;
           // checkedItems.add(i, true);

            i++;
        }



        loanedItemList = new ArrayList<String>();
        loansNotReturning = new ArrayList<Loan>();



        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Returning driver: " + selectDriver.firstName + " " + selectDriver.lastName);


        builder.setMultiChoiceItems(loansToStringList, checkItemsBool, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (!isChecked){
                    //returnedItemsList.remove(loanedItems.get(which));
                    loansNotReturning.add(loanedItems.get(which));
                    checkItemsBool[which] = false;
                }else{
                    loansNotReturning.remove(loanedItems.get(which));
                    checkItemsBool[which] = true;
                }
            }

        });



        builder.setPositiveButton("Submit Returned Items", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ArrayList<Loan> loansToReturn = new ArrayList<>();
                loansToReturn = loanedItems;
                if (areAllTrue(checkItemsBool)){
                    for (Loan loan:loanedItems){
                        db.removeLoanID(loan.getId());
                        db.updateItemQuantity(loan.getItemID(),loan.getQuantity() + db.getItemByID(loan.getItemID()).getQuantity());
                    }
                    //Toast.makeText(getActivity(), "Driver: " + driver.getFirstName() + " " + driver.getLastName() + " has returned all items!", Toast.LENGTH_SHORT).show();
                }
                else {

                    int index =0;
//use the check here
                    //may be logic flaw
                    for (Loan loan:loanedItems){
                        if (checkItemsBool[index]){
                           db.removeLoanID(loan.getId());
                           db.updateItemQuantity(loansToReturn.get(index).getItemID(),loan.getQuantity() + db.getItemByID(loan.getItemID()).getQuantity());
                        }index++;
                    }


                }
                //UPLOAD CLOCK OUT TIME TO TXT FILE HERE
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy 'at' HH:mm:ss z");
                String currentDateAndTime = sdf.format(new Date());
                String userLoggedIn = db.getLoggedInUser();
                String text = "DA: " + selectDriver.firstName + " " + selectDriver.lastName + "\nSigned out by " + userLoggedIn +  " on: " + currentDateAndTime + "\n\n";
                try {
                    writeToFile("returnToStationTimes.txt", text);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                //KEEP ONCE POSITIONG WORKS
                db.updateDriver(selectDriver.getId(), selectDriver.getFirstName(), selectDriver.getLastName(), selectDriver.getPhone(), 0);
                driverList.remove(selectDriver);
                driverAdapter.setData(driverList);
                driverAdapter.notifyDataSetChanged();


                /*HOLD ONTO THIS
                //MAKE REMOVE LOAN BUT FOR EVERYTHING BUT GIVE LOANID
                String b = String.valueOf(db.removeLoan(id));
                db.updateDriver(id, driver.getFirstName(), driver.getLastName(), driver.getPhone(), 0);
                Toast.makeText(getActivity(), b, Toast.LENGTH_LONG).show();

                 */

            }
        });


        builder.setNeutralButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.BLACK);
            }
        });

        dialog.show();

    }

    private void writeToFile(String file, String text) throws IOException {
        File path = getActivity().getApplicationContext().getFilesDir();
        try {
            FileOutputStream writer = new FileOutputStream(new File(path, file), true);
            writer.write(text.getBytes());
            writer.close();
            Toast.makeText(getActivity(), "DA return time logged to " + file, Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            Toast.makeText(getActivity(), "Issue logging the data. Please manually save DVIC time", Toast.LENGTH_SHORT).show();
            throw new RuntimeException(e);
        }
    }

    /**
     * @param driver
     */
    @Override
    public void onDriverHold(Driver driver) {

    }
    public static boolean areAllTrue(boolean[] array)
    {
        for(boolean b : array) if(!b) return false;
        return true;
    }


}
