package com.example.inventarisapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.inventarisapp.ui.customdialog.CustomDialogFragment
import com.example.inventarisapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        binding.btnAddProduct.setOnClickListener {
            showDialog(" ", true)
        }


        return binding.root
    }

    private fun showDialog(category: String, isCategoryEditable: Boolean) {
        val dialog = CustomDialogFragment.newInstance(category, isCategoryEditable)
        dialog.show(requireActivity().supportFragmentManager, "CustomDialog")
    }

}