package com.example.user.musicplayer;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;
/**
 * Created by User on 7/11/2017.
 */

public class playlistsong extends ListActivity {

    playlist.MediaCursorAdapter mediaAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlistsong);

        Bundle extras = getIntent().getExtras();
        long id = extras.getLong("key");

listAllSongsByPlaylistId(id);

    }



    public void listAllSongsByPlaylistId(long id) {
        Cursor cursor;
        ArrayList<String> songList = new ArrayList<String>();
        ContentResolver musicResolver = getContentResolver();

        Uri allSongsUri = MediaStore.Audio.Playlists.Members.getContentUri("external", id);
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        cursor = musicResolver.query(allSongsUri, null, null, null, null);
        if (null != cursor && cursor.moveToFirst()) {
            cursor.moveToFirst();
            mediaAdapter = new playlist.MediaCursorAdapter(this, R.layout.listitem, cursor);
            setListAdapter(mediaAdapter);


        }





    }
}
            class MediaCursorAdapter extends SimpleCursorAdapter {
                public MediaCursorAdapter(Context context, int layout, Cursor c) {
                    super(context, layout, c, new String[]{MediaStore.Audio.Media.DISPLAY_NAME,MediaStore.Audio.Media.ARTIST},
                            new int[]{R.id.displayname, R.id.title});
                }

                @Override
                public void bindView(View view, Context context, Cursor cursor) {
                    TextView title = (TextView) view.findViewById(R.id.title);
                    TextView name = (TextView) view.findViewById(R.id.displayname);
                    //TextView duration = (TextView) view.findViewById(R.id.duration);

                    name.setText(cursor.getString(
                            cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));

                    title.setText(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));

                    view.setTag(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));

                }

                @Override
                public View newView(Context context, Cursor cursor, ViewGroup parent) {
                    LayoutInflater inflater = LayoutInflater.from(context);
                    View v = inflater.inflate(R.layout.listitem, parent, false);

                    bindView(v, context, cursor);
                    return v;

                }
            }




