package com.example.assignment2_19012021104


import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView


import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions

class MainActivity : AppCompatActivity() {

    lateinit var btnTranslateNow : Button
    lateinit var etOriginalLanguage: EditText
    lateinit var tvResult: TextView
    var originalText : String = ""
    lateinit var englishHindiTranslate : Translator

    lateinit var pDialog: SweetAlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnTranslateNow = findViewById(R.id.btnTranslateNow)
        etOriginalLanguage = findViewById(R.id.etOriginalText)
        tvResult = findViewById(R.id.tvTranslateLanguage)

        setUpProgressDialog()

        btnTranslateNow.setOnClickListener {
            originalText = etOriginalLanguage.text.toString()

            prepareTranslateModel()
        }
    }

    private fun setUpProgressDialog() {
        pDialog = SweetAlertDialog(this@MainActivity,SweetAlertDialog.PROGRESS_TYPE)
        pDialog.progressHelper.barColor = Color.parseColor("black")
        pDialog.titleText = "Loading"
        pDialog.setCancelable(false)
    }


    private fun prepareTranslateModel() {
        val options: TranslatorOptions = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.HINDI)
            .build()

        englishHindiTranslate = Translation.getClient(options)

        pDialog.titleText = "Model Downloading..."
        pDialog.show()

        englishHindiTranslate.downloadModelIfNeeded().addOnSuccessListener {
            pDialog.dismissWithAnimation()
            translateText()
        }.addOnFailureListener {
            pDialog.dismissWithAnimation()
            tvResult.text = "Error ${it.message}"

        }
    }


    private fun translateText() {
        pDialog.titleText="Translate Text"
        pDialog.show()
        englishHindiTranslate.translate(originalText).addOnSuccessListener {
            pDialog.dismissWithAnimation()
            tvResult.text = it
        }.addOnFailureListener {

            pDialog.dismissWithAnimation()
//            Toast.makeText(this@MainActivity,"Error",Toast.LENGTH_SHORT).show()
            tvResult.text = "Error ${it.message}"
        }
    }
}