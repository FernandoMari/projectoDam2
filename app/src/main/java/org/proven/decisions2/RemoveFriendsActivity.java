package org.proven.decisions2;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RemoveFriendsActivity extends Activity {

    Button btFriends, btHome, btSettings;
    EditText searchFriend;
    ListView listFriend;
    CustomListAdapter mFriendsAdapter;
    ArrayList<String> friendsNames = new ArrayList<>();
    String token;

    String url = "http://143.47.249.102:7070/getFriends";
    String url2 = "http://143.47.249.102:7070/getUserId";
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remove_friends_layout);
        //Initialize the elements
        initializeElements();
        //Call the method
        readUser();
        //Call the method
        getFriends(token);

        btHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RemoveFriendsActivity.this, SocialInterface.class);
                readUser();
                Log.d("TAG", "userIdFriendsActivity: " + userId);
                startActivity(intent);
            }
        });
        btFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RemoveFriendsActivity.this, FriendsActivity.class);
                readUser();
                Log.d("TAG", "userIdFriendsActivity: " + userId);
                startActivity(intent);
            }
        });

        btSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RemoveFriendsActivity.this, SettingsActivity.class);
                readUser();
                Log.d("TAG", "userIdFriendsActivity: " + userId);
                startActivity(intent);
            }
        });

    }

    /*Initialize the elements*/
    private void initializeElements() {
        btHome = findViewById(R.id.btHome);
        btFriends = findViewById(R.id.btFriends);
        btSettings = findViewById(R.id.btSettings);
        listFriend = findViewById(R.id.lvPersons);
        searchFriend = findViewById(R.id.etSearch);
    }

    /*Method for the get friends*/
    private void getFriends(String username) {
        //new userIdAsyncTask().execute(username);
        new FriendsAsyncTask().execute(username);
    }
    /*Method to show friends list */
    private void setList(ArrayList<String> friendsList) {
        mFriendsAdapter = new CustomListAdapter(this, friendsList, R.layout.remove_friends_layout);
        listFriend.setAdapter(mFriendsAdapter);
        searchFriend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mFriendsAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    /*Method to execute the post requests for the friends*/
    private class FriendsAsyncTask extends AsyncTask<String, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            token = params[0];
            ArrayList<String> friendsList = new ArrayList<>();
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            if (token != null) {
                String requestBodyString = "username=" + userId;
                RequestBody requestBody = RequestBody.create(mediaType, requestBodyString);
                Request request = new Request.Builder()
                        //.url("http://5.75.251.56:7070/getFriends")
                        .url(url)
                        .post(requestBody)
                        .addHeader("content-type", "application/json")
                        .addHeader("cache-control", "no-cache")
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    String toSplit = response.body().string();
                    String textoSinComillas = toSplit.replace("\"", "");
                    String textoSinCorchetes = textoSinComillas.replace("[", "").replace("]", "");
                    friendsList.addAll(Arrays.asList(textoSinCorchetes.split(",")));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return friendsList;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            super.onPostExecute(result);
            friendsNames = result;
            setList(friendsNames);
        }
    }

//    private class userIdAsyncTask extends AsyncTask<String, Void, ArrayList<String>> {
//        @Override
//        protected ArrayList<String> doInBackground(String... params) {
//            userName = params[0];
//            ArrayList<String> friendsList = new ArrayList<>();
//            OkHttpClient client = new OkHttpClient();
//            MediaType mediaType = MediaType.parse("application/json");
//            if (userName != null) {
//                String requestBodyString = "username=" + userName;
//                System.out.println("parametro FriendsActivity" + requestBodyString);
//                RequestBody requestBody = RequestBody.create(mediaType, requestBodyString);
//                Request request = new Request.Builder()
//                        //.url("http://5.75.251.56:7070/getUserId")
//                        .url(url2)
//                        .post(requestBody)
//                        .addHeader("content-type", "application/json")
//                        .addHeader("cache-control", "no-cache")
//                        .build();
//
//                try {
//                    Response response = client.newCall(request).execute();
//                    int ur = Integer.parseInt(response.body().string());
//                    userId = ur;
//                    System.out.println("id" + ur);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            return friendsList;
//        }
//
//        @Override
//        protected void onPostExecute(ArrayList<String> result) {
//            super.onPostExecute(result);
//            friendsNames = result;
//            setList(friendsNames);
//        }
//    }

    /*Method to read the login token for use in the activity*/
    private void readUser() {
        File filename = new File(getFilesDir(), "token.txt");
        try {
            FileReader reader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(reader);
            token = bufferedReader.readLine();
            bufferedReader.close();
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}