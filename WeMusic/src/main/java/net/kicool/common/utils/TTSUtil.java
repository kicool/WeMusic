package net.kicool.common.utils;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;

import java.util.Locale;

public class TTSUtil {
    private static TTSUtil instance;
    private TextToSpeech tts;
    private boolean isTtsInited = false;

    private TTSUtil(Context context) {
        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.US);
                    if (result != TextToSpeech.LANG_COUNTRY_AVAILABLE
                            && result != TextToSpeech.LANG_AVAILABLE) {
                    } else {
                        isTtsInited = true;
                    }
                }
            }
        });

    }

    public static TTSUtil getInstance(Context context) {
        if (instance == null) {
            synchronized (TTSUtil.class) {
                if (instance == null)
                    instance = new TTSUtil(context);
            }
        }

        return instance;
    }

    public void speak(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }

        if (isTtsInited && null != tts) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH,
                    null);
        }
    }

    public boolean isTtsInited() {
        return isTtsInited;
    }


    public void destroy() {
        tts.shutdown();
        isTtsInited = false;
        instance = null;
    }

    public void stop() {
        tts.stop();
    }
}
