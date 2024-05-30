package com.eugene.intentexam.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.eugene.intentexam.R
import com.eugene.intentexam.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        userPreferences = getSharedPreferences(resources.getString(R.string.preferences_user), Context.MODE_PRIVATE)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar.apply {
            title = resources.getString(R.string.app_name)
        }

        if(userPreferences.getString(resources.getString(R.string.preferences_user_username), null) == null){
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        loadImage()
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.profile -> startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
            R.id.logout -> {
                MaterialAlertDialogBuilder(this@MainActivity)
                    .setTitle(getString(R.string.keluar))
                    .setMessage(getString(R.string.apakah_kamu_yakin_ingin_keluar))
                    .setPositiveButton(getString(R.string.oke)) { _, _ ->
                        userPreferences.edit {
                            remove(resources.getString(R.string.preferences_user_username))
                            apply()
                        }

                        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                        finish()
                    }
                    .setNegativeButton(getString(R.string.batalkan)){dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadImage(){
        try{
            val profilePictureURL = "https://eugenefeilianputrarangga.vercel.app/_next/image?url=%2Fimg%2Fprofile.jpg&w=640&q=75"
            Glide.with(this@MainActivity)
                .load(profilePictureURL)
                .centerCrop()
                .circleCrop()
                .addListener(object: RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        Toast.makeText(this@MainActivity, resources.getString(R.string.gagal_mendapatkan_foto), Toast.LENGTH_SHORT).show()
                        return true
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        resource.let {
                            lifecycleScope.launch {
                                binding.toolbar.menu.findItem(R.id.profile).icon = resource
                            }
                        }

                        return true
                    }
                }).submit()
        }catch(e: Exception){
            Toast.makeText(this@MainActivity, resources.getString(R.string.gagal_mendapatkan_foto), Toast.LENGTH_SHORT).show()
        }
    }
}