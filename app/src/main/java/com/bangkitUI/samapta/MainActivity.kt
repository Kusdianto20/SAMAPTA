package com.bangkitUI.samapta

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.bangkitUI.samapta.databinding.ActivityMainBinding
import com.bangkitUI.samapta.main.bantuanActivity.BantuanActivity
import com.bangkitUI.samapta.main.profil.ProfilActivity
import com.bangkitUI.samapta.main.tentangActivity.TentangActivity
import com.bangkitUI.samapta.report.UserReportActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.gms.tasks.OnCompleteListener
import kotlinx.android.synthetic.main.main_home.*

const val RC_SIGN_IN = 123

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        init()

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        
        sign_in_button.visibility = View.VISIBLE
        ll_home.visibility = View.GONE
        btm_navigation_main.visibility = View.GONE

        sign_in_button.setSize(SignInButton.SIZE_WIDE)

        sign_in_button.setOnClickListener{
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        val acct = GoogleSignIn.getLastSignedInAccount(this)
        if (acct != null) {
            sign_in_button.visibility = View.GONE
            tv_name.text = acct.displayName
            ll_home.visibility = View.VISIBLE
            btm_navigation_main.visibility = View.VISIBLE
        }

        btn_user_report.setOnClickListener(this)
        btn_bantuan.setOnClickListener(this)
        btn_tentang.setOnClickListener(this)
        button1.setOnClickListener{
            AlertDialog.Builder(this).setTitle(getString(R.string.are_you))
                .setPositiveButton(getString(R.string.yes)){ialog, _ ->
                    val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://covid19.go.id/faskesvaksin"))
                    startActivity(i)
                }
                .setNegativeButton(getString(R.string.no)){dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
        button2.setOnClickListener{
            AlertDialog.Builder(this).setTitle(getString(R.string.are_you))
                .setPositiveButton(getString(R.string.yes)){ialog, _ ->
                    val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://covid19.go.id/edukasi/ibu-dan-anak/aku-pakai-masker-supaya-virusnya-kalah"))
                    startActivity(i)
                }
                .setNegativeButton(getString(R.string.no)){dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
        button3.setOnClickListener{
            AlertDialog.Builder(this).setTitle(getString(R.string.are_you))
                .setPositiveButton(getString(R.string.yes)){ialog, _ ->
                    val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://kipi.covid19.go.id/"))
                    startActivity(i)
                }
                .setNegativeButton(getString(R.string.no)){dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
        btn_ramalan.setOnClickListener{
            AlertDialog.Builder(this).setTitle(getString(R.string.are_you))
                .setPositiveButton(getString(R.string.yes)){ialog, _ ->
                    val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://freemeteo.co.id/"))
                    startActivity(i)
                }
                .setNegativeButton(getString(R.string.no)){dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            sign_in_button.visibility = View.GONE
            tv_name.text = account.displayName
            ll_home.visibility = View.VISIBLE
            btm_navigation_main.visibility = View.VISIBLE

        } catch (e: ApiException) {

            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(ContentValues.TAG, "signInResult:failed code=" + e.statusCode)
            sign_in_button.visibility = View.VISIBLE
            tv_name.text = ""
            ll_home.visibility = View.GONE
            btm_navigation_main.visibility = View.GONE
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
           R.id.btn_user_report -> {
                val moveUserReportActivity = Intent(this, UserReportActivity::class.java)
                startActivity(moveUserReportActivity)
            }
            R.id.btn_bantuan -> {
                val moveBantuanActivity = Intent(this, BantuanActivity::class.java)
                startActivity(moveBantuanActivity)
            }
            R.id.btn_tentang -> {
                val moveBantuanActivity = Intent(this, TentangActivity::class.java)
                startActivity(moveBantuanActivity)
            }
            R.id.action_home -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
    private fun init() {
        binding.btmNavigationMain?.setOnNavigationItemSelectedListener {
            when(it.itemId) {
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