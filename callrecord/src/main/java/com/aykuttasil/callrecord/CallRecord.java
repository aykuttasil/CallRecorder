package com.aykuttasil.callrecord;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.aykuttasil.callrecord.receiver.CallRecordReceiver;
import com.aykuttasil.callrecord.service.CallRecordService;

/**
 * Created by aykutasil on 20.10.2016.
 */

public class CallRecord {

    private static final String TAG = CallRecord.class.getSimpleName();

    public static String INTENT_FILE_NAME = "RecordFileName";
    public static String INTENT_DIR_NAME = "RecordDirName";

    private Context mContext;
    private CallRecordReceiver mCallRecordReceiver;
    private Intent intent;
    private String fileName;
    private String dirName;


    private CallRecord(Context context) {
        this.mContext = context;
        this.mCallRecordReceiver = new CallRecordReceiver();
    }

    private CallRecord(Context context, Intent intent) {
        this.mContext = context;
        this.intent = intent;
        this.mCallRecordReceiver = new CallRecordReceiver();
    }

    public static CallRecord initReceiver(Context context) {
        CallRecord callRecord = new Builder(context).build();
        callRecord.startCallReceiver();
        return callRecord;
    }

    public static CallRecord initService(Context context) {
        CallRecord callRecord = new Builder(context).buildService();
        callRecord.startCallRecordService();
        return callRecord;
    }

    private void setFileName(String fileName) {
        this.fileName = fileName;
    }

    private String getFileName() {
        return fileName;
    }

    private void setDirName(String dirName) {
        this.dirName = dirName;
    }

    private String getDirName() {
        return dirName;
    }

    private void setIntent(Intent intent) {
        this.intent = intent;
    }

    private Intent getIntent() {
        return intent;
    }

    //

    public void startCallReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CallRecordReceiver.ACTION_IN);
        intentFilter.addAction(CallRecordReceiver.ACTION_OUT);

        mCallRecordReceiver.setFileName(getFileName());
        mCallRecordReceiver.setDirName(getDirName());

        mContext.registerReceiver(mCallRecordReceiver, intentFilter);
    }

    public void stopCallReceiver() {
        try {
            mContext.unregisterReceiver(mCallRecordReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startCallRecordService() {

        if (intent == null) {
            try {
                throw new Exception("Intent nesnesi boş. Lütfen buildService() i çalıştırdığınızdan emin olun.");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        intent.setClass(mContext, CallRecordService.class);

        Log.i(TAG, "startService()");

        mContext.startService(intent);
    }

    public static class Builder {

        private Context context;
        private String recordFileName = "Record";
        private String recordDirName = "CallRecord";

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setRecordFileName(String recordFileName) {
            this.recordFileName = recordFileName;
            return this;
        }

        private String getRecordFileName() {
            return recordFileName;
        }

        public Builder setRecordDirName(String recordDirName) {
            this.recordDirName = recordDirName;
            return this;
        }

        private String getRecordDirName() {
            return recordDirName;
        }

        public CallRecord build() {
            CallRecord callRecord = new CallRecord(context);
            callRecord.setFileName(getRecordFileName());
            callRecord.setDirName(getRecordDirName());
            return callRecord;
        }

        public CallRecord buildService() {
            Intent intent = new Intent();
            intent.putExtra(INTENT_FILE_NAME, getRecordFileName());
            intent.putExtra(INTENT_DIR_NAME, getRecordDirName());
            return new CallRecord(context, intent);
        }
    }
}
