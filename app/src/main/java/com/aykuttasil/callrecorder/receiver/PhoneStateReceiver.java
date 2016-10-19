package com.aykuttasil.callrecorder.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Timer;

/**
 * Created by aykutasil on 19.10.2016.
 */

public class PhoneStateReceiver extends BroadcastReceiver {

    private static final String TAG = PhoneStateReceiver.class.getSimpleName();
    private static final String ACTION_IN = "android.intent.action.PHONE_STATE";
    private static final String ACTION_OUT = "android.intent.action.NEW_OUTGOING_CALL";


    String name, phonenumber;
    String audio_format;
    public String Audio_Type;
    int audioSource;
    Context context;
    private Handler handler;
    Timer timer;
    Boolean offHook = false, ringing = false;
    Toast toast;
    Boolean isOffHook = false;


    private static MediaRecorder recorder;
    File audiofile;
    private static boolean recordstarted = false;
    private static boolean wasRinging = false;

    Bundle bundle;
    String state;
    String inCall, outCall;


    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(ACTION_IN)) {
            Log.i(TAG, "android.intent.action.PHONE_STATE");


            if ((bundle = intent.getExtras()) != null) {
                state = bundle.getString(TelephonyManager.EXTRA_STATE);
                Log.i(TAG, state == null ? "null" : state);
            }
            //

            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                inCall = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                wasRinging = true;
                Toast.makeText(context, "IN : " + inCall, Toast.LENGTH_LONG).show();
            } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {

                Log.i(TAG, wasRinging ? "wasRinging is true" : "wasRinging is false");

                if (wasRinging) {
                    Toast.makeText(context, "ANSWERED", Toast.LENGTH_LONG).show();
                    startRecord("incoming");
                    Log.i(TAG, "start record");
                }
            } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                wasRinging = false;
                Toast.makeText(context, "REJECT || DISCO", Toast.LENGTH_LONG).show();
                Log.i(TAG, recordstarted ? "recordstarted is true" : "recordstarted is false");
                if (recordstarted) {
                    Log.i(TAG, "stop record");
                    recorder.stop();
                    recordstarted = false;
                }
            }

        } else if (intent.getAction().equals(ACTION_OUT)) {
            Log.i(TAG, "android.intent.action.NEW_OUTGOING_CALL");

            if ((bundle = intent.getExtras()) != null) {
                outCall = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
                Toast.makeText(context, "OUT : " + outCall, Toast.LENGTH_LONG).show();
                Log.i(TAG, "start record");
                startRecord("outgoing");


                if ((bundle = intent.getExtras()) != null) {
                    state = bundle.getString(TelephonyManager.EXTRA_STATE);
                    Log.i(TAG, state == null ? "null" : state);

                    if (state != null) {
                        if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                            wasRinging = false;
                            Toast.makeText(context, "REJECT || DISCO", Toast.LENGTH_LONG).show();
                            if (recordstarted) {
                                recorder.stop();
                                recordstarted = false;
                                Log.i(TAG, "stop record");
                            }
                        }
                    }


                }
            }
        }


    }

    private void startRecord(String seed) {
        //String out = new SimpleDateFormat("dd-MM-yyyy hh-mm-ss").format(new Date());
        File sampleDir = new File(Environment.getExternalStorageDirectory(), "/CallRecording");
        if (!sampleDir.exists()) {
            sampleDir.mkdirs();
        }
        String file_name = "Record" + seed;
        try {
            audiofile = File.createTempFile(file_name, ".amr", sampleDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //String path = Environment.getExternalStorageDirectory().getAbsolutePath();

        recorder = new MediaRecorder();

        recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(audiofile.getAbsolutePath());
        try {
            recorder.prepare();
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
        recorder.start();
        recordstarted = true;
        Log.i(TAG, recordstarted ? "recordstarted is true" : "recordstarted is false");
        Log.i(TAG, "record start");
    }
}
