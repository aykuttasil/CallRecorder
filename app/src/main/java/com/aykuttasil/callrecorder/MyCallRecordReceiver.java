package com.aykuttasil.callrecorder;

import android.content.Context;

import com.aykuttasil.callrecord.CallRecord;
import com.aykuttasil.callrecord.receiver.CallRecordReceiver;

import java.util.Date;

/**
 * Created by aykutasil on 26.12.2016.
 */

public class MyCallRecordReceiver extends CallRecordReceiver {

    public MyCallRecordReceiver(CallRecord callRecord) {
        super(callRecord);
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
        //callRecord.disableSaveFile();
        super.onOutgoingCallStarted(ctx, number, start);
    }
}
