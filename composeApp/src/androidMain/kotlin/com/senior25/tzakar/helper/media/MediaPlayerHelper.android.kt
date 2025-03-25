package com.senior25.tzakar.helper.media

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import com.senior25.tzakar.helper.ApplicationProvider

actual class MediaPlayerHelper actual constructor() {

    private var mediaPlayer:MediaPlayer? = null

    private var onCompletionListener: (() -> Unit)? = null

    actual fun init(name: String){
         mediaPlayer = MediaPlayer().apply {
            try {
                val uri = Uri.parse("android.resource://${ApplicationProvider.application.packageName}/raw/${name.replace(".wav", "")}")
                setDataSource(ApplicationProvider.application, uri)
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build()
                )
                setOnCompletionListener {
                        onCompletionListener?.invoke()
                }
                prepare()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    actual fun play() {
        mediaPlayer?.start()
    }

    actual fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
    actual fun setOnCompletionListener(listener: () -> Unit) {
        onCompletionListener = listener
    }
}
