package com.callrecorderapp;

import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import androidx.annotation.RequiresApi;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CallRecorderModule extends ReactContextBaseJavaModule {
    private static final String TAG = "CallRecorderModule";
    private static ReactApplicationContext reactContext;
    private MediaRecorder mediaRecorder;
    private boolean isRecording = false;

    CallRecorderModule(ReactApplicationContext context) {
        super(context);
        reactContext = context;
    }

    @Override
    public String getName() {
        return "CallRecorder";
    }

    @ReactMethod
    public void startRecording() {
        if (mediaRecorder != null) {
            stopRecording();
        }

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        String outputPath = getOutputFilePath();
        mediaRecorder.setOutputFile(outputPath);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            isRecording = true;
            Log.d(TAG, "Recording started: " + outputPath);
        } catch (IOException e) {
            Log.e(TAG, "Error starting recording", e);
        }
    }

    @ReactMethod
    public void stopRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            isRecording = false;
            Log.d(TAG, "Recording stopped");
        }
    }

    @ReactMethod
    public void isRecording(Callback callback) {
        callback.invoke(isRecording);
    }

    private String getOutputFilePath() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String outputDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath();
        return outputDir + "/recording_" + timeStamp + ".m4a";
    }
}
