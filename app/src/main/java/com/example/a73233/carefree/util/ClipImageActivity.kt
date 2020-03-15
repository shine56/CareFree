package com.example.a73233.carefree.util

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.a73233.carefree.R
import com.example.a73233.carefree.baseView.BaseActivity

class ClipImageActivity : BaseActivity() {
    private var inputPath = ""

    private var outputPath = ""

    private var maxScale = 1.0f

    private var minScale = 1.0f

    private var clipWidth = 90

    private var clipHeight = 90
    private lateinit var  clipImageView: ClipImageView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clip_image)
        handleIntentData()
        initView()
        initClipImageView()

        val back : ImageView = findViewById(R.id.clip_image_toolbar_left)
        val use : TextView = findViewById(R.id.clip_image_toolbar_right)

        back.setOnClickListener{finish()}
        use.setOnClickListener{
            showLoadingProgress()
            clipImageView.clipAndSaveImage(outputPath)
        }
    }

    private fun handleIntentData() {
        val bundle = intent.extras
        bundle?.let {
            inputPath = it.getString(INPUT_PATH)
            outputPath = it.getString(OUTPUT_PATH)
            maxScale = it.getFloat(MAX_SCALE)
            minScale = it.getFloat(MIN_SCALE)
            clipWidth = it.getInt(CLIP_WIDTH)
            clipHeight = it.getInt(CLIP_HEIGHT)
        }
    }
    private fun initView(){
        ReviseStatusBar(TRANSPARENT_BLACK)
        clipImageView = findViewById(R.id.clipImageView)
        progressBar = findViewById(R.id.clip_progress)
        progressBar.visibility = View.GONE
        loadPhoto()
    }
    private fun initClipImageView() {
        clipImageView.clipWidth = clipWidth
        clipImageView.clipHeight = clipHeight
        clipImageView.minScale = minScale
        clipImageView.maxScale = maxScale
        clipImageView.onsaveClipImageListener = object : ClipImageView.OnSaveClipImageListsner {
            override fun onFinishedSave() {
                hideLoadingProgress()
                val intent = Intent()
                intent.putExtra("outputPath",outputPath)
                setResult(Activity.RESULT_OK, intent)
                finish()
                LogUtil.LogD("结束裁剪")
            }
        }
    }
    private fun loadPhoto(){
        Glide.with(this).load(inputPath)
                .skipMemoryCache(true) // 不使用内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE) // 不使用磁盘缓存
                .error(R.mipmap.find_photo_fail)
                .into(clipImageView)
    }

    private fun showLoadingProgress() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideLoadingProgress() {
        progressBar.visibility = View.GONE
    }

    //伴生对象，类似Java的静态方法
    companion object {
        const val CLIP_WIDTH = "clip_width"
        const val CLIP_HEIGHT = "clip_height"
        const val MAX_SCALE = "max_scale"
        const val MIN_SCALE = "min_scale"
        const val INPUT_PATH = "input_path"
        const val OUTPUT_PATH = "output_path"
        fun startCropImage(context: Activity, requestCode: Int, inputPath: String, outputPath: String, clipWidth: Int, clipHeight: Int, maxScale: Float = 1.0f, minScale: Float = 1.0f) {
            val intent = Intent(context, ClipImageActivity::class.java)
            val bundle = Bundle()
            bundle.putInt(CLIP_WIDTH, clipWidth)
            bundle.putInt(CLIP_HEIGHT, clipHeight)
            bundle.putFloat(MAX_SCALE, maxScale)
            bundle.putFloat(MIN_SCALE, minScale)
            bundle.putString(INPUT_PATH, inputPath)
            bundle.putString(OUTPUT_PATH, outputPath)
            intent.putExtras(bundle)
            context.startActivityForResult(intent, requestCode)
        }
    }
}