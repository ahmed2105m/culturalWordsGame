package com.barmej.culturalwordsgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ShowAnswer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_answer)

        //casting
        val answerText = findViewById<TextView>(R.id.text_view_answer)
        val backBtn = findViewById<TextView>(R.id.button_return)

        //get Answer From Intent
        val mAnswer = intent.getStringExtra("SHOW_ANSWER")
        answerText.text = mAnswer

        backBtn.setOnClickListener {
            finish()
        }
    }
}