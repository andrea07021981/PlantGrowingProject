package com.example.plantgrowingapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.planntgrowingapp.ui.LoginFragment
import com.example.plantgrowingapp.component.PlantAdapter
import com.example.plantgrowingapp.databinding.FragmentHomeBinding
import com.example.plantgrowingapp.local.dao.PlantDatabaseDao
import com.example.plantgrowingapp.local.database.PlantGrowingDatabase
import com.example.plantgrowingapp.local.domain.DataCollectionDomain
import com.example.plantgrowingapp.local.domain.UserDomain
import com.example.plantgrowingapp.viewmodel.HomeViewModel
import com.github.mikephil.charting.charts.LineChart
import com.wajahatkarim3.roomexplorer.RoomExplorer

class HomeFragment : Fragment() {

    private val TAG: String = HomeFragment::class.java.name
    lateinit var user: UserDomain

    private val homeViewModel: HomeViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProviders
            .of(this, HomeViewModel.Factory(app = activity.application, user = user))
            .get(HomeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        user = HomeFragmentArgs.fromBundle(arguments!!).user
        val binding = FragmentHomeBinding.inflate(inflater)
        binding.homeViewModel = homeViewModel
        binding.lifecycleOwner = this
        binding.plantRecycleview.layoutManager = LinearLayoutManager(activity)
        binding.plantRecycleview.adapter = PlantAdapter(
            PlantAdapter.OnClickListener {
                //TODO OPEN CHART VIEW
            },
            PlantAdapter.OnButtonClickListener {
                //TODO SEND COMMAND FOR WATER
                Log.d(TAG, it.plantName)
            })
        return binding.root
    }
}