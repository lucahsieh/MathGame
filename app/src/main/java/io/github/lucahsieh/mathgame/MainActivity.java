package io.github.lucahsieh.mathgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import io.github.lucahsieh.mathgame.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void useJava(View view){
        Intent i = new Intent(MainActivity.this, JavaMainActivity.class);
        startActivity(i);
    }

    public void useKotlin(View view){
        Intent i = new Intent(MainActivity.this, KotlinMainActivity.class);
        startActivity(i);
    }

}
