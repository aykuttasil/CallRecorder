package com.aykuttasil.callrecorder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.aykuttasil.callrecord.CallRecord;

public class MainActivity extends AppCompatActivity {

    CallRecord callRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //callRecord = CallRecord.init(this);

        /*
        callRecord = new CallRecord.Builder(this)
                .setRecordFileName("DenemeKayit")
                .setRecordDirName("CallRecordDir")
                .build();
        */

        callRecord = new CallRecord.Builder(this)
                .setRecordDirName("ServiceRecordDirName")
                .setRecordFileName("ServiceRecordFileName")
                .buildService();

        callRecord.startCallRecordService();

    }

    public void StartCallRecordClick(View view) {
        callRecord.startCallReceiver();
    }

    public void StopCallRecordClick(View view) {
        callRecord.stopCallReceiver();
    }
}
