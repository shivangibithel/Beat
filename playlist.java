package com.example.shivangibithel.beat;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.io.IOException;

public class playlist extends ListActivity
{
    private static final int UPDATE_FREQUENCY = 500;
    private static final int STEP_VALUE = 4000;

    private MediaCursorAdapter mediaAdapter = null;
    private TextView selectedFile = null;
    private SeekBar seekbar= null;
    private MediaPlayer player = null;
    private ImageButton playButton= null;
    private ImageButton prevButton= null;
    private ImageButton nextButton= null;

    private boolean isStarted = true;
    private String currentFile ="";
    private boolean isMovingSeekbar = false;

    private final Handler handler = new Handler();

    private final Runnable updatePositionRunnable = new Runnable() {
        @Override
        public void run() {

        }
    };


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist);

        player = new MediaPlayer();
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, null, null, null, null);

        if (cursor == null) {
            Log.e("abc", "Found no playlists.");
            return;
        }


        if (null != cursor &&  cursor.moveToFirst()) {
            cursor.moveToFirst();
            mediaAdapter = new MediaCursorAdapter(this, R.layout.playlistsong, cursor);
            setListAdapter(mediaAdapter);
        }

    }



    @Override
    protected void onListItemClick(ListView list, View view , int position, long id)
    {
        super.onListItemClick(list, view, position, id);
        Intent i = new Intent(this, playlistsong.class);
        i.putExtra("key",id);
         startActivity(i);

    }


    static class MediaCursorAdapter extends SimpleCursorAdapter {
        public MediaCursorAdapter(Context context, int layout, Cursor c)
        {
            super(context, layout, c, new String[]{MediaStore.Audio.Playlists._ID,  MediaStore.Audio.Playlists.NAME},
                    new int[]{R.id.displayname,R.id.title});
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor)
        {
            TextView title = (TextView) view.findViewById(R.id.title);
            TextView name = (TextView) view.findViewById(R.id.displayname);
            //TextView duration = (TextView) view.findViewById(R.id.duration);

            name.setText(cursor.getString(
                    cursor.getColumnIndex(MediaStore.Audio.Playlists._ID)));

            title.setText(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Playlists.NAME)));
/*
            long durationInMs = Long.parseLong(cursor.getString(
                    cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION) ));

            double durataionInMin = ((double) durationInMs/1000.0)/60.0;

            durataionInMin = new BigDecimal(Double.toString(durataionInMin)).setScale(2, BigDecimal.ROUND_UP).doubleValue();

            duration.setText("" + durataionInMin);
*/
            view.setTag(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Playlists.DATA)));

        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent)
        {
            LayoutInflater inflater= LayoutInflater.from(context);
            View v = inflater.inflate(R.layout.listitem, parent, false);

            bindView(v, context, cursor);
            return  v;

        }
    }



}
