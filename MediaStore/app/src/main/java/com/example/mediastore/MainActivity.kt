package com.example.mediastore

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.mediastore.databinding.ActivityMainBinding
import com.example.mediastore.entity.MediaStoreImage
import com.example.mediastore.ui.MusicActivity
import com.example.mediastore.ui.NotificationActivity
import com.example.mediastore.ui.ServiceActivity
import com.example.mediastore.view_model.ImageViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

private const val READ_EXTERNAL_STORAGE_REQUEST = 0x1045

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: ImageViewModel

    private lateinit var adapter: ImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this)[ImageViewModel::class.java]
        adapter = ImageAdapter {
            deleteImage(it)
        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(applicationContext, 3)
        binding.lifecycleOwner = this
        binding.button.setOnClickListener {
            openMediaStore()
        }
        binding.buttonToService.setOnClickListener {
            startActivity(Intent(this, ServiceActivity::class.java))
        }
        binding.buttonToMusic.setOnClickListener{
            startActivity(Intent(this, MusicActivity::class.java))
        }

        binding.buttonToNotification.setOnClickListener{
            startActivity(Intent(this, NotificationActivity::class.java))
        }


        viewModel.images.observe(this) {
            Log.d("TAG", "onCreate: updateAdapter")
            adapter.submitList(it)
        }
        viewModel.netImage.observe(this) {
            Glide.with(binding.root)
                .load(it[2].preview)
                .centerCrop()
                .into(binding.netImageView)
        }
        viewModel.loadImage()

        val result =
            registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
                if (it.resultCode == RESULT_OK) {
                    viewModel.deletePendingImage()
                }
            }

        viewModel.permissionNeededForDelete.observe(this) { intentSender ->
            intentSender?.let {
                Log.d("TAG", "onCreate: print $it")
                val request = IntentSenderRequest.Builder(it).build()
                result.launch(request)

            }
        }


    }


    private fun openMediaStore() {
        if (haveStoragePermission()) {
            showImages()
        } else {
            requestPermission()
        }
    }

    private fun showImages() {
        viewModel.loadImages()
    }

    private fun deleteImage(image: MediaStoreImage) {
        MaterialAlertDialogBuilder(this)
            .setTitle("删除图片")
            .setMessage(image.displayName)
            .setPositiveButton("确认") { _: DialogInterface, _: Int ->
                viewModel.deleteImage(image)
            }
            .setNegativeButton("取消") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
            .show()
    }

    private fun haveStoragePermission() =
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

    private fun requestPermission() {
        if (!haveStoragePermission()) {
            val permissions = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            ActivityCompat.requestPermissions(this, permissions, READ_EXTERNAL_STORAGE_REQUEST)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            READ_EXTERNAL_STORAGE_REQUEST -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showImages()
                } else {
                    // If we weren't granted the permission, check to see if we should show
                    // rationale for the permission.
                    val showRationale =
                        ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                    if (showRationale) {
//                        showNoAccess()
                    } else {
                        goToSettings()
                    }
                }
                return
            }
        }
    }

    private fun goToSettings() {
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:$packageName")
        ).apply {
            addCategory(Intent.CATEGORY_DEFAULT)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }.also { intent ->
            startActivity(intent)
        }
    }

}

class ImageAdapter(private val onClick: (MediaStoreImage) -> Unit) :
    ListAdapter<MediaStoreImage, ImageViewHolder>(MediaStoreImage.diffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.gallery_layout, parent, false)
        return ImageViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val mediaStoreImage = getItem(position)
        holder.itemView.tag = mediaStoreImage
        Glide.with(holder.itemView)
            .load(mediaStoreImage.contentUri)
            .centerCrop()
            .into(holder.itemView.findViewById(R.id.image))

    }


}

class ImageViewHolder(view: View, onClick: (MediaStoreImage) -> Unit) : ViewHolder(view) {
    private val imageView: ImageView

    init {
        imageView = view.findViewById(R.id.image)
        imageView.setOnClickListener {
            val image = view.tag as? MediaStoreImage ?: return@setOnClickListener
            onClick(image)

        }
    }
}

//class ImageAdapter(private val data: List<String>) : RecyclerView.Adapter<ImageViewHolder>() {
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
//        val view =
//            LayoutInflater.from(parent.context).inflate(R.layout.text_row_item, parent, false)
//        return ImageViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
//        holder.textView.text = data[position]
//    }
//
//    override fun getItemCount(): Int {
//        return data.size
//    }
//
//}
//
//class ImageViewHolder(view: View) : ViewHolder(view) {
//    val textView: TextView
//
//    init {
//        textView = view.findViewById(R.id.textView)
//    }
//}