package com.aykuttasil.callrecord.receiver;

import android.content.Context;
import android.media.MediaRecorder;
import android.util.Log;

import com.aykuttasil.callrecord.CallRecord;
import com.aykuttasil.callrecord.helper.PrefsHelper;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by aykutasil on 19.10.2016.
 */
public class CallRecordReceiver extends PhoneCallReceiver {


    private static final String TAG = CallRecordReceiver.class.getSimpleName();

    public static final String ACTION_IN = "android.intent.action.PHONE_STATE";
    public static final String ACTION_OUT = "android.intent.action.NEW_OUTGOING_CALL";
    public static final String EXTRA_PHONE_NUMBER = "android.intent.extra.PHONE_NUMBER";

    protected CallRecord callRecord;
    private static MediaRecorder recorder;
    private File audiofile;
    private boolean isRecordStarted = false;

    public CallRecordReceiver(CallRecord callRecord) {
        this.callRecord = callRecord;
    }

    @Override
    protected void onIncomingCallReceived(Context context, String number, Date start) {

    }

    @Override
    protected void onIncomingCallAnswered(Context context, String number, Date start) {
        startRecord(context, "incoming", number);
    }

    @Override
    protected void onIncomingCallEnded(Context context, String number, Date start, Date end) {
        stopRecord(context);
    }

    @Override
    protected void onOutgoingCallStarted(Context context, String number, Date start) {
        startRecord(context, "outgoing", number);
    }

    @Override
    protected void onOutgoingCallEnded(Context context, String number, Date start, Date end) {
        stopRecord(context);
    }

    @Override
    protected void onMissedCall(Context context, String number, Date start) {

    }

    // Derived classes could override these to respond to specific events of interest
    protected void onRecordingStarted(Context context, CallRecord callRecord, File audioFile) {
    }

    protected void onRecordingFinished(Context context, CallRecord callRecord, File audioFile) {
    }

    private void startRecord(Context context, String seed, String phoneNumber) {

        try {

            boolean isSaveFile = PrefsHelper.readPrefBool(context, CallRecord.PREF_SAVE_FILE);
            Log.i(TAG, "isSaveFile: " + isSaveFile);

            // dosya kayıt edilsin mi?
            if (!isSaveFile) {
                return;
            }

            String file_name = PrefsHelper.readPrefString(context, CallRecord.PREF_FILE_NAME);
            String dir_path = PrefsHelper.readPrefString(context, CallRecord.PREF_DIR_PATH);
            String dir_name = PrefsHelper.readPrefString(context, CallRecord.PREF_DIR_NAME);
            boolean show_seed = PrefsHelper.readPrefBool(context, CallRecord.PREF_SHOW_SEED);
            boolean show_phone_number = PrefsHelper.readPrefBool(context, CallRecord.PREF_SHOW_PHONE_NUMBER);
            int output_format = PrefsHelper.readPrefInt(context, CallRecord.PREF_OUTPUT_FORMAT);
            int audio_source = PrefsHelper.readPrefInt(context, CallRecord.PREF_AUDIO_SOURCE);
            int audio_encoder = PrefsHelper.readPrefInt(context, CallRecord.PREF_AUDIO_ENCODER);

            File sampleDir = new File(dir_path + "/" + dir_name);

            if (!sampleDir.exists()) {
                sampleDir.mkdirs();
            }


            StringBuilder fileNameBuilder = new StringBuilder();
            fileNameBuilder.append(file_name);
            fileNameBuilder.append("_");

            if (show_seed) {
                fileNameBuilder.append(seed);
                fileNameBuilder.append("_");
            }

            if (show_phone_number) {
                fileNameBuilder.append(phoneNumber);
                fileNameBuilder.append("_");
            }


            file_name = fileNameBuilder.toString();

            String suffix = "";
            switch (output_format) {
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
            recorder.setAudioSource(audio_source);
            recorder.setOutputFormat(output_format);
            recorder.setAudioEncoder(audio_encoder);
            recorder.setOutputFile(audiofile.getAbsolutePath());

            recorder.prepare();
            //Thread.sleep(2000);
            recorder.start();

            isRecordStarted = true;
            onRecordingStarted(context, callRecord, audiofile);

            Log.i(TAG, "record start");

        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecord(Context context) {
        if (recorder != null && isRecordStarted) {

            recorder.stop();
            recorder.reset();
            recorder.release();
            recorder = null;

            isRecordStarted = false;
            onRecordingFinished(context, callRecord, audiofile);

            Log.i(TAG, "record stop");
        }
    }

}
