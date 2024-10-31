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
import com.example.inventarisapp.data.response.ProductsItem
import com.example.inventarisapp.databinding.FragmentHomeBinding
import com.example.inventarisapp.ui.adapter.HistoryAdapter
import com.example.inventarisapp.ui.detail.DetailActivity
import com.example.inventarisapp.ui.upload.UploadActivity
import com.example.inventarisapp.utils.DateUtils

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

            adapter.setOnItemClickCallback{ data -> selectedData(data)}
        }
    }

    private fun selectedData(data: ProductsItem) {
        val intent = Intent(requireActivity(), DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_DATA, data)
        requireActivity().startActivity(intent)
    }

    private fun setupViewModel(){
        val tokenSession = TokenSession(requireActivity().application)
        val token = tokenSession.passToken().toString()
        Log.d("TokenSession", "Retrieved Token: Bearer $token")
        viewModel.getProducts("Bearer $token")
        showLoading(true)
        viewModel.getAllProducts.observe(viewLifecycleOwner){ allProducts ->
            if (allProducts.size != 0){
                showLoading(false)
                val sortedProducts = allProducts.sortedByDescending { DateUtils.parseDate(it.date)}.take(5)

                binding.rvHistoryHome.visibility = View.VISIBLE
                adapter.getDataProduct(ArrayList(sortedProducts))
            }else{
                binding.rvHistoryHome.visibility = View.GONE
                Toast.makeText(requireActivity().application, "Terjadi Kesalahan, Silahkan Coba Kembali", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onResume() {
        super.onResume()
        val tokenSession = TokenSession(requireActivity().application)
        val token = tokenSession.passToken().toString()
        viewModel.getProducts("Bearer $token")
    }

}