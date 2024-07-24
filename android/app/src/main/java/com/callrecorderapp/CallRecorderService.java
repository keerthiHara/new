package com.callrecorderapp;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class CallRecorderService extends Service {
    private MediaRecorder mediaRecorder;
    private static boolean isRecording = false;
    public static final String ACTION_START_RECORDING = "com.callrecorderapp.START_RECORDING";
    public static final String ACTION_STOP_RECORDING = "com.callrecorderapp.STOP_RECORDING";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            switch (intent.getAction()) {
                case ACTION_START_RECORDING:
                    startRecording();
                    break;
                case ACTION_STOP_RECORDING:
                    stopRecording();
                    break;
            }
        }
        return START_STICKY;
    }

    private void startRecording() {
        if (isRecording) return;

        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            Log.e("CallRecorderService", "External storage is not mounted");
            return;
        }

        File recordingDir = new File(Environment.getExternalStorageDirectory(), "CallRecordings");
        if (!recordingDir.exists() && !recordingDir.mkdirs()) {
            Log.e("CallRecorderService", "Failed to create recording directory");
            return;
        }

        String fileName = "call_recording_" + System.currentTimeMillis() + ".mp3";
        File outputFile = new File(recordingDir, fileName);

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setOutputFile(outputFile.getAbsolutePath());

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            isRecording = true;
            Log.d("CallRecorderService", "Recording started: " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            Log.e("CallRecorderService", "Error starting recording", e);
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    private void stopRecording() {
        if (!isRecording || mediaRecorder == null) return;

        try {
            mediaRecorder.stop();
            mediaRecorder.release();
            Log.d("CallRecorderService", "Recording stopped");
        } catch (RuntimeException e) {
            Log.e("CallRecorderService", "Error stopping recording", e);
        } finally {
            mediaRecorder = null;
            isRecording = false;
        }
    }

    public static boolean isRecording() {
        return isRecording;
    }
}
