package com.android.albert.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.Nullable
import androidx.annotation.RawRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import java.io.File

/**
 * @author 张雷
 * @data 2018.06.19
 * @brief 图片加载库
 */
object ImageLoaderUtils {

    /**
     * 加载图片
     */
    fun loadUrl(mContext: Context, @Nullable url: String, img: ImageView) {
        Glide.with(mContext).clear(img)
        Glide.with(mContext).load(url).into(img)
    }

    /**
     * 加载图片
     */
    fun loadUrl(mContext: Context, @Nullable url: String, img: ImageView, @DrawableRes placeholder: Int) {
        Glide.with(mContext).clear(img)
        val options = RequestOptions().placeholder(placeholder)
                .error(placeholder)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        Glide.with(mContext).load(url).apply(options).into(img)
    }


    fun loadUrl(mContext: Context, @Nullable url: String, img: ImageView, @DrawableRes placeholder: Int, width: Int, height: Int) {
        Glide.with(mContext).clear(img)
        var options = RequestOptions().placeholder(placeholder)
                .error(placeholder)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .dontAnimate()
        if (width != 0 && height != 0) {
            options = options.override(width, height)
        }
        Glide.with(mContext).load(url).apply(options).into(img)
    }


    /**
     * gif 图片加载
     */
    fun loadGif(mContext: Context, @RawRes @DrawableRes @Nullable gifResourceDrawable: Int, img: ImageView) {
        Glide.with(mContext).clear(img)
        val options = RequestOptions()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
        Glide.with(mContext).asGif().load(gifResourceDrawable).apply(options).into(img)
    }

    /**
     * 加载File 类型文件
     */
    fun loadFile(mContext: Context, @Nullable file: File, img: ImageView, @DrawableRes placeholder: Int) {
        Glide.with(mContext).clear(img)
        val options = RequestOptions().placeholder(placeholder)
                .error(placeholder)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
        Glide.with(mContext).asFile().load(file).apply(options).into(img)
    }

    /**
     * sd卡图片加载
     */
    fun loadLocalPath(mContext: Context, path: String, img: ImageView, @DrawableRes placeholder: Int) {
        Glide.with(mContext).clear(img)
        val options = RequestOptions().placeholder(placeholder)
                .error(placeholder)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
        Glide.with(mContext).asBitmap().load(path).apply(options).into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                img.setImageBitmap(resource)
            }

            override fun onLoadFailed(errorDrawable: Drawable?) {
                super.onLoadFailed(errorDrawable)
                img.setImageResource(placeholder)
            }
        })
    }
}