package com.pp.nasaapp

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pp.nasaapp.databinding.ActivityMainBinding
import com.pp.nasaapp.network.ApiInterface
import com.pp.nasaapp.network.Repository
import com.pp.nasaapp.network.ResultResponse
import com.pp.nasaapp.view.FavouriteFragment
import com.pp.nasaapp.view.HomeScreenAdapter
import com.pp.nasaapp.view.PictureCollectionFragment
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var databinding: ActivityMainBinding
    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var viewModel: MainViewModel

    private lateinit var viewPager: ViewPager
    private var homeScreenAdapter: HomeScreenAdapter? = null
    lateinit var navigation: BottomNavigationView
    private var calendar: Calendar = Calendar.getInstance()

    //getting current day,month and year.
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        this.viewPager = databinding.vpScreens
        this.viewPager.offscreenPageLimit = 2
        this.navigation = databinding.navigation
        setClickListeners()

        val retrofitService = ApiInterface.getInstance()
        val mainRepository = Repository(retrofitService)
        viewModel = ViewModelProvider(
            this,
            MyViewModelFactory(mainRepository)
        ).get(MainViewModel::class.java)
        databinding.viewmodel = viewModel
        setupViewPager()

        viewModel.isCalendarDisplayed.observe(this, androidx.lifecycle.Observer { it ->
           if(checkForInternet(this)){
               if (it) selectDate()
           }else{
               Toast.makeText(this,"Please check internet connectivity",Toast.LENGTH_SHORT).show()
           }


        })
    }

    private fun setClickListeners() {
        this.navigation.setOnNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.navigation_Photo -> {
                    viewPager.setCurrentItem(0, false)
                    true
                }
                R.id.navigation_favourite -> {
                    viewPager.setCurrentItem(1, false)
                    true
                }
                else -> false
            }

        }
    }

    private fun selectDate() {
        datePickerDialog = DatePickerDialog(
            this@MainActivity, DatePickerDialog.OnDateSetListener
            { _, year, monthOfYear, dayOfMonth ->
                val date = "" + dayOfMonth + " - " + (monthOfYear + 1) + " - " + year
                databinding.edtDate.setText(date)
                val formattedDate = "$year-${monthOfYear + 1}-${dayOfMonth - 1}"
                viewModel.getPictureOftheDay(formattedDate).observe(this) { it ->
                    when (it) {
                        is ResultResponse.Success -> {
                            val response = it.data

                            response?.url?.let {

                                if (response.media_type == "image") {
                                    val intent = Intent(
                                        this@MainActivity,
                                        PictureOfTheDayActivity::class.java
                                    )
                                    intent.putExtra("image_uri", it)
                                    startActivity(intent)
                                } else if (response.media_type == "video") {
                                    val intent =
                                        Intent(this@MainActivity, ExoPlayerActivity::class.java)
                                    intent.putExtra("video_uri", it)
                                    startActivity(intent)
                                }

                            }

                        }
                        is ResultResponse.Error -> {
                            Toast.makeText(this@MainActivity, it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }, year, month, day
        )
        datePickerDialog.show()
    }
     fun checkForInternet(context: Context): Boolean {

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

    private fun setupViewPager() {

        homeScreenAdapter = HomeScreenAdapter(supportFragmentManager, true)
        homeScreenAdapter?.addFrag(PictureCollectionFragment(viewModel))
        homeScreenAdapter?.addFrag(FavouriteFragment())
        viewPager.adapter = homeScreenAdapter
        viewPager.currentItem = 0

    }

}