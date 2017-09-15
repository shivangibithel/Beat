package com.example.shivangibithel.beat;

        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.media.MediaPlayer;
        import android.media.MediaRecorder;
        import android.os.Environment;
        import android.provider.MediaStore;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ListView;
        import android.widget.Toast;

        import java.io.BufferedInputStream;
        import java.io.File;
        import java.io.FileInputStream;
        import java.io.FileNotFoundException;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.util.ArrayList;
        import java.util.StringTokenizer;

public class recording extends AppCompatActivity {
    Button btn,btnstp,btnplay,abc,save;
    private MediaRecorder myrec;
    ListView l11;
    EditText ed1;
    ArrayList<String> man;
    ArrayList<byte[]> mang;
    private  String outputfile=null;
    sql db;
    private MediaPlayer myplayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recording);

        axz();
        man=new ArrayList<String>();
        mang=new ArrayList<byte[]>();

        db=new sql(this,"abcv.sqlite",null,1);

        db.querydata("CREATE TABLE IF NOT EXISTS t1(Id INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR, path BLOB)");
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.insert(ed1.getText().toString(),file_localtobyte(outputfile));
                Toast.makeText(recording.this,"saved",Toast.LENGTH_SHORT).show();
            }
        });




        abc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                outputfile= Environment.getExternalStorageDirectory().getAbsolutePath()+"/abcd.3gpp";
                myrec=new MediaRecorder();
                myrec.setAudioSource(MediaRecorder.AudioSource.MIC);
                myrec.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

                myrec.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                myrec.setOutputFile(outputfile);
                try {
                    start(v);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop(v);
            }
        });

        btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play(v);
            }
        });
        btnstp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    stoplay(v);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        load();
        l11.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                playmp3frombyte(mang.get(position));
            }
        });
    }
    private void load()
    { man.clear();
        Cursor cursor=db.getdata("SELECT * FROM t1");
        while(cursor.moveToNext())
        {
            man.add(cursor.getString(1));
            mang.add(cursor.getBlob(2));

        }
        ArrayAdapter ad=new ArrayAdapter(this,android.R.layout.simple_list_item_1,man);
        l11.setAdapter(ad);

    }

    private void playmp3frombyte(byte []mp3)
    {
        try{
            File temp=File.createTempFile("kkll","mp3",getCacheDir());
            temp.deleteOnExit();
            FileOutputStream f1=new FileOutputStream(temp);
            f1.write(mp3);
            f1.close();
            MediaPlayer mp=new MediaPlayer();
            FileInputStream f11=new FileInputStream(temp);
            mp.setDataSource(f11.getFD());

            mp.prepare();
            mp.start();



        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    public byte[] file_localtobyte (String s)
    { File f=new File(s);
        int size=(int)f.length();
        byte[] bytes=new byte[size];
        try
        {
            BufferedInputStream buf=new BufferedInputStream(new FileInputStream(f));
            buf.read(bytes,0,bytes.length);
            buf.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;

    }



    public void start(View view) throws IOException {
        myrec.prepare();
        myrec.start();
        Toast.makeText(getApplicationContext(),"start recording",Toast.LENGTH_SHORT).show();


    }

    public void stop(View view) {
        myrec.stop();
        myrec.release();
        myrec=null;
        Toast.makeText(getApplicationContext(),"stop recording",Toast.LENGTH_SHORT).show();


    }

    public void play(View view)  {
        try {

            myplayer = new MediaPlayer();
            myplayer.setDataSource(outputfile);
            myplayer.prepare();
            myplayer.start();
            Toast.makeText(getApplicationContext(), "play recording", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }
    public void stoplay(View view) throws IOException {

        if(myplayer!=null)
        {
            myplayer.stop();
            myplayer.release();
            myplayer=null;

        }

        Toast.makeText(getApplicationContext(),"stop playing recording",Toast.LENGTH_SHORT).show();


    }
    public void axz()
    {
        btn=(Button)findViewById(R.id.button);
        abc=(Button)findViewById(R.id.abc);
        btnplay=(Button)findViewById(R.id.play);
        btnstp=(Button)findViewById(R.id.stop);
        save=(Button)findViewById(R.id.save);
        ed1=(EditText)findViewById(R.id.editText);
        l11=(ListView)findViewById(R.id.list11);
    }
}
