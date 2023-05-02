package org.proven.decisions2.Friends;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.proven.decisions2.R;
import org.proven.decisions2.Settings.SettingsActivity;
import org.proven.decisions2.SocialInterface;

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
    //The buttons of the footer to navigate in the app
    Button btFriends, btHome, btSettings;
    //Text to inform the user
    TextView infoText;
    //Filter for search the user in the list
    EditText searchFriend;
    //Listview shows a list of friends
    ListView listFriend;
    //hide the line
    View line;
    //CustomListAdapter is a custom class that extends Android's default list adapter. It is used to customize the appearance of each item in the friends list.
    CustomListAdapter mFriendsAdapter;
    //This the list the friends
    ArrayList<String> friendsNames = new ArrayList<>();
    //User authentication token
    String token;
    //User selected in friend list
    String selectedUsername;
    //Url for the http post in get friends
    String url = "http://143.47.249.102:7070/getFriends";
    //Url for the http post in remove friend
    String url2 = "http://143.47.249.102:7070/removeFriend";


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
                Log.d("TAG", "userIdFriendsActivity: " + token);
                startActivity(intent);
            }
        });
        btFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RemoveFriendsActivity.this, FriendsActivity.class);
                readUser();
                Log.d("TAG", "userIdFriendsActivity: " + token);
                startActivity(intent);
            }
        });

        btSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RemoveFriendsActivity.this, SettingsActivity.class);
                readUser();
                Log.d("TAG", "userIdFriendsActivity: " + token);
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
        infoText = findViewById(R.id.infoText);
        line = findViewById(R.id.line);
    }

    /*Method for the get friends*/
    private void getFriends(String username) {
        new FriendsAsyncTask().execute(username);
    }

    /*Method to show friends list */
    private void setList(ArrayList<String> friendsList) {
        mFriendsAdapter = new CustomListAdapter(this, friendsList, R.layout.list_item_remove);
        listFriend.setAdapter(mFriendsAdapter);
        //check the list friend is empty or size is zero or contains is a empty string
        if (friendsList.isEmpty() || friendsList.size() == 0 || friendsList.contains("")) {
            //list is gone
            listFriend.setVisibility(View.GONE);
            //info text is visible
            infoText.setVisibility(View.VISIBLE);
            //search friend is gone
            searchFriend.setVisibility(View.GONE);
            //line is gone
            line.setVisibility(View.INVISIBLE);

        } else {
            //list is visible
            listFriend.setVisibility(View.VISIBLE);
            //info text is gone
            infoText.setVisibility(View.GONE);
            //search friend is visible
            searchFriend.setVisibility(View.VISIBLE);
            //line is visible
            line.setVisibility(View.VISIBLE);
        }
        listFriend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the username of the selected item in the list
                selectedUsername = (String) parent.getItemAtPosition(position);
                Log.d("Selected username", selectedUsername);

                if (selectedUsername == null) {
                    Toast.makeText(getApplicationContext(), "Please select a username", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Dialog for the user confirm
                AlertDialog.Builder builder = new AlertDialog.Builder(RemoveFriendsActivity.this);
                builder.setTitle(getString(R.string.btConfirm));
                builder.setMessage(R.string.confirm_remove);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Send the friend request
                        new removeFriendTask().execute();
                    }
                });
                builder.setNegativeButton(R.string.no, null);
                builder.show();

            }

        });

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
                String requestBodyString = "username=" + token;
                RequestBody requestBody = RequestBody.create(mediaType, requestBodyString);
                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .addHeader("content-type", "application/json")
                        .addHeader("cache-control", "no-cache")
                        .addHeader("Authorization", token)
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

    /*Method to execute the post delete for the friend*/
    private class removeFriendTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {

            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody requestBody = RequestBody.create(mediaType, "username=" + selectedUsername);

            Request request = new Request.Builder()
                    .url(url2)
                    .post(requestBody)
                    .addHeader("content-type", "application/json")
                    .addHeader("Authorization", token)
                    .build();

            // Send HTTP POST friend request
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    return true;
                } else {
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                Toast.makeText(getApplicationContext(), getString(R.string.friend_deleted) + selectedUsername, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Error delete friend  " + selectedUsername, Toast.LENGTH_SHORT).show();
            }
        }
    }


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
