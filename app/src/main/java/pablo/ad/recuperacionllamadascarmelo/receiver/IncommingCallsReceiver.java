package pablo.ad.recuperacionllamadascarmelo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.lifecycle.ViewModelProvider;

import pablo.ad.recuperacionllamadascarmelo.viewmodel.MyViewmodel;
import pablo.ad.recuperacionllamadascarmelo.viewmodel.Repository;

import static android.telephony.PhoneStateListener.LISTEN_NONE;
import static pablo.ad.recuperacionllamadascarmelo.MainActivity.TAG;


public class IncommingCallsReceiver extends BroadcastReceiver {

    private PhoneStateListener listener;
    private Repository repository;
    private MyViewmodel viewmodel;
    private TelephonyManager telephony;
    @Override
    public void onReceive(Context context, Intent intent) {

        repository = new Repository(context);
        telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);


        /*Bundle extras = intent.getExtras();
        if (extras != null) {
            String state = extras.getString(TelephonyManager.EXTRA_STATE);
            Log.v(TAG, state);
            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                String phoneNumber = extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                Log.v(TAG, phoneNumber);
            }
        }*/

        /*if (telephony == null){
            telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            telephony.listen(new PhoneStateListener(){
                @Override
                public void onCallStateChanged(int state, String incomingNumber) {
                    super.onCallStateChanged(state, incomingNumber);
                    Log.v(TAG, String.valueOf(state));
                    if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                        Log.v(TAG, "LLAMADA ESTABLECIDA");
                    } else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_IDLE)) {

                        Log.v(TAG, "LLAMADA TERMINADA ");
                        //repository.newCall(incomingNumber);
                        //Log.v(TAG, "Receiver de IncommingCallsReceiver " + incomingNumber);

                    } else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                        Log.v(TAG, "TE ESTAN LLAMANDO");
                    }
                }
            }, PhoneStateListener.LISTEN_CALL_STATE);
        }*/

        telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        listener = new PhoneStateListener(){
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);
                if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                    Log.v(TAG, "LLAMADA ESTABLECIDA");
                } else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                    Log.v(TAG, "LLAMADA TERMINADA ");
                } else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                    Log.v(TAG, "TE ESTAN LLAMANDO  " + incomingNumber);
                    repository.newCall(incomingNumber);
                }
                telephony.listen(listener, LISTEN_NONE);
            }
        };
        telephony.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

    }

}
