package p2p.cellcom.com.cn.thread;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

import osprey_adphone_hn.cellcom.com.cn.util.LogMgr;
import p2p.cellcom.com.cn.utils.Utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;


public class UDPHelper{
	public Boolean IsThreadDisable = false;
	public int port;
    InetAddress mInetAddress;
    public Handler mHandler;
    DatagramSocket datagramSocket = null;
    public static final int HANDLER_MESSAGE_BIND_ERROR = 0x01;
    public static final int HANDLER_MESSAGE_RECEIVE_MSG = 0x02;
    public UDPHelper(int port) {
    	this.port = port;
    }
    
    public void setCallBack(Handler handler){
    	this.mHandler = handler;
    }
    
    public void StartListen()  {
        // 接收的字节大小，客户端发送的数据不能超过这个大小
    	new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				byte[] message = new byte[1024];
		        
		        try {
		            // 建立Socket连接
		        	try {
			            datagramSocket = new DatagramSocket(port);
			            Log.e("port","port="+port);
			        	}catch (Exception e) {
			        		port = 57521;
			        		datagramSocket = new DatagramSocket(port);
			        		Log.e("port","port="+port);
			        	}
		            
		            datagramSocket.setBroadcast(true);
		            DatagramPacket datagramPacket = new DatagramPacket(message,
		                    message.length);
		                while (!IsThreadDisable) 
		                {
		                    // 准备接收数据
		                    datagramSocket.receive(datagramPacket);
		                    byte[] data = datagramPacket.getData();
		                    if(data.length > 20){
//		                    	byte[] value = new byte[4];
//		                    	System.arraycopy(data, 16, value, 0, 4);
		                    	int deviceID = Utils.bytesToInt(data, 16);
		                    	LogMgr.showLog("deviceID------------>" + deviceID);
		                    	
		                    }
		                    if(data[0]==1)
		                    {
		                    	if(null!=mHandler)
		                    	{
		                    		if(data.length > 20){
				                    	byte[] value = new byte[4];
				                    	System.arraycopy(data, 16, value, 0, 4);
				                    	int deviceID = Utils.bytesToInt(value, 0);
//				                    	int deviceID = ByteBuffer.wrap(data, 16, 4).getInt();
				                    	LogMgr.showLog("deviceID------------>" + deviceID);
			                    		Message msg = new Message();
			                    		msg.what = HANDLER_MESSAGE_RECEIVE_MSG;
			                    		msg.arg1 = deviceID;
			                    		mHandler.sendMessage(msg);
				                    }else{
				                    	mHandler.sendEmptyMessage(HANDLER_MESSAGE_RECEIVE_MSG);
				                    }
					            	break;
					            }	
		                    }
		                    
		                }
		        } catch(SocketException e){
		        	
		        }catch (Exception e) {
		        	e.printStackTrace();
		            IsThreadDisable = true;
		            if(null!=mHandler){
		            	mHandler.sendEmptyMessage(HANDLER_MESSAGE_BIND_ERROR);
		            }
		        }finally{
		        	if(null!=datagramSocket){
		        		datagramSocket.close();
		        		datagramSocket = null;
		        	}
		        }
			}
    		
    	}).start();
        

    }

    public void StopListen(){
    	this.IsThreadDisable = true;
    	if(null!=datagramSocket){
    		datagramSocket.close();
    		datagramSocket = null;
    	}
    }
    
}
