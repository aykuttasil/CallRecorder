package com.aykuttasil.callrecord.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.aykuttasil.callrecord.CallRecord;
import com.aykuttasil.callrecord.helper.PrefsHelper;

import java.io.File;
import java.io.IOException;

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


    Context mContext;
    private CallRecord.Builder mBuilder;
    private String inCall, outCall, state;

    public CallRecordReceiver() {
    }

    public void setmBuilder(CallRecord.Builder mBuilder) {
        this.mBuilder = mBuilder;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        try {

            this.mContext = context;

            if ((bundle = intent.getExtras()) == null) {
                Log.e(TAG, "Intent extras are null");
                return;
            }

            state = bundle.getString(TelephonyManager.EXTRA_STATE);
            Log.i(TAG, state == null ? "null" : state);

            if (intent.getAction().equals(ACTION_IN)) {
                Log.i(TAG, ACTION_IN);

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
                Log.i(TAG, ACTION_OUT);

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

            boolean isSaveFile = PrefsHelper.readPrefBool(mContext, CallRecord.PREF_SAVE_FILE);
            Log.i(TAG, "isSaveFile: " + isSaveFile);

            // dosya kayıt edilsin mi?
            if (!isSaveFile) {
                return;
            }

            File sampleDir = new File(mBuilder.getRecordDirPath() + "/" + mBuilder.getRecordDirName());
            if (!sampleDir.exists()) {
                sampleDir.mkdirs();
            }

            String file_name = "";
            if (mBuilder.isShowSeed()) {
                file_name = mBuilder.getRecordFileName() + "_" + seed + "_"; // temp dosyaya kayıt edildiği için dosya isminin en sonuna random karakter ekleniyor
            } else {
                file_name = mBuilder.getRecordFileName();
            }

            String suffix = "";
            switch (mBuilder.getOutputFormat()) {
                case MediaRecorder.OutputFormat.AMR_NB: {
                    suffix = ".amr";
                    break;
                }
                case MediaRecorder.OutputFormat.AMR_WB: {
                    suffix = ".amr";
                    break;
                }
                case MediaRecorder.OutputFormat.MPEG_4: {
                    suffix = ".mp4";
                    break;
                }
                case MediaRecorder.OutputFormat.THREE_GPP: {
                    suffix = ".3gp";
                    break;
                }
                default: {
                    suffix = ".amr";
                    break;
                }
            }

            audiofile = File.createTempFile(file_name, suffix, sampleDir);

            recorder = new MediaRecorder();
            recorder.setAudioSource(mBuilder.getAudioSource());
            recorder.setOutputFormat(mBuilder.getOutputFormat());
            recorder.setAudioEncoder(mBuilder.getAudioEncoder());
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
