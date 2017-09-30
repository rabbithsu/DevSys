package com.example.rabbit.demoapp.DataConnector;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.rabbit.demoapp.Constants;
import com.example.rabbit.demoapp.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Rabbit徐 on 2017/9/23.
 */

public class ServerService extends Service{

    private NotificationManager mNM;
    public static final String TAG = "Serverservice";

    static int id = 0;
    private Socket mSocket;
    private BufferedWriter bwrite;
    private BufferedReader bread;
    private String tmp;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onCreate(){
        Log.d(TAG, "onSeC");
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        //showNotification();
        ConnectThread.start();
    }

    private void showNotification(String message) {
        Log.d(TAG, "Show"+message);
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = message;

        // The PendingIntent to launch our activity if the user selects this notification
        //PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
        //        new Intent(this, LocalServiceActivities.Controller.class), 0);

        // Set the info for the views that show in the notification panel.
        Notification notification = new Notification.Builder(this)
                .setTicker(text)  // the status text
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentTitle("Alert")//getText(R.string.local_service_label))  // the label of the entry
                .setContentText(Integer.toString(id))  // the contents of the entry
                .setSmallIcon(R.mipmap.ic_launcher)
                //.setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                .build();
        // Send the notification.
        mNM.notify(id, notification);
        id += 1;
    }

    Thread ConnectThread = new Thread()
    {
        @Override
        public void run()
        {
            //showNotification();
            try{
                InetAddress IPAddress = InetAddress.getByName(Constants.ServerIP);
                int serverPort = Constants.ServerPort;
                mSocket = new Socket(IPAddress, serverPort);

                bwrite = new BufferedWriter( new OutputStreamWriter(mSocket.getOutputStream()));
                bread = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));

                if(mSocket.isConnected()){
                    bwrite.write("87918787");
                    bwrite.flush();
                    Log.d(TAG, "write");
                }

                while (mSocket.isConnected()) {

                    tmp = bread.readLine();    //宣告一個緩衝,從br串流讀取值
                    // 如果不是空訊息
                    if(tmp != null){
                        Log.d(TAG, tmp);
                        showNotification(tmp);
                        //將取到的String抓取{}範圍資料
                        //tmp=tmp.substring(tmp.indexOf("{"), tmp.lastIndexOf("}") + 1);
                        //json_read=new JSONObject(tmp);
                        //從java伺服器取得值後做拆解,可使用switch做不同動作的處理
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
                Log.e(TAG, "Socket Error.");

            }

            //ket = new
        }
    };

}
