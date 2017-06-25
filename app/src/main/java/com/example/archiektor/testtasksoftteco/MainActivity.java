package com.example.archiektor.testtasksoftteco;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static com.example.archiektor.testtasksoftteco.R.attr.layoutManager;

public class MainActivity extends Activity {

    private ImageButton button;
    //private GifView gifView;
    private Animation animation;
    private ImageView imageView;
    private List<Items> mItems;

    private List<Items> inetItems;

    private TextView textView;

    private int overallXScroll = 0;

    private RecyclerView rvItems;

    private ItemsAdapter adapter;

    private static final String ENDPOINT = "http://jsonplaceholder.typicode.com/posts";
    private RequestQueue requestQueue;

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
        button.setVisibility(View.INVISIBLE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeLogCat();
            }
        });

        imageView = (ImageView) findViewById(R.id.image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.combo);

                imageView.startAnimation(animation);

                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        button.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

            }
        });
        //gifView = (GifView) findViewById(R.id.gifView);
        //gifView = new GifView(this);
        textView = (TextView) findViewById(R.id.indicator);


        final RecyclerView rvItems = (RecyclerView) findViewById(R.id.rvItems);

        adapter = new ItemsAdapter(getApplicationContext(), inetItems);

        rvItems.setAdapter(adapter);


        final GridLayoutManager layoutManager2 = new GridLayoutManager(this, 2, LinearLayoutManager.HORIZONTAL, false);
        layoutManager2.scrollToPosition(0);

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
//                Toast.makeText(MainActivity.this, "Single Click on position: " + (position + 1),
//                        Toast.LENGTH_SHORT).show();
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
//                Toast.makeText(MainActivity.this, "Long press on position: " + position,
//                        Toast.LENGTH_LONG).show();
            }
        }));

        rvItems.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

//                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
//                    //Dragging
//                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    reviewPosition = ((GridLayoutManager) (rvItems.getLayoutManager())).findFirstVisibleItemPosition();


            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int offset = recyclerView.computeHorizontalScrollOffset();
                /**int extent = recyclerView.computeHorizontalScrollExtent();
                 int range = recyclerView.computeHorizontalScrollRange();

                 int percentage = (int)(100.0 * offset / (float)(range - extent));*/

                int currentPosition = ((GridLayoutManager) (rvItems.getLayoutManager())).findFirstVisibleItemPosition();

                int modifiedPosition = currentPosition + 6;
                int previousPosition = modifiedPosition - 1;

                if ((currentPosition == 0) && (offset == 0)) {
                    textView.setText("3/4");
                } else if ((offset > 0) && (currentPosition == 0)) {
                    textView.setText("5/6");
                } else {
                    textView.setText(String.valueOf((previousPosition) + "/" + (modifiedPosition)));
                }

                //textView.setText(String.valueOf((currentPosition) + "/" + (offset)));
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

        }
    };

    public static Boolean ContainsAllNulls(List<Items> list) {
        if (list != null) {
            for (Items a : list)
                if (a != null) return false;
        }

        return true;
    }

    protected void writeLogCat() {
        try {
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder log = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                log.append(line);
                log.append("\n");
            }

            //Convert log to string
            final String logString = new String(log.toString());

            //Create txt file in SD Card
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath() + File.separator + "Log File");

            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File(dir, "logcat.txt");

            //To write logcat in text file
            FileOutputStream fout = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fout);

            //Writing the string to file
            osw.write(logString);
            osw.flush();
            osw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}