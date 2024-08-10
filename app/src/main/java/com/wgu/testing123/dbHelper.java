package com.wgu.testing123;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatatypeMismatchException;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class dbHelper extends SQLiteOpenHelper {
    Context context;
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "logistics.db";
    public static final String INV_TABLE = "Inventory_Table";
    public static final String DRIVER_TABLE = "Drivers_Table";
    public static final String DISPATCH_TABLE = "Dispatchers_Table";
    public static final String LOAN_TABLE = "Loan_Table";
    public static final String AUTH_TABLE = "Auth_Table";
    //public static final String TODAY_TABLE = "Today_Table";

    //Inventory Columns
    public static final String ItemName = "Item";
    public static final String ItemDescription = "Description";
    public static final String ItemQuantity = "Quantity";


    //Drivers Columns
    public static final String DriverFirstName = "FirstName";
    public static final String DriverLastName = "LastName";
    public static final String DriverPhoneNumber = "Phone";
    public static final String DriverWorkToday = "WorkToday";

    //Dispatchers Columns
    public static final String DispatchFirstName = "FirstName";
    public static final String DispatchLastName = "LastName";
    public static final String DispatchUserName = "Username";
    public static final String DispatchPass = "Password";

    //Loan Columns
    public static final String LoanedOutItem = "Item";
    public static final String LoanedOutItemID = "ItemID";
    public static final String LoanedOutQuantity = "Quantity";
    public static final String LoanedOutToID = "DriverID";
    public static final String LoanedOutTo = "Driver";
    public static final String LoanedOutOnDate = "Date";

    //Auth columns
    public static final String AuthUser = "Username";
    public static final String LoggedIn = "LoggedIn";
    /*
    //Todays Table
    public static final String TodayName = "TodayName";
    public static final String TodayKey = "TodayKey";
    public static final String TodayPhone = "TodayPhone";
    public static final String TodayBattery = "TodayBattery";
    public static final String TodayCable = "TodayCable";
     */




    public dbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    /**
     * @param sqLiteDatabase
     */
    @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {

            sqLiteDatabase.execSQL("create table " + INV_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,Item TEXT,Description TEXT,Quantity INTEGER)");
            sqLiteDatabase.execSQL("create table " + DRIVER_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,FirstName TEXT,LastName TEXT,Phone TEXT,WorkToday INTEGER)");
            sqLiteDatabase.execSQL("create table " + DISPATCH_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,FirstName TEXT,LastName TEXT,Username TEXT UNIQUE, Password TEXT)");
            sqLiteDatabase.execSQL("create table " + LOAN_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,Item TEXT, ItemID INTEGER, Quantity INTEGER, Driver TEXT, DriverID INTEGER, Date TEXT, FOREIGN KEY(DriverID) REFERENCES Drivers_Table(ID), FOREIGN KEY(ItemID) REFERENCES Inventory_Table(ID))");
            sqLiteDatabase.execSQL("create table " + AUTH_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,Username TEXT, LoggedIn INTEGER, FOREIGN KEY(Username) REFERENCES Dispatch_Table(Username))");
            //sqLiteDatabase.execSQL("create table " + TODAY_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,TodayName TEXT, TodayKey Text, TodayPhone TEXT,TodayBattery TEXT, TodayCable TEXT)");

        }

    /**
     * @param sqLiteDatabase
     * @param i
     * @param ii
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int ii) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + INV_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DRIVER_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DISPATCH_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + LOAN_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + AUTH_TABLE);
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TODAY_TABLE);
        onCreate(sqLiteDatabase);
    }

    //ITEM CRUD
    public String insertItem(String itemName, String itemDescription, int itemQuantity){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + INV_TABLE + " WHERE CourseTitle = ?", new String[]{Item});

                contentValues.put(ItemName, itemName);
                contentValues.put(ItemDescription, itemDescription);
                contentValues.put(ItemQuantity, itemQuantity);
                sqLiteDatabase.insert(INV_TABLE, null, contentValues);

                return "Success";
    }

    public String updateItem(int id, String itemName, String itemDescription, int itemQuantity){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + INV_TABLE + " WHERE ID = ?", new String[]{String.valueOf(id)});

       // if (result.moveToNext()) {

            try {
                contentValues.put(ItemName, itemName);
                contentValues.put(ItemDescription, itemDescription);
                contentValues.put(ItemQuantity, itemQuantity);

                sqLiteDatabase.update(INV_TABLE, contentValues, "ID=?", new String[]{String.valueOf(id)});
            }catch (SQLException mSQLException) {
                if (mSQLException instanceof SQLiteConstraintException) {
                    return "SQLite Constraint Exception. Not unique";
                } else if (mSQLException instanceof SQLiteDatatypeMismatchException) {
                    return "SQLite Data Type Mismatch Exception";
                } else {
                    return "SQLite ERROR";
                }
            }
            return "Success";
        //}else{return "ERROR, no such item name exists";}
    }

    public String updateItemQuantity(int itemID, int quantity){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Cursor result = sqLiteDatabase.rawQuery("SELECT Quantity FROM " + INV_TABLE + " WHERE ID = ?", new String[]{String.valueOf(itemID)});

        if (result.moveToNext()) {

            try {
                contentValues.put(ItemQuantity, quantity);

                sqLiteDatabase.update(INV_TABLE, contentValues, "ID=?", new String[]{String.valueOf(itemID)});
            }catch (SQLException mSQLException) {
                if (mSQLException instanceof SQLiteConstraintException) {
                    return "SQLite Constraint Exception. Not unique";
                } else if (mSQLException instanceof SQLiteDatatypeMismatchException) {
                    return "SQLite Data Type Mismatch Exception";
                } else {
                    return "SQLite ERROR";
                }
            }
            return "Success";
        }else{return "ERROR, no such item name exists";}
    }

    public boolean deleteItem(int id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long result = sqLiteDatabase.delete(INV_TABLE, "ID=?",new String[]{String.valueOf(id)});

        return result != -1;
    }


    //AUTH CRUD
    public String insertAuth(String authUser, int loggedIn){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + INV_TABLE + " WHERE CourseTitle = ?", new String[]{Item});

        contentValues.put(AuthUser, authUser);
        contentValues.put(LoggedIn, loggedIn);
        sqLiteDatabase.insert(AUTH_TABLE, null, contentValues);

        return authUser;
    }

    public String updateAuth(String authUser, int loggedIn){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + INV_TABLE + " WHERE ID = ?", new String[]{String.valueOf(id)});

        // if (result.moveToNext()) {

        try {
            contentValues.put(AuthUser, authUser);
            contentValues.put(LoggedIn, loggedIn);

            sqLiteDatabase.update(AUTH_TABLE, contentValues, "Username=?", new String[]{String.valueOf(authUser)});
        }catch (SQLException mSQLException) {
            if (mSQLException instanceof SQLiteConstraintException) {
                return "SQLite Constraint Exception. Not unique";
            } else if (mSQLException instanceof SQLiteDatatypeMismatchException) {
                return "SQLite Data Type Mismatch Exception";
            } else {
                return "SQLite ERROR";
            }
        }
        return "Success";
        //}else{return "ERROR, no such item name exists";}
    }
    public boolean checkIfLoggedIn(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + AUTH_TABLE + " WHERE LoggedIn = ?", new String[]{String.valueOf(1)});
        if(result.moveToNext()){
            return true;
        }else {
            return false;
        }
    }

    public String getLoggedInUser(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + AUTH_TABLE + " WHERE LoggedIn = ?", new String[]{String.valueOf(1)});
        if(result.moveToNext()){
            return result.getString(1);
        }else {
            return "error dispatcher";
        }
    }

    public Driver getDriverFromData(String first, String last, String phone){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + DRIVER_TABLE + " WHERE FirstName = ? AND LastName = ? AND Phone = ?", new String[]{String.valueOf(first), String.valueOf(last), String.valueOf(phone)});
        if(result.moveToNext()){
            Driver driver = new Driver(result.getInt(0), first, last, phone, result.getInt(4));
            return driver;
        }else {
            return null;
        }
    }

    public int deleteAuthWhere(String username){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + AUTH_TABLE + " WHERE Username = ?", new String[]{String.valueOf(username)});
        int i = 0;
        if(result.moveToNext()){
            sqLiteDatabase.delete(AUTH_TABLE, "Username=?", new String[]{String.valueOf(username)});
            if (result.getInt(2) == 1) {
                i = 1;
            }
        }return i;
        //long result = sqLiteDatabase.delete(DRIVER_TABLE, "ID=?",new String[]{String.valueOf(id)});
        //return result != -1;
    }

    public Dispatch getDispatchFromData(String first, String last, String username){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + DISPATCH_TABLE + " WHERE FirstName = ? AND LastName = ? AND Username = ?", new String[]{String.valueOf(first), String.valueOf(last), String.valueOf(username)});
        if(result.moveToNext()){
            Dispatch dispatch = new Dispatch(result.getInt(0), first, last, username, result.getString(4));
            return dispatch;
        }else {
            return null;
        }
    }


    public Loan getLoanFromData(String item, int quantity, String driver, String date){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + LOAN_TABLE + " WHERE Driver = ? AND Quantity = ? AND Driver = ? And Date = ?", new String[]{String.valueOf(item), String.valueOf(quantity), String.valueOf(driver), String.valueOf(date)});
        if(result.moveToNext()){
            Loan loan = new Loan(result.getInt(0), item, result.getInt(2), quantity, driver, result.getInt(5), date);
            return loan;
        }else {
            return null;
        }
    }
    public Item getItemFromData(String name, String description, int quantity){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + INV_TABLE + " WHERE Item = ? AND Description = ? AND Quantity = ?", new String[]{String.valueOf(name), String.valueOf(description), String.valueOf(quantity)});
        if(result.moveToNext()){
            Item item = new Item(result.getInt(0), name, description, quantity);
            return item;
        }else {
            return null;
        }
    }
    public String getAuthCount(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + AUTH_TABLE + " WHERE LoggedIn = 1",null);
            if (result.moveToNext()){
                return result.getString(1);
        }else {
            return null;
        }
    }
    public void logAllOut(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + AUTH_TABLE + " WHERE LoggedIn = 1",null);
        ContentValues contentValues = new ContentValues();
        if (result.moveToNext()){
            contentValues.put(AuthUser, result.getString(1));
            contentValues.put(LoggedIn, 0);
            sqLiteDatabase.update(AUTH_TABLE, contentValues, "LoggedIn=?", new String[]{String.valueOf(1)});
        }
    }
    //DRIVER CRUD
    public String insertDriver(String firstName, String lastName, String phone, int workday){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + INV_TABLE + " WHERE CourseTitle = ?", new String[]{Item});

        contentValues.put(DriverFirstName, firstName);
        contentValues.put(DriverLastName, lastName);
        contentValues.put(DriverPhoneNumber, phone);
        contentValues.put(DriverWorkToday, workday);
        sqLiteDatabase.insert(DRIVER_TABLE, null, contentValues);

        return "Success";
    }

    public String updateDriver(int id, String firstName, String lastName, String phone, int workday){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + DRIVER_TABLE + " WHERE ID = ?", new String[]{String.valueOf(id)});
        if (result.moveToNext()){
            try {
                contentValues.put(DriverFirstName, firstName);
                contentValues.put(DriverLastName, lastName);
                contentValues.put(DriverPhoneNumber, phone);
                contentValues.put(DriverWorkToday, workday);

                sqLiteDatabase.update(DRIVER_TABLE, contentValues,"ID=?",new String[]{String.valueOf(id)});
            } catch (SQLException mSQLException) {
                if (mSQLException instanceof SQLiteConstraintException) {
                    return "SQLite Constraint Exception. Not unique";
                } else if (mSQLException instanceof SQLiteDatatypeMismatchException) {
                    return "SQLite Data Type Mismatch Exception";
                } else {
                    return "SQLite ERROR";
                }
            }return "Success";}
        else{return "Fail. No such driver exists.";}
    }

    public boolean deleteDriver(int id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long result = sqLiteDatabase.delete(DRIVER_TABLE, "ID=?",new String[]{String.valueOf(id)});

        return result != -1;
    }

    public boolean deleteLoansFromItem(int itemId){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long result = sqLiteDatabase.delete(LOAN_TABLE, "ItemID=?",new String[]{String.valueOf(itemId)});

        return result != -1;
    }


    //DISPATCH CRUD
    public String insertDispatch(String firstName, String lastName, String username, String pass){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + INV_TABLE + " WHERE CourseTitle = ?", new String[]{Item});
        try {
            contentValues.put(DispatchFirstName, firstName);
            contentValues.put(DispatchLastName, lastName);
            contentValues.put(DispatchUserName, username);
            contentValues.put(DispatchPass, pass);

            long result = sqLiteDatabase.insert(DISPATCH_TABLE, null, contentValues);


        } catch (SQLException mSQLException) {
            if (mSQLException instanceof SQLiteConstraintException) {
                return "SQLite Constraint Exception. Not unique";
            } else if (mSQLException instanceof SQLiteDatatypeMismatchException) {
                return "SQLite Data Type Mismatch Exception";
            } else {
                return "SQLite ERROR";
            }
        }return "";
    }

    public int getDispatchIdFromUser(String user){

            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            Cursor result = sqLiteDatabase.rawQuery("SELECT ID FROM " + DISPATCH_TABLE + " WHERE Username = ?", new String[]{user});
            int i = -1;
            while(result.moveToNext()){
                i = result.getInt(0);
            }

            return i;
        }

    public String updateDispatch(int id, String firstName, String lastName, String username, String pass){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + DISPATCH_TABLE + " WHERE ID = ?", new String[]{String.valueOf(id)});
        if (result.moveToNext()){
            try {
                contentValues.put(DispatchFirstName, firstName);
                contentValues.put(DispatchLastName, lastName);
                contentValues.put(DispatchUserName, username);
                contentValues.put(DispatchPass, pass);

                sqLiteDatabase.update(DISPATCH_TABLE, contentValues,"ID=?",new String[]{String.valueOf(id)});
            } catch (SQLException mSQLException) {
                if (mSQLException instanceof SQLiteConstraintException) {
                    return "SQLite Constraint Exception. Cot unique";
                } else if (mSQLException instanceof SQLiteDatatypeMismatchException) {
                    return "SQLite Data Type Mismatch Exception";
                } else {
                    return "SQLite ERROR";
                }
            }return "Success";}
        else{return "Fail. No such dispatcher exists.";}
    }

    public boolean deleteDispatcher(int id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long result = sqLiteDatabase.delete(DISPATCH_TABLE, "ID=?",new String[]{String.valueOf(id)});

        return result != -1;
    }

    //LOAN CRUD4
    public String insertLoan(String item, int itemID, int quantity, int driverID, String driver, String date){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + INV_TABLE + " WHERE CourseTitle = ?", new String[]{Item});

        contentValues.put(LoanedOutItem, item);
        contentValues.put(LoanedOutItemID, itemID);
        contentValues.put(LoanedOutQuantity, quantity);
        contentValues.put(LoanedOutToID, driverID);
        contentValues.put(LoanedOutTo, driver);
        contentValues.put(LoanedOutOnDate, date);

        sqLiteDatabase.insert(LOAN_TABLE, null, contentValues);

        return "Success";
    }

    public String updateLoan(int id, String item, int itemID, int quantity, int driverID, String driver, String date){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + LOAN_TABLE + " WHERE ID = ?", new String[]{String.valueOf(id)});
        if (result.moveToNext()){
            try {
                contentValues.put(LoanedOutItem, item);
                contentValues.put(LoanedOutItemID, itemID);
                contentValues.put(LoanedOutQuantity, quantity);
                contentValues.put(LoanedOutToID, driverID);
                contentValues.put(LoanedOutTo, driver);
                contentValues.put(LoanedOutOnDate, date);

                sqLiteDatabase.update(LOAN_TABLE, contentValues,"ID=?",new String[]{String.valueOf(id)});
            } catch (SQLException mSQLException) {
                if (mSQLException instanceof SQLiteConstraintException) {
                    return "SQLite Constraint Exception. Not unique";
                } else if (mSQLException instanceof SQLiteDatatypeMismatchException) {
                    return "SQLite Data Type Mismatch Exception";
                } else {
                    return "SQLite ERROR";
                }
            }return "Success";}
        else{return "Fail. No such loan exists.";}
    }

    public Cursor getAllItems(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + INV_TABLE,null);
        return result;
    }


    public Cursor getAllDrivers(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + DRIVER_TABLE,null);

        return result;
    }

    public ArrayList<Driver> getAllDriversArray(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + DRIVER_TABLE, null);
        ArrayList<Driver> list = new ArrayList<>();
        while (result.moveToNext()){
            Driver driver1 = new Driver(result.getInt(0), result.getString(1), result.getString(2), result.getString(3), result.getInt(4));
            list.add(driver1);
        }
        return list;
    }

    public ArrayList<Item> getAllItemsArray(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + INV_TABLE, null);
        ArrayList<Item> list = new ArrayList<>();
        while (result.moveToNext()){
            Item item = new Item(result.getInt(0), result.getString(1), result.getString(2), result.getInt(3));
            list.add(item);
        }
        return list;
    }
    public Cursor getAllDriversToday(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + DRIVER_TABLE + " WHERE WorkToday = ?", new String[]{"1"});

        return result;
    }

    public ArrayList<String> getAllDriverNames(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + DRIVER_TABLE,null);

        ArrayList<String> list = new ArrayList<>();
        while (result.moveToNext()){
            list.add(result.getString(1) + " " + result.getString(2));
        }
        return list;
    }

    public ArrayList<String> getAllItemNames(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + INV_TABLE,null);

        ArrayList<String> list = new ArrayList<>();
        while (result.moveToNext()){
            list.add(result.getString(1));
        }
        return list;
    }

    //likely not useful
    public ArrayList<String> getAllDriverIDs(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + DRIVER_TABLE,null);

        ArrayList<String> list = new ArrayList<>();
        while (result.moveToNext()){
            list.add(result.getString(0));
        }
        return list;
    }

    //test
    public ArrayList<String> getAllTest(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + DRIVER_TABLE,null);

        ArrayList<String> list = new ArrayList<>();
        while (result.moveToNext()){
            list.add(result.getString(1));
        }
        return list;
    }

    public Cursor getAllDispatchers(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + DISPATCH_TABLE, null);
        return result;
    }

    public Cursor getAllLoans(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + LOAN_TABLE, null);

        return result;
    }

    public ArrayList<Loan> getAllLoans(int driverID){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + LOAN_TABLE + " WHERE DriverID = ?", new String[]{String.valueOf(driverID)});

        ArrayList<Loan> list = new ArrayList<>();
        while (result.moveToNext()){
            Loan loan = new Loan(result.getInt(0), result.getString(1), result.getInt(2), result.getInt(3), result.getString(4), result.getInt(5), result.getString(6));
            list.add(loan);
        }
        return list;
    }

    public Item getItemByID(int ID){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + INV_TABLE + " WHERE ID = ?", new String[]{String.valueOf(ID)});
        Item item = new Item(1, "test", "test", 1);
        while (result.moveToNext()){
            item = new Item(result.getInt(0), result.getString(1), result.getString(2), result.getInt(3));
        }

        return item;
    }

    public Cursor getAllLoansToday(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        month++;
        String dateToday = month + "/" + day + "/" + year;
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + LOAN_TABLE + " WHERE Date = ?", new String[]{String.valueOf(dateToday)});

        return result;
    }



    public String removeLoan(int driverID){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        //Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + LOAN_TABLE + " WHERE DriverID = ?", new String[]{String.valueOf(driverID)});

        long result = sqLiteDatabase.delete(LOAN_TABLE, "DriverID=?",new String[]{String.valueOf(driverID)});

        return result + " " + driverID;

        }

    public String removeLoanID(int loanID){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long result = sqLiteDatabase.delete(LOAN_TABLE, "ID=?",new String[]{String.valueOf(loanID)});

        return String.valueOf(result);
    }
    public String removeLoanIDList(List<Integer> loanID){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String [] arr = new String[loanID.size()];
       // arr[0] = String.valueOf(driverID);
        arr[0] = String.valueOf(loanID.get(0));
        int i;
        String temp = "ID=?";
        if (loanID.size() > 1){
            for (i = 1; i < loanID.size(); i++){
                temp = temp + " OR ID=?";
                arr[i] = String.valueOf(loanID.get(i));
            }
        }
        long result = sqLiteDatabase.delete(LOAN_TABLE, temp,arr);

        return String.valueOf(result);
    }

        public String getDriverNameByID(int driverID){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + DRIVER_TABLE + " WHERE ID = ?", new String[]{String.valueOf(driverID)});

        return result.getString(0);
        }


        public boolean validateCredentials(String user, String pass){
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + DISPATCH_TABLE + " WHERE Username = ? AND Password = ? ", new String[]{String.valueOf(user), String.valueOf(pass)});

            // result.getString(0);
            if (result.moveToNext()){
                return true;
            }
            else {
                return false;
            }
        }
    public void clearData(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + INV_TABLE + ";");
        sqLiteDatabase.execSQL("DELETE FROM " + DRIVER_TABLE + ";");
        sqLiteDatabase.execSQL("DELETE FROM " + DISPATCH_TABLE + ";");
        sqLiteDatabase.execSQL("DELETE FROM " + LOAN_TABLE);
    }

}
