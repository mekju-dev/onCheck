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

public class DispatchAdapter extends RecyclerView.Adapter<DispatchAdapter.DispatchViewHolder>{
    private List<Dispatch> dispatchList;
    private Context context;
    private dbHelper db;
    private final DispatchRecyclerViewInterface dispatchRecyclerViewInterface;

    public DispatchAdapter(List<Dispatch> dispatchList, DispatchRecyclerViewInterface dispatchRecyclerViewInterface) {
        this.dispatchList = dispatchList;
        this.dispatchRecyclerViewInterface = dispatchRecyclerViewInterface;
    }

    /**
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return
     */
    @NonNull
    @Override
    public DispatchAdapter.DispatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View dispatchView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_row_dispatch, parent,false);
        return new DispatchAdapter.DispatchViewHolder(dispatchView, dispatchRecyclerViewInterface);
    }

    public void setFilteredList(List<Dispatch> filteredList){
        this.dispatchList = filteredList;
        notifyDataSetChanged();
    }

    /**
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull DispatchAdapter.DispatchViewHolder holder, int position) {
        Dispatch dispatch = dispatchList.get(position);

        holder.dispatchFirst.setText((dispatchList.get(position)).getFirstName());
        holder.dispatchLast.setText((dispatchList.get(position)).getLastName());
        holder.dispatchUsername.setText((dispatchList.get(position).getUsername()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchRecyclerViewInterface.onDispatchClick(dispatch);
            }
        });
    }

    /**
     * @return
     */
    @Override
    public int getItemCount() {
        if(dispatchList==null) return 0;
        return dispatchList.size();
    }
    public void setData(List<Dispatch> newData) {

        if (dispatchList == null || dispatchList.isEmpty()){
            dispatchList = new ArrayList<Dispatch>();

        }

        dispatchList.clear();
        if (newData == null){

        }else{
            dispatchList.addAll(newData);
        }

        /*
        if (dispatchList == null || dispatchList.size() == 0) {
            this.dispatchList = new ArrayList<Dispatch>();
        }else {
            dispatchList.clear();
        }
        if (newData.size() > 0) {
            dispatchList.addAll(newData);
        }

         */
    }
    public class DispatchViewHolder extends RecyclerView.ViewHolder{
        TextView dispatchFirst, dispatchLast, dispatchUsername;

        public DispatchViewHolder(@NonNull View itemView, DispatchRecyclerViewInterface dispatchRecyclerViewInterface) {
            super(itemView);
            dispatchFirst = itemView.findViewById(R.id.dispatchRecyclerFirstName);
            dispatchLast = itemView.findViewById(R.id.dispatchRecyclerLastName);
            dispatchUsername = itemView.findViewById(R.id.userNameTextView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dispatchRecyclerViewInterface != null){
                        int pos = getAbsoluteAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            Dispatch dispatch = db.getDispatchFromData(dispatchFirst.getText().toString(), dispatchLast.getText().toString(), dispatchUsername.getText().toString());
                            dispatchRecyclerViewInterface.onDispatchClick(dispatch);
                        }
                    }
                }
            });
        }
    }

}
