package com.example.frequency;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.jar.Attributes;

public class MainActivity3 extends AppCompatActivity {
    @Override
     protected  void onDestroy(){
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateseek.interrupt();
    }

   TextView textview3;
   ImageView play,previous,next;
   SeekBar seekBar;
   MediaPlayer mediaPlayer;
   ArrayList songs;
    public String textContent;
    int position;

    Thread updateseek;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        textview3=findViewById(R.id.textView3);
        play =findViewById(R.id.play);
        previous =findViewById(R.id.previous);
        next = findViewById(R.id.next);
        seekBar = findViewById(R.id.seekBar);


        Intent intent= getIntent();
        Bundle bundle=intent.getExtras();
        songs=(ArrayList) bundle.getParcelableArrayList("songlist");
        textContent = intent.getStringExtra("currentsong");
        textview3.setText(textContent);
        textview3.setSelected(true);
        position=intent.getIntExtra("position",0);
        Uri uri= Uri.parse(songs.get(position).toString());
        mediaPlayer = MediaPlayer.create(this, uri);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
              mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
              updateseek = new Thread(){
                  @Override
                  public void run(){
                      super.run();
                      int currentPosition = 0;
                      try {
                          while(currentPosition<mediaPlayer.getDuration()){
                              currentPosition = mediaPlayer.getCurrentPosition();
                              seekBar.setProgress(currentPosition);
                              sleep(800);
                          }
                      }
                      catch (Exception e){
                          e.printStackTrace();
                      }
                  }
              };
              updateseek.start();
              play.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      if(mediaPlayer.isPlaying()){
                          play.setImageResource(R.drawable.play);
                          mediaPlayer.pause();
                      }
                      else{
                          play.setImageResource((R.drawable.pause));
                          mediaPlayer.start();
                      }
                  }
              });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position!=0){
                    position=position-1;
                }
                else{
                    position=songs.size()-1;
                }
                Uri uri= Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
                play.setImageResource((R.drawable.pause));
                seekBar.setMax(mediaPlayer.getDuration());
                textContent = ((File)songs.get(position)).getName();
                textview3.setText(textContent);

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position!=songs.size()-1){
                    position=position+1;
                }
                else{
                    position=0;
                }

                Uri uri= Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
                play.setImageResource((R.drawable.pause));
                seekBar.setMax(mediaPlayer.getDuration());
                textContent = ((File)songs.get(position)).getName();
                textview3.setText(textContent);

            }
        });

    }
}