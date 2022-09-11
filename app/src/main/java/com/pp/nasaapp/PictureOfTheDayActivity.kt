package com.pp.nasaapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pp.nasaapp.databinding.ActivityMainBinding
import com.pp.nasaapp.databinding.ActivityPictureOfTheDayBinding
import com.pp.nasaapp.network.ApiInterface
import com.pp.nasaapp.network.Repository
import com.pp.nasaapp.network.ResultResponse

class PictureOfTheDayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPictureOfTheDayBinding
    private lateinit var imageView: ImageView
    lateinit var imageUrl: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_picture_of_the_day)
        this.imageView = binding.imageView
        imageUrl = intent.getStringExtra("image_uri").toString()
        loadImage(imageUrl, imageView)
    }

    fun loadImage(imageUrl: String, imageView: ImageView) {
        val options: RequestOptions = RequestOptions()
            .centerCrop()
            .placeholder(R.mipmap.ic_launcher_round)
            .error(R.mipmap.ic_launcher_round)
        Glide.with(this)
            .load(imageUrl).apply(options)
            .into(imageView)
    }
}