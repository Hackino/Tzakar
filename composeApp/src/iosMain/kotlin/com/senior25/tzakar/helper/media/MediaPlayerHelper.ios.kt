package com.senior25.tzakar.helper.media

import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFAudio.AVAudioPlayer
import platform.AVFAudio.AVAudioPlayerDelegateProtocol
import platform.Foundation.NSBundle
import platform.Foundation.NSURL
import platform.darwin.NSObject

actual class MediaPlayerHelper actual constructor(): NSObject(),    AVAudioPlayerDelegateProtocol {

    private var mediaPlayer: AVAudioPlayer? = null
    private var onCompletionListener: (() -> Unit)? = null

    @OptIn(ExperimentalForeignApi::class)
    actual fun init(name: String){
        val bundle = NSBundle.mainBundle
        val path = bundle.URLForResource(name, "wav")
        if (path != null) {
            val url = path.path?.let { NSURL.fileURLWithPath(it) }
            mediaPlayer = url?.let { AVAudioPlayer(it, error = null) }
            mediaPlayer?.delegate = this@MediaPlayerHelper
            mediaPlayer?.prepareToPlay()
        }
    }

    actual fun play() {
        mediaPlayer?.play()
    }

    actual fun release() {
        mediaPlayer?.stop()
        mediaPlayer = null
    }
    actual fun setOnCompletionListener(listener: () -> Unit) {
        onCompletionListener = listener
    }


    override fun audioPlayerDidFinishPlaying(player: AVAudioPlayer, successfully: Boolean) {
        onCompletionListener?.invoke()
    }
}

