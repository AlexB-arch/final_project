package com.example.afinal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.afinal.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    // View Binding Instances
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter: ReceiptListAdapter
    private lateinit var fabClickListener: OnFabClickListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        with(binding.recyclerView) {
           setHasFixedSize(true)
            val divider = DividerItemDecoration(context, LinearLayoutManager(context).orientation)
            addItemDecoration(divider)
        }

        viewModel.receiptList.observe(viewLifecycleOwner, Observer { receipts ->
            Log.i("MainFragment", "Receipts: $receipts")
            adapter = ReceiptListAdapter(receipts)
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        })

        binding.fabNewScan.setOnClickListener {
            startActivity(Intent(activity, ReceiptScanner::class.java))
        }
        return binding.root
    }

    // Create an interface to use the fabNewScan button in the MainActivity
    interface OnFabClickListener {
        fun onFabClick()
    }
}