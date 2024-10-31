package com.example.inventarisapp.ui.history

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
import com.example.inventarisapp.databinding.FragmentHistoryBinding
import com.example.inventarisapp.ui.adapter.HistoryAdapter
import com.example.inventarisapp.ui.adapter.HistoryFullAdapter
import com.example.inventarisapp.ui.detail.DetailActivity
import com.example.inventarisapp.utils.DateUtils

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private val viewModel by viewModels<HistoryViewModel> {
        ViewModelFactory.getInstance(requireActivity().application)
    }
    private val adapter = HistoryFullAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)

        setupRecyclerView()
        setupViewModel()

        return binding.root
    }

    private fun setupRecyclerView(){
        binding.apply {
            rvHistory.layoutManager = LinearLayoutManager(activity)
            rvHistory.setHasFixedSize(true)
            rvHistory.adapter = adapter

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
                val sortedProducts = allProducts.sortedByDescending { DateUtils.parseDate(it.date)}

                binding.rvHistory.visibility = View.VISIBLE
                adapter.getDataProduct(ArrayList(sortedProducts))
            }else{
                binding.rvHistory.visibility = View.GONE
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