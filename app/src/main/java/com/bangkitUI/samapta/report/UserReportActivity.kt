package com.bangkitUI.samapta.report

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bangkitUI.samapta.API.ApiConfig
import com.bangkitUI.samapta.API.AppConfig
import com.bangkitUI.samapta.API.ServerResponse
import com.bangkitUI.samapta.R
import com.bangkitUI.samapta.MainActivity
import com.bangkitUI.samapta.databinding.ActivityMainBinding
import com.bangkitUI.samapta.databinding.ActivityUserReportBinding
import com.bangkitUI.samapta.main.bantuanActivity.BantuanActivity
import com.bangkitUI.samapta.main.profil.ProfilActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_user_report.*
import kotlinx.android.synthetic.main.main_report.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class UserReportActivity : AppCompatActivity() {

    private var progress: ProgressDialog? = null
    private var fileUri: Uri? = null
    private var mediaPath: String? = null
    private var mImageFileLocation = ""
    private var postPath: String? = null
    private lateinit var binding: ActivityUserReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserReportBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.elevation = 0F
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "LAPOR"

        init()

        val anim = AnimationUtils.loadAnimation(this, R.anim.animasi)
        animasi.startAnimation(anim)
        edt_nama_bencana.startAnimation(anim)
        edt_deskripsi.startAnimation(anim)
        edt_provinsi.startAnimation(anim)
        edt_kecamatan.startAnimation(anim)
        edt_kota.startAnimation(anim)
        chooseBtn.startAnimation(anim)
        cardView2.startAnimation(anim)
        buttonProcess.startAnimation(anim)
        camBtn.startAnimation(anim)

        buttonProcess.setOnClickListener{

            AlertDialog.Builder(this).setTitle(getString(R.string.are_you_sure))
                .setPositiveButton(getString(R.string.yes)){dialog, _ ->
                    uploadFile()

                    val intent2 = Intent(this, MainActivity::class.java)
                    startActivity(intent2)
                    Toast.makeText(this, "Thank You for Your Report!", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton(getString(R.string.no)){dialog, _ ->
                    dialog.dismiss()
                }
                .show()

        }
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        camBtn.setOnClickListener {
            captureImage()
        }

        chooseBtn.setOnClickListener {
            val galleryIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(galleryIntent, REQUEST_PICK_PHOTO)
        }
    }

    companion object {
        const val REQUEST_CODE_PERMISSIONS = 10
        val REQUIRED_PERMISSIONS = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )

        //resouce variable for camera feature and make file
        const val REQUEST_TAKE_PHOTO = 0
        const val REQUEST_PICK_PHOTO = 2
        const val CAMERA_PIC_REQUEST = 1111
        const val MEDIA_TYPE_IMAGE = 1

        private val TAG = MainActivity::class.java.simpleName
        private const val IMAGE_DIRECTORY_NAME = "Android File Upload"

        private fun getOutputMediaFile(type: Int): File? {
            val mediaStorageDir = File(
                Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME
            )
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d(
                        TAG, "Oops! Failed create "
                                + IMAGE_DIRECTORY_NAME + " directory")
                    return null
                }
            }
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val mediaFile: File
            if (type == MEDIA_TYPE_IMAGE) {
                mediaFile = File(
                    mediaStorageDir.path + File.separator
                            + "IMG_" + ".jpg")
            } else {
                return null
            }
            return mediaFile
        }
    }
    //for  ask permission
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }
    // on activity result usefull when user has done with action take camera or choose from gallery
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_TAKE_PHOTO || requestCode == REQUEST_PICK_PHOTO) {
            if (data != null) {
                val selectedImage = data.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val cursor = applicationContext?.contentResolver?.query(
                    selectedImage!!,
                    filePathColumn,
                    null,
                    null,
                    null
                )
                assert(cursor != null)
                cursor!!.moveToFirst()

                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                mediaPath = cursor.getString(columnIndex)
                imageAnalitics.setImageBitmap(BitmapFactory.decodeFile(mediaPath))
                cursor.close()
                postPath = mediaPath
            }
        } else if (requestCode == CAMERA_PIC_REQUEST) {
            postPath = if (Build.VERSION.SDK_INT > 21) {
                Glide.with(this).load(mImageFileLocation).into(imageAnalitics)
                mImageFileLocation
            } else {
                Glide.with(this).load(fileUri).into(imageAnalitics)
                fileUri!!.path
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    internal fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmSS").format(Date())
        val imageFileName = "IMAGE_" + timeStamp
        val storageDirectory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/photo_saving_app")
        if (!storageDirectory.exists()) storageDirectory.mkdir()
        val image = File(storageDirectory, "$imageFileName.jpg")
        mImageFileLocation = image.absolutePath
        return image
    }

    private fun captureImage() {
        if (Build.VERSION.SDK_INT > 21) {
            val callCameraApplicationIntent = Intent()
            callCameraApplicationIntent.action = MediaStore.ACTION_IMAGE_CAPTURE

            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            val outputUri = FileProvider.getUriForFile(
                this,
                "com.bangkitUI.samapta" + ".provider",
                photoFile!!
            )
            callCameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri)
            callCameraApplicationIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivityForResult(callCameraApplicationIntent, CAMERA_PIC_REQUEST)
        } else {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
            startActivityForResult(intent, CAMERA_PIC_REQUEST)
        }
    }

    private fun getOutputMediaFileUri(type: Int): Uri {
        return Uri.fromFile(getOutputMediaFile(type))
    }


    private fun uploadFile() {

        if (postPath == null || postPath == "") {
            Toast.makeText(this, "image null ", Toast.LENGTH_SHORT).show()
            return
        } else {

            val map = HashMap<String, RequestBody>()
            val file = File(postPath!!)
            val provinsi = edt_provinsi.text.toString()
            val kota = edt_kota.text.toString()
            val kecamatan = edt_kecamatan.text.toString()
            val namabencana: String = edt_nama_bencana.text.toString()
            val desc: String = edt_deskripsi.text.toString()

            val requestBody = file.asRequestBody("*/*".toMediaTypeOrNull())
            map["file\"; filename=\"" + file.name + "\""] = requestBody

            val modal = ServerResponse(namabencana, desc, provinsi, kota, kecamatan)

            val getResponse = AppConfig.getRetrofit().create(ApiConfig::class.java)
            val call = getResponse.upload("",namabencana, desc, provinsi, kota, kecamatan, map)

            if (call != null) {
                call.enqueue(object : Callback<ServerResponse?> {
                    override fun onResponse(
                        call: Call<ServerResponse?>,
                        response: retrofit2.Response<ServerResponse?>
                    ) {
                        if (response.isSuccessful) {
                            if (response.body() != null) {
                                val serverResponse = response.body()
                                edt_nama_bencana.setText("")
                                edt_deskripsi.setText("")
                                edt_provinsi.setText("")
                                edt_kota.setText("")
                                edt_kecamatan.setText("")
                                Toast.makeText(applicationContext, serverResponse?.response, Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            val serverResponse = response.body()
                            Toast.makeText(applicationContext, serverResponse?.error , Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<ServerResponse?>, t: Throwable) {
                        t.message?.let { Log.v("Response gotten is", it) }
                    }
                })
            }

            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading")
            progress?.show()
        }
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