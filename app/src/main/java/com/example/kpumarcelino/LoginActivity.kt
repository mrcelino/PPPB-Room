package com.example.kpumarcelino

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kpumarcelino.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var prefManager: PrefManager

    companion object {
        const val TOAST_FILL_ALL_FIELDS = "Mohon isi semua data"
        const val TOAST_INVALID_CREDENTIALS = "Username atau password salah"
        const val TOAST_LOGIN_SUCCESS = "Login berhasil"
        const val TOAST_LOGIN_FAILED = "Login gagal"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PrefManager.getInstance(this)

        with(binding) {
            btnLogin.setOnClickListener {
                handleLogin()
            }

            txtRegister.setOnClickListener {
                navigateToRegister()
            }
        }
    }

    private fun handleLogin() {
        val username = binding.etUserLogin.text.toString()
        val password = binding.etPassLogin.text.toString()

        if (!isInputValid(username, password)) {
            showToast(TOAST_FILL_ALL_FIELDS)
            return
        }

        if (isValidUsernamePassword()) {
            prefManager.setLoggedIn(true)
            checkLoginStatus()
        } else {
            showToast(TOAST_INVALID_CREDENTIALS)
        }
    }

    private fun isInputValid(username: String, password: String): Boolean {
        return username.isNotEmpty() && password.isNotEmpty()
    }

    private fun isValidUsernamePassword(): Boolean {
        val username = prefManager.getUsername()
        val password = prefManager.getPassword()
        val inputUsername = binding.etUserLogin.text.toString()
        val inputPassword = binding.etPassLogin.text.toString()
        return username == inputUsername && password == inputPassword
    }

    private fun checkLoginStatus() {
        if (prefManager.isLoggedIn()) {
            showToast(TOAST_LOGIN_SUCCESS)
            navigateToMain()
        } else {
            showToast(TOAST_LOGIN_FAILED)
        }
    }

    private fun navigateToRegister() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish() // Tutup LoginActivity setelah berhasil login
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
