package com.example.archiektor.testtasksoftteco;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.List;

class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    private List<Items> mItems;
    private Context mContext;

    private DisplayMetrics metrics = new DisplayMetrics();


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView idTV;
        TextView titleTV;

        ViewHolder(View itemView) {
            super(itemView);

            idTV = (TextView) itemView.findViewById(R.id.id);
            titleTV = (TextView) itemView.findViewById(R.id.title);
        }
    }

    ItemsAdapter(Context context, List<Items> items) {
        mContext = context;
        mItems = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_row, parent, false);

        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        // Get the data model based on position
        Items item = mItems.get(position);

        // Set item views based on your views and data model
        TextView textView1;
        TextView textView2;

        int screenWidth;
        float itemWidth;
        if (android.os.Build.VERSION.SDK_INT >= 17) {
            screenWidth = getScreenWidth();
            itemWidth = (float) ((screenWidth / 3) * 0.75);
        } else {
            WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(metrics);
            screenWidth = metrics.widthPixels;

            itemWidth = (float) ((screenWidth / 3) * 0.72);
        }

        textView1 = viewHolder.idTV;
        textView1.setWidth(Math.round(itemWidth));
        textView1.setText(item.getId());
        textView2 = viewHolder.titleTV;
        textView2.setText(item.getTitle());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    private static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

}

