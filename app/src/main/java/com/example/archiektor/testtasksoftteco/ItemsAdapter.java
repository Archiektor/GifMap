package com.example.archiektor.testtasksoftteco;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Archiektor on 17.06.2017.
 */


public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView idTV;
        public TextView titleTV;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            idTV = (TextView) itemView.findViewById(R.id.id);
            titleTV = (TextView) itemView.findViewById(R.id.title);
        }
    }

    private List<Items> mItems;
    private Context mContext;

    public ItemsAdapter(Context context, List<Items> items) {
        mContext = context;
        mItems = items;
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_row, parent, false);
        //View contactView = inflater.inflate(R.layout.card_item_row, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Items item = mItems.get(position);

        // Set item views based on your views and data model
        TextView textView1 = viewHolder.idTV;
        textView1.setText(item.getId());
        TextView textView2 = viewHolder.titleTV;
        textView2.setText(item.getTitle());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}