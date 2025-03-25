    package com.senior25.tzakar.helper.media

    expect class MediaPlayerHelper() {
        fun init(name: String)
        fun play()
        fun release()
        fun setOnCompletionListener(listener: () -> Unit)
    }