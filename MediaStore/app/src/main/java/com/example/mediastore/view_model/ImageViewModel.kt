package com.example.mediastore.view_model

import android.annotation.SuppressLint
import android.app.Application
import android.app.RecoverableSecurityException
import android.content.ContentResolver
import android.content.ContentUris
import android.content.IntentSender
import android.database.ContentObserver
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mediastore.entity.MediaStoreImage
import com.example.mediastore.entity.Vertical
import com.example.mediastore.net.MainService
import com.example.mediastore.net.NetConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

private const val TAG = "MainActivityVM"

class ImageViewModel(application: Application) : AndroidViewModel(application) {


    private val _images = MutableLiveData<List<MediaStoreImage>>()
    private val _netImages = MutableLiveData<List<Vertical>>()
    val images: LiveData<List<MediaStoreImage>> get() = _images
    val netImage: LiveData<List<Vertical>> get() = _netImages

    private var pendingDeleteImage: MediaStoreImage? = null
    private val _permissionNeededForDelete = MutableLiveData<IntentSender?>()
    val permissionNeededForDelete get() = _permissionNeededForDelete
    private var contentObserver: ContentObserver? = null


    fun loadImages() {
        viewModelScope.launch {
            val imagesList = queryImages()
            _images.postValue(imagesList)
            if (contentObserver == null) {
                contentObserver = getApplication<Application>().contentResolver.registerObserver(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                ) {
                    loadImages()
                }
            }
        }
    }

    fun deleteImage(image: MediaStoreImage) {
        Log.d(TAG, "deleteImage: ")
        viewModelScope.launch {
            performDeleteImage(image)
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

    private suspend fun performDeleteImage(image: MediaStoreImage) {
        withContext(Dispatchers.IO) {
            try {
                /**
                 * In [Build.VERSION_CODES.Q] and above, it isn't possible to modify
                 * or delete items in MediaStore directly, and explicit permission
                 * must usually be obtained to do this.
                 *
                 * The way it works is the OS will throw a [RecoverableSecurityException],
                 * which we can catch here. Inside there's an [IntentSender] which the
                 * activity can use to prompt the user to grant permission to the item
                 * so it can be either updated or deleted.
                 */
                getApplication<Application>().contentResolver.delete(
                    image.contentUri,
                    "${MediaStore.Images.Media._ID} = ?",
                    arrayOf(image.id.toString())
                )
            } catch (securityException: SecurityException) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val recoverableSecurityException =
                        securityException as? RecoverableSecurityException
                            ?: throw securityException

                    // Signal to the Activity that it needs to request permission and
                    // try the delete again if it succeeds.
                    pendingDeleteImage = image
                    _permissionNeededForDelete.postValue(
                        recoverableSecurityException.userAction.actionIntent.intentSender
                    )
                } else {
                    throw securityException
                }
            }
        }


    }

    @SuppressLint("SimpleDateFormat")
    private fun dateToTimestamp(day: Int, month: Int, year: Int): Long =
        SimpleDateFormat("dd.MM.yyyy").let { formatter ->
            TimeUnit.MICROSECONDS.toSeconds(formatter.parse("$day.$month.$year")?.time ?: 0)
        }

    override fun onCleared() {
        super.onCleared()
        contentObserver?.let {
            getApplication<Application>().contentResolver.unregisterContentObserver(it)
        }
    }

    fun deletePendingImage() {
        pendingDeleteImage?.let {
            deleteImage(it)
            pendingDeleteImage = null
        }
    }

    fun loadImage() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val response =
                    NetConfig.getService(MainService::class.java).getImage().awaitResponse().body()
                if (response != null) {
                    _netImages.postValue(response.res.vertical)
                }

            }
        }
    }
}

private fun ContentResolver.registerObserver(
    uri: Uri,
    observer: (selfChange: Boolean) -> Unit
): ContentObserver {
    val contentObserver = object : ContentObserver(Looper.myLooper()?.let { Handler(it) }) {
        override fun onChange(selfChange: Boolean) {
            observer(selfChange)
        }
    }
    registerContentObserver(uri, true, contentObserver)

    return contentObserver

}
