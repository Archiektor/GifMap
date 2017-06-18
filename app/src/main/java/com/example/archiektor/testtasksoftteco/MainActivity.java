package com.example.archiektor.testtasksoftteco;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private ImageButton button;
    private GifView gifView;
    private ArrayList<Items> mItems;

    private TextView textView;

    private int overallXScroll = 0;

    private static final String ENDPOINT = "http://jsonplaceholder.typicode.com/posts";
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (ImageButton) findViewById(R.id.imageLogcat);
        gifView = (GifView) findViewById(R.id.gifView);

        textView = (TextView) findViewById(R.id.indicator);


        final RecyclerView rvItems = (RecyclerView) findViewById(R.id.rvItems);

        mItems = Items.createList(15);

        ItemsAdapter adapter = new ItemsAdapter(this, mItems);

        rvItems.setAdapter(adapter);

        final GridLayoutManager layoutManager2 = new GridLayoutManager(this, 2, LinearLayoutManager.HORIZONTAL, false);
        layoutManager2.scrollToPosition(0);

//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        layoutManager.scrollToPosition(0);

        rvItems.setLayoutManager(layoutManager2);

        rvItems.setHasFixedSize(true);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rvItems);

        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        rvItems.addItemDecoration(itemDecoration);

        rvItems.addOnItemTouchListener(new RecyclerTouchListener(this, rvItems, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                //Values are passing to activity & to fragment as well
                Toast.makeText(MainActivity.this, "Single Click on position: " + position,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(MainActivity.this, "Long press on position: " + position,
                        Toast.LENGTH_LONG).show();
            }
        }));

        rvItems.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //int firstVisibleItemPosition = ((GridLayoutManager) (rvItems.getLayoutManager())).findFirstVisibleItemPosition();
                //textView.setText(String.valueOf(firstVisibleItemPosition + " 1. OnSSC"));
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

//                overallXScroll = overallXScroll + dx;
//                String coord = String.valueOf(overallXScroll);
//                textView.setText(coord);


                int firstVisibleItemPosition = ((GridLayoutManager) (rvItems.getLayoutManager())).findFirstVisibleItemPosition();

                textView.setText(String.valueOf(firstVisibleItemPosition + 1));
            }
        });
    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener) {

            this.clicklistener = clicklistener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recycleView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clicklistener != null) {
                        clicklistener.onLongClick(child, recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clicklistener != null && gestureDetector.onTouchEvent(e)) {
                clicklistener.onClick(child, rv.getChildAdapterPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }


    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.i("PostActivity", response);
        }
    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("PostActivity", error.toString());
        }
    };

}
