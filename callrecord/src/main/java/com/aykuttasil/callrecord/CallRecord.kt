package com.aykuttasil.callrecord

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaRecorder
import android.os.Environment
import com.aykuttasil.callrecord.helper.LogUtils
import com.aykuttasil.callrecord.helper.PrefsHelper
import com.aykuttasil.callrecord.receiver.CallRecordReceiver
import com.aykuttasil.callrecord.service.CallRecordService
import timber.log.Timber

class CallRecord private constructor(private val mContext: Context) {
    private var mCallRecordReceiver: CallRecordReceiver? = null

    val stateSaveFile: Boolean
        get() = PrefsHelper.readPrefBool(mContext, PREF_SAVE_FILE)

    val recordFileName: String?
        get() = PrefsHelper.readPrefString(mContext, PREF_FILE_NAME)

    val recordDirName: String?
        get() = PrefsHelper.readPrefString(mContext, PREF_DIR_NAME)

    val recordDirPath: String?
        get() = PrefsHelper.readPrefString(mContext, PREF_DIR_PATH)

    fun startCallReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(CallRecordReceiver.ACTION_IN)
        intentFilter.addAction(CallRecordReceiver.ACTION_OUT)

        if (mCallRecordReceiver == null) {
            mCallRecordReceiver = CallRecordReceiver(this)
        }
        mContext.registerReceiver(mCallRecordReceiver, intentFilter)
    }

    fun stopCallReceiver() {
        try {
            if (mCallRecordReceiver != null) {
                mContext.unregisterReceiver(mCallRecordReceiver)
            }
        } catch (e: Exception) {
            LogUtils.e(e)
        }
    }

    fun startCallRecordService() {
        val intent = Intent()
        intent.setClass(mContext, CallRecordService::class.java)

        mContext.startService(intent)
        LogUtils.i(TAG, "startService()")
    }

    fun enableSaveFile() {
        PrefsHelper.writePrefBool(mContext, PREF_SAVE_FILE, true)
        LogUtils.i("CallRecord", "Save file enabled")
    }

    fun disableSaveFile() {
        LogUtils.i("CallRecord", "Save file disabled")
        PrefsHelper.writePrefBool(mContext, PREF_SAVE_FILE, false)
    }

    fun changeRecordFileName(newFileName: String?) {
        if (newFileName == null || newFileName.isEmpty()) {
            try {
                throw Exception("newFileName can not be empty or null")
            } catch (e: Exception) {
                e.printStackTrace()
                return
            }
        }
        PrefsHelper.writePrefString(mContext, PREF_FILE_NAME, newFileName)
        LogUtils.i("CallRecord", "New file name: $newFileName")
    }

    fun changeRecordDirName(newDirName: String?) {
        if (newDirName == null || newDirName.isEmpty()) {
            try {
                throw Exception("newDirName can not be empty or null")
            } catch (e: Exception) {
                e.printStackTrace()
                return
            }
        }
        PrefsHelper.writePrefString(mContext, PREF_DIR_NAME, newDirName)
        LogUtils.i("CallRecord", "New dir name: $newDirName")
    }

    fun changeRecordDirPath(newDirPath: String?) {
        if (newDirPath == null || newDirPath.isEmpty()) {
            try {
                throw Exception("newDirPath can not be empty or null")
            } catch (e: Exception) {
                e.printStackTrace()
                return
            }
        }
        PrefsHelper.writePrefString(mContext, PREF_DIR_PATH, newDirPath)
        LogUtils.i("CallRecord", "New dir path: $newDirPath")
    }

    fun changeReceiver(receiver: CallRecordReceiver) {
        mCallRecordReceiver = receiver
    }

    class Builder(private val mContext: Context) {

        val recordFileName: String
            get() = PrefsHelper.readPrefString(mContext, PREF_FILE_NAME)!!

        val recordDirName: String
            get() = PrefsHelper.readPrefString(mContext, PREF_DIR_NAME)!!

        val audioSource: Int
            get() = PrefsHelper.readPrefInt(mContext, PREF_AUDIO_SOURCE)

        val audioEncoder: Int
            get() = PrefsHelper.readPrefInt(mContext, PREF_AUDIO_ENCODER)

        val outputFormat: Int
            get() = PrefsHelper.readPrefInt(mContext, PREF_OUTPUT_FORMAT)

        val isShowSeed: Boolean
            get() = PrefsHelper.readPrefBool(mContext, PREF_SHOW_SEED)

        val isShowPhoneNumber: Boolean
            get() = PrefsHelper.readPrefBool(mContext, PREF_SHOW_PHONE_NUMBER)

        val recordDirPath: String
            get() = PrefsHelper.readPrefString(mContext, PREF_DIR_PATH)!!

        val logEnable: Boolean
            get() = PrefsHelper.readPrefBool(mContext, PREF_LOG_ENABLE)

        init {
            PrefsHelper.writePrefString(mContext, PREF_FILE_NAME, "Record")
            PrefsHelper.writePrefString(mContext, PREF_DIR_NAME, "CallRecord")
            PrefsHelper.writePrefString(mContext, PREF_DIR_PATH, Environment.getExternalStorageDirectory().path)
            PrefsHelper.writePrefInt(mContext, PREF_AUDIO_SOURCE, MediaRecorder.AudioSource.VOICE_COMMUNICATION)
            PrefsHelper.writePrefInt(mContext, PREF_AUDIO_ENCODER, MediaRecorder.AudioEncoder.AMR_NB)
            PrefsHelper.writePrefInt(mContext, PREF_OUTPUT_FORMAT, MediaRecorder.OutputFormat.AMR_NB)
            PrefsHelper.writePrefBool(mContext, PREF_SHOW_SEED, true)
            PrefsHelper.writePrefBool(mContext, PREF_SHOW_PHONE_NUMBER, true)
            PrefsHelper.writePrefBool(mContext, PREF_LOG_ENABLE, false)
        }

        fun build(): CallRecord {
            val callRecord = CallRecord(mContext)
            callRecord.enableSaveFile()

            if (logEnable) {
                Timber.plant(Timber.DebugTree())
            }

            return callRecord
        }

        fun setRecordFileName(recordFileName: String?): Builder {
            PrefsHelper.writePrefString(mContext, PREF_FILE_NAME, recordFileName)
            return this
        }

        fun setRecordDirName(recordDirName: String?): Builder {
            PrefsHelper.writePrefString(mContext, PREF_DIR_NAME, recordDirName)
            return this
        }

        fun setAudioSource(audioSource: Int): Builder {
            PrefsHelper.writePrefInt(mContext, PREF_AUDIO_SOURCE, audioSource)
            return this
        }

        fun setAudioEncoder(audioEncoder: Int): Builder {
            PrefsHelper.writePrefInt(mContext, PREF_AUDIO_ENCODER, audioEncoder)
            return this
        }

        fun setOutputFormat(outputFormat: Int): Builder {
            PrefsHelper.writePrefInt(mContext, PREF_OUTPUT_FORMAT, outputFormat)
            return this
        }

        fun setShowSeed(showSeed: Boolean = true): Builder {
            PrefsHelper.writePrefBool(mContext, PREF_SHOW_SEED, showSeed)
            return this
        }

        fun setShowPhoneNumber(showNumber: Boolean = true): Builder {
            PrefsHelper.writePrefBool(mContext, PREF_SHOW_PHONE_NUMBER, showNumber)
            return this
        }

        fun setRecordDirPath(recordDirPath: String?): Builder {
            PrefsHelper.writePrefString(mContext, PREF_DIR_PATH, recordDirPath)
            return this
        }

        fun setLogEnable(isEnable: Boolean = false): Builder {
            PrefsHelper.writePrefBool(mContext, PREF_LOG_ENABLE, isEnable)
            return this
        }
    }

    companion object {

        private val TAG = CallRecord::class.java.simpleName

        const val PREF_SAVE_FILE = "PrefSaveFile"
        const val PREF_FILE_NAME = "PrefFileName"
        const val PREF_DIR_NAME = "PrefDirName"
        const val PREF_DIR_PATH = "PrefDirPath"
        const val PREF_SHOW_SEED = "PrefShowSeed"
        const val PREF_SHOW_PHONE_NUMBER = "PrefShowPhoneNumber"
        const val PREF_AUDIO_SOURCE = "PrefAudioSource"
        const val PREF_AUDIO_ENCODER = "PrefAudioEncoder"
        const val PREF_OUTPUT_FORMAT = "PrefOutputFormat"
        const val PREF_LOG_ENABLE = "PrefLogEnable"

        fun initReceiver(context: Context): CallRecord {
            val callRecord = Builder(context).build()
            callRecord.startCallReceiver()
            return callRecord
        }

        fun initService(context: Context): CallRecord {
            val callRecord = Builder(context).build()
            callRecord.startCallRecordService()
            return callRecord
        }
    }
}
