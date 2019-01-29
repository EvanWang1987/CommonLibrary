package com.github.evan.common_library.manager.audio;

import android.content.Context;
import android.media.SoundPool;
import android.support.annotation.RawRes;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Evan on 2019/1/28 0028.
 */
public class AudioManager {
    public static final int MAX_SOUND_POOL_STREAM_COUNT = 10;       //SoundPool最多加载10条
    private static AudioManager mIns = null;
    private static AudioManager getIns(Context context){
        if(null == mIns){
            synchronized (AudioManager.class){
                if(null == mIns){
                    mIns = new AudioManager(context);
                    return mIns;
                }
            }
        }
        return mIns;
    }

    private android.media.AudioManager mSystemAudioManager;
    private SoundPool mSoundPool;
    private ConcurrentHashMap<Integer, Integer> mLoadedSound;

    private AudioManager(Context context){
        mSystemAudioManager = (android.media.AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mSoundPool = new SoundPool(MAX_SOUND_POOL_STREAM_COUNT, android.media.AudioManager.STREAM_ALARM, 0);
    }

    /**
     * 加载声音到声音池
     * @param context
     * @param sound
     * @param priority
     * @return
     */
    public boolean loadSoundIntoSoundPool(Context context, @RawRes int sound, int priority){
        if(mLoadedSound.contains(sound)){
            return false;
        }

        int soundId = mSoundPool.load(context, sound, priority);
        mLoadedSound.put(sound, soundId);
        return true;
    }

    /**
     * 播放声音池声音
     * @param sound
     * @param leftVolume
     * @param rightVolume
     * @param priority
     * @param loop
     * @param rate
     * @return
     */
    public boolean playSoundWhichInSoundPool(@RawRes int sound, float leftVolume, float rightVolume, int priority, int loop, float rate){
        if(!mLoadedSound.containsKey(sound)){
            return false;
        }

        Integer soundId = mLoadedSound.get(sound);
        int played = mSoundPool.play(soundId, leftVolume, rightVolume, priority, loop, rate);
        return played != 0;
    }

    /** 暂停播放 */
    public boolean pauseSoundWhichInSoundPool(@RawRes int sound){
        if(!mLoadedSound.containsKey(sound)){
            return false;
        }

        mSoundPool.pause(mLoadedSound.get(sound));
        return true;
    }

    /** 继续播放 */
    public boolean resumeSoundWhichInSoundPool(@RawRes int sound){
        if(!mLoadedSound.containsKey(sound)){
            return false;
        }

        mSoundPool.resume(mLoadedSound.get(sound));
        return true;
    }

    /** 释放声音池 */
    public void releaseSoundPool(){
        mLoadedSound.clear();
        mSoundPool.release();
    }

    /** 获取最大铃声音量 */
    public int getMaxRingSound(){
        return mSystemAudioManager.getStreamMaxVolume(android.media.AudioManager.STREAM_RING);
    }

    /** 获取最大多媒体音量 */
    public int getMaxMusicSound(){
        return mSystemAudioManager.getStreamMaxVolume(android.media.AudioManager.STREAM_MUSIC);
    }

    /** 获取最大提示音音量 */
    public int getMaxAlarmSound(){
        return mSystemAudioManager.getStreamMaxVolume(android.media.AudioManager.STREAM_ALARM);
    }

    /** 获取最大系统音量 */
    public int getMaxSystemSound(){
        return mSystemAudioManager.getStreamMaxVolume(android.media.AudioManager.STREAM_SYSTEM);
    }

    /** 调整铃声音量 */
    public void adjustRingVolume(int value, int flag){
        mSystemAudioManager.adjustStreamVolume(android.media.AudioManager.STREAM_RING, value, android.media.AudioManager.FLAG_VIBRATE);
    }

    /** 调整多媒体音量 */
    public void adjustMusicVolume(int value, int flag){
        mSystemAudioManager.adjustStreamVolume(android.media.AudioManager.STREAM_MUSIC, value, android.media.AudioManager.FLAG_VIBRATE);
    }

    /** 调整系统音量 */
    public void adjustSystemVolume(int value, int flag){
        mSystemAudioManager.adjustStreamVolume(android.media.AudioManager.STREAM_SYSTEM, value, android.media.AudioManager.FLAG_VIBRATE);
    }

    /** 调整提示音音量 */
    public void adjustAlarmVolume(int value, int flag){
        mSystemAudioManager.adjustStreamVolume(android.media.AudioManager.STREAM_ALARM, value, android.media.AudioManager.FLAG_VIBRATE);
    }

    /** 调整当前声音模式音量 */
    public void adjustCurrentVolume(int value, int flag){
        mSystemAudioManager.adjustVolume(value, flag);
    }

    /** 获取铃声音量 */
    public void getRingAudioVolume(){
        mSystemAudioManager.getStreamVolume(android.media.AudioManager.STREAM_RING);
    }

    /** 获取多媒体音量 */
    public void getMusicAudioVolume(){
        mSystemAudioManager.getStreamVolume(android.media.AudioManager.STREAM_MUSIC);
    }

    /** 获取提示音音量 */
    public void getAlarmAudioVolume(){
        mSystemAudioManager.getStreamVolume(android.media.AudioManager.STREAM_ALARM);
    }

    /** 获取系统音量 */
    public void getSystemAudioVolume(){
        mSystemAudioManager.getStreamVolume(android.media.AudioManager.STREAM_SYSTEM);
    }

    /** 设置麦克风静音 */
    public void setMicrophoneMute(boolean isMute){
        mSystemAudioManager.setMicrophoneMute(isMute);
    }

    /** 麦克风是否静音 */
    public boolean isMicrophoneMute(){
        return mSystemAudioManager.isMicrophoneMute();
    }
}
