package com.sgr.glucomaster

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CurrentRegimenActivity : AppCompatActivity() {

    val list1: MutableList<String> = mutableListOf("0 - 60mg/l   Dosis: 1UD",
        "60 - 100mg/l   Dosis: 2UD",
        "100 - 999mg/l   Dosis: 3UD")
    lateinit var mRecyclerView: RecyclerView
    val mAdapter : MAdapter = MAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_current_regimen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView(R.id.tMRecycler, list1)
        setupRecyclerView(R.id.tMRecyclerT, list1)
        setupRecyclerView(R.id.tMRecyclerC, list1)
        setupRecyclerView(R.id.tMRecyclerR, list1)
    }

    fun setupRecyclerView(Recycler: Int, list: MutableList<String>) {

        mRecyclerView = findViewById(Recycler) as RecyclerView
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter.MAdapter(list, this)
        mRecyclerView.adapter = mAdapter

    }
}