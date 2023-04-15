package org.proven.decisions2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ChildFragment extends Fragment {

    TextView tvTarget;
    int imageResId;

    public ChildFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_child, container, false);
        tvTarget = (TextView) view.findViewById(R.id.tvTarget);


        Bundle bundle = getArguments();
        tvTarget.setText("Target: " + bundle.getString("target"));


        // Cargar la imagen de recursos
        ImageView imageView = view.findViewById(R.id.fragmentImageView);
        imageView.setImageResource(imageResId);

        return view;
    }

    public void setImage(int imageResId) {
        this.imageResId = imageResId;
    }
}

