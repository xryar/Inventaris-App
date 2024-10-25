package com.example.inventarisapp.authentication.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.inventarisapp.MainActivity
import com.example.inventarisapp.R
import com.example.inventarisapp.ViewModelFactory
import com.example.inventarisapp.authentication.register.RegisterActivity
import com.example.inventarisapp.data.response.UserModel
import com.example.inventarisapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
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
        binding.tvMoveToRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            val username = binding.NameEditText.text.toString()
            val password = binding.PasswordEditText.text.toString()

            viewModel.setLogin(username, password)

            viewModel.loginResult.observe(this) { event ->
                event.getContentIfNotHandled()?.let { result ->
                    when (result) {
                        is LoginViewModel.LoginResult.Loading -> showLoading(true)
                        is LoginViewModel.LoginResult.Success -> {
                            showLoading(false)
                            val response = result.data
                            if (response.error) {
                                showErrorDialog(response.message)
                            } else {
                                viewModel.saveSession(UserModel(response.user.username, response.user.email, "", response.user.role, true))
                                viewModel.login()
                                saveToken(response.token)
                                showSuccessDialog(response.message)
                            }
                        }
                        is LoginViewModel.LoginResult.Error -> {
                            showLoading(false)
                            showErrorDialog(result.message)
                        }
                    }
                }
            }
        }
    }
    private fun showErrorDialog(message: String) {
        if (!isFinishing && !isDestroyed) {
            AlertDialog.Builder(this@LoginActivity).apply {
                setTitle("Login Gagal")
                setMessage(message)
                setPositiveButton(R.string.positive_button_login) { dialog, _ ->
                    dialog.dismiss()
                }
                create()
                show()
            }
        }
    }

    private fun showSuccessDialog(message: String) {
        if (!isFinishing && !isDestroyed) {
            AlertDialog.Builder(this@LoginActivity).apply {
                setTitle("Login Berhasil")
                setMessage(message)
                setPositiveButton(R.string.positive_button_login) { dialog, _ ->
                    dialog.dismiss()
                    val intent = Intent(context, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                create()
                show()
            }
        }
    }

    private fun saveToken(token: String) {
        val loginSession = TokenSession(this)
        loginSession.saveToken(token)
    }

}