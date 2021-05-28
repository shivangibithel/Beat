package com.example.user.mk;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static int oneTimeOnly = 0;

    private double startTime = 0;
    private double finalTime = 0;
    private SeekBar seekbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seekbar = (SeekBar)findViewById(R.id.seekbar);
        seekbar.setClickable(false);



      final ListView  l1 = (ListView) findViewById(R.id.listview);
        ArrayList a11 = new ArrayList<>();
//        getmusic();
        ContentResolver cr = getContentResolver();
        Uri song = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        final Cursor sc = cr.query(song, null, null, null, null);

        if (sc != null && sc.moveToFirst()) {
          int  songt = sc.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int art = sc.getColumnIndex(MediaStore.Audio.Media.ARTIST);

            do {
                String curtitle = sc.getString(songt);
                String curartist = sc.getString(art);
                a11.add(curtitle + "\n" + curartist);

            } while (sc.moveToNext());
        }


//        sc.close();

       final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, a11);
        l1.setAdapter(adapter);

        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapterview, View view, int p, long id) {


                int idColumn = sc.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);



             int    music_column_index = sc
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                sc.moveToPosition(p);
                String filename = sc.getString(music_column_index);

                MediaPlayer mp = new MediaPlayer();


try {
    if (mp.isPlaying()) {
        mp.reset();
        mp.setDataSource(filename);
        mp.prepare();
        mp.start();
    }

    try {
        mp.setDataSource(filename);
    } catch (IOException e) {
        e.printStackTrace();
    }
    try {
        mp.prepare();
    } catch (IOException e) {
        e.printStackTrace();
    }
    mp.start();
    finalTime = mp.getDuration();
    startTime = mp.getCurrentPosition();

 //   seekbar.setProgress((int)startTime);

}
catch(Exception e)
{

}


                // Intent it= new Intent(MainActivity.this,newact.class);
                //it.putExtra("title",l1.getItemAtPosition(p).toString());
             //   startActivity(it);
            }
        });


    }


}



