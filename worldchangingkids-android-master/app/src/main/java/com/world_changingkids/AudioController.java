package com.world_changingkids;

import android.content.Context;
import android.media.MediaPlayer;
import com.world_changingkids.model.BackgroundSoundService;
import static com.world_changingkids.MainActivity.svc;

/**
 * AudioController to control play audio and stop
 */
public class AudioController {

	/**
	 * singleton instance of AudioController
	 */
	private static AudioController mAudioController;
	private MediaPlayer mMediaPlayer;
	private Context mContext;

	/**
	 * get singleton instance of AudioController
	 * @param context
	 * @return
	 */
	public static AudioController getInstance(Context context) {
		if (mAudioController == null) {
			mAudioController = new AudioController(context);
		}
		return mAudioController;
	}

	/**
	 * initial constructor
	 * @param context
	 */
	private AudioController(Context context) {
		mContext = context;
		mMediaPlayer = new MediaPlayer();
	}

	/**
	 * load audio by resource id
	 * @param resourceId
	 */
	public void loadAudio(int resourceId) {
		mMediaPlayer = MediaPlayer.create(mContext, resourceId);
	}

	/**
	 * handle to play audio
	 * @param isLooping
	 * @return
	 */
	public boolean playAudio(boolean isLooping) {
		if (isLooping) {
			BackgroundSoundService.player.stop();
			mMediaPlayer.setLooping(isLooping);
		}

		if (mMediaPlayer != null) {
			if(svc == null){
				mMediaPlayer.start();
			}
			else{
				mContext.stopService(svc);
				mMediaPlayer.start();
			}
		}
		return true;
	}

	public void stopAudio() {
		if (mMediaPlayer != null) {
			mMediaPlayer.stop();
		}
	}

}
