package com.example.inventarisapp.authentication.register

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.inventarisapp.R
import com.example.inventarisapp.ViewModelFactory
import com.example.inventarisapp.authentication.login.LoginActivity
import com.example.inventarisapp.data.response.UserModel
import com.example.inventarisapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
    }

    private fun showLoading(isLoading: Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction(){
        binding.btnRegister.setOnClickListener {
            val username = binding.NameEditText.text.toString()
            val email = binding.EmailEditText.text.toString()
            val password = binding.PasswordEditText.text.toString()
            val confPassword = binding.ConfPasswordEditText.text.toString()

            viewModel.saveSession(UserModel(username, email, password, false))

            viewModel.setRegister(username, email, password, confPassword)
            showLoading(true)

            viewModel.registerResult.observe(this) { result ->
                when (result) {
                    is RegisterViewModel.RegisterResult.Loading -> showLoading(true)
                    is RegisterViewModel.RegisterResult.Success -> {
                        showLoading(false)
                        val response = result.data
                        if (response.error) {
                            showErrorDialog(response.message)
                        } else {
                            showSuccessDialog(response.message)
                        }
                    }
                    is RegisterViewModel.RegisterResult.Error -> {
                        showLoading(false)
                        showErrorDialog(result.message)
                    }
                }
            }
        }
    }

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(this@RegisterActivity).apply {
            setTitle("Registrasi Gagal")
            setMessage(message)
            setPositiveButton(R.string.positive_button_failed) { _, _ ->
                clearInputFields()
            }
            create()
            show()
        }
    }

    private fun showSuccessDialog(message: String) {
        AlertDialog.Builder(this@RegisterActivity).apply {
            setTitle("Registrasi Berhasil")
            setMessage(message)
            setPositiveButton(R.string.positive_button_register) { _, _ ->
                val intent = Intent(context, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            create()
            show()
        }
    }

    private fun clearInputFields() {
        binding.NameEditText.text?.clear()
        binding.EmailEditText.text?.clear()
        binding.PasswordEditText.text?.clear()
        binding.ConfPasswordEditText.text?.clear()
    }
}