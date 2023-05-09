package org.proven.decisions2.PublicDecision;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.proven.decisions2.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ResultPhoto extends Activity {
    private Button btNo, btYes;
    private Bitmap bitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_photo_layout);

        instantiateElements();

        showResultPhoto();

        btNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmap != null) {
                    bitmap.recycle();
                }
                // Start camera activity or do other necessary actions here
                finish();
            }
        });

        btYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
    }

    private void showResultPhoto() {
        byte[] byteArray = getIntent().getByteArrayExtra("photo");
        bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        ImageView imageView = findViewById(R.id.bitmapResult);
        imageView.setImageBitmap(bitmap);
    }

    private void instantiateElements() {
        btNo = findViewById(R.id.btNo);
        btYes = findViewById(R.id.btYes);
    }

    private void uploadImage() {
        // Obtén la ruta del archivo de la imagen
        String filePath = getFilesDir().getPath().toString() + "/result_photo.jpg";
        System.out.println(filePath);

        // Crea un objeto File con la ruta del archivo
        File file = new File(filePath);

        // Crea una instancia de OkHttpClient
        OkHttpClient client = new OkHttpClient();

        // Crea una instancia de MultipartBody.Builder para construir el cuerpo de la solicitud HTTP
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(MediaType.parse("image/jpg"), file));
        System.out.println("antes de la url " + filePath);
        // Crea la solicitud HTTP con la URL del servidor
        Request request = new Request.Builder()
                .url("http://5.75.251.56:7070/upload" + "/result_photo.jpg")
                .post(builder.build())
                .build();
        System.out.println("Pasa de la url " + filePath);
        // Ejecuta la solicitud HTTP en un hilo en segundo plano
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Maneja el error en caso de que la solicitud HTTP falle
                Log.e("TAG", "Error: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Maneja la respuesta del servidor
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Log.i("TAG", "Response: " + responseBody);
                } else {
                    Log.e("TAG", "Error: " + response.code() + " " + response.message());
                }
            }
        });
    }

}

