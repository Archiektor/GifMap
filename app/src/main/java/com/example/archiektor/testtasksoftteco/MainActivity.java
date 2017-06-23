package com.example.archiektor.testtasksoftteco;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class MainActivity extends Activity {

    private ImageButton button;
    private GifView gifView;
    private List<Items> mItems;

    private List<Items> inetItems;

    private TextView textView;

    private int overallXScroll = 0;

    private RecyclerView rvItems;

    private ItemsAdapter adapter;

    private static final String ENDPOINT = "http://jsonplaceholder.typicode.com/posts";
    private RequestQueue requestQueue;

    //final CountDownLatch countDownLatch = new CountDownLatch(1);
    //final Object[] responseHolder = new Object[1];

    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inetItems = new ArrayList<>();

        requestQueue = Volley.newRequestQueue(this);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        fetchPosts();

        button = (ImageButton) findViewById(R.id.imageLogcat);
        gifView = (GifView) findViewById(R.id.gifView);
        gifView = new GifView(this);
        textView = (TextView) findViewById(R.id.indicator);

        final RecyclerView rvItems = (RecyclerView) findViewById(R.id.rvItems);

        //mItems = Items.createList(15);

        /**try {
         while (ContainsAllNulls(inetItems)) {
         wait(10000);
         }
         } catch (InterruptedException e) {
         e.printStackTrace();
         }*/

        adapter = new ItemsAdapter(getApplicationContext(), inetItems);

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
                Toast.makeText(MainActivity.this, "Single Click on position: " + (position + 1),
                        Toast.LENGTH_SHORT).show();
                Items intentItem = inetItems.get(position);
                int userId = Integer.parseInt(intentItem.getUserId());
                int id = Integer.parseInt(intentItem.getId());

                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("id", id);
                startActivity(intent);

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

    private void fetchPosts() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(ENDPOINT, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    if (response.length() > 0) {
                        inetItems.clear();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);
                            Items item = new Items();
                            if (!jsonObject.isNull("userId")) {
                                item.setUserId(String.valueOf(jsonObject.getInt("userId")));
                            }
                            if (!jsonObject.isNull("id")) {
                                item.setId(String.valueOf(jsonObject.getString("id")));
                            }
                            if (!jsonObject.isNull("title")) {
                                item.setTitle(jsonObject.getString("title"));
                            }
                            Log.i("object item = ", item.toString());
                            inetItems.add(i, item);
                        }
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // do something
            }
        });

        requestQueue.add(jsonArrayRequest);

        /**StringRequest request = new StringRequest(Request.Method.GET, ENDPOINT, onPostsLoaded, onPostsError);

         Log.i("Fetch method", "in fetch method");
         requestQueue.add(request);*/

        /**try {
         countDownLatch.await();
         } catch (InterruptedException e) {
         throw new RuntimeException(e);
         }
         if (responseHolder[0] instanceof VolleyError) {
         final VolleyError volleyError = (VolleyError) responseHolder[0];
         //TODO: Handle error...
         } else {
         final String response = (String) responseHolder[0];
         //TODO: Handle response...
         Log.i("After countdown", response);
         inetItems = Arrays.asList(gson.fromJson(response, Items[].class));
         }
         //requestQueue.notifyAll();*/
    }

    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {

            Log.i("PostActivity here first", response);

            /**responseHolder[0] = response;
             countDownLatch.countDown();*/

            inetItems = Arrays.asList(gson.fromJson(response, Items[].class));

            Log.i("PostActivity", inetItems.size() + " items loaded.");
            for (Items post : inetItems) {
                Log.i("PostActivity", "userId: " + post.getUserId() + "id: " + post.getId() + "title: " + post.getTitle());
            }

        }
    };


    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("PostActivity", error.toString());

            /** responseHolder[0] = error;
             countDownLatch.countDown();*/
        }
    };

    public static Boolean ContainsAllNulls(List<Items> list) {
        if (list != null) {
            for (Items a : list)
                if (a != null) return false;
        }

        return true;
    }
}