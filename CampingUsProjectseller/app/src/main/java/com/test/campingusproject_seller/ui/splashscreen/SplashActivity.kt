package com.test.campingusproject_seller.ui.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AlphaAnimation
import android.widget.TextView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.test.campingusproject_seller.databinding.ActivitySplashBinding
import com.test.campingusproject_seller.ui.main.MainActivity

class SplashActivity : AppCompatActivity() {

    val splashTimeOut: Long = 1500 // 스플래시 화면이 보여지는 시간 (밀리초)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activitySplashBinding = ActivitySplashBinding.inflate(layoutInflater)

        installSplashScreen()

        setContentView(activitySplashBinding.root)

        animateText(activitySplashBinding.splashText)

        // 스플래시 화면 표시 후 메인 액티비티로 이동
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, splashTimeOut)
    }

    // 스플래시 액티비티 실행 후 메인 액티비티를 띄우므로 중간에 꺼버려도 메인 액티비티가 실행됨
    // 따라서, 백버튼을 막아버림
    override fun onBackPressed() {
    }

    fun animateText(textView: TextView) {
        val animation = AlphaAnimation(0.0f, 1.0f) // 투명도를 0~1로 바꿈
        animation.duration = 1500 // 애니메이션 지속 시간 (밀리초)
        textView.startAnimation(animation)
    }
}