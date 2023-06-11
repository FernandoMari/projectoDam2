package org.proven.decisions2.SeePost;


import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private Handler handler;
    private int[] imageIds;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        handler = new Handler(Looper.getMainLooper());
        imageIds = new int[0]; // Inicializar con un arreglo vacío
    }

    @Override
    public Fragment getItem(int position) {
        ChildFragment child = new ChildFragment();
        Bundle bundle = new Bundle();
        bundle.putString("target", String.valueOf(position));
        child.setArguments(bundle);

        if (imageIds.length == 0) {

            // No hay IDs de imagen disponibles, puedes mostrar una imagen de carga o un mensaje de espera
        } else {
            int imageId = imageIds[position];
            // Cargar la imagen utilizando Glide
            handler.post(() -> {
                Glide.with(child)
                        .load("http://5.75.251.56:7070/imagen/" + imageId)
                        .into(child.imageView);
            });
        }

        return child;
    }

    @Override
    public int getCount() {
        return imageIds.length;
    }

    public void setImageIds(int[] ids) {
        imageIds = ids;
    }

}








