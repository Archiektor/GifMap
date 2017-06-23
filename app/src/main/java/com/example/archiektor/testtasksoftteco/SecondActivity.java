package com.example.archiektor.testtasksoftteco;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.Double2;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SecondActivity extends AppCompatActivity {

    private int userId;
    private int id;

    private TextView toolbarTitle;
    private TextView idPost;

    private TextView userName;
    private TextView nickName;

    private TextView email;
    private TextView webSite;
    private TextView phone;
    private TextView city;

    private ImageButton btnSave;

    RequestQueue queue;
    String url = "http://jsonplaceholder.typicode.com/posts";

    private String nameStr;
    private String nickNameStr;
    private String emailStr;
    private String phoneStr;
    private String websiteStr;
    private String cityStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            userId = extras.getInt("userId");
            id = extras.getInt("id");
        }

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Contact # " + userId);

        idPost = (TextView) findViewById(R.id.idPost);
        idPost.setText(String.valueOf(id));

        userName = (TextView) findViewById(R.id.userName);
        nickName = (TextView) findViewById(R.id.nickName);

        email = (TextView) findViewById(R.id.email);
        webSite = (TextView) findViewById(R.id.webSite);
        phone = (TextView) findViewById(R.id.phone);
        city = (TextView) findViewById(R.id.userCity);

        btnSave = (ImageButton) findViewById(R.id.btnSave);

        queue = Volley.newRequestQueue(this);
        url = "http://jsonplaceholder.typicode.com/users/" + userId;

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response

                        Log.i("Response", response.toString());
                        try {

                            nameStr = response.getString("name");
                            userName.setText(nameStr);

                            nickNameStr = response.getString("username");
                            nickName.setText(nickNameStr);


                            emailStr = response.getString("email");
                            Spannable spanEmail = new SpannableString("email: " + emailStr);
                            int emailStrLength = spanEmail.length();
                            spanEmail.setSpan(new UnderlineSpan(), 7, emailStrLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            spanEmail.setSpan(new ForegroundColorSpan(Color.DKGRAY), 7, emailStrLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            Intent intentSendEmail = new Intent(Intent.ACTION_SEND);
                            intentSendEmail.setType("text/plain");
                            intentSendEmail.putExtra(Intent.EXTRA_SUBJECT, "Content subject");
                            intentSendEmail.putExtra(Intent.EXTRA_TEXT, "Content text");
                            //startActivity(Intent.createChooser(shareIntent, "Sharing something."));
                            setClicable(String.valueOf(spanEmail), 7, emailStrLength, intentSendEmail, email);
                            //email.setText(spanEmail);

                            phoneStr = response.getString("phone");
                            phone.setText("phone: " + phoneStr);
                            int phoneStrLength = new String("phone: " + phoneStr).length();
                            Intent intentCall = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneStr));
                            setClicable(("phone: " + phoneStr), 7, phoneStrLength, intentCall, phone);

                            websiteStr = response.getString("website");
                            webSite.setText("website: " + websiteStr);
                            int webStrLength = new String("website: " + websiteStr).length();
                            Intent intentWeb = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + websiteStr));
                            setClicable(("website: " + websiteStr), 7, webStrLength, intentWeb, webSite);

                            JSONObject address = response.getJSONObject("address");
                            cityStr = address.getString("city");
                            city.setText("city: " + cityStr);

                            JSONObject geo = address.getJSONObject("geo");
                            Double latStr = Double.parseDouble(geo.getString("lat"));
                            Log.i("latStr = ", String.valueOf(latStr));
                            Double lngStr = Double.parseDouble(geo.getString("lng"));
                            Log.i("lngStr = ", String.valueOf(lngStr));

                            int geoStrString = new String("city: " + cityStr).length();
                            Intent intentMap = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:" + latStr + "," + lngStr + "?z=17"));
                            intentMap.setPackage("com.google.android.apps.maps");
                            setClicable(("city: " + cityStr), 6, geoStrString, intentMap, city);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Error.Response", String.valueOf(error));
                    }
                }
        );

// add it to the RequestQueue
        queue.add(getRequest);
    }

    private void setClicable(String string, int start, int end, final Intent intent, TextView textView) {
        SpannableString ss = new SpannableString(string);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        //TextView textView = (TextView) findViewById(R.id.hello);
        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setHighlightColor(Color.TRANSPARENT);
    }

    public void saveToDb(View view) {
        //deleteDatabase("softtecoDb.db");

        DbHelper databaseHelper;
        databaseHelper = new DbHelper(this);

        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        DbHelper.insertData(db, nameStr, emailStr, websiteStr, phoneStr, cityStr);
    }

}
