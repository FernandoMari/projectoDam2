package org.proven.decisions2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


//public class SocialInterface extends FragmentActivity {
//    private VerticalViewPager viewPager;
//    private ViewPagerAdapter viewAdapter;
//    //The buttons to navigate in the app
//    Button btFriends, btDecisions, btSettings;
//    //User authentication token
//    String token;
//    private Handler handler;
//
//    //    private int[] initialImageIds = {1,2,4,6,8,9,10,11,13,14,16,17,18,19,20}; // Arreglo inicial de IDs de imágenes
//    private int[] imageIds; // Cambiar a un arreglo vacío inicialmente
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_social_interface);
//        //Initialize the elements
//        initializeElements();
//        handler = new android.os.Handler(Looper.getMainLooper());
//        // Solicitar permiso WRITE_EXTERNAL_STORAGE en tiempo de ejecución si no está concedido
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//        } else {
//            //call the method
//            readUser();
//        }
//        // Descargar las imágenes antes de establecer el adaptador en el ViewPager
//        getPhotos();
//
////        // Recuperar el ID de la imagen de los datos extras del Intent
////        int imageId = getIntent().getIntExtra("uploadedImageId", -1);
////        System.out.println("Imagen id "+imageId);
//
////        // Si se ha proporcionado un nuevo ID de imagen, agregarlo al arreglo inicial de IDs
////        if (imageId != -1) {
////            int[] newImageIds = new int[initialImageIds.length + 1];
////            System.arraycopy(initialImageIds, 0, newImageIds, 0, initialImageIds.length);
////            newImageIds[initialImageIds.length] = imageId;
////            initialImageIds = newImageIds;
////        }
//        btFriends.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(SocialInterface.this, FriendsActivity.class);
//                readUser();
//                Log.d("TAG", "userIdSocial: " + token);
//                startActivity(intent);
//                finish();
//
//            }
//        });
//        btDecisions.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(SocialInterface.this, ChooseModality.class));
//            }
//        });
//
//        btSettings.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(SocialInterface.this, SettingsActivity.class);
//                Log.d("TAG", "userIdSocial: " + token);
//                startActivity(intent);
//                finish();
//            }
//        });
//    }
//
//    /*Initialize the elements*/
//    private void initializeElements() {
//        viewPager = (VerticalViewPager) findViewById(R.id.viewPager);
//        viewAdapter = new ViewPagerAdapter(getSupportFragmentManager());
//        viewPager.setAdapter(viewAdapter);
//        btFriends = findViewById(R.id.btFriends);
//        btDecisions = findViewById(R.id.btDecisions);
//        btSettings = findViewById(R.id.btSettings);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 1) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permiso concedido, puedes acceder al archivo token.txt aquí
//                readUser();
//            } else {
//                // Permiso denegado, maneja el escenario en consecuencia
//            }
//        }
//    }
//
//
//    private void getPhotos() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                OkHttpClient client = new OkHttpClient();
//                Request request = new Request.Builder()
//                        .url("http://5.75.251.56:7070/imagen/")
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
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            int id = jsonArray.getInt(i);
//                            ids.add(id);
//                        }
//
//                        // Actualizar el arreglo imageIds y notificar al adaptador
//                        imageIds = new int[ids.size()];
//                        for (int i = 0; i < ids.size(); i++) {
//                            imageIds[i] = ids.get(i);
//                        }
//                        updateAdapterWithImageIds();
//                    } else {
//                        // Manejar la respuesta no exitosa
//                        System.out.println("Error en la respuesta: " + response.code() + " " + response.message());
//                    }
//                } catch (IOException | JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }
//
//    private void updateAdapterWithImageIds() {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                viewAdapter.setImageIds(imageIds);
//                viewAdapter.notifyDataSetChanged();
//            }
//        });
//    }

//    private void getPhotos() {
//        new Thread(() -> {
//            OkHttpClient client = new OkHttpClient();
//            Request request = new Request.Builder()
//                    .url("http://5.75.251.56:7070/imagen/")
//                    .get()
//                    .build();
//
//            try {
//                Response response = client.newCall(request).execute();
//                if (response.isSuccessful()) {
//                    String jsonResponse = response.body().string();
//                    JSONArray jsonArray = new JSONArray(jsonResponse);
//
//                    List<Integer> ids = new ArrayList<>();
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        int id = jsonArray.getInt(i);
//                        ids.add(id);
//                    }
//
//                    // Ejecutar la actualización del ViewPagerAdapter en el hilo principal utilizando Handler
//                    handler.post(() -> {
//                        imageIds = new int[ids.size()];
//                        for (int i = 0; i < ids.size(); i++) {
//                            imageIds[i] = ids.get(i);
//                        }
//                        notifyDataSetChanged();
//                    });
//                } else {
//                    // Manejar la respuesta no exitosa
//                    System.out.println("Error en la respuesta: " + response.code() + " " + response.message());
//                }
//            } catch (IOException | JSONException e) {
//                e.printStackTrace();
//            }
//        }).start();
//    }
public class SocialInterface extends FragmentActivity {
    private VerticalViewPager viewPager;
    private ViewPagerAdapter viewAdapter;
    // The buttons to navigate in the app
    Button btFriends, btDecisions, btSettings;
    // User authentication token
    String token;
    TextView decisions;

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

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int id = jsonObject.getInt("id");
                            String decision = jsonObject.getString("decision");

                            ids.add(id);
                            decisions.add(decision);
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