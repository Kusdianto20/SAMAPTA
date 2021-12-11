package com.bangkitUI.samapta.main.tentangActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkitUI.samapta.MainActivity
import com.bangkitUI.samapta.R
import com.bangkitUI.samapta.databinding.ActivityTentangBinding
import com.bangkitUI.samapta.main.bantuanActivity.BantuanActivity
import com.bangkitUI.samapta.main.profil.ProfilActivity
import com.bangkitUI.samapta.report.UserReportActivity

class TentangActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTentangBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTentangBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        init()

        supportActionBar?.elevation = 0F
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "TENTANG"
    }

    private fun init() {
        binding.btmNavigationMain?.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.action_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }

                R.id.action_help -> {
                    val intent = Intent(this, BantuanActivity::class.java)
                    startActivity(intent)
                }
                R.id.action_report -> {
                    val intent = Intent(this, UserReportActivity::class.java)
                    startActivity(intent)
                }
                R.id.action_profil -> {
                val intent = Intent(this, ProfilActivity::class.java)
                startActivity(intent)
            }
                /*R.id.action_helsp -> {
                openFragment(ProfileFragment())
                    return@setOnNavigationItemSelectedListener true
                }*/
            }
            return@setOnNavigationItemSelectedListener false
        }
    }
}