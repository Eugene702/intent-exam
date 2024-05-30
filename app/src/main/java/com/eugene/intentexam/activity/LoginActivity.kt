package com.eugene.intentexam.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.eugene.intentexam.R
import com.eugene.intentexam.databinding.ActivityLoginBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        userPreferences = getSharedPreferences(resources.getString(R.string.preferences_user), Context.MODE_PRIVATE)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.submitBtn.setOnClickListener(onSubmit())
    }

    private fun onSubmit(): View.OnClickListener = View.OnClickListener {
        with(binding) {
            usernameInputLayout.error = null
            passwordInputLayout.error = null

            if (usernameInput.text.isNullOrEmpty()) {
                usernameInputLayout.error = getString(R.string.nama_pengguna_tidak_boleh_kosong)
            }

            if (passwordInput.text.isNullOrEmpty()) {
                passwordInputLayout.error = getString(R.string.kata_sandi_tidak_boleh_kosong)
            }

            if(usernameInput.text.toString().isNotEmpty() && passwordInput.text.toString().isNotEmpty()){
                if (usernameInput.text.toString() == getString(R.string.user_username) && passwordInput.text.toString() == getString(R.string.user_password)) {
                    userPreferences.edit {
                        putString(getString(R.string.preferences_user_username), usernameInput.text.toString())
                        apply()
                    }
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }else{
                    MaterialAlertDialogBuilder(this@LoginActivity)
                        .setTitle(getString(R.string.kesalahan))
                        .setMessage(getString(R.string.nama_pengguna_atau_kata_sandi_salah))
                        .setPositiveButton(getString(R.string.oke)) { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
            }
        }
    }
}