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

    private static MediaRecorder recorder;
    private File audiofile;
    private boolean isRecordStarted = false;


    public CallRecordReceiver() {
    }

    @Override
    protected void onIncomingCallReceived(Context ctx, String number, Date start) {

    }

    @Override
    protected void onIncomingCallAnswered(Context ctx, String number, Date start) {
        startRecord(ctx, "incoming");
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {

        if (recorder != null && isRecordStarted) {

            recorder.stop();
            recorder.reset();
            recorder.release();

            isRecordStarted = false;

            Log.i(TAG, "record stop");
        }
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
        startRecord(ctx, "outgoing");
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {

        if (recorder != null && isRecordStarted) {

            recorder.stop();
            recorder.reset();
            recorder.release();

            isRecordStarted = false;

            Log.i(TAG, "record stop");
        }
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start) {

    }


    private void startRecord(Context context, String seed) {

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
            int output_format = PrefsHelper.readPrefInt(context, CallRecord.PREF_OUTPUT_FORMAT);
            int audio_source = PrefsHelper.readPrefInt(context, CallRecord.PREF_AUDIO_SOURCE);
            int audio_encoder = PrefsHelper.readPrefInt(context, CallRecord.PREF_AUDIO_ENCODER);

            File sampleDir = new File(dir_path + "/" + dir_name);

            if (!sampleDir.exists()) {
                sampleDir.mkdirs();
            }


            if (show_seed) {
                file_name = file_name + "_" + seed + "_"; // temp dosyaya kayıt edildiği için dosya isminin en sonuna random karakter ekleniyor
            } else {
                file_name = file_name + "_";
            }

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

        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }

        recorder.start();

        isRecordStarted = true;

        Log.i(TAG, "record start");
    }

}
