package com.example.user.musicplayer;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ListActivity;
import android.os.Handler;
import java.io.IOException;
import java.math.BigDecimal;

import static android.R.attr.id;

public class songs extends ListActivity implements View.OnClickListener
{  DBHelper db;
    private static final int UPDATE_FREQUENCY = 500;
    private static final int STEP_VALUE = 8000;
    private MediaCursorAdapter mediaAdapter = null;
    private TextView selectedFile = null;
    private SeekBar seekbar= null;
    private MediaPlayer player=null;
    private ImageButton playButton= null;
    private ImageButton prevButton= null;
    private ImageButton nextButton= null;
   // private ListView list= null;
    private boolean isStarted = true;
    private String currentFile ="";
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
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.songs);
db=new DBHelper(this);


        findViewById(R.id.imageButton2).setOnClickListener(this);
        findViewById(R.id.imageButton3).setOnClickListener(this);
        findViewById(R.id.imageButton5).setOnClickListener(this);


        selectedFile = (TextView)findViewById(R.id.selectedfile);
        seekbar = (SeekBar)findViewById(R.id.seekbar);
        playButton = (ImageButton)findViewById(R.id.play);
        prevButton = (ImageButton)findViewById(R.id.prev);
        nextButton = (ImageButton)findViewById(R.id.next);
        //list = (ListView) findViewById(android.R.id.list);
        player = new MediaPlayer();
        player.setOnCompletionListener(onCompletion);
        player.setOnErrorListener(onError);
        seekbar.setOnSeekBarChangeListener(seekBarChanged);

        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);




        if (null != cursor) {
            cursor.moveToFirst();
            mediaAdapter = new MediaCursorAdapter(this, R.layout.listitem, cursor);
            setListAdapter(mediaAdapter);

            playButton.setOnClickListener(onButtonClick);
            nextButton.setOnClickListener(onButtonClick);
            prevButton.setOnClickListener(onButtonClick);
        }

    }
    @Override
    protected void onListItemClick(ListView list, View view , int position, long id)
    {
        super.onListItemClick(list, view, position, id);

        currentFile= (String) view.getTag();
        startPlay(currentFile);

     /*   boolean isInserted= false;
        try {
            isInserted = db.insert(currentFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(isInserted == true)
            Toast.makeText(songs.this,"Data Inserted",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(songs.this,"Data not Inserted",Toast.LENGTH_LONG).show();
*/

    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        handler.removeCallbacks(updatePositionRunnable);
        player.stop();
        player.reset();
        player.release();
        player= null;

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
        playButton.setImageResource(android.R.drawable.ic_media_pause);
        updatePosition();
        isStarted = true;

    }

    private void stopPlay()
    {
        player.stop();
        player.reset();
        playButton.setImageResource(android.R.drawable.ic_media_play);
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

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId())
        {
            case R.id.imageButton2:
                Utils.changeToTheme(this, Utils.THEME_DEFAULT);
                break;
            case R.id.imageButton3:
                Utils.changeToTheme(this, Utils.THEME_WHITE);
                break; case R.id.imageButton5:
                    Utils.changeToTheme(this, Utils.THEME_BLUE);
            break;
        }

    }

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
                        playButton.setImageResource(android.R.drawable.ic_media_play);
                    }
                    else{
                        if(isStarted){
                            player.start();

                            playButton.setImageResource(android.R.drawable.ic_media_pause);

                            updatePosition();
                        }else{
                            startPlay(currentFile);
                        }

                    }
                    break;
                }
                case R.id.next:{
                    int current = player.getCurrentPosition();


                    if(current +STEP_VALUE <= player.getDuration())
                    {   player.seekTo(current+STEP_VALUE); }
                    else
                    {
                        player.seekTo(player.getDuration());
                    }

                 //   player.pause();
                 //   player.seekTo(seekto);
                   // player.start();
                    break;
                }
                case R.id.prev:{
                    int current = player.getCurrentPosition();

                    if(current- STEP_VALUE >= 0)
                      //  seekto = 0;
                    {player.seekTo(current-STEP_VALUE); }
                    else
                    {
                        player.seekTo(0);
                    }
                    //player.pause();

                    //player.start();
                    break;
                }

            }
        }
    };

    private MediaPlayer.OnCompletionListener onCompletion = new MediaPlayer.OnCompletionListener(){

        @Override
        public void onCompletion(MediaPlayer mp){
            stopPlay();
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);//Menu Resource, Menu
        return true;
    }

    @Override


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.one:
                Toast.makeText(getApplicationContext(), "Playing All Songs", Toast.LENGTH_LONG).show();
                return true;
            case R.id.two:
                Toast.makeText(getApplicationContext(), "Rename The Song", Toast.LENGTH_LONG).show();
                return true;
            case R.id.three:
                Toast.makeText(getApplicationContext(), "Adding Ringtone", Toast.LENGTH_LONG).show();
                return true;
            case R.id.four:
                Toast.makeText(getApplicationContext(), "Choose Playlist", Toast.LENGTH_LONG).show();
                return true;
            case R.id.five:
                Toast.makeText(getApplicationContext(), "Song deleted", Toast.LENGTH_LONG).show();
                return true;
            case R.id.six:
                Toast.makeText(getApplicationContext(), "Songs Deleted", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
