package osprey_adphone_hn.cellcom.com.cn.broadcast;

import osprey_adphone_hn.cellcom.com.cn.activity.dhb.DhbBhpCallActivity;
import osprey_adphone_hn.cellcom.com.cn.util.PhoneUtils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;

import com.android.internal.telephony.ITelephony;

public class MyPhoneBroadcastReceiver extends BroadcastReceiver {
  private DhbBhpCallActivity activity;
  private boolean isRun = false;

  public MyPhoneBroadcastReceiver() {
    // TODO Auto-generated constructor stub
  }

  public MyPhoneBroadcastReceiver(DhbBhpCallActivity activity) {
    // TODO Auto-generated constructor stub
    this.activity = activity;
  }

  private final static String TAG = DhbBhpCallActivity.TAG;

  @Override
  public void onReceive(Context context, Intent intent) {
    String action = intent.getAction();
    // 呼入电话
    if (action.equals(DhbBhpCallActivity.B_PHONE_STATE)) {
      Log.e(TAG, "[Broadcast]PHONE_STATE");
      activity.stopMediaPlayer();
      if (!isRun) {
        isRun = true;
        doReceivePhone(context, intent);
      }
    }
  }

  /**
   * 处理电话广播.
   * 
   * @param context
   * @param intent
   */
  public void doReceivePhone(Context context, Intent intent) {
    String phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
    TelephonyManager telephony =
        (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    int state = telephony.getCallState();

    switch (state) {
      case TelephonyManager.CALL_STATE_RINGING:
        Log.e(TAG, "[Broadcast]等待接电话=" + phoneNumber);
        autoAnswerPhone();
        // Log.i(TAG, "[Broadcast]等待接电话=" + phoneNumber);
        // try {
        // Method localMethod =
        // telephony.getClass().getDeclaredMethod("answerRingingCall",
        // null);
        // localMethod.setAccessible(true);
        // localMethod.invoke(telephony, null);
        // // ITelephony iTelephony = PhoneUtils.getITelephony(telephony);
        // // iTelephony.answerRingingCall();// 自动接通电话
        // // iTelephony.endCall();//自动挂断电话
        // } catch (Exception e) {
        // try {
        // Log.e("Sandy", "for version 4.1 or larger");
        // // Intent intentPhone = new Intent(
        // // "android.intent.action.MEDIA_BUTTON");
        // // KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_UP,
        // // KeyEvent.KEYCODE_HEADSETHOOK);
        // // intent.putExtra("android.intent.extra.KEY_EVENT", keyEvent);
        // // activity.sendOrderedBroadcast(intentPhone,
        // // "android.permission.CALL_PRIVILEGED");
        //
        // Intent localIntent1 = new Intent(Intent.ACTION_HEADSET_PLUG);
        // localIntent1.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        // localIntent1.putExtra("state", 1);
        // localIntent1.putExtra("microphone", 1);
        // localIntent1.putExtra("name", "Headset");
        // activity.sendOrderedBroadcast(localIntent1,
        // "android.permission.CALL_PRIVILEGED");
        // Intent localIntent2 = new Intent(Intent.ACTION_MEDIA_BUTTON);
        // KeyEvent localKeyEvent1 = new KeyEvent(KeyEvent.ACTION_DOWN,
        // KeyEvent.KEYCODE_HEADSETHOOK);
        // localIntent2.putExtra("android.intent.extra.KEY_EVENT",
        // localKeyEvent1);
        // activity.sendOrderedBroadcast(localIntent2,
        // "android.permission.CALL_PRIVILEGED");
        // Intent localIntent3 = new Intent(Intent.ACTION_MEDIA_BUTTON);
        // KeyEvent localKeyEvent2 = new KeyEvent(KeyEvent.ACTION_UP,
        // KeyEvent.KEYCODE_HEADSETHOOK);
        // localIntent3.putExtra("android.intent.extra.KEY_EVENT",
        // localKeyEvent2);
        // activity.sendOrderedBroadcast(localIntent3,
        // "android.permission.CALL_PRIVILEGED");
        // Intent localIntent4 = new Intent(Intent.ACTION_HEADSET_PLUG);
        // localIntent4.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        // localIntent4.putExtra("state", 0);
        // localIntent4.putExtra("microphone", 1);
        // localIntent4.putExtra("name", "Headset");
        // activity.sendOrderedBroadcast(localIntent4,
        // "android.permission.CALL_PRIVILEGED");
        // } catch (Exception e2) {
        // Log.d("Sandy", "", e2);
        // Intent meidaButtonIntent = new Intent(
        // Intent.ACTION_MEDIA_BUTTON);
        // KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_UP,
        // KeyEvent.KEYCODE_HEADSETHOOK);
        // meidaButtonIntent
        // .putExtra(Intent.EXTRA_KEY_EVENT, keyEvent);
        // }
        // }
        break;
      case TelephonyManager.CALL_STATE_IDLE:
        Log.e(TAG, "[Broadcast]电话挂断=" + phoneNumber);
        break;
      case TelephonyManager.CALL_STATE_OFFHOOK:
        Log.e(TAG, "[Broadcast]通话中=" + phoneNumber);
        break;
    }
  }

  /** * 该方法可以用于4.1的接听 */
  public void autoAnswerPhone() {
      try {
          TelephonyManager telephony = (TelephonyManager) activity
                  .getSystemService(Context.TELEPHONY_SERVICE);
          ITelephony itelephony = PhoneUtils.getITelephony(telephony);
          // itelephony.silenceRinger();
          itelephony.answerRingingCall();
      } catch (Exception e) {
          Log.e(TAG, e.getMessage());
          e.printStackTrace();
          try {
              if(!android.os.Build.MODEL.contains("SCH") && !android.os.Build.MODEL.contains("SM") ){
                  Log.e(TAG, "android.os.Build.MODEL==>"+android.os.Build.MODEL);
                  Intent intent = new Intent("android.intent.action.MEDIA_BUTTON");
                  KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN,
                          KeyEvent.KEYCODE_HEADSETHOOK);
                  intent.putExtra("android.intent.extra.KEY_EVENT", keyEvent);
                  activity.sendOrderedBroadcast(intent,
                          "android.permission.CALL_PRIVILEGED");
                  intent = new Intent("android.intent.action.MEDIA_BUTTON");
                  keyEvent = new KeyEvent(KeyEvent.ACTION_UP,
                          KeyEvent.KEYCODE_HEADSETHOOK);
                  intent.putExtra("android.intent.extra.KEY_EVENT", keyEvent);
                  activity.sendOrderedBroadcast(intent,
                          "android.permission.CALL_PRIVILEGED");
              }
              if(!android.os.Build.MODEL.contains("PE") && !android.os.Build.MODEL.contains("P7") 
                  && !android.os.Build.MODEL.contains("MT7") && !android.os.Build.MODEL.contains("HTC")){
                Intent localIntent1 = new Intent(Intent.ACTION_HEADSET_PLUG);
                  localIntent1.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                  localIntent1.putExtra("state", 1);
                  localIntent1.putExtra("microphone", 1);
                  localIntent1.putExtra("name", "Headset");
                  activity.sendOrderedBroadcast(localIntent1,
                          "android.permission.CALL_PRIVILEGED");
                  Intent localIntent2 = new Intent(Intent.ACTION_MEDIA_BUTTON);
                  KeyEvent localKeyEvent1 = new KeyEvent(KeyEvent.ACTION_DOWN,
                          KeyEvent.KEYCODE_HEADSETHOOK);
                  localIntent2.putExtra("android.intent.extra.KEY_EVENT",
                          localKeyEvent1);
                  activity.sendOrderedBroadcast(localIntent2,
                          "android.permission.CALL_PRIVILEGED");
                  Intent localIntent3 = new Intent(Intent.ACTION_MEDIA_BUTTON);
                  KeyEvent localKeyEvent2 = new KeyEvent(KeyEvent.ACTION_UP,
                          KeyEvent.KEYCODE_HEADSETHOOK);
                  localIntent3.putExtra("android.intent.extra.KEY_EVENT",
                          localKeyEvent2);
                  activity.sendOrderedBroadcast(localIntent3,
                          "android.permission.CALL_PRIVILEGED");
                  Intent localIntent4 = new Intent(Intent.ACTION_HEADSET_PLUG);
                  localIntent4.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                  localIntent4.putExtra("state", 0);
                  localIntent4.putExtra("microphone", 1);
                  localIntent4.putExtra("name", "Headset");
                  activity.sendOrderedBroadcast(localIntent4,
                          "android.permission.CALL_PRIVILEGED");
              }
          } catch (Exception e2) {
              Log.e(TAG, e2.getMessage());
              e2.printStackTrace();
              Intent meidaButtonIntent = new Intent(
                      Intent.ACTION_MEDIA_BUTTON);
              KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_UP,
                      KeyEvent.KEYCODE_HEADSETHOOK);
              meidaButtonIntent.putExtra(Intent.EXTRA_KEY_EVENT, keyEvent);
              activity.sendOrderedBroadcast(meidaButtonIntent, null);
          }
      }
  }
}
