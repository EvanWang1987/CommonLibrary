package com.github.evan.common_library.manager.audio;

import android.media.AudioRecord;
import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Evan on 2019/1/29 0029.
 */
public class AudioRecordTask implements Runnable {
    private AudioRecordManager.AudioQuality mQuality;   //音质
    private AudioRecord mAudioRecord;   //录音器
    private String mTempFilePath;       //临时保存路径
    private String mTargetFilePath;     //真正存储路径
    private int mBufferSize;            //缓存大小

    public AudioRecordTask(AudioRecord mAudioRecord, String mTempFilePath, String mTargetFilePath, int mBufferSize, AudioRecordManager.AudioQuality mQuality) {
        this.mAudioRecord = mAudioRecord;
        this.mTempFilePath = mTempFilePath;
        this.mTargetFilePath = mTargetFilePath;
        this.mBufferSize = mBufferSize;
        this.mQuality = mQuality;
        if(null == mAudioRecord || TextUtils.isEmpty(mTargetFilePath) || TextUtils.isEmpty(mTargetFilePath) || mBufferSize <= 0 || null == mQuality){
            throw new RuntimeException("Args can not be NULL!");
        }
    }

    @Override
    public void run() {
        DataOutputStream outputStream = null;
        try {
            byte[] buffer = new byte[mBufferSize];
            outputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File(mTempFilePath))));
            mAudioRecord.startRecording();
            while (mAudioRecord.getState() == AudioRecord.RECORDSTATE_RECORDING){
                int readedData = mAudioRecord.read(buffer, 0, mBufferSize);
                for (int i = 0; i < readedData; i++){
                    outputStream.write(buffer[i]);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                outputStream.close();
                boolean isPcmToWavSuccess = AudioRecordManager.getIns().pcmToWav(mQuality, mTempFilePath, mTargetFilePath);
                mAudioRecord.stop();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
