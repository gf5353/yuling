package org.example.yulin.util;

import org.example.yulin.R;
import org.mobile.gqz.GQZInject;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

/**
 * 讯飞utils
 * 
 * @author admin
 *
 */
public class IflytekUtil implements InitListener, RecognizerListener,
		RecognizerDialogListener, SynthesizerListener {
	// 语音听写对象
	public static SpeechRecognizer mIat;
	// 语音听写UI
	public RecognizerDialog iatDialog;
	private Context context;
	private static IflytekUtil iflytekUtil;
	// 语音合成对象
	public static SpeechSynthesizer mTts;
	private String[] cloudVoicersEntries;
	private String[] cloudVoicersValue;
	// 缓冲进度
	private int mPercentForBuffering = 0;
	// 播放进度
	private int mPercentForPlaying = 0;

	// 引擎类型
	private String mEngineType = SpeechConstant.TYPE_CLOUD;

	private IflytekUtil() {

	}

	private IflytekuListener iflytekuListener;

	public void setIflytekuListener(IflytekuListener iflytekuListener) {
		this.iflytekuListener = iflytekuListener;
	}

	public interface IflytekuListener {
		/**
		 * 讯飞语音识别返回字符串
		 * 
		 * @param result
		 */
		void onResult(String result);
	}

	public static IflytekUtil getInstance() {
		if (iflytekUtil == null) {
			iflytekUtil = new IflytekUtil();
		}
		return iflytekUtil;
	}

	public IflytekUtil initIflytek(Context context) {
		this.context = context;
		// 初始化识别对象
		mIat = SpeechRecognizer.createRecognizer(context, this);
		// 初始化合成对象
		mTts = SpeechSynthesizer.createSynthesizer(context, this);

		// 初始化听写Dialog,如果只使用有UI听写功能,无需创建SpeechRecognizer
		iatDialog = new RecognizerDialog(context, this);
		// 云端发音人名称列表
		cloudVoicersEntries = context.getResources().getStringArray(
				R.array.voicer_cloud_entries);
		cloudVoicersValue = context.getResources().getStringArray(
				R.array.voicer_cloud_values);
		mEngineType = SpeechConstant.TYPE_CLOUD;// 本地合成
		return iflytekUtil;
	}

	public void setVoicer(String voicer) {
		this.voicer = voicer;
		setParam();
	}

	// 默认发音人
	private String voicer = "xiaoyan";

	private void setParam() {
		// 设置音频保存路径
		mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH,
				Environment.getExternalStorageDirectory()
						+ "/iflytek/wavaudio.pcm");
		// 设置语言
		mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn"); // 设置语言区域
		mIat.setParameter(SpeechConstant.ACCENT, "zh_cn");
		// 设置语音前端点
		mIat.setParameter(SpeechConstant.VAD_BOS, "4000");
		// 设置语音后端点
		mIat.setParameter(SpeechConstant.VAD_EOS, "1000");
		// 设置标点符号
		mIat.setParameter(SpeechConstant.ASR_PTT, "0");

		// 设置合成
		if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
			mTts.setParameter(SpeechConstant.ENGINE_TYPE,
					SpeechConstant.TYPE_CLOUD);
			// 设置发音人
			mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);

			// 设置语速
			mTts.setParameter(SpeechConstant.SPEED, "50");

			// 设置音调
			mTts.setParameter(SpeechConstant.PITCH, "50");

			// 设置音量
			mTts.setParameter(SpeechConstant.VOLUME, "50");

			// 设置播放器音频流类型
			mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
		} else {
			mTts.setParameter(SpeechConstant.ENGINE_TYPE,
					SpeechConstant.TYPE_LOCAL);
			// 设置发音人 voicer为空默认通过语音+界面指定发音人。
			mTts.setParameter(SpeechConstant.VOICE_NAME, "");
		}
	}

	public void onDestroy() {
		// 退出时释放连接
		mIat.cancel();
		mIat.destroy();
		mTts.stopSpeaking();
		// 退出时释放连接
		mTts.destroy();
	}

	@Override
	public void onInit(int code) {
		if (code != ErrorCode.SUCCESS) {
			GQZInject.toast("初始化失败,错误码：" + code);
		}
	}

	public void show() {
		if (iatDialog.isShowing()) {
			iatDialog.cancel();
		} else {
			iatDialog.setListener(this);
			iatDialog.show();
		}
	}

	public void speak(String text) {
		int code = mTts.startSpeaking(text, this);
		if (code != ErrorCode.SUCCESS) {
			if (code == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED) {
				// 未安装则跳转到提示安装页面
			} else {
				GQZInject.toast("语音合成失败,错误码: " + code);
			}
		}
	}

	/**
	 * 听写监听器。start
	 */
	@Override
	public void onBeginOfSpeech() {
		GQZInject.toast("开始说话");
	}

	@Override
	public void onEndOfSpeech() {
		GQZInject.toast("结束说话");
	}

	@Override
	public void onError(SpeechError error) {
		GQZInject.toast(error.getPlainDescription(true));
	}

	@Override
	public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {

	}

	@Override
	public void onResult(RecognizerResult results, boolean isLast) {
		String text = JsonParser.parseIatResult(results.getResultString());
		if (!TextUtils.isEmpty(text) && iflytekuListener != null) {
			iflytekuListener.onResult(text);
		}
		if (isLast) {
			// TODO 最后的结果
		}
	}

	@Override
	public void onVolumeChanged(int volume) {
		GQZInject.toast("当前正在说话，音量大小：" + volume);
	}

	/**
	 * 听写监听器。end
	 */

	/**
	 * 语音合成回调接口
	 * 
	 * @return
	 */
	@Override
	public void onBufferProgress(int arg0, int arg1, int arg2, String arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCompleted(SpeechError arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSpeakBegin() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSpeakPaused() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSpeakProgress(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSpeakResumed() {
		// TODO Auto-generated method stub

	}

}
