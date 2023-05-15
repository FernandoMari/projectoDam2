package org.proven.decisions2.Games;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.proven.decisions2.Friends.CustomListAdapter;
import org.proven.decisions2.Friends.FriendsActivity;
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

public class PlayOnlineActivity extends Activity {

    TextView infoRequests, infoConnect;
    Button btHome, btSettings, btFriends;
    ListView listFriend,listOfPetitions;
    CustomListAdapter mFriendsAdapter;
    String selectedUsername;
    String url = "http://143.47.249.102:7070/getFriends";
    String url2 = "http://143.47.249.102:7070/getNameOfUser";



    ArrayList<String> roomList;
    ArrayList<String> friendList;
    String username;
    boolean rivalFound, waiting=false;

    String playerName = "";
    String verify = "";
    String roomName = "";
    ProgressDialog dialog;



    FirebaseDatabase database;
    DatabaseReference playerRef;
    DatabaseReference roomRef;
    DatabaseReference toCompare;
    DatabaseReference checkCanPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_online_layout);

        readUser();

        initelements();

        initButtons();

        getFriends(username);

        getsPlayer();
        getRooms();

    }

    private void initelements(){
        infoConnect = findViewById(R.id.infoConnect);
        infoRequests = findViewById(R.id.infoRequests);
        btHome = findViewById(R.id.btHome);
        btSettings = findViewById(R.id.btSettings);
        btFriends = findViewById(R.id.btFriends);
        database = FirebaseDatabase.getInstance();
        roomList = new ArrayList<>();
        friendList = new ArrayList<>();
        listFriend = findViewById(R.id.lvConnect);
        //cambiar a false
        rivalFound = false;
        listOfPetitions = findViewById(R.id.lvAccept);
        dialog = new ProgressDialog(this);
    }

    private void initButtons(){
        btHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PlayOnlineActivity.this, SocialInterface.class));
            }
        });

        btSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PlayOnlineActivity.this, SettingsActivity.class));
            }
        });

        btFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PlayOnlineActivity.this, FriendsActivity.class));
            }
        });

    }



    private void checkPlayerExists(){
        SharedPreferences preferences = getSharedPreferences("PREFS",0);
        verify = preferences.getString("playerName","");

        System.out.println("nombre: "+playerName);

        if(!verify.equals("")){
            playerRef = database.getReference("player/"+playerName);
            addEventListener();
            playerRef.setValue("");
        }else{
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("playerName",playerName);
            editor.commit();
        }
    }

    private void getsPlayer(){
        playerRef = database.getReference("player/"+playerName);
        addEventListener();
        playerRef.setValue("");

        System.out.println(playerName);
    }

    private void assignPlayerToRoom(){
        roomRef = database.getReference("rooms/"+roomName+"/player1");
        addRoomEventListener();
        roomRef.setValue(playerName);
    }

    private void asignSecondPlayer(String playN){
        roomRef = database.getReference("rooms/"+roomName+"/player2");
        addRoomEventListener();
        roomRef.setValue(playN);
    }




    private void addEventListener(){
        playerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!playerName.equals("")){
                    SharedPreferences preferences = getSharedPreferences("PREFS",0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("playerName",playerName);
                    editor.apply();

                    System.out.println(playerName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addRoomEventListener(){
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Error creating room");
            }
        });
    }

    private void getRooms(){
        roomRef = database.getReference("rooms");
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                roomList.clear();
                Iterable<DataSnapshot> rooms = snapshot.getChildren();

                for(DataSnapshot dataS : rooms){
                    toCompare = database.getReference("rooms/"+dataS.getKey()+"/player2");
                    addCompareListener(dataS.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addCompareListener(String things){
        toCompare.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(playerName != null && snapshot.getValue(String.class) != null){
                    if(snapshot.getValue(String.class).equalsIgnoreCase(playerName)){
                        roomList.add(things);
                    }
                }
                setListOfPetitions(roomList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setCheckCanPlay(String roomNa){
        checkCanPlay.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue(String.class) != null){
                    if(snapshot.getValue(String.class).contains("waiting")){
                        rivalFound = false;

                    } else if(snapshot.getValue(String.class).contains("start")){

                        Intent intent = new Intent(getApplicationContext(), PenaltiesGameOnline.class);
                        intent.putExtra("roomName", roomNa);
                        startActivity(intent);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void setList(ArrayList<String> friendsList) {
        mFriendsAdapter = new CustomListAdapter(this,friendsList, R.layout.list_item_send);
        listFriend.setAdapter(mFriendsAdapter);

        if (friendsList.isEmpty() || friendsList.size() == 0 || friendsList.contains("")){
            listFriend.setVisibility(View.GONE);
            infoConnect.setVisibility(View.VISIBLE);

        }else{
            listFriend.setVisibility(View.VISIBLE);
            infoConnect.setVisibility(View.GONE);
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

                AlertDialog.Builder builder= new AlertDialog.Builder(PlayOnlineActivity.this);
                builder.setTitle(R.string.btConfirm);
                builder.setMessage(getString(R.string.play_with) + " " + selectedUsername +"?");
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        assignPlayerToRoom();
                        asignSecondPlayer(selectedUsername);

                        PlayOnlineActivity.this.dialog.setMessage("Waiting for the opponent to accept");
                        PlayOnlineActivity.this.dialog.setCanceledOnTouchOutside(false);
                        PlayOnlineActivity.this.dialog.show();

                        checkCanPlay = database.getReference("rooms/"+roomName+"/status");
                        setCheckCanPlay(roomName);
                        checkCanPlay.setValue("waiting");
                        System.out.println("Check can play: "+checkCanPlay);
                        waiting=true;


                        if (rivalFound){
                            PlayOnlineActivity.this.dialog.dismiss();
                            checkCanPlay.setValue("");
                            System.out.println("Rival found");
                        }

                        PlayOnlineActivity.this.dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialogInterface) {
                                onBackPressed();
                            }
                        });


                    }
                });
                builder.setNegativeButton(R.string.no,null);
                builder.show();

            }


        });
    }


    private void setListOfPetitions(ArrayList<String> friendsList) {
        mFriendsAdapter = new CustomListAdapter(this,friendsList, R.layout.list_item_request);
        listOfPetitions.setAdapter(mFriendsAdapter);

        if (friendsList.isEmpty() || friendsList.size() == 0 || friendsList.contains("")){

            listOfPetitions.setVisibility(View.GONE);
            infoRequests.setVisibility(View.VISIBLE);

        }else{
            listOfPetitions.setVisibility(View.VISIBLE);
            infoRequests.setVisibility(View.GONE);
        }
        listOfPetitions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the username of the selected item in the list
                selectedUsername = (String) parent.getItemAtPosition(position);
                Log.d("Selected username", selectedUsername);

                if (selectedUsername == null) {
                    Toast.makeText(getApplicationContext(), "Please select a username", Toast.LENGTH_SHORT).show();
                    return;
                }
                AlertDialog.Builder builder= new AlertDialog.Builder(PlayOnlineActivity.this);
                builder.setTitle(R.string.btConfirm);
                builder.setMessage(" Play with "+ selectedUsername +"?");
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // introducir codigo de ver el estado
                        checkCanPlay = database.getReference("rooms/"+selectedUsername+"/status");
                        setCheckCanPlay(selectedUsername);
                        checkCanPlay.setValue("start");
                    }
                });
                builder.setNegativeButton(R.string.no,null);
                builder.show();

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (checkCanPlay == null || checkCanPlay.equals("")){
            super.onBackPressed();
        } else if(waiting==true){
            deleteRoom(roomName);
            PlayOnlineActivity.this.dialog.dismiss();
            waiting = !waiting;
            System.out.println("Waiting canceled");
        }
    }

    private void getFriends(String username) {
        new GetUserName().execute(username);
        new PlayFriend().execute(username);
    }

    private class PlayFriend extends AsyncTask<String, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(String... params) {
            username = params[0];
            ArrayList<String> friendsList = new ArrayList<>();
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            if (username != null) {
                String requestBodyString = "";
                RequestBody requestBody = RequestBody.create(mediaType, requestBodyString);
                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .addHeader("content-type", "application/json")
                        .addHeader("cache-control", "no-cache")
                        .addHeader("Authorization", username)
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
            friendList = result;
            setList(friendList);
        }
    }

    private class GetUserName extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            username = params[0];
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            if (username != null) {
                String requestBodyString = "";
                RequestBody requestBody = RequestBody.create(mediaType, requestBodyString);
                Request request = new Request.Builder()
                        .url(url2)
                        .post(requestBody)
                        .addHeader("content-type", "application/json")
                        .addHeader("cache-control", "no-cache")
                        .addHeader("Authorization", username)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    String toSplit = response.body().string();
                    String textoSinComillas = toSplit.replace("\"", "");
                    String textoSinCorchetes = textoSinComillas.replace("[", "").replace("]", "");

                    playerName = textoSinCorchetes;
                    checkPlayerExists();
                    roomName = playerName;

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    private void readUser() {
        File filename = new File(getFilesDir(), "token.txt");
        try {
            FileReader reader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(reader);
            username = bufferedReader.readLine();
            bufferedReader.close();
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void deleteRoom(String roomName) {
        database.getReference("rooms/" + roomName).removeValue();
    }

}