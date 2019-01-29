package com.github.evan.common_library.manager.audio;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import com.github.evan.common_library.manager.thread.ThreadManager;
import com.github.evan.common_library.utils.DateUtil;
import com.github.evan.common_library.utils.FileUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Evan on 2019/1/29 0029.
 */
public class AudioRecordManager {
    //----------采样率----------
    private static final int MOBILE_NORMAL_SAMPLE_RATE_IN_HZ = 11025;       //手机标准录音采样率
    private static final int MOBILE_HD_SAMPLE_RATE_IN_HZ = 22050;           //手机高清录音采样率
        //---↑手机标准---
    private static final int CD_NORMAL_RATE_IN_HZ = 44100;                  //CD标准录音采样率
    private static final int CD_HD_RATE_IN_HZ = 88200;                      //CD高清录音采样率
    private static final int CD_FULL_HD_RATE_IN_HZ = 176400;                //CD全高清录音采样率
        //---↑CD标准
    private static final int HD_RATE_IN_HZ = 48000;                         //高清录音采样率
    private static final int FULL_HD_RATE_IN_HZ = 96000;                    //全高清录音采样率
    private static final int SUPER_HD_RATE_IN_HZ = 192000;                  //超高清录音采样率
        //---↑高清标准
    //----------采样率----------

    //----------声道----------
    private static final int SINGLE_AUDIO_CHANNEL = AudioFormat.CHANNEL_IN_MONO;    //单声道
    private static final int DOUBLE_AUDIO_CHANNEL = AudioFormat.CHANNEL_IN_STEREO;    //双声道
    //----------声道----------

    //----------编码格式----------
    private static final int EIGHT_BIT_ENCODING = AudioFormat.ENCODING_PCM_8BIT;        //8位编码格式
    private static final int SIXTEEN_BIT_ENCODING = AudioFormat.ENCODING_PCM_16BIT;     //16位编码格式
    //----------编码格式----------

    //----------音源----------
    private static final int AUDIO_SOURCE_MIC = MediaRecorder.AudioSource.MIC;          //麦克风音源

    /***
     * 录音音质
     */
    public enum AudioQuality{
        /** 手机标准录音音质 */
        MOBILE_NORMAL(0),
        /** 手机高清音质 */
        MOBILE_HD(1),
        /** CD标准音质 */
        CD_NORMAL(2),
        /** CD高清音质 */
        CD_HD(3),
        /** CD全高清音质 */
        CD_FULL_HD(4),
        /** 高清音质 */
        HD(5),
        /** 全高清音质 */
        FULL_HD(6),
        /** 超高清音质 */
        SUPER_HD(7);

        private int value;

        AudioQuality(int value) {
            this.value = value;
        }
    }

    private static AudioRecordManager mIns = null;
    public static AudioRecordManager getIns(){
        if(null == mIns){
            synchronized (AudioRecordManager.class){
                if(null == mIns){
                    mIns = new AudioRecordManager();
                    return mIns;
                }
            }
        }
        return mIns;
    }

    private AudioRecordManager(){
        init();
    }

    private ConcurrentHashMap<AudioQuality, AudioRecord> mAudioRecords = new ConcurrentHashMap<>(AudioQuality.values().length);
    private Map<AudioQuality, Integer> mBufferSize = new HashMap<>();

    /**
     * 开启录音
     * @param filePath              录音文件地址
     * @param isDeleteFileIfExits   如果录音文件已存在是否删除
     * @param quality               音质
     * @return                      是否已经开启
     */
    public boolean record(String filePath, boolean isDeleteFileIfExits, AudioQuality quality){
        final File file = new File(filePath);
        if(file.exists() || file.isDirectory()){
            if(!isDeleteFileIfExits){
                return false;
            }else{
                FileUtil.FileStatus fileStatus = FileUtil.createFile(filePath, true);
                if(fileStatus != FileUtil.FileStatus.CREATE_SUCCESS){
                    return false;
                }
            }
        }

        String filesDir = FileUtil.getFilesDir();
        String currentTime2String = DateUtil.currentTime2String(DateUtil.yyyy_MM_dd_HH_mm_ss_SSS, Locale.getDefault());
        currentTime2String += ".pcm";
        File tempFile = new File(filesDir + "/" + currentTime2String);
        if(tempFile.exists() || tempFile.isDirectory()){
            FileUtil.FileStatus fileStatus = FileUtil.createFile(filePath, true);
            if(fileStatus != FileUtil.FileStatus.CREATE_SUCCESS){
                return false;
            }
        }

        final AudioRecord audioRecord = mAudioRecords.get(quality);
        if(null == audioRecord || audioRecord.getState() == AudioRecord.STATE_UNINITIALIZED){
            return false;
        }

        final int bufferSize = mBufferSize.get(quality);
        if(bufferSize <= 0){
            return false;
        }

        ThreadPoolExecutor ioThreadPool = ThreadManager.getInstance().getIOThreadPool();
        AudioRecordTask task = new AudioRecordTask(audioRecord, tempFile.getAbsolutePath(), filePath, bufferSize, quality);
        ioThreadPool.submit(task);
        return true;
    }


    /**
     * pcm文件转wav文件
     *
     * @param inFilename 源文件路径
     * @param outFilename 目标文件路径
     */
    public boolean pcmToWav(AudioQuality quality, String inFilename, String outFilename) {
        try {
            FileInputStream in;
            FileOutputStream out;
            in = new FileInputStream(inFilename);
            out = new FileOutputStream(outFilename);
            long totalAudioLen;
            long totalDataLen;
            long sampleRate = 0;
            int channels = 0;
            long byteRate = 0;
            byte[] data = null;
            totalAudioLen = in.getChannel().size();
            totalDataLen = totalAudioLen + 36;
            switch (quality){
                case MOBILE_NORMAL:
                    sampleRate = MOBILE_NORMAL_SAMPLE_RATE_IN_HZ;
                    channels = SINGLE_AUDIO_CHANNEL;
                    byteRate = 16 * sampleRate * channels / 8;
                    data = new byte[mBufferSize.get(quality)];
                    break;

                case MOBILE_HD:
                    sampleRate = MOBILE_HD_SAMPLE_RATE_IN_HZ;
                    channels = DOUBLE_AUDIO_CHANNEL;
                    byteRate = 16 * sampleRate * channels / 8;
                    data = new byte[mBufferSize.get(quality)];
                    break;

                case CD_NORMAL:
                    sampleRate = CD_NORMAL_RATE_IN_HZ;
                    channels = DOUBLE_AUDIO_CHANNEL;
                    byteRate = 16 * sampleRate * channels / 8;
                    data = new byte[mBufferSize.get(quality)];
                    break;

                case CD_HD:
                    sampleRate = CD_HD_RATE_IN_HZ;
                    channels = DOUBLE_AUDIO_CHANNEL;
                    byteRate = 16 * sampleRate * channels / 8;
                    data = new byte[mBufferSize.get(quality)];
                    break;

                case CD_FULL_HD:
                    sampleRate = CD_FULL_HD_RATE_IN_HZ;
                    channels = DOUBLE_AUDIO_CHANNEL;
                    byteRate = 16 * sampleRate * channels / 8;
                    data = new byte[mBufferSize.get(quality)];
                    break;

                case HD:
                    sampleRate = HD_RATE_IN_HZ;
                    channels = DOUBLE_AUDIO_CHANNEL;
                    byteRate = 16 * sampleRate * channels / 8;
                    data = new byte[mBufferSize.get(quality)];
                    break;

                case FULL_HD:
                    sampleRate = FULL_HD_RATE_IN_HZ;
                    channels = DOUBLE_AUDIO_CHANNEL;
                    byteRate = 16 * sampleRate * channels / 8;
                    data = new byte[mBufferSize.get(quality)];
                    break;

                case SUPER_HD:
                    sampleRate = SUPER_HD_RATE_IN_HZ;
                    channels = DOUBLE_AUDIO_CHANNEL;
                    byteRate = 16 * sampleRate * channels / 8;
                    data = new byte[mBufferSize.get(quality)];
                    break;
            }

            writeWaveFileHeader(out, totalAudioLen, totalDataLen, sampleRate, channels, byteRate);
            while (in.read(data) != -1) {
                out.write(data);
            }
            in.close();
            out.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 加入wav文件头
     */
    private void writeWaveFileHeader(FileOutputStream out, long totalAudioLen, long totalDataLen, long longSampleRate, int channels, long byteRate)
            throws IOException {
        byte[] header = new byte[44];
        // RIFF/WAVE header
        header[0] = 'R';
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        //WAVE
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        // 'fmt ' chunk
        header[12] = 'f';
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        // 4 bytes: size of 'fmt ' chunk
        header[16] = 16;
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        // format = 1
        header[20] = 1;
        header[21] = 0;
        header[22] = (byte) channels;
        header[23] = 0;
        header[24] = (byte) (longSampleRate & 0xff);
        header[25] = (byte) ((longSampleRate >> 8) & 0xff);
        header[26] = (byte) ((longSampleRate >> 16) & 0xff);
        header[27] = (byte) ((longSampleRate >> 24) & 0xff);
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        // block align
        header[32] = (byte) (2 * 16 / 8);
        header[33] = 0;
        // bits per sample
        header[34] = 16;
        header[35] = 0;
        //data
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (totalAudioLen & 0xff);
        header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
        header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
        header[43] = (byte) ((totalAudioLen >> 24) & 0xff);
        out.write(header, 0, 44);
    }












    private void init(){
        AudioQuality[] values = AudioQuality.values();
        for (int i = 0; i < values.length; i++) {
            AudioQuality audioQuality = values[i];
            AudioRecord audioRecord;
            int minBuffer = 0;
            switch (audioQuality){
                case MOBILE_NORMAL:
                    minBuffer = AudioRecord.getMinBufferSize(MOBILE_NORMAL_SAMPLE_RATE_IN_HZ, SINGLE_AUDIO_CHANNEL, EIGHT_BIT_ENCODING);
                    audioRecord = new AudioRecord(AUDIO_SOURCE_MIC, MOBILE_NORMAL_SAMPLE_RATE_IN_HZ, SINGLE_AUDIO_CHANNEL, EIGHT_BIT_ENCODING, minBuffer);
                    mAudioRecords.put(AudioQuality.MOBILE_NORMAL, audioRecord);
                    break;

                case MOBILE_HD:
                    minBuffer = AudioRecord.getMinBufferSize(MOBILE_HD_SAMPLE_RATE_IN_HZ, DOUBLE_AUDIO_CHANNEL, SIXTEEN_BIT_ENCODING);
                    audioRecord = new AudioRecord(AUDIO_SOURCE_MIC, MOBILE_HD_SAMPLE_RATE_IN_HZ, DOUBLE_AUDIO_CHANNEL, SIXTEEN_BIT_ENCODING, minBuffer);
                    mAudioRecords.put(AudioQuality.MOBILE_HD, audioRecord);
                    break;

                case CD_NORMAL:
                    minBuffer = AudioRecord.getMinBufferSize(CD_NORMAL_RATE_IN_HZ, DOUBLE_AUDIO_CHANNEL, SIXTEEN_BIT_ENCODING);
                    audioRecord = new AudioRecord(AUDIO_SOURCE_MIC, CD_NORMAL_RATE_IN_HZ, DOUBLE_AUDIO_CHANNEL, SIXTEEN_BIT_ENCODING, minBuffer);
                    mAudioRecords.put(AudioQuality.CD_NORMAL, audioRecord);
                    break;

                case CD_HD:
                    minBuffer = AudioRecord.getMinBufferSize(CD_HD_RATE_IN_HZ, DOUBLE_AUDIO_CHANNEL, SIXTEEN_BIT_ENCODING);
                    audioRecord = new AudioRecord(AUDIO_SOURCE_MIC, CD_HD_RATE_IN_HZ, DOUBLE_AUDIO_CHANNEL, SIXTEEN_BIT_ENCODING, minBuffer);
                    mAudioRecords.put(AudioQuality.CD_HD, audioRecord);
                    break;

                case CD_FULL_HD:
                    minBuffer = AudioRecord.getMinBufferSize(CD_FULL_HD_RATE_IN_HZ, DOUBLE_AUDIO_CHANNEL, SIXTEEN_BIT_ENCODING);
                    audioRecord = new AudioRecord(AUDIO_SOURCE_MIC, CD_FULL_HD_RATE_IN_HZ, DOUBLE_AUDIO_CHANNEL, SIXTEEN_BIT_ENCODING, minBuffer);
                    mAudioRecords.put(AudioQuality.CD_FULL_HD, audioRecord);
                    break;

                case HD:
                    minBuffer = AudioRecord.getMinBufferSize(HD_RATE_IN_HZ, DOUBLE_AUDIO_CHANNEL, SIXTEEN_BIT_ENCODING);
                    audioRecord = new AudioRecord(AUDIO_SOURCE_MIC, HD_RATE_IN_HZ, DOUBLE_AUDIO_CHANNEL, SIXTEEN_BIT_ENCODING, minBuffer);
                    mAudioRecords.put(AudioQuality.HD, audioRecord);
                    break;

                case FULL_HD:
                    minBuffer = AudioRecord.getMinBufferSize(FULL_HD_RATE_IN_HZ, DOUBLE_AUDIO_CHANNEL, SIXTEEN_BIT_ENCODING);
                    audioRecord = new AudioRecord(AUDIO_SOURCE_MIC, FULL_HD_RATE_IN_HZ, DOUBLE_AUDIO_CHANNEL, SIXTEEN_BIT_ENCODING, minBuffer);
                    mAudioRecords.put(AudioQuality.FULL_HD, audioRecord);
                    break;

                case SUPER_HD:
                    minBuffer = AudioRecord.getMinBufferSize(SUPER_HD_RATE_IN_HZ, DOUBLE_AUDIO_CHANNEL, SIXTEEN_BIT_ENCODING);
                    audioRecord = new AudioRecord(AUDIO_SOURCE_MIC, SUPER_HD_RATE_IN_HZ, DOUBLE_AUDIO_CHANNEL, SIXTEEN_BIT_ENCODING, minBuffer);
                    mAudioRecords.put(AudioQuality.SUPER_HD, audioRecord);
                    break;
            }
        }
    }

}
