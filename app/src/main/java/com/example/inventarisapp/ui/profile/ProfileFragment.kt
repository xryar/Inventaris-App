package com.example.inventarisapp.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.inventarisapp.R
import com.example.inventarisapp.ViewModelFactory
import com.example.inventarisapp.authentication.login.TokenSession
import com.example.inventarisapp.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        setupViewModel()
        setupAction()

        Glide.with(this)
            .load(R.drawable.defaultprofile)
            .into(binding.ivPhoto)

        return binding.root
    }

    private fun setupViewModel(){
        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            binding.tvUsername.text = user.username
            binding.tvEmail.text = user.email
            binding.tvRole.text = user.role
        }
    }

    private fun setupAction() {
        binding.btnLogout.setOnClickListener {
            val session = TokenSession(requireContext())
            session.logoutSession()
            viewModel.logout()
        }
    }

}