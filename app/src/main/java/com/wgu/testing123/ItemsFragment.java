package com.wgu.testing123;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class ItemsFragment extends Fragment implements ItemRecyclerViewInterface{

    RecyclerView itemRecyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    dbHelper db;
    private List<Item> itemList;
    private ItemAdapter itemAdapter;
    private final int pass = R.drawable.baseline_check_24;
    private final int fail = R.drawable.baseline_close_24;


    public ItemsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item_home, container, false);

        itemList = generateItemList();

        itemRecyclerView = rootView.findViewById(R.id.itemRecyclerView);
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        itemAdapter = new ItemAdapter(itemList, this);
        itemRecyclerView.setAdapter(itemAdapter);

        return rootView;
    }

    private List<Item> generateItemList()
    {
        db = new dbHelper(getActivity());
        Cursor result = db.getAllItems();
        List<Item> items = new ArrayList<>();

        if (result.getCount() == 0) {
            displayMessage("Error", "No items found");
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
}