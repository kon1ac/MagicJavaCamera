package com.example.magicjavacamera

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private val REQUEST_CODE_CAMERA = 123
    private val REQUEST_CODE_PHOTO = 456
    private val imageList = mutableListOf<Bitmap>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageAdapter: ImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Проверяем разрешения на использование камеры и хранение файлов
        checkPermissions()

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        imageAdapter = ImageAdapter(imageList) { position -> onImageClick(position) }
        recyclerView.adapter = imageAdapter

        val authorTextView = findViewById<TextView>(R.id.authorTextView)
        authorTextView.text = "Егоров И.В."
    }

    fun onCameraClick(view: View) {
        // Открываем камеру для съемки фото
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_CODE_CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_CAMERA -> {
                    // Получаем снятое фото и сохраняем его
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    saveImage(imageBitmap)
                    imageList.add(imageBitmap)
                    imageAdapter.notifyDataSetChanged()
                }
                REQUEST_CODE_PHOTO -> {
                    // Обработка выбранного из галереи фото
                }
            }
        }
    }

    private fun onImageClick(position: Int) {
        // Открываем новое активити для просмотра увеличенного изображения
        val intent = Intent(this, ImageViewActivity::class.java)
        intent.putExtra("image", imageList[position])
        startActivity(intent)
    }

    private fun saveImage(bitmap: Bitmap) {
        // Сохраняем фото в кеше приложения
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "JPEG_$timeStamp.jpg"
        val file = File(cacheDir, fileName)
        try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun checkPermissions() {
// Проверяем разрешения на использование камеры и хранение файлов

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_PHOTO)
        }
    }
}