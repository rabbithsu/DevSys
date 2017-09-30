package com.example.rabbit.demoapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.rabbit.demoapp.DataConnector.Dataquery;
import com.example.rabbit.demoapp.DataConnector.Pingoperator;
import com.example.rabbit.demoapp.DataConnector.ServerService;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private Button pingbutton;
    private Button querybutton;
    private TextView contentview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onC");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = new Intent(this, ServerService.class);
        startService(intent);

        pingbutton = (Button)findViewById(R.id.pingb);
        querybutton = (Button)findViewById(R.id.qbutton);
        contentview = (TextView)findViewById(R.id.Acontent);

        pingbutton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                Pingoperator p = new Pingoperator(mHandler);
                p.start();
                Snackbar.make(v, "Ping sent.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        querybutton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                Dataquery d = new Dataquery(mHandler, "SELECT * FROM news WHERE id = 2330 Limit 10");
                d.start();
                Snackbar.make(v, "Q sent.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //FragmentActivity activity = getActivity();
            switch (msg.what) {
                case Constants.P_Fail:
                    contentview.setText("Failed");
                    break;
                case Constants.P_Success:
                    String mtext = (String) msg.obj;
                    contentview.setText(mtext);
                    break;
                case Constants.P_Erorr:
                    contentview.setText("ErrorQAQ");
                    break;
                case Constants.Q_Success:
                    String qtext = (String) msg.obj;
                    contentview.setText(qtext);
                    break;
                default:
                    Snackbar.make(findViewById(android.R.id.content), "Don't Know.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        }
    };
}
