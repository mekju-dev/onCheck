package com.wgu.testing123;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DriverAdapter extends RecyclerView.Adapter<DriverAdapter.DriverViewHolder> {
    private List<Driver> driverList;
    private Context context;
    public TodayDriverRecyclerViewInterface todayDriverRecyclerViewInterface;
    public dbHelper dbHelper;


    String test;
    public DriverAdapter(List<Driver> driverList, TodayDriverRecyclerViewInterface todayDriverRecyclerViewInterface) {
        this.driverList = driverList;
        this.todayDriverRecyclerViewInterface = todayDriverRecyclerViewInterface;
    }

    public void setFilteredList(List<Driver> filteredList){
        this.driverList = filteredList;
        notifyDataSetChanged();
    }

    //public void setFilteredDBList(List<>)

    /**
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return
     */
    @NonNull
    @Override
    public DriverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View driverView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_row_driver, parent,false);
        context = parent.getContext();
        return new DriverViewHolder(driverView, todayDriverRecyclerViewInterface);
        //return new DriverViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_view_row_driver, parent, false), todayDriverRecyclerViewInterface);
    }

    /**
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull DriverViewHolder holder, int position) {
        Driver driver = driverList.get(position);

        if (driver.getWorkToday() == 0) {
            holder.check.setImageResource(R.drawable.baseline_close_24);
        }
        //these aren't aligning. something in the last half of each statement
        holder.driverFirst.setText((driverList.get(position)).getFirstName());
        holder.driverLast.setText((driverList.get(position)).getLastName());
        holder.driverPhone.setText((driverList.get(position)).getPhone());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todayDriverRecyclerViewInterface.onDriverClick(driver);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                todayDriverRecyclerViewInterface.onDriverHold(driver);
                return false;
            }
        });

    }

    /**
     * @return
     */
    @Override
    public int getItemCount() {
        if(driverList==null) {return 0;}
        else {
            return driverList.size();
        }
    }

    public void setData(List<Driver> newData) {
        if (driverList == null || driverList.size() == 0) {
            this.driverList = new ArrayList<Driver>();
        }else {
            driverList.clear();
        }
        if (newData == null ){
            Toast.makeText(context, "No drivers for today", Toast.LENGTH_SHORT).show();
        }
        else if (newData.size() > 0) {
            driverList.addAll(newData);
        }

    }
    //VIEW HOLDER
    public class DriverViewHolder extends RecyclerView.ViewHolder{
        ImageView check;
        TextView driverFirst, driverLast, driverPhone;

        public DriverViewHolder(@NonNull View itemView, TodayDriverRecyclerViewInterface todayDriverRecyclerViewInterface) {
            super(itemView);
            check = itemView.findViewById(R.id.driverRecyclerImage);
            driverFirst = itemView.findViewById(R.id.driverRecyclerFirstName);
            driverLast = itemView.findViewById(R.id.driverRecyclerLastName);
            driverPhone = itemView.findViewById(R.id.driverRecyclerPhone);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (todayDriverRecyclerViewInterface != null){
                        int pos = getAbsoluteAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            Driver driver = dbHelper.getDriverFromData(driverFirst.getText().toString(), driverLast.getText().toString(), driverPhone.getText().toString());
                            todayDriverRecyclerViewInterface.onDriverClick(driver);
                        }
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (todayDriverRecyclerViewInterface != null){
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            Driver driver = dbHelper.getDriverFromData(driverFirst.getText().toString(), driverLast.getText().toString(), driverPhone.getText().toString());
                            todayDriverRecyclerViewInterface.onDriverHold(driver);
                        }
                    }
                    return true;
                }
            });
        }
    }
}
