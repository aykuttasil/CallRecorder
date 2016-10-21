package com.aykuttasil.callrecord.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by aykutasil on 19.10.2016.
 */
public class CallRecordReceiver extends BroadcastReceiver {

    private static final String TAG = CallRecordReceiver.class.getSimpleName();

    public static final String ACTION_IN = "android.intent.action.PHONE_STATE";
    public static final String ACTION_OUT = "android.intent.action.NEW_OUTGOING_CALL";

    private static MediaRecorder recorder;
    private static boolean recordstarted = false;
    private static boolean wasRinging = false;
    Bundle bundle;
    File audiofile;

    private String inCall, outCall, state;
    private String fileName, dirName;

    public CallRecordReceiver() {
        //setFileName(fileName);
        //setDirName(dirName);
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public String getDirName() {
        return dirName;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        try {

            if ((bundle = intent.getExtras()) == null) {
                Log.e(TAG, "Intent extras are null");
                return;
            }

            state = bundle.getString(TelephonyManager.EXTRA_STATE);
            Log.i(TAG, state == null ? "null" : state);

            if (intent.getAction().equals(ACTION_IN)) {
                Log.i(TAG, "android.intent.action.PHONE_STATE");

                if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {

                    inCall = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                    wasRinging = true;

                } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {

                    if (wasRinging && !recordstarted) {
                        startRecord("incoming");
                        Log.i(TAG, "start record");
                    }
                } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {

                    wasRinging = false;
                    Log.i(TAG, recordstarted ? "recordstarted is true" : "recordstarted is false");

                    if (recordstarted) {
                        recorder.stop();
                        recorder.reset();
                        recorder.release();
                        recordstarted = false;
                        Log.i(TAG, "stop record");
                    }

                }

            } else if (intent.getAction().equals(ACTION_OUT)) {
                Log.i(TAG, "android.intent.action.NEW_OUTGOING_CALL");

                if ((bundle = intent.getExtras()) != null) {
                    outCall = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);

                    if (!recordstarted) {
                        startRecord("outgoing");
                        Log.i(TAG, "start record");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void startRecord(String seed) {

        try {
            String dateString = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss", Locale.US).format(new Date());
            File sampleDir = new File(Environment.getExternalStorageDirectory(), "/" + getDirName());
            if (!sampleDir.exists()) {
                sampleDir.mkdirs();
            }
            String file_name = getFileName() + "_" + seed + "_" + dateString;
            audiofile = File.createTempFile(file_name, ".amr", sampleDir);

            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile(audiofile.getAbsolutePath());

            recorder.prepare();
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
        recorder.start();
        recordstarted = true;
        Log.i(TAG, "record start");

    }
}
