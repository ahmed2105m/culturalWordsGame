package com.barmej.culturalwordsgame

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*


class MainActivity : AppCompatActivity() {

    private val permissionWriteExternalStorage: Int = 123

    private var mCurrentImage = 0

    //images Array
    private var mImages = arrayListOf(
        R.drawable.icon_1,
        R.drawable.icon_2,
        R.drawable.icon_3,
        R.drawable.icon_4,
        R.drawable.icon_5,
        R.drawable.icon_6,
        R.drawable.icon_7,
        R.drawable.icon_8,
        R.drawable.icon_9,
        R.drawable.icon_10,
        R.drawable.icon_11,
        R.drawable.icon_12,
        R.drawable.icon_13
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences = getSharedPreferences("data", MODE_PRIVATE)
        val appLang = sharedPreferences.getString("language", Locale.getDefault().language)
            appLang?.let { setAppLocale(it) }


        //casting
        val answerButton = findViewById<ImageButton>(R.id.answerButton)
        val shearButton = findViewById<ImageButton>(R.id.shearButton)
        val languageButton = findViewById<ImageButton>(R.id.languageButton)
        val changeButton = findViewById<ImageButton>(R.id.changeButton)
        val image = findViewById<ImageView>(R.id.image)


        languageButton.setOnClickListener {
            showLanguageDialog()
        }
        shearButton.setOnClickListener {
            checkPermissionAndShare()
        }
        changeButton.setOnClickListener {
            ++mCurrentImage
            showDisplay(image)
        }
        answerButton.setOnClickListener {
            showAnswer()
        }

    }

    private fun showDisplay(view: ImageView) {
        if (mCurrentImage < mImages.size) {
            val setImage = ContextCompat.getDrawable(this, mImages[mCurrentImage])
            view.setImageDrawable(setImage)
        } else {
            Toast.makeText(applicationContext, getString(R.string.qfinish), Toast.LENGTH_SHORT)
                .show()
        }
    }


    private fun showLanguageDialog() {
        val alertDialog: AlertDialog = AlertDialog.Builder(this)
            .setTitle(R.string.change_language)
            .setItems(R.array.lang) { dialog, which ->
                var language = "ar"
                when (which) {
                    0 -> language = "ar"
                    1 -> language = "en"
                }
                saveLanguage(language)
                setAppLocale(language)
                val intent = Intent(applicationContext, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }.create()
        alertDialog.show()
    }


    private fun checkPermissionAndShare() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                // بسبب عدم منح المستخدم الصلاحية للتطبيق فمن الأفضل شرح فائدتها له عن طريق عرض رسالة تشرح ذلك
                // هنا نقوم بإنشاء AlertDialog لعرض رسالة تشرح للمستخدم فائدة منح الصلاحية
                val alertDialog = AlertDialog.Builder(this)
                    .setTitle(R.string.permission_title)
                    .setMessage(R.string.permission_explanation)
                    .setPositiveButton(
                        android.R.string.ok
                    ) { _, _ -> // requestPermissions عند الضغط على زر منح نقوم بطلب الصلاحية عن طريق الدالة
                        ActivityCompat.requestPermissions(
                            this@MainActivity,
                            arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            permissionWriteExternalStorage
                        )
                    }.setNegativeButton(
                        android.R.string.cancel
                    ) { dialogInterface, _ -> //  عند الضغط على زر منع نقوم بإخفاء الرسالة وكأن شيء لم يكن
                        dialogInterface.dismiss()
                    }.create()

                // نقوم بإظهار الرسالة بعد إنشاء alertDialog

                // نقوم بإظهار الرسالة بعد إنشاء alertDialog
                alertDialog.show()

            } else {
                // لا داعي لشرح فائدة الصلاحية للمستخدم ويمكننا طلب الصلاحية منه

                // لا داعي لشرح فائدة الصلاحية للمستخدم ويمكننا طلب الصلاحية منه
                ActivityCompat.requestPermissions(
                    this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    permissionWriteExternalStorage
                )
            }

        } else {
            shareImage()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionWriteExternalStorage) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                shareImage()
            } else {
                Toast.makeText(
                    this@MainActivity,
                    R.string.permission_explanation,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    private fun shareImage() {
        val intent = Intent(this, ShearActivity::class.java)
        intent.putExtra("SHARE_IMAGE", mImages[mCurrentImage])
        startActivity(intent)
    }

    private fun showAnswer() {
        val answer = resources.getStringArray(R.array.answers)
        val intent = Intent(this, ShowAnswer::class.java)
        intent.putExtra("SHOW_ANSWER", answer[mCurrentImage])
        startActivity(intent)
    }

    private fun saveLanguage(language: String) {
        val sharedPreferences = getSharedPreferences("data", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("language", language)
        editor.apply()
    }

    private fun setAppLocale(localeCode: String) {
        val config = resources.configuration
        val locale = Locale(localeCode)
        Locale.setDefault(locale)
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            createConfigurationContext(config)
        }

        resources.updateConfiguration(config, resources.displayMetrics)

    }

}

