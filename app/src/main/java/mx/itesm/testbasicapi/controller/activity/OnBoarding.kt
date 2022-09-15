package mx.itesm.testbasicapi.controller.activity

import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import android.widget.LinearLayout
import mx.itesm.testbasicapi.controller.adapter.SliderAdapter
import android.widget.TextView
import android.view.animation.Animation
import android.os.Bundle
import android.view.WindowManager
import mx.itesm.testbasicapi.R
import android.content.Intent
import mx.itesm.testbasicapi.controller.activity.Forms
import android.text.Html
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.viewpager.widget.ViewPager.OnPageChangeListener

class OnBoarding : AppCompatActivity() {
    // Variables
    lateinit var viewPager: ViewPager
    lateinit var dotsLayout: LinearLayout
    lateinit var sliderAdapter: SliderAdapter
    lateinit var dots: Array<TextView?>
    lateinit var letsGetStarted: Button
    lateinit var animation: Animation
    var currentPosition = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_on_boarding)

        // Hooks
        viewPager = findViewById(R.id.slider)
        dotsLayout = findViewById(R.id.dots)
        letsGetStarted = findViewById(R.id.get_started_btn)

        // Call Adapter
        sliderAdapter = SliderAdapter(this)
        viewPager.adapter = sliderAdapter
        addDots(0)
        viewPager.addOnPageChangeListener(changeListener)
    }

    fun skip(view: View?) {
        startActivity(Intent(this, Forms::class.java))
        finish()
    }

    fun next(view: View?) {
        viewPager.currentItem = currentPosition + 1
    }

    private fun addDots(position: Int) {
        dots = arrayOfNulls(3)
        dotsLayout.removeAllViews()
        for (i in dots.indices) {
            dots[i] = TextView(this)
            dots[i]!!.text = Html.fromHtml("&#8226;")
            dots[i]!!.textSize = 35f
            dotsLayout.addView(dots[i])
        }
        if (dots.size > 0) {
            dots[position]!!.setTextColor(resources.getColor(R.color.blue))
        }
    }

    var changeListener: OnPageChangeListener = object : OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            addDots(position)
            currentPosition = position
            if (position == 0) {
                letsGetStarted.visibility = View.INVISIBLE
            } else if (position == 1) {
                letsGetStarted.visibility = View.INVISIBLE
            } else {
                animation = AnimationUtils.loadAnimation(this@OnBoarding, R.anim.side_anim)
                letsGetStarted.animation = animation
                letsGetStarted.visibility = View.VISIBLE
            }
        }

        override fun onPageScrollStateChanged(state: Int) {}
    }
}