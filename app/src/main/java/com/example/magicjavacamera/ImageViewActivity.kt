package com.example.magicjavacamera

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class ImageViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_view)

        val image = intent.getParcelableExtra<Bitmap>("image")
        val imageView = findViewById<ImageView>(R.id.imageView)
        imageView.setImageBitmap(image)
    }
}