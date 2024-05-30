package com.eugene.intentexam.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.eugene.intentexam.R
import com.eugene.intentexam.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = resources.getString(R.string.profile)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(ContextCompat.getDrawable(this@ProfileActivity, R.drawable.ic_arrow_back))
        }

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        loadImage()

        binding.githubImageview.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(getString(R.string.https_github_com_Eugene702))
            startActivity(intent)
        }

        binding.portfolioTextview.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(getString(R.string.https_eugenefeilianputrarangga_vercel_app))
            startActivity(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    private fun loadImage(){
        val profilePictureURL = "https://eugenefeilianputrarangga.vercel.app/_next/image?url=%2Fimg%2Fprofile.jpg&w=640&q=75"
        Glide.with(this@ProfileActivity)
            .load(profilePictureURL)
            .into(binding.avatar)
    }
}