package com.aykuttasil.callrecord.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.aykuttasil.callrecord.CallRecord;

/**
 * Created by aykutasil on 19.10.2016.
 */

public class CallRecordService extends Service {

    private static final String TAG = CallRecordService.class.getSimpleName();

    CallRecord mCallRecord;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand()");

        mCallRecord = new CallRecord.Builder(this)
                .setRecordFileName(intent.getStringExtra(CallRecord.INTENT_FILE_NAME))
                .setRecordDirName(intent.getStringExtra(CallRecord.INTENT_DIR_NAME))
                .setAudioEncoder(intent.getIntExtra(CallRecord.INTENT_AUDIO_ENCODER, MediaRecorder.AudioEncoder.AMR_NB))
                .setAudioSource(intent.getIntExtra(CallRecord.INTENT_AUDIO_SOURCE, MediaRecorder.AudioSource.VOICE_COMMUNICATION))
                .setOutputFormat(intent.getIntExtra(CallRecord.INTENT_OUTPUT_FORMAT, MediaRecorder.OutputFormat.AMR_NB))
                .setShowSeed(intent.getBooleanExtra(CallRecord.INTENT_SHOW_SEED, false))
                .build();

        Log.i(TAG, "mCallRecord.startCallReceiver()");
        mCallRecord.startCallReceiver();

        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy()");
    }
}
