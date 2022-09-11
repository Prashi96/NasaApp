package com.pp.nasaapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.pp.nasaapp.MainActivity
import com.pp.nasaapp.MainViewModel
import com.pp.nasaapp.R
import com.pp.nasaapp.network.ResultResponse

class PictureCollectionFragment(val viewModel: MainViewModel) : Fragment() {
    private lateinit var progressBar: ProgressBar
    private lateinit var  androidListView: GridView
    private var isDataloaded:Boolean=false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_picture_collection, container, false)
        progressBar = root.findViewById(R.id.progressBar) as ProgressBar
        androidListView=root.findViewById(R.id.grid_cards) as GridView
        loadData()
        return root
    }

    private fun loadData() {
        if ((activity as MainActivity).checkForInternet(activity as MainActivity)) {
            var images = mutableListOf<String>()
            viewModel.getPictures().observe(viewLifecycleOwner, Observer {
                progressBar.visibility = View.VISIBLE
                when (it) {
                    is ResultResponse.Success -> {
                        val resultList = it.data
                        if (resultList?.isEmpty() == false)
                            for (image in resultList) {
                                images.add(image.url)
                            }
                        val simpleAdapter =
                            this.context?.let { ImageAdapter(it, images) }

                        androidListView.adapter = simpleAdapter
                        progressBar.visibility = View.GONE
                        isDataloaded=true
                    }
                    is ResultResponse.Error -> {
                        progressBar.visibility = View.GONE
                        Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
                    }
                }

            })
        } else {
            progressBar.visibility = View.GONE
            Toast.makeText(activity, "Please check internet connectivity", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onResume() {
        super.onResume()
        if(!isDataloaded)
            loadData()
    }
}