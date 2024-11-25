package com.example.kpumarcelino

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kpumarcelino.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PrefManager.getInstance(this)

        with(binding) {
            btnRegis.setOnClickListener {
                handleRegister()
            }

            txtLogin.setOnClickListener {
                navigateToLogin()
            }
        }
    }

    private fun handleRegister() {
        val username = binding.etUserRegis.text.toString().trim()
        val password = binding.etPassRegis.text.toString().trim()
        val confirmPassword = binding.etCpassRegis.text.toString().trim()

        when {
            username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() -> {
                showToast("Mohon isi semua data")
            }
            password != confirmPassword -> {
                showToast("Password tidak sama")
            }
            else -> {
                prefManager.saveUsername(username)
                prefManager.savePassword(password)
                prefManager.setLoggedIn(true)
                showToast("Registrasi berhasil")
                navigateToMain()
            }
        }
    }

    private fun navigateToLogin() {
        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
    }

    private fun navigateToMain() {
        startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
