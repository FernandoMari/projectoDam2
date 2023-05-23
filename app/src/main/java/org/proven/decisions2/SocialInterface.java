package org.proven.decisions2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.proven.decisions2.Friends.FriendsActivity;
import org.proven.decisions2.Games.ChooseModality;
import org.proven.decisions2.SeePost.VerticalViewPager;
import org.proven.decisions2.SeePost.ViewPagerAdapter;
import org.proven.decisions2.Settings.SettingsActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class SocialInterface extends FragmentActivity {
    private VerticalViewPager viewPager;
    private ViewPagerAdapter viewAdapter;
    // The buttons to navigate in the app
    Button btFriends, btDecisions, btSettings;
    // User authentication token
    String token;
    TextView decisions;
    String username="ismael3";
    SecureConnection secureConnection = new SecureConnection();
    private int[] imageIds; // Arreglo de IDs de imágenes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_interface);
        // Initialize the elements
        initializeElements();

        // Solicitar permiso WRITE_EXTERNAL_STORAGE en tiempo de ejecución si no está concedido
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            //call the method
            readUser();
        }

        // Descargar las imágenes antes de establecer el adaptador en el ViewPager
        getPhotos();

        btFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SocialInterface.this, FriendsActivity.class);
                readUser();
                Log.d("TAG", "userIdSocial: " + token);
                startActivity(intent);
                finish();
            }
        });

        btDecisions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SocialInterface.this, ChooseModality.class));
            }
        });

        btSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SocialInterface.this, SettingsActivity.class);
                Log.d("TAG", "userIdSocial: " + token);
                startActivity(intent);
                finish();
            }
        });
    }

    /* Initialize the elements */
    private void initializeElements() {
        viewPager = (VerticalViewPager) findViewById(R.id.viewPager);
        viewAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewAdapter);
        btFriends = findViewById(R.id.btFriends);
        btDecisions = findViewById(R.id.btDecisions);
        btSettings = findViewById(R.id.btSettings);
        decisions = findViewById(R.id.decision);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, puedes acceder al archivo token.txt aquí
                readUser();

            } else {
                // Permiso denegado, maneja el escenario en consecuencia
            }
        }
    }

//    private void getPhotos() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                OkHttpClient client = new OkHttpClient();
//                Request request = new Request.Builder()
//                        .url("http://5.75.251.56:7070/imagen")
//                        .get()
//                        .build();
//
//                try {
//                    Response response = client.newCall(request).execute();
//                    if (response.isSuccessful()) {
//                        String jsonResponse = response.body().string();
//                        JSONArray jsonArray = new JSONArray(jsonResponse);
//
//                        List<Integer> ids = new ArrayList<>();
//                        List<String> decisions = new ArrayList<>(); // Lista para almacenar los textos de las decisiones
//
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject jsonObject = jsonArray.getJSONObject(i);
//                            int id = jsonObject.getInt("id");
//                            String decision = jsonObject.getString("decision");
//
//                            ids.add(id);
//                            decisions.add(decision);
//                        }
//
//                        // Actualizar el arreglo imageIds y notificar al adaptador
//                        imageIds = new int[ids.size()];
//                        for (int i = 0; i < ids.size(); i++) {
//                            imageIds[i] = ids.get(i);
//                        }
//                        updateAdapterWithImageIds();
//
//                        // Mostrar el texto de la decisión en el TextView (asumiendo que solo hay una imagen mostrada)
//                        if (!decisions.isEmpty()) {
//                            String decisionText = decisions.get(0);
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    SocialInterface.this.decisions.setText(decisionText);
//                                }
//                            });
//                        }
//                    } else {
//                        // Manejar la respuesta no exitosa
//                        System.out.println("Error en la respuesta: " + response.code() + " " + response.message());
//                    }
//                } catch (IOException | JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }).start();
//    }
//Esto funciona correctamente no borrar !!!!
//    private void getPhotos() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                OkHttpClient client = new OkHttpClient();
//                Request request = new Request.Builder()
//                        .url("http://5.75.251.56:7070/imagen")
//                        .get()
//                        .build();
//
//                try {
//                    Response response = client.newCall(request).execute();
//                    if (response.isSuccessful()) {
//                        String jsonResponse = response.body().string();
//                        JSONArray jsonArray = new JSONArray(jsonResponse);
//
//                        List<Integer> ids = new ArrayList<>();
//                        List<String> decisions = new ArrayList<>(); // Lista para almacenar los textos de las decisiones
//
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject jsonObject = jsonArray.getJSONObject(i);
//                            int id = jsonObject.getInt("id");
//                            String decision = jsonObject.getString("decision");
//
//                            ids.add(id);
//                            decisions.add(decision);
//                        }
//
//                        // Actualizar el arreglo imageIds y notificar al adaptador
//                        imageIds = new int[ids.size()];
//                        for (int i = 0; i < ids.size(); i++) {
//                            imageIds[i] = ids.get(i);
//                        }
//                        updateAdapterWithImageIds();
//
//                        // Mostrar el texto de la decisión correspondiente a la foto inicial
//                        if (!decisions.isEmpty()) {
//                            String decisionText = decisions.get(0);
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    SocialInterface.this.decisions.setText(decisionText);
//                                }
//                            });
//                        }
//
//                        // Establecer el listener para detectar cambios de página en el ViewPager
//                        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//                            @Override
//                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                                // No se requiere implementación
//                            }
//
//                            @Override
//                            public void onPageSelected(int position) {
//                                // Mostrar el texto de la decisión correspondiente a la foto seleccionada
//                                if (!decisions.isEmpty() && position < decisions.size()) {
//                                    String decisionText = decisions.get(position);
//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            SocialInterface.this.decisions.setText(decisionText);
//                                        }
//                                    });
//                                }
//                            }
//
//                            @Override
//                            public void onPageScrollStateChanged(int state) {
//                                // No se requiere implementación
//                            }
//                        });
//                    } else {
//                        // Manejar la respuesta no exitosa
//                        System.out.println("Error en la respuesta: " + response.code() + " " + response.message());
//                    }
//                } catch (IOException | JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }).start();
//    }


    private void getPhotos() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://5.75.251.56:7070/imagen")
                        .get()
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String jsonResponse = response.body().string();
                        JSONArray jsonArray = new JSONArray(jsonResponse);

                        List<Integer> ids = new ArrayList<>();
                        List<String> decisions = new ArrayList<>(); // Lista para almacenar los textos de las decisiones

                        // Step 1: Get the list of friends for the user
                        List<String> friendList = getFriends(username); // Replace 'userID' with the actual user ID

                        // Step 2: Filter the JSON array to include only photos from friends
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int id = jsonObject.getInt("id");
                            String decision = jsonObject.getString("decision");
                            String username = jsonObject.getString("username");

                            // Check if the username is in the friend list
                            if (friendList.contains(username)) {
                                ids.add(id);
                                decisions.add(decision);
                            }
                        }

                        // Actualizar el arreglo imageIds y notificar al adaptador
                        imageIds = new int[ids.size()];
                        for (int i = 0; i < ids.size(); i++) {
                            imageIds[i] = ids.get(i);
                        }
                        updateAdapterWithImageIds();

                        // Mostrar el texto de la decisión correspondiente a la foto inicial
                        if (!decisions.isEmpty()) {
                            String decisionText = decisions.get(0);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    SocialInterface.this.decisions.setText(decisionText);
                                }
                            });
                        }

                        // Establecer el listener para detectar cambios de página en el ViewPager
                        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                // No se requiere implementación
                            }

                            @Override
                            public void onPageSelected(int position) {
                                // Mostrar el texto de la decisión correspondiente a la foto seleccionada
                                if (!decisions.isEmpty() && position < decisions.size()) {
                                    String decisionText = decisions.get(position);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            SocialInterface.this.decisions.setText(decisionText);
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {
                                // No se requiere implementación
                            }
                        });
                    } else {
                        // Manejar la respuesta no exitosa
                        System.out.println("Error en la respuesta: " + response.code() + " " + response.message());
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private List<String> getFriends(String username) {
        new FriendsAsyncTask().execute(username);
        return null;
    }


    /*Method to execute the post requests for the friends*/
    @SuppressLint("StaticFieldLeak")
    private class FriendsAsyncTask extends AsyncTask<String, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            token = params[0];
            ArrayList<String> friendsList = new ArrayList<>();
            OkHttpClient client =secureConnection.getClient();
            MediaType mediaType = MediaType.parse("application/json");
            if (token != null) {
                String requestBodyString = "username=" + token;
                RequestBody requestBody = RequestBody.create(mediaType, requestBodyString);
                Request request = new Request.Builder()
                        .url("https://5.75.251.56:8443/getFriends")
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

    }


    private void updateAdapterWithImageIds() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                viewAdapter.setImageIds(imageIds);
                viewAdapter.notifyDataSetChanged();
            }
        });
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