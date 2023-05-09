package org.proven.decisions2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.proven.decisions2.Friends.AddFriendsActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


//public class ViewPagerAdapter extends FragmentStatePagerAdapter {
//    Handler handler;
//    //private String[] imageUrls = {"http://5.75.251.56:7070/imagen/4", "http://5.75.251.56:7070/imagen/2", "https://i.pinimg.com/236x/4d/41/69/4d4169ce26c13c62a1cf20d45a05f130.jpg", "http://5.75.251.56:7070/imagen/1"};
//    //List<String> imageUrls;
//    String[] imageUrls;
//
//    public ViewPagerAdapter(FragmentManager fm) {
//        super(fm);
//        //imageUrls=getAllImageUrlsFromDatabase();
//    }

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
//            Glide.with(child)
//                    .load(imageUrl)
//                    .into(child.imageView);
//        }
//
//        return child;
//    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {
        Handler handler;
        //private String[] imageUrls = {"http://5.75.251.56:7070/imagen/4", "http://5.75.251.56:7070/imagen/2", "https://i.pinimg.com/236x/4d/41/69/4d4169ce26c13c62a1cf20d45a05f130.jpg", "http://5.75.251.56:7070/imagen/1"};
        //List<String> imageUrls;
        String[] imageUrls;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            getPhotos();
            //imageUrls=getAllImageUrlsFromDatabase();
        }


        @Override
    public Fragment getItem(int position) {
        ChildFragment child = new ChildFragment();
        Bundle bundle = new Bundle();
        bundle.putString("target", String.valueOf(position));
        child.setArguments(bundle);
        handler = new Handler(Looper.getMainLooper());


        if (imageUrls == null) {
            // Descargar las imágenes antes de cargar la imagen en Glide
        } else {
            // Obtener la URL de la imagen correspondiente a esta posición
            String imageUrl = imageUrls[position];
            System.out.println(imageUrl);


            // Cargar la imagen utilizando Glide
            handler.post(new Runnable() {
                @Override
                public void run() {
                    child.setImage(imageUrl);
                    System.out.println("carga imagen");
                }
            });


        }
        return child;
    }

    @Override
    public int getCount() {
        return imageUrls == null ? 0 : imageUrls.length;
    }


    private void getPhotos() {
        new PhotoAsyncTask().execute();
    }

    private class PhotoAsyncTask extends AsyncTask<String, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            String requestBodyString = "";
            RequestBody requestBody = RequestBody.create(mediaType, requestBodyString);
            Request request = new Request.Builder()
                    .url("http://5.75.251.56:7070/imagenes")
                    .post(requestBody)
                    .build();

            try {
                Response response = client.newCall(request).execute();

                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                String jsonResponse = response.body().string();
                JSONArray jsonArray = new JSONArray(jsonResponse);

                ArrayList<String> imagenes = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    String base64 = jsonArray.getString(i);
                    imagenes.add(base64);
                }

                return imagenes;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> imagenes) {
            if (imagenes != null) {
                imageUrls = imagenes.toArray(new String[0]);
                notifyDataSetChanged();
            } else {
                // Mostrar un mensaje de error
            }
        }
    }
}








