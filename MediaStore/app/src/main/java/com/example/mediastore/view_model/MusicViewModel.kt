package com.example.mediastore.view_model

import android.media.MediaPlayer
import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val tag = "MusicViewModel"

class MusicViewModel : ViewModel() {
    private val _currentMusicTime = MutableLiveData(0)

    private val _text = MutableLiveData("111")
    val text: LiveData<String>
        get() = _text
    val currentMusicTime: LiveData<Int>
        get() = _currentMusicTime


    fun updateCurrentMusicTime(time: Int) {
        _currentMusicTime.postValue(time)
    }

    fun updateText(a: String){
        _text.postValue(a)
    }

    fun observeMusic(mediaPlayer: MediaPlayer?) {
        Log.d(tag, "observeMusic: ")
//        viewModelScope.launch {
//            withContext(Dispatchers.IO) {
//                while (true) {
////                    if(mediaPlayer.timestamp)
//                    if (mediaPlayer?.isPlaying == true) {
//                        Log.d(tag, "current music time: ${mediaPlayer.currentPosition}")
//                        _currentMusicTime.postValue((mediaPlayer.currentPosition / 1000))
//                        _text.postValue(currentMusicTime.value.toString())
//                    }
//                    Thread.sleep(1000)
//                }
//            }
//        }

    }

}
