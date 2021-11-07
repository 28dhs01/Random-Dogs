package com.dhruv.randomdogs

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.dhruv.randomdogs.databinding.ActivityMainBinding
import com.dhruv.randomdogs.model.DogDataClass
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getData()
        binding.btnNext.setOnClickListener {
            getData()
        }

        binding.btnShare.setOnClickListener {
            requestPermission()
            val bitmapDrawable = binding.ivDog.drawable as BitmapDrawable
            val bitmap = bitmapDrawable.bitmap
            val bitmapPath = MediaStore.Images.Media.insertImage(contentResolver,bitmap,"bitmap img","dog img")
            val bitmapUri = Uri.parse(bitmapPath)

            val action = Intent.ACTION_SEND
            val intent = Intent(action)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_STREAM,bitmapUri)
            val chooser = Intent.createChooser(intent,"share via")
            startActivity(chooser)
        }
    }

    private fun getData() {
        binding.progressBar.isVisible = true
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://dog.ceo/")
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getData()


        retrofitData.enqueue(object : Callback<DogDataClass?> {
            override fun onResponse(call: Call<DogDataClass?>, response: Response<DogDataClass?>) {
                val responseBody = response.body()
                binding.progressBar.isVisible = false

                Glide.with(baseContext).load(responseBody?.message).into(binding.ivDog)
            }

            override fun onFailure(call: Call<DogDataClass?>, t: Throwable) {
                binding.progressBar.isVisible = false
                Toast.makeText(baseContext,t.message,Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun hasExternalStoragePermission() =
        ActivityCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    private fun requestPermission() {
        var permissionsToRequest = mutableListOf<String>()
        if(!hasExternalStoragePermission()){
            permissionsToRequest.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if(permissionsToRequest.isNotEmpty()){
            ActivityCompat.requestPermissions(this,permissionsToRequest.toTypedArray(),0)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 0 && grantResults.isNotEmpty() ){
            for(i in grantResults.indices ){
                if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
                    Log.d("permission request","${permissions[i]} was granted")
                }
            }

        }
    }

}