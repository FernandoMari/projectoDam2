package org.proven.decisions2;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.proven.decisions2.Friends.AddFriendsActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


//public class ViewPagerAdapter extends FragmentStatePagerAdapter {
//    Handler handler;
//    private String[] imageUrls = {"http://5.75.251.56:7070/imagen/6", "http://5.75.251.56:7070/imagen/7", "https://i.pinimg.com/236x/4d/41/69/4d4169ce26c13c62a1cf20d45a05f130.jpg", "http://5.75.251.56:7070/imagen/8", "http://5.75.251.56:7070/imagen/9"};
//    //private int[] imageIds = {1, 2, 3, 4};
//
//    public ViewPagerAdapter(FragmentManager fm) {
//        super(fm);
//        handler = new Handler(Looper.getMainLooper());
//    }
//
//    @Override
//    public Fragment getItem(int position) {
//        ChildFragment child = new ChildFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("target", String.valueOf(position));
//        child.setArguments(bundle);
//
//        if (imageUrls == null) {
//            // Descargar las imágenes antes de cargar la imagen en Glide
//            getPhotos();
//        } else {
//            // Obtener la URL de la imagen correspondiente a esta posición
//            String imageUrl = imageUrls[position];
//            System.out.println(imageUrl);
//
//            // Cargar la imagen utilizando Glide
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    Glide.with(child)
//                            .load(imageUrl)
//                            .into(child.imageView);
//                }
//            });
//
//
//        }
//        return child;
//    }
//
//
//
//
//    @Override
//    public int getCount() {
//        return imageUrls == null ? 0 : imageUrls.length;
//    }
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
//                        List<String> imagenes = new ArrayList<>();
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            String imageUrl = jsonArray.getString(i);
//                            imagenes.add(imageUrl);
//                        }
//
//
//                        // Ejecutar la actualización del ViewPagerAdapter en el hilo principal utilizando Handler
//                        final List<String> finalImagenes = imagenes;
//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                imageUrls = finalImagenes.toArray(new String[0]);
//                                notifyDataSetChanged();
//                            }
//                        });
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
//}

//public class ViewPagerAdapter extends FragmentStatePagerAdapter {
//    Handler handler;
//    private String[] imageUrls={"http://5.75.251.56:7070/imagen/4","http://5.75.251.56:7070/imagen/4", "https://i.pinimg.com/236x/4d/41/69/4d4169ce26c13c62a1cf20d45a05f130.jpg"};
//
//    public ViewPagerAdapter(FragmentManager fm) {
//        super(fm);
//    }
//
//
//    @Override
//    public Fragment getItem(int position) {
//        ChildFragment child = new ChildFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("target", String.valueOf(position));
//        child.setArguments(bundle);
//        handler = new Handler(Looper.getMainLooper());
//
//
//        // Obtener la URL de la imagen correspondiente a esta posición
//        String imageUrl = imageUrls[position];
//        System.out.println(imageUrl);
//
//        // Cargar la imagen utilizando Glide
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                child.setImage(imageUrl);
//                System.out.println("carga imagen");
//            }
//        });
//
//
//        return child;
//    }
//
//
//
//    @Override
//    public int getCount() {
//        return imageUrls == null ? 0 : imageUrls.length;
//    }
//
//
//}
//Esto funciona
//public class ViewPagerAdapter extends FragmentStatePagerAdapter {
//    Handler handler;
//
//    private int[] imageIds = {6, 7, 8, 9,10,11,12};
//
//    public ViewPagerAdapter(FragmentManager fm) {
//        super(fm);
//        handler = new Handler(Looper.getMainLooper());
//    }
//
//    @Override
//    public Fragment getItem(int position) {
//        ChildFragment child = new ChildFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("target", String.valueOf(position));
//        child.setArguments(bundle);
//
//        if (imageIds == null) {
//            // Descargar las imágenes antes de cargar la imagen en Glide
//            getPhotos();
//        } else {
//            // Obtener la URL de la imagen correspondiente a esta posición
//            String imageUrl = "http://5.75.251.56:7070/imagen/" + imageIds[position];
//            System.out.println(imageUrl);
//
//            // Cargar la imagen utilizando Glide
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    Glide.with(child)
//                            .load(imageUrl)
//                            .into(child.imageView);
//                }
//            });
//        }
//
//        return child;
//    }
//
//    @Override
//    public int getCount() {
//        return imageIds == null ? 0 : imageIds.length;
//    }
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
//                            int imageId = jsonArray.getInt(i);
//                            ids.add(imageId);
//                        }
//
//                        // Ejecutar la actualización del ViewPagerAdapter en el hilo principal utilizando Handler
//                        final List<Integer> finalIds = ids;
//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                                    imageIds = finalIds.stream().mapToInt(Integer::intValue).toArray();
//                                }
//                                notifyDataSetChanged();
//                            }
//                        });
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
//}

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    Handler handler;
    private String[] imageUrls;
    private int[] imageIds;

    public ViewPagerAdapter(FragmentManager fm, int[] initialIds) {
        super(fm);
        handler = new Handler(Looper.getMainLooper());
        imageIds = initialIds;
    }

    @Override
    public Fragment getItem(int position) {
        ChildFragment child = new ChildFragment();
        Bundle bundle = new Bundle();
        bundle.putString("target", String.valueOf(position));
        child.setArguments(bundle);

        if (imageIds == null) {
            // Descargar las imágenes antes de cargar la imagen en Glide
            getPhotos();
        } else {
            // Obtener la URL de la imagen correspondiente a esta posición
            int imageUrl = imageIds[position];
            System.out.println(imageUrl);

            // Cargar la imagen utilizando Glide
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Glide.with(child)
                            .load("http://5.75.251.56:7070/imagen/" + imageUrl)
                            .into(child.imageView);
                }
            });
        }

        return child;
    }

    @Override
    public int getCount() {
        return imageIds == null ? 0 : imageIds.length;
    }

    private void getPhotos() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://5.75.251.56:7070/imagen/")
                        .get()
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String jsonResponse = response.body().string();
                        JSONArray jsonArray = new JSONArray(jsonResponse);

                        List<Integer> ids = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            int id = jsonArray.getInt(i);
                            ids.add(id);
                        }

                        // Ejecutar la actualización del ViewPagerAdapter en el hilo principal utilizando Handler
                        final List<Integer> finalIds = ids;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    imageIds = finalIds.stream().mapToInt(Integer::intValue).toArray();
                                }
                                notifyDataSetChanged();
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
}








