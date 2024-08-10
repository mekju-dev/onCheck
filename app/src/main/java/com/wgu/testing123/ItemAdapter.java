package com.wgu.testing123;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private List<Item> itemList;
    private Context context;
    public dbHelper dbHelper;

    private final ItemRecyclerViewInterface itemRecyclerViewInterface;

    public ItemAdapter(List<Item> itemList, ItemRecyclerViewInterface itemRecyclerViewInterface) {
        this.itemList = itemList;
        this.itemRecyclerViewInterface = itemRecyclerViewInterface;
    }

    public void setFilteredList(List<Item> filteredList){
        this.itemList = filteredList;
        notifyDataSetChanged();
    }

    /**
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return
     */
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_row_item, parent,false);
        return new ItemViewHolder(itemView, itemRecyclerViewInterface);
    }

    /**
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemList.get(position);

        //holder.check.setImageResource(R.drawable.baseline_close_24);


        holder.itemName.setText((itemList.get(position)).getItem());
        holder.itemDescription.setText((itemList.get(position).getDescription()));
        holder.itemQuantity.setText(Integer.toString((itemList.get(position)).getQuantity()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemRecyclerViewInterface.onItemClick(item);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                itemRecyclerViewInterface.onItemHold(item);
                return false;
            }
        });
    }

    /**
     * @return
     */
    @Override
    public int getItemCount() {
        if(itemList==null) return 0;
        return itemList.size();
    }

    public void setData(List<Item> newData) {
        if (itemList == null || itemList.size() == 0) {
            this.itemList = new ArrayList<Item>();
        }else {
            itemList.clear();
        }
        if (newData.size() > 0) {
            itemList.addAll(newData);
        }
    }
    public List<Item> getItemList(){
        return this.itemList;
    }
    public class ItemViewHolder extends RecyclerView.ViewHolder{
        ImageView check;
        TextView itemName, itemDescription, itemQuantity;

        public ItemViewHolder(@NonNull View itemView, ItemRecyclerViewInterface itemRecyclerViewInterface) {
            super(itemView);
            //check = itemView.findViewById(R.id.itemRecyclerImage);
            itemName = itemView.findViewById(R.id.itemRecyclerItem);
            itemDescription = itemView.findViewById(R.id.itemRecyclerDescription);
            itemQuantity = itemView.findViewById(R.id.itemRecyclerQuantity);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemRecyclerViewInterface != null){
                        int pos = getAbsoluteAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            Item item = dbHelper.getItemFromData(itemName.getText().toString(), itemDescription.getText().toString(), Integer.parseInt(itemQuantity.getText().toString()));
                            itemRecyclerViewInterface.onItemClick(item);
                        }
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (itemRecyclerViewInterface != null){
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            Item item = dbHelper.getItemFromData(itemName.getText().toString(), itemDescription.getText().toString(), Integer.parseInt(itemQuantity.getText().toString()));
                            itemRecyclerViewInterface.onItemHold(item);
                        }
                    }
                    return true;
                }
            });
        }
    }
}

