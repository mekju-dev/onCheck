package com.wgu.testing123;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class TodaysOutstandingFragment extends Fragment implements LoanRecyclerViewInterface{

    RecyclerView itemRecyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    dbHelper db;
    public static List<Loan> loanList;
    public static LoanAdapter itemAdapter;
    private final int pass = R.drawable.baseline_check_24;
    private final int fail = R.drawable.baseline_close_24;
    private SearchView searchView;
    private List<Loan> tempList;


    public TodaysOutstandingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item_home, container, false);

        loanList = generateItemList();
        tempList = loanList;

        //line divider for recycler view
        DividerItemDecoration divider =
                new DividerItemDecoration(this.getContext(),
                        DividerItemDecoration.VERTICAL);

        divider.setDrawable(ContextCompat.getDrawable(getContext(),
                R.drawable.line_divider));

        //Search functionality
        searchView = rootView.findViewById(R.id.loanHomeRecyclerSearch);
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

        itemRecyclerView = rootView.findViewById(R.id.itemRecyclerView);
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        itemAdapter = new LoanAdapter(loanList, this);
        itemRecyclerView.setAdapter(itemAdapter);


        itemRecyclerView.addItemDecoration(divider);
        //itemRecyclerView.onClickListener can be added here in future

        return rootView;
    }

    private void filterList(String text) {
        List<Loan> filteredList = new ArrayList<>();
        for (Loan loan : loanList){
            if (loan.getItem().toLowerCase().contains(text.toLowerCase()) || loan.getDriver().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(loan);
            }
        }
        if (filteredList.isEmpty()){
            //no data found
        }
        else{
            itemAdapter.setFilteredList(filteredList);
            tempList = filteredList;
        }
    }
    private List<Loan> generateItemList()
    {
        db = new dbHelper(getActivity());
        Cursor result = db.getAllLoansToday();
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

    public void displayMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    /**
     * @param loan
     */
    @Override
    public void onLoanClick(Loan loan) {

    }

/*
    @Override
    public void onRefresh() {
        itemAdapter.notifyDataSetChanged();
        if (!(generateItemList() == null)){

        itemAdapter.setData(generateItemList());
        itemAdapter.notifyDataSetChanged();
        }
        swipeRefreshLayout.setRefreshing(false);
    }

 */
}
