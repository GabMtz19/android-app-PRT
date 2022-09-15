package mx.itesm.testbasicapi.controller.activity

import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.content.SharedPreferences
import android.view.animation.Animation
import android.os.Bundle
import android.view.WindowManager
import mx.itesm.testbasicapi.R
import android.content.Intent
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import mx.itesm.testbasicapi.controller.activity.OnBoarding
import mx.itesm.testbasicapi.controller.activity.Forms
import mx.itesm.testbasicapi.controller.activity.SplashScreenActivity

class SplashScreenActivity : AppCompatActivity() {
    // Variables
    lateinit var backgroundImage: ImageView
    lateinit var onBoardingScreen: SharedPreferences

    // Animations
    lateinit var sideAnim: Animation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_splash_screen)

        // Hooks
        backgroundImage = findViewById(R.id.background_image)
        //        poweredByLine = findViewById(R.id.powered_by_line);

        // Animations
        sideAnim = AnimationUtils.loadAnimation(this, R.anim.side_anim)
        //        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_anim);

        // set Animations on elements
        backgroundImage.animation = sideAnim
        //        poweredByLine.setAnimation(bottomAnim);
        Handler().postDelayed({
            onBoardingScreen = getSharedPreferences("onBoardingScreen", MODE_PRIVATE)
            val isFirstTime = onBoardingScreen.getBoolean("firstTime", true)
            //            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//            startActivity(intent);
//            finish();
            if (!isFirstTime) {
                val editor = onBoardingScreen.edit()
                editor.putBoolean("firstTime", false) // Change
                editor.commit()
                val intent = Intent(applicationContext, OnBoarding::class.java)
                startActivity(intent)
                finish()
            } else {
//                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                val intent = Intent(applicationContext, Forms::class.java)
                startActivity(intent)
                finish()
            }
        }, SPLASH_TIMER.toLong())
    }

    companion object {
        // Constants
        private const val SPLASH_TIMER = 3000
    }
}