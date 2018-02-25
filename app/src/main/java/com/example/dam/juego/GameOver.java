package com.example.dam.juego;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

public class GameOver extends AppCompatActivity {

    private TextView scoreLabel, highScoreLabel;
    private int score, highScore;

    private void init(){
        scoreLabel = findViewById(R.id.scoreLabel);
        highScoreLabel = findViewById(R.id.highScoreLabel);
        findViewById(R.id.btAgain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Start.class));
            }
        });

        score = getIntent().getIntExtra("score", 0);
        scoreLabel.setText(score + "");

        //Guarda high score
        SharedPreferences settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        highScore = settings.getInt("high_score", 0);

        if(score > highScore){
            highScoreLabel.setText("High Score: " + score);

            //Guardar highScore
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("high_score", score);
            editor.commit();
        }else{
            highScoreLabel.setText("High_Score: " + highScore);
        }
    }

    //Deshabilitar boton atras
    public boolean dispatchKeyEvent(KeyEvent event){
        if(event.getAction() == KeyEvent.ACTION_DOWN){
            switch (event.getKeyCode()){
                case KeyEvent.KEYCODE_BACK:
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        init();
    }
}