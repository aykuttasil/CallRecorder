package com.aykuttasil.callrecord.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

import com.aykuttasil.callrecord.CallRecord
import com.aykuttasil.callrecord.helper.PrefsHelper

import androidx.annotation.Nullable

/**
 * Created by aykutasil on 19.10.2016.
 */

open class CallRecordService : Service() {

    private lateinit var mCallRecord: CallRecord

    @Nullable
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "onCreate()")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        Log.i(TAG, "onStartCommand()")

        val file_name = PrefsHelper.readPrefString(this, CallRecord.PREF_FILE_NAME)
        val dir_path = PrefsHelper.readPrefString(this, CallRecord.PREF_DIR_PATH)
        val dir_name = PrefsHelper.readPrefString(this, CallRecord.PREF_DIR_NAME)
        val show_seed = PrefsHelper.readPrefBool(this, CallRecord.PREF_SHOW_SEED)
        val show_phone_number = PrefsHelper.readPrefBool(this, CallRecord.PREF_SHOW_PHONE_NUMBER)
        val output_format = PrefsHelper.readPrefInt(this, CallRecord.PREF_OUTPUT_FORMAT)
        val audio_source = PrefsHelper.readPrefInt(this, CallRecord.PREF_AUDIO_SOURCE)
        val audio_encoder = PrefsHelper.readPrefInt(this, CallRecord.PREF_AUDIO_ENCODER)

        mCallRecord = CallRecord.Builder(this)
                .setRecordFileName(file_name)
                .setRecordDirName(dir_name)
                .setRecordDirPath(dir_path)
                .setAudioEncoder(audio_encoder)
                .setAudioSource(audio_source)
                .setOutputFormat(output_format)
                .setShowSeed(show_seed)
                .setShowPhoneNumber(show_phone_number)
                .build()

        Log.i(TAG, "mCallRecord.startCallReceiver()")
        mCallRecord.startCallReceiver()

        return Service.START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        super.onDestroy()
        mCallRecord.stopCallReceiver()

        Log.i(TAG, "onDestroy()")
    }

    companion object {

        private val TAG = CallRecordService::class.java.simpleName
    }
}
