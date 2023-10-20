package com.example.mediastore

import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentUris
import android.database.ContentObserver
import android.provider.MediaStore
import android.provider.MediaStore.Video.Media
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

private const val TAG = "MainActivityVM"

class ImageViewModel(application: Application) : AndroidViewModel(application) {


    private val _images = MutableLiveData<List<MediaStoreImage>>()
    val images: LiveData<List<MediaStoreImage>> get() = _images

    private var contentOverride: ContentObserver? = null

    fun loadImages() {
        viewModelScope.launch {
            val imagesList = queryImages()
            _images.postValue(imagesList)
        }
    }

    private suspend fun queryImages(): List<MediaStoreImage> {
        val images = mutableListOf<MediaStoreImage>()
        withContext(Dispatchers.IO) {
            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED
            )
            val selection = "${MediaStore.Images.Media.DATE_ADDED} >= ?"

            val selectionArgs = arrayOf(
                dateToTimestamp(day = 22, month = 10, year = 2008).toString()
            )
            val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

            getApplication<Application>().contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                sortOrder
            )?.apply {
                val idColumn = this.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val dateModifiedColumn =
                    this.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)
                val displayNameColumn =
                    this.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                while (this.moveToNext()) {
                    val id = this.getLong(idColumn)
                    val dateModified =
                        Date(TimeUnit.SECONDS.toMillis(this.getLong(dateModifiedColumn)))
                    val displayName = this.getString(displayNameColumn)
                    val contentUri =
                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                    val image = MediaStoreImage(id, displayName, dateModified, contentUri)
                    images += image
                    Log.v(TAG, "Added image: $image")
                }
            }
        }
        Log.i(TAG, "Found ${images.size} images")
        return images
    }

    @SuppressLint("SimpleDateFormat")
    private fun dateToTimestamp(day: Int, month: Int, year: Int): Long =
        SimpleDateFormat("dd.MM.yyyy").let { formatter ->
            TimeUnit.MICROSECONDS.toSeconds(formatter.parse("$day.$month.$year")?.time ?: 0)
        }
}