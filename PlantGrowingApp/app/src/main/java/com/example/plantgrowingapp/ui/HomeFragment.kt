package com.example.plantgrowingapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.plantgrowingapp.databinding.FragmentHomeBinding
import com.example.plantgrowingapp.viewmodel.HomeViewModel
import com.github.mikephil.charting.charts.LineChart

class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProviders.of(this).get(HomeViewModel::class.java)
    }

    private val TAG: String = HomeFragment::class.java.name
    private lateinit var chart: LineChart

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater)
        binding.homeViewModel = homeViewModel
        binding.lifecycleOwner = this

        homeViewModel.dataCollection.observe(this.viewLifecycleOwner, Observer {
            if (it != null) {

            }
        })
        return binding.root
    }
}