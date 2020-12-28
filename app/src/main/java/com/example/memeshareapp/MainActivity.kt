package com.example.memeshareapp


import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

var requestcode =1
var imageurl:String? = null

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val snackbar = Snackbar.make(findViewById(android.R.id.content), "Swipe to Change The Memes", Snackbar.LENGTH_LONG)
        snackbar.show()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadmeme()
        imageView.setOnTouchListener(object : OnSwipeTouchListener(this@MainActivity) {

            override fun onSwipeTop() {
                super.onSwipeTop()
                loadmeme()
            }

            override fun onSwipeBottom() {
                super.onSwipeBottom()
                loadmeme()
            }

            override fun onSwipeLeft() {
                super.onSwipeLeft()
                loadmeme()
            }

            override fun onSwipeRight() {
                super.onSwipeRight()
                loadmeme()
            }
        })
    }

    fun loadmeme(){
        progress_circular.visibility=View.VISIBLE

// ...

// Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.herokuapp.com/gimme"

// Request a string response from the provided URL.
        val jsonObjectRequest =JsonObjectRequest(Request.Method.GET, url, null, Response.Listener { response ->
            imageurl = response.getString("url")


            Glide.with(this).load(imageurl).listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    progress_circular.visibility = View.GONE
                    return false
                }

            }).into(imageView)
        }, {

        })

// Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)


    }

    open class OnSwipeTouchListener(ctx: Context) : View.OnTouchListener {

        private val gestureDetector: GestureDetector

        companion object {

            private val SWIPE_THRESHOLD = 100
            private val SWIPE_VELOCITY_THRESHOLD = 100
        }

        init {
            gestureDetector = GestureDetector(ctx, GestureListener())
        }

        override fun onTouch(v: View, event: MotionEvent): Boolean {
            return gestureDetector.onTouchEvent(event)
        }

        private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {


            override fun onDown(e: MotionEvent): Boolean {
                return true
            }

            override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                var result = false
                try {
                    val diffY = e2.y - e1.y
                    val diffX = e2.x - e1.x
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                onSwipeRight()
                            } else {
                                onSwipeLeft()
                            }
                            result = true
                        }
                    } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeBottom()
                        } else {
                            onSwipeTop()
                        }
                        result = true
                    }
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }

                return result
            }


        }

        open fun onSwipeRight() {}

        open fun onSwipeLeft() {}

        open fun onSwipeTop() {}

        open fun onSwipeBottom() {}
    }

    fun sharememe(view: View) {
        val intent  = Intent(Intent.ACTION_SEND)
        intent.type="text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "Hey Check This Cool Meme $imageurl")
        val chooser = Intent.createChooser(intent, "Share This Meme using")
        startActivity(chooser)

    }


}