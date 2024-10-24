package com.example.inventarisapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inventarisapp.ViewModelFactory
import com.example.inventarisapp.authentication.login.TokenSession
import com.example.inventarisapp.databinding.FragmentHomeBinding
import com.example.inventarisapp.ui.adapter.HistoryAdapter
import com.example.inventarisapp.ui.upload.UploadActivity

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels <HomeViewModel>{
        ViewModelFactory.getInstance(requireActivity().application)
    }
    private val adapter = HistoryAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        binding.btnAddProduct.setOnClickListener {
            val intent = Intent(requireContext(), UploadActivity::class.java)
            requireActivity().startActivity(intent)
        }

        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (user.isLogin){
                binding.tvUsername.text = user.username
            }
        }

        setupRecyclerView()
        setupViewModel()

        return binding.root
    }

    private fun setupRecyclerView(){
        binding.apply {
            rvHistoryHome.layoutManager = LinearLayoutManager(activity)
            rvHistoryHome.setHasFixedSize(true)
            rvHistoryHome.adapter = adapter
        }
    }

    private fun setupViewModel(){
        val tokenSession = TokenSession(requireActivity().application)
        val token = tokenSession.passToken().toString()
        Log.d("TokenSession", "Retrieved Token: Bearer $token")
        viewModel.getProducts("Bearer $token")
        //disini loading(true)
        viewModel.getAllProducts.observe(viewLifecycleOwner){ allProducts ->
            if (allProducts.size != 0){
                binding.rvHistoryHome.visibility = View.VISIBLE
                adapter.getDataProduct(allProducts)
                //disini loading (false)
            }else{
                binding.rvHistoryHome.visibility = View.GONE
                Toast.makeText(requireActivity().application, "Ada error mas", Toast.LENGTH_SHORT).show()
            }
        }
    }

}