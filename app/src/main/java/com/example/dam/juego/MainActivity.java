package com.example.dam.juego;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private int score = 0;

    //Layout
    private TextView scoreLabel, startLabel;
    private ImageView pnj, blueGhost, ghost, cherry;

    //Marco
    private int frameHeight, screenWidth, screenHeight;

    //Pnj
    private int pnjSize;
    private int pnjY;

    //Posiciones
    private int blueGhostX, blueGhotY;
    private int cherryX, cherryY;
    private int ghostX, ghostY;

    //Inicializadores
    private Handler handler = new Handler();
    private Timer timer = new Timer();

    //Banderas Chekeo
    private boolean flg_accion = false;
    private boolean flg_start = false;

    //Velocidad
    private int pnjSpeed, blueGhostSpeed, cherrySpeed, ghostSpeed;

    /**************************** Fin de declaraciones *********************************************/

    private void changePos(){
        hitCheck();

        //blueghost
        blueGhostX -= blueGhostSpeed;
        if(blueGhostX < 0){
            blueGhostX = screenWidth + 20;
            blueGhotY = (int) Math.floor(Math.random() * (frameHeight - blueGhost.getHeight()));
        }
        blueGhost.setX(blueGhostX);
        blueGhost.setY(blueGhotY);

        //cherry
        cherryX -= cherrySpeed;
        if(cherryX < 0){
            cherryX = screenWidth + 1;
            cherryY = (int) Math.floor(Math.random() * (frameHeight - cherry.getHeight()));
        }
        cherry.setX(cherryX);
        cherry.setY(cherryY);

        //ghost
        ghostX -= ghostSpeed;
        if(ghostX < 0){
            ghostX = screenWidth + 10;
            ghostY = (int) Math.floor(Math.random() * (frameHeight - ghost.getHeight()));
        }
        ghost.setX(ghostX);
        ghost.setY(ghostY);

        //Movimiento
        if(flg_accion == true){
            //Pulsando
            pnjY -= pnjSpeed;
        }else{
            //Sin pulsar
            pnjY += pnjSpeed;
        }

        //Para que pnj no se salga de la pantalla
            //Por arriba
        if(pnjY < 0){
            pnjY = 0;
        }
            //Por abajo
        if(pnjY > frameHeight - pnjSize){
            pnjY = frameHeight - pnjSize;
        }
        pnj.setY(pnjY);

        scoreLabel.setText("Score: " + score);
    }

    //Choque con pnj
    private void hitCheck(){
        //Si el centro del objeto choca contra el pnj, contara como hit

        //blueGhost
        int orangeCenterX = blueGhostX + blueGhost.getWidth() / 2;
        int orangeCenterY = blueGhotY + blueGhost.getHeight() / 2;

        if(0 <= orangeCenterX && orangeCenterX <= pnjSize &&
                pnjY <= orangeCenterY && orangeCenterY <= pnjY + pnjSize){

            score += 10;
            blueGhostX = -10;
        }

        //cherry
        int pinkCenterX = cherryX + cherry.getWidth() / 2;
        int pinkCenterY = cherryY + cherry.getHeight() / 2;

        if(0 <= pinkCenterX && pinkCenterX <= pnjSize &&
                pnjY <= pinkCenterY && pinkCenterY <= pnjY + pnjSize){

            score += 30;
            cherryX = -10;
        }

        //ghost
        int blackCenterX = ghostX + ghost.getWidth() / 2;
        int blackCenterY = ghostY + ghost.getHeight() / 2;

        if(0 <= blackCenterX && blackCenterX <= pnjSize &&
                pnjY <= blackCenterY && blackCenterY <= pnjY + pnjSize){

            //Parar el juego
            timer.cancel();
            timer = null;

            //Pantalla de resultados.
            Intent intent = new Intent(getApplicationContext(), GameOver.class);
            intent.putExtra("score" , score);
            startActivity(intent);
        }
    }

    private void init(){
        //Referencias al layout
        this.ghost = findViewById(R.id.ghost);
        this.cherry = findViewById(R.id.cherry);
        this.blueGhost = findViewById(R.id.blueGhost);
        this.pnj = findViewById(R.id.pnj);
        this.startLabel = findViewById(R.id.startLabel);
        this.scoreLabel = findViewById(R.id.scoreLabel);

        //Tamano de la pantalla
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;

        //Para que no se vea en la pantalla la iniciar el juego
        blueGhost.setX(-200);
        cherry.setX(-200);
        ghost.setX(-200);

        //Velocidad
        pnjSpeed = Math.round(screenHeight / 60F);
        blueGhostSpeed = Math.round(screenWidth / 60F);
        cherrySpeed = Math.round(screenWidth / 36F);
        ghostSpeed = Math.round(screenWidth / 45F);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    public boolean onTouchEvent(MotionEvent me){
        if(flg_start == false){
            flg_start = true;

            FrameLayout frame = findViewById(R.id.frame);
            frameHeight = frame.getHeight();

            pnjY = (int) pnj.getY();
            //Como es un cuadrado solo hace falta una medida
            pnjSize = pnj.getHeight();

            startLabel.setVisibility(View.GONE);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            }, 0, 20);//cada 20 ms
        }else{
            if(me.getAction() == MotionEvent.ACTION_DOWN) {
                flg_accion = true;
            }else if(me.getAction() == MotionEvent.ACTION_UP){
                flg_accion = false;
            }
        }
        return true;
    }

    //Deshabilita el boton atras
    public boolean dispatchKeyEvent(KeyEvent event){
        if(event.getAction() == KeyEvent.ACTION_DOWN){
            switch (event.getKeyCode()){
                case KeyEvent.KEYCODE_BACK:
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}