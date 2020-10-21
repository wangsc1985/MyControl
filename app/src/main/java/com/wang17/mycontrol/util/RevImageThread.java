package com.wang17.mycontrol.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class RevImageThread implements Runnable {

    public Socket s;
    public ServerSocket ss;

    //向UI线程发送消息
    private Handler handler;
    private Bitmap bitmap;
    private static final int COMPLETED = 0x111;

    public RevImageThread(Handler handler){
        this.handler = handler;
    }

    public void run()
    {
        try {
            ss = new ServerSocket(8003);
        } catch (IOException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }

        while(true){
            try {
                s = ss.accept();
                Log.e("strat","ljq");
                new Thread(){
                    public void run() {
                        try {
                            byte [] buffer = new byte[1024];
                            InputStream ins = null;
                            ins = s.getInputStream();
                            Log.v("socket", "socketcc1111");
                            if(s != null){
                                Log.v("socket", "socketcc2222");
                                while(true){
                                    int len = 0;
                                    int i = 0;
                                    int j = 0;
                                    boolean flag = true;
		                            			/*while(flag){
		                            				 i += ins.read();
		                            				 Log.e("socket", ":"+i + " : " + j++);
		                            			}
		                            			*/
                                    if(ins.read() == 0xA0){
                                        byte[] src = new byte[4];
                                        len = ins.read(src);
                                        Log.v("sck", "src3:"+src[3]);
                                        Log.v("sck", "src2:"+src[2]);
                                        Log.v("sck", "src1:"+src[1]);
                                        Log.v("sck", "src0:"+src[0]);
                                        Log.v("sck", "src:"+src);
                                        Log.v("socket", "socketcc55551:"+len);
                                        Log.v("socket", "socketcc55552:"+src);
                                        len = bytesToInt(src, 0);
                                        Log.v("socket", "socketcc55553:"+src);
                                        Log.v("socket", "socketcc55554:"+len);
                                        //len = 4000000;
                                        byte[] srcData = new byte[len];
                                        sendMSG(len+":len");
                                        int readc = 0;

                                        ins.read(srcData, readc, len);
                                        Log.v("socket", "srcData0:"+srcData[0]);
                                        sendMSG("srcData0:"+srcData[0]);
                                        Log.v("socket", "srcData0:"+srcData[1]);
                                        Log.v("socket", "srcData0:"+srcData[2]);
                                        Log.v("socket", "srcData0:"+srcData[3]);
                                        Log.v("socket", "srcData0:"+srcData[4]);
                                        Log.v("socket", "srcData0:"+srcData[5]);
                                        Log.v("socket", "srcData0:"+srcData[6]);
                                        Log.v("socket", "srcData0:"+srcData[7]);
                                        Log.v("socket", "srcData0:"+srcData[len-1]);
                                        sendMSG(srcData[len-1]+"L");
                                        bitmap = BitmapFactory.decodeByteArray(srcData, 0, len);

                                        Message msg =handler.obtainMessage();
                                        msg.what = COMPLETED;
                                        msg.obj = bitmap;
                                        handler.removeMessages(COMPLETED);
                                        handler.sendMessage(msg);
                                    }
                                }
                            }
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            sendMSG(e.toString()+"bao cuo " +e.getMessage());
                        }
                    };
                }.start();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }
    public int bytesToInt(byte[] src, int offset){
        int value;
        value = (int)((src[offset] & 0xFF))
                | ((src[offset+1] & 0xFF)<<8)
                | ((src[offset+2] & 0xFF)<<16)
                | ((src[offset+3] & 0xFF)<<24);
        return value;
    }
    public int bytesToInt2(byte[] src, int offset){
        int value;
        value = (int)((src[offset] & 0xFF<<24)
                | ((src[offset+1] & 0xFF)<<16)
                | ((src[offset+2] & 0xFF)<<8)
                | (src[offset+3] & 0xFF));
        return value;
    }
    public  void sendMSG(String text){
        Message msg = new Message();
        msg.what = 908;
        msg.obj = text;
        handler.sendMessage(msg);
    }
}
