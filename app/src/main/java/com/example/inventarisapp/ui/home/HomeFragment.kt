package com.example.inventarisapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inventarisapp.ViewModelFactory
import com.example.inventarisapp.authentication.login.TokenSession
import com.example.inventarisapp.ui.customdialog.CustomDialogFragment
import com.example.inventarisapp.databinding.FragmentHomeBinding
import com.example.inventarisapp.ui.adapter.HistoryAdapter

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
            showDialog(" ", true)
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

    private fun showDialog(category: String, isCategoryEditable: Boolean) {
        val dialog = CustomDialogFragment.newInstance(category, isCategoryEditable)
        dialog.show(requireActivity().supportFragmentManager, "CustomDialog")
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

    override fun onResume() {
        super.onResume()
        val tokenSession = TokenSession(requireActivity().application)
        val token = tokenSession.passToken().toString()
        viewModel.getProducts("Bearer $token")
    }
}