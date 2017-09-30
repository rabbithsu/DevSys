package com.example.rabbit.demoapp.DataConnector;

import android.os.Handler;
import android.os.Message;

import com.example.rabbit.demoapp.Constants;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by Rabbit徐 on 2017/9/21.
 */

public class Pingoperator extends Thread{

    public static final String TAG = "Pingoperator";

    private final Handler mHandler;

    private void isPingSuccess(int pingNum, String m_strForNetAddress) {
        StringBuffer tv_PingInfo = new StringBuffer();
        String pingResult;
        try {

            Process p = Runtime.getRuntime()
                    .exec("/system/bin/ping -c " + pingNum + " "
                            + m_strForNetAddress); // 10.83.50.111
            // m_strForNetAddress
            int status = p.waitFor();
            String result = "";
            if (status == 0) {
                result = "success";
            } else {
                result = "failed";
                pingResult = "failded";
                Message msg=new Message();
                msg.obj = m_strForNetAddress;
                msg.what = Constants.P_Fail;
                mHandler.sendMessage(msg);
                return;
            }
            String lost = new String();
            String delay = new String();
            BufferedReader buf = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));

            String str = new String();
            // 讀出所有信息並顯示
            while ((str = buf.readLine()) != null) {
                str = str + "\r\n";
                tv_PingInfo.append(str);
            }

            pingResult = tv_PingInfo.toString();
            Message msg = new Message();
            msg.obj = pingResult;
            msg.what = Constants.P_Success;
            mHandler.sendMessage(msg);
            return;
        } catch (Exception ex) {
            ex.printStackTrace();
            pingResult = "拼通了，但是有異常";
            mHandler.sendEmptyMessage(Constants.P_Erorr);
            return;
        }

    }
    public Pingoperator(Handler handler) {
        mHandler = handler;
    }
    public void run(){

        isPingSuccess(1, "192.168.0.13");
    }
}
