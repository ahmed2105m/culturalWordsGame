package com.barmej.culturalwordsgame

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import android.content.ContentResolver

class ShearActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shear)

        //casting
        val shareImage = findViewById<ImageView>(R.id.share_imageView)
        val shareBtn = findViewById<TextView>(R.id.share_button)
        val shareTitle = findViewById<TextInputLayout>(R.id.share_title)

        //get Image ID From Intent & View Image in ImageView
        val mImageId = intent.getIntExtra("SHARE_IMAGE",0)
        shareImage.setImageResource(mImageId)

        shareBtn.setOnClickListener {

            val titleFromSharing = shareTitle.editText!!.text.toString()

            //build Image Uri
            val imageUri = Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(resources.getResourcePackageName(mImageId))
                .appendPath(resources.getResourceTypeName(mImageId))
                .appendPath(resources.getResourceEntryName(mImageId))
                .build()

            //share Image Intent
            val shareIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, titleFromSharing)
                putExtra(Intent.EXTRA_STREAM, imageUri)
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                type = "image/*"
            }
            startActivity(Intent.createChooser(shareIntent, resources.getText(R.string.send_to)))
        }
    }
}