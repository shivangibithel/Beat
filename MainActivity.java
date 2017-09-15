package com.example.shivangibithel.beat;
import android.os.Bundle;
import android.app.TabActivity;
import android.content.Intent;
import android.widget.TabHost;
import android.widget.Toast;
public class MainActivity extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();
        TabHost.TabSpec spec; // Reusable TabSpec for each tab
        Intent intent; // Reusable Intent for each tab

        spec = tabHost.newTabSpec("Search"); // Create a new TabSpec using tab host
        spec.setIndicator("SEARCH");
        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent(MainActivity.this, search.class);
        spec.setContent(intent);
        tabHost.addTab(spec);

        // Do the same for the other tabs

        spec = tabHost.newTabSpec("Songs"); // Create a new TabSpec using tab host
        spec.setIndicator("SONGS");
        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent(MainActivity.this, songs.class);
        spec.setContent(intent);
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("playlist"); // Create a new TabSpec using tab host
        spec.setIndicator("PLAYLIST");
        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent(MainActivity.this, playlist.class);
        spec.setContent(intent);
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("recoder"); // Create a new TabSpec using tab host
        spec.setIndicator("RECODER");
        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent(MainActivity.this, recording.class);
        spec.setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(1);
           tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                // display the name of the tab whenever a tab is changed
                Toast.makeText(getApplicationContext(), tabId, Toast.LENGTH_SHORT).show();
            }
        });
    }
}



