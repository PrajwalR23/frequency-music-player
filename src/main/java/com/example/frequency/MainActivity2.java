package com.example.frequency;

import static com.example.frequency.R.id.listview;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.icu.text.Transliterator;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {
    ListView listview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        listview=findViewById(R.id.listview);
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                       ArrayList<File> mysongs =fetchSongs(Environment.getExternalStorageDirectory());
                       String [] items = new String[mysongs.size()];
                       for(int i=0;i<mysongs.size();i++)
                       {
                           items[i]=mysongs.get(i).getName().replace(".com","");
                       }
                        ArrayAdapter<String> adapter =new ArrayAdapter<String>(MainActivity2.this, android.R.layout.simple_list_item_1,items);
                       listview.setAdapter(adapter);
                       listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                           @Override
                           public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                               Intent intent= new Intent(MainActivity2.this,MainActivity3.class);
                               String currentsong=listview.getItemAtPosition(position).toString();
                               intent.putExtra("songlist",mysongs);
                               intent.putExtra("currentsong",currentsong);
                               intent.putExtra("position",position);
                               startActivity(intent);
                           }
                       });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();

                    }
                })
                .check();
    }
    public ArrayList<File> fetchSongs(File file){
        ArrayList arraylist = new ArrayList();
        File [] songs = file.listFiles();
        if(songs!=null){
           for(File myFile:songs)
           {
               if(!myFile.isHidden()&& myFile.isDirectory()){
                   arraylist.addAll(fetchSongs(myFile));
               }
               else{
                   if(myFile.getName().endsWith(".mp3")&&!myFile.getName().startsWith(".")){
                       arraylist.add(myFile);
                   }
               }
           }
        }
        return arraylist;
    }
}