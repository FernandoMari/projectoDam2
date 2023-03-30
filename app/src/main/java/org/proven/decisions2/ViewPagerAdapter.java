package org.proven.decisions2;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private int[] images = {R.drawable.ejemplo, R.drawable.ejemplo2, R.drawable.ejemplo3, R.drawable.ejemplo4, R.drawable.ejemplo5, R.drawable.ejemplo6};

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        ChildFragment child = new ChildFragment();
        Bundle bundle = new Bundle();
        bundle.putString("target", String.valueOf(position));
        child.setArguments(bundle);

        // Obtener la imagen correspondiente a esta posición
        int imageResId = getImageForPosition(position);
        // Establecer la imagen en el fragmento
        child.setImage(imageResId);

        return child;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    private int getImageForPosition(int position) {
        // Devuelve la imagen correspondiente a esta posición
        return images[position];
    }
}