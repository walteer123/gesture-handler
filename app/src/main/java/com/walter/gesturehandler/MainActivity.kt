package com.walter.gesturehandler

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.walter.gesturehandler.databinding.ActivityMainBinding
import com.walter.gesturehandler.list.CustomAdapter
import com.walter.gesturehandler.list.DataFixtures.dummyItems
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val customAdapter = CustomAdapter(dummyItems)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.testeRefreshView.setOnRefreshListener(object: PullToRefreshView.OnRefreshListener {
            override fun onRefresh() {
                lifecycleScope.launch {
                    delay(5000)
                    Toast.makeText(this@MainActivity, "Loading", Toast.LENGTH_LONG).show()
                }
            }

        })

        binding?.recylerMain?.apply {
            adapter = customAdapter
        }

    }

}