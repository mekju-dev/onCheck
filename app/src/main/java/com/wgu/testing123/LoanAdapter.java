package com.wgu.testing123;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.LoanViewHolder>{
private List<Loan> loanList;
private Context context;
public dbHelper db;

public LoanRecyclerViewInterface loanRecyclerViewInterface;
public LoanAdapter(List<Loan> loanList, LoanRecyclerViewInterface loanRecyclerViewInterface) {
    this.loanList = loanList;
    this.loanRecyclerViewInterface = loanRecyclerViewInterface;
}

/**
 * @param parent   The ViewGroup into which the new View will be added after it is bound to
 *                 an adapter position.
 * @param viewType The view type of the new View.
 * @return
 */
@NonNull
@Override
public LoanAdapter.LoanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View loanView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_row_loan, parent, false);
    return new LoanAdapter.LoanViewHolder(loanView, loanRecyclerViewInterface);
}

    public void setFilteredList(List<Loan> filteredList){
        this.loanList = filteredList;
        notifyDataSetChanged();
    }

/**
 * @param holder   The ViewHolder which should be updated to represent the contents of the
 *                 item at the given position in the data set.
 * @param position The position of the item within the adapter's data set.
 */
@Override
public void onBindViewHolder(@NonNull LoanAdapter.LoanViewHolder holder, int position) {
    Loan loan = loanList.get(position);

    holder.loanedItem.setText((loanList.get(position)).getItem());
    holder.itemQuantity.setText(Integer.toString(loanList.get(position).getQuantity()));
    //holder.loanedOutTo.setText(loanList.get(position).getDriver());
    holder.loanedOutTo.setText(loan.driver);
    holder.loanedOutOnDate.setText((loanList.get(position).getDate()));
    //holder.returnedStatus.setText(Integer.toString(loanList.get(position).getDriverID()));
    holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            loanRecyclerViewInterface.onLoanClick(loan);
        }
    });
}

/**
 * @return
 */
@Override
public int getItemCount() {
    if (loanList == null) return 0;
    return loanList.size();
}
    public void setData(List<Loan> newData) {
        if (loanList == null || loanList.size() == 0) {
            this.loanList = new ArrayList<Loan>();
        }else {
            loanList.clear();
        }
        if (newData == null){
        }
        else if (newData.size() > 0) {
            loanList.addAll(newData);
        }
    }
public class LoanViewHolder extends RecyclerView.ViewHolder {

    TextView loanedItem, itemQuantity, loanedOutTo, loanedOutOnDate, returnedStatus;

    public LoanViewHolder(@NonNull View loanView, LoanRecyclerViewInterface loanRecyclerViewInterface) {
        super(loanView);
        loanedItem = loanView.findViewById(R.id.loanRecyclerItem);
        itemQuantity = loanView.findViewById(R.id.loanRecyclerItemQuantity);
        loanedOutTo = loanView.findViewById(R.id.loanRecyclerOutTo);
        loanedOutOnDate = loanView.findViewById(R.id.loanRecyclerOutOnDate);
        //returnedStatus = loanView.findViewById(R.id.loanRecyclerReturnedStatus);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loanRecyclerViewInterface != null){
                    int pos = getAbsoluteAdapterPosition();

                    if (pos != RecyclerView.NO_POSITION){
                        Loan loan = db.getLoanFromData(loanedItem.getText().toString(), Integer.parseInt(itemQuantity.getText().toString()), loanedOutTo.getText().toString(), loanedOutOnDate.getText().toString());
                        loanRecyclerViewInterface.onLoanClick(loan);
                    }
                }
            }
        });
    }
}
}
