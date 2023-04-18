package org.proven.decisions2.Settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import org.proven.decisions2.Friends.FriendsActivity;
import org.proven.decisions2.R;
import org.proven.decisions2.SocialInterface;

public class LanguageActivity extends Activity {


    Button btHome, btSettings, btFriends, btSpanish, btEnglish, btChinese, btHindi, btPortuguese, btCatalan, btGerman;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language_layout);

        btHome = findViewById(R.id.btHome);
        btSettings = findViewById(R.id.btSettings);
        btFriends = findViewById(R.id.btFriends);
        btSpanish = findViewById(R.id.btSpanish);
        btEnglish = findViewById(R.id.btEnglish);
        btChinese = findViewById(R.id.btChinese);
        btHindi = findViewById(R.id.btHindi);
        btPortuguese = findViewById(R.id.btPortuguese);
        btCatalan = findViewById(R.id.btCatalan);
        btGerman = findViewById(R.id.btGerman);
        LanguageManager lang = new LanguageManager(this);

        btHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LanguageActivity.this, SocialInterface.class));
            }
        });

        btSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LanguageActivity.this, SettingsActivity.class));
            }
        });

        btFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LanguageActivity.this, FriendsActivity.class));
            }
        });

        btSpanish.setOnClickListener(view ->{

            lang.updateResource("es");
            recreate();

        });

        btEnglish.setOnClickListener(view ->{

            lang.updateResource("en");
            recreate();

        });

        btChinese.setOnClickListener(view ->{

            lang.updateResource("zh");
            recreate();

        });

        btHindi.setOnClickListener(view ->{

            lang.updateResource("hi");
            recreate();

        });

        btPortuguese.setOnClickListener(view ->{

            lang.updateResource("pt");
            recreate();

        });

        btGerman.setOnClickListener(view ->{

            lang.updateResource("de");
            recreate();

        });

        btCatalan.setOnClickListener(view ->{

            lang.updateResource("ca");
            recreate();

        });

    }
}