package com.wmsj.baselibs.utils

import android.annotation.TargetApi
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


/**
 * Created by tian
on 2019/8/14.
 */
object FileUtils {
    /**
     * 获取视频的缩略图
     * 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
     * @param videoPath 视频的路径
     * @param width 指定输出视频缩略图的宽度
     * @param height 指定输出视频缩略图的高度度
     * @param kind 参照MediaStore.Images(Video).Thumbnails类中的常量MINI_KIND和MICRO_KIND。
     * 其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
     * @return 指定大小的视频缩略图
     */
    fun getVideoThumbnail(videoPath: String): Bitmap? {
        var bitmap: Bitmap? = null
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Video.Thumbnails.MINI_KIND)
        if (bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, 1080, 980,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT)
        } else {
            return null
        }
        return bitmap
    }


    /**
     * Bitmap保存成File
     *
     * @param bitmap input bitmap
     * @param name output file's name
     * @return String output file's path
     */

    fun bitmap2File(bitmap: Bitmap?, name: String): String {

        val f = File(Environment.getExternalStorageDirectory().toString() +File.separator+ name + ".jpg")

        if (f.exists()) f.delete()

        var fOut: FileOutputStream? = null

        try {

            fOut = FileOutputStream(f)

            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, fOut)

            fOut!!.flush()

            fOut!!.close()

        } catch (e: IOException) {

            L.d("IOException:" + e.message.toString())
            return ""

        }

        return f.absolutePath

    }

    /**
     * 获取网络视频第一帧
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    fun createVideoThumbnail(url: String, width: Int, height: Int): Bitmap? {
        var bitmap: Bitmap? = null
        val retriever = MediaMetadataRetriever()
        val kind = MediaStore.Video.Thumbnails.MINI_KIND
        try {
            if (Build.VERSION.SDK_INT >= 14) {
                retriever.setDataSource(url, HashMap())
            } else {
                retriever.setDataSource(url)
            }
            bitmap = retriever.frameAtTime
        } catch (ex: IllegalArgumentException) {
            // Assume this is a corrupt video file
        } catch (ex: RuntimeException) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release()
            } catch (ex: RuntimeException) {
                // Ignore failures while cleaning up.
            }

        }
        if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT)
        }
        return bitmap
    }

    fun deleteFile(name: String) {
        if(StringUtils.isEmpty(name)){
            return
        }
        val file = File(name)
        if (file.exists()) {
            file.delete()
        }
    }
}