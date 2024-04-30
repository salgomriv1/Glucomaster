package com.sgr.glucomaster

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sgr.glucomaster.databinding.ActivityCurrentRegimenBinding
import com.sgr.glucomaster.databinding.ActivityTipsBinding

class TipsActivity : AppCompatActivity() {

    lateinit var mRecyclerView: RecyclerView
    val mAdapter : MAdapter = MAdapter()
    private lateinit var binding: ActivityTipsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTipsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tips: MutableList<String> = mutableListOf(getString(R.string.consejo1),
            getString(R.string.consejo1),
            getString(R.string.consejo1))

        setupRecyclerView(R.id.rvTips, tips)

    }

    fun setupRecyclerView(Recycler: Int, list: MutableList<String>) {

        mRecyclerView = findViewById(Recycler) as RecyclerView
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter.MAdapter(list, this)
        mRecyclerView.adapter = mAdapter

    }
}