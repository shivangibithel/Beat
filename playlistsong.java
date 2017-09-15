package com.example.shivangibithel.beat;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
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
import android.widget.Toast;

import java.io.IOException;
import java.math.BigDecimal;

import static com.example.shivangibithel.beat.songs.player;

public class playlistsong extends ListActivity {

    private static final int UPDATE_FREQUENCY = 500;
    private static final int STEP_VALUE = 1200;
    MediaCursorAdapter mediaAdapter = null;
    private TextView selectedFile = null;
    private SeekBar seekbar= null;

    public ImageButton play= null;
    public ImageButton prev= null;
    public ImageButton next= null;
    // private ListView list= null;
    private boolean isStarted = true;
    private String currentFile ="";
    private String outputfile=null;
    private boolean isMovingSeekbar = false;

    private final Handler handler = new Handler();
     private final Runnable updatePositionRunnable = new Runnable() {
        @Override
        public void run() {
            updatePosition();
        }
    };
         protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlistsong);

             selectedFile = (TextView)findViewById(R.id.selectedfile);
             seekbar = (SeekBar)findViewById(R.id.seekbar);
             play = (ImageButton)findViewById(R.id.play);
             prev = (ImageButton)findViewById(R.id.prev);
             next = (ImageButton)findViewById(R.id.next);
             //list = (ListView) findViewById(android.R.id.list);

             player.setOnCompletionListener(onCompletion);
             player.setOnErrorListener(onError);
             seekbar.setOnSeekBarChangeListener(seekBarChanged);
        Bundle extras = getIntent().getExtras();
        long id = extras.getLong("key");
        listAllSongsByPlaylistId(id);
    }
    public void listAllSongsByPlaylistId(long id) {
        //Cursor cursor;
        //ArrayList<String> songList = new ArrayList<String>();
        //ContentResolver musicResolver = getContentResolver();

        Uri allSongsUri = MediaStore.Audio.Playlists.Members.getContentUri("external", id);
      //  String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        Cursor cursor = getContentResolver().query(allSongsUri, null, null, null, null);
        if (null != cursor && cursor.moveToFirst()) {
            cursor.moveToFirst();
            mediaAdapter = new MediaCursorAdapter(this, R.layout.listitem, cursor);
            setListAdapter(mediaAdapter);


            play.setOnClickListener(onButtonClick);
            next.setOnClickListener(onButtonClick);
            prev.setOnClickListener(onButtonClick);
        }
    }

    protected void onListItemClick(ListView list, View view , int position, long id) {
        super.onListItemClick(list, view, position, id);
        currentFile = (String) view.getTag();
        startPlay(currentFile);

    }

    private void startPlay(String file)
    {
        Log.i("Selected:" , file);

        selectedFile.setText(file);
        seekbar.setProgress(0);
        player.stop();
        player.reset();

        try{
            player.setDataSource(file);
            player.prepare();
            player.start();

        }catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        catch (IllegalStateException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        seekbar.setMax(player.getDuration());
        play.setImageResource(android.R.drawable.ic_media_pause);
        updatePosition();
        isStarted = true;

    }
    private void stopPlay()
    {
        player.stop();
        player.reset();
        play.setImageResource(android.R.drawable.ic_media_play);
        handler.removeCallbacks(updatePositionRunnable);
        seekbar.setProgress(0);
        isStarted = false;

    }

    private void updatePosition()
    {
        handler.removeCallbacks(updatePositionRunnable);
        seekbar.setProgress(player.getCurrentPosition());
        handler.postDelayed(updatePositionRunnable, UPDATE_FREQUENCY);
    }

    private View.OnClickListener onButtonClick = new View.OnClickListener()
    {
        public  void onClick(View v)
        {
            switch (v.getId()) {
                case R.id.play:
                {
                    if(player.isPlaying()){
                        handler.removeCallbacks(updatePositionRunnable);
                        player.pause();
                        play.setImageResource(android.R.drawable.ic_media_play);
                    }
                    else{
                        if(isStarted){
                            player.start();

                            play.setImageResource(android.R.drawable.ic_media_pause);

                            updatePosition();
                        }else{
                            startPlay(currentFile);
                        }

                    }
                    break;
                }
                case R.id.next:{
                    int seekto = player.getCurrentPosition()+ STEP_VALUE;

                    if(seekto > player.getDuration())
                        seekto = player.getDuration();

                    player.pause();
                    player.seekTo(seekto);
                    player.start();
                    break;
                }
                case R.id.prev:{
                    int seekto = player.getCurrentPosition()- STEP_VALUE;

                    if(seekto < 0)
                        seekto = 0;

                    player.pause();
                    player.seekTo(seekto);
                    player.start();
                    break;
                }
            }
        }
    };

    private MediaPlayer.OnCompletionListener onCompletion = new MediaPlayer.OnCompletionListener(){

        @Override
        public void onCompletion(MediaPlayer mp){
            stopPlay();

            Toast.makeText(getApplicationContext(), "change your song", Toast.LENGTH_SHORT).show();


        }
    };

    private MediaPlayer.OnErrorListener onError = new MediaPlayer.OnErrorListener(){
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra){
            return  false;

        }
    };

    private SeekBar.OnSeekBarChangeListener seekBarChanged = new SeekBar.OnSeekBarChangeListener() {

        public void onStopTrackingTouch(SeekBar seekBar) {
            isMovingSeekbar = false;
        }
        public void onStartTrackingTouch(SeekBar seekBar) {
            isMovingSeekbar = true;
        }

        @Override

        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
            if(isMovingSeekbar){
                player.seekTo(progress);

                Log.i("OnSeekBarChangeListener", "onProgressChanged");
            }
        }

    };
    protected static class MediaCursorAdapter extends SimpleCursorAdapter{
        public MediaCursorAdapter(Context context,int layout, Cursor c)
        {
            super(context, layout, c, new String[]{MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.MediaColumns.TITLE, MediaStore.Audio.AudioColumns.DURATION},
                    new int[]{R.id.displayname, R.id.duration});
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor)
        {
            TextView title = (TextView) view.findViewById(R.id.title);
            TextView name = (TextView) view.findViewById(R.id.displayname);
            TextView duration = (TextView) view.findViewById(R.id.duration);

            name.setText(cursor.getString(
                    cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)));

            title.setText(cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.TITLE)));

            long durationInMs = Long.parseLong(cursor.getString(
                    cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION) ));

            double durataionInMin = ((double) durationInMs/1000.0)/60.0;

            durataionInMin = new BigDecimal(Double.toString(durataionInMin)).setScale(2, BigDecimal.ROUND_UP).doubleValue();

            duration.setText("" + durataionInMin);

            view.setTag(cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA)));

        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent)
        {
            LayoutInflater inflator= LayoutInflater.from(context);
            View v = inflator.inflate(R.layout.listitem, parent, false);

            bindView(v, context, cursor);
            return  v;

        }
    }

}


