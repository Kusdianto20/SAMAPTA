package com.bangkitUI.samapta.main.profil

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.bangkitUI.samapta.MainActivity
import com.bangkitUI.samapta.R
import com.bangkitUI.samapta.databinding.ActivityBantuanBinding
import com.bangkitUI.samapta.databinding.ActivityProfilBinding
import com.bangkitUI.samapta.main.bantuanActivity.BantuanActivity
import com.bangkitUI.samapta.report.UserReportActivity
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_profil.*
import kotlinx.android.synthetic.main.main_home.*
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.android.synthetic.main.main_report.*
import kotlinx.android.synthetic.main.main_report.view.*


class ProfilActivity : AppCompatActivity() {


    private lateinit var imgAvatar : String
    private lateinit var binding: ActivityProfilBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfilBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        init()

        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        sign_out_button.setOnClickListener{
            mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, OnCompleteListener<Void?> {

                })
            AlertDialog.Builder(this).setTitle(getString(R.string.are_you_sure))
                .setPositiveButton(getString(R.string.yes)){ialog, _ ->
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                .setNegativeButton(getString(R.string.no)){dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        val acct = GoogleSignIn.getLastSignedInAccount(this)
        if (acct != null) {
            edt_namaprofil.text = acct.displayName
            edt_emailprofil.text = acct.email
        }
        if(acct.getPhotoUrl() == null){
            //set default image
        } else {
            val personPhoto1 : Uri? = acct?.photoUrl
            Glide.with(this)
                .load(acct.photoUrl)
                .into(personPhoto)
            imgAvatar = acct.photoUrl.toString() //photo_url is String
        }
            /*    val personPhoto1 : Uri? = acct?.photoUrl
            if (acct != null) {
                Glide.with(this)
                    .load(acct.photoUrl)
                    .into(personPhoto)
            }
            if (acct != null) {
                imgAvatar= acct.photoUrl.toString()
            }
            }*/
    }

    private fun init() {
        binding.btmNavigationProfil?.setOnNavigationItemSelectedListener {
            when (it.itemId) {
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

            }
            return@setOnNavigationItemSelectedListener false
        }
    }

}