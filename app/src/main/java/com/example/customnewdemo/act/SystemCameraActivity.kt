package com.example.customnewdemo.act


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.media.MediaScannerConnection

import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore


import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.FileProvider
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.UriUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.customnewdemo.BuildConfig
import com.example.customnewdemo.app.base.BaseActivity
import com.example.customnewdemo.databinding.ActivitySystemCameraBinding
import com.example.customnewdemo.utils.GlideUtils
import com.tbruyelle.rxpermissions3.RxPermissions
import org.jzvd.jzvideo.TAG
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


/**
 * 系统相机的开启
 */
class SystemCameraActivity : BaseActivity<CustomPresenter>() {
    //    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_system_camera)
//    }

    lateinit var mBinding: ActivitySystemCameraBinding
    lateinit var rxPermission: RxPermissions
    var startActivity: ActivityResultLauncher<Intent>? = null

    companion object {

        public fun startSelf(activity: Activity) {
            val intent = Intent(activity, SystemCameraActivity::class.java)
            activity.startActivity(intent)
        }

    }

    private var imgUri: Uri? = null

    override fun setUpListener() {

        mBinding.tvOpenCamera.setOnClickListener {
            // 打开相机

            rxPermission.request(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
                .subscribe {
                    if (it) {
                        //


//                        var filePath: String =
//                            Environment.getExternalStorageDirectory().path + File.separator  + Environment.DIRECTORY_DCIM+ File.separator + "Camera" + File.separator;

                      var filePath=   Environment.getExternalStorageDirectory().absolutePath + File.separator + Environment.DIRECTORY_DCIM+File.separator+"Camera"+File.separator;
                        var imgFile = File(filePath)
                        if (!imgFile.exists()) {
                            imgFile.mkdirs()
                        }

                        // 根据时间来生成 图片
                        val currentTime = SimpleDateFormat("yyyyMMddhhmmss").format(Date())
                        var fileName = "IMG_" + currentTime + Locale.CANADA + ".jpg"

                        filePath = filePath + fileName

                        var outFile = File(filePath)

                         imgUri = Uri.fromFile(outFile)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            imgUri = FileProvider.getUriForFile(
                                this,
                                "${BuildConfig.APPLICATION_ID}",
                                outFile

                            )
                        }

                        var intent = Intent()
                        intent.action = MediaStore.ACTION_IMAGE_CAPTURE

                        intent.putExtra(MediaStore.EXTRA_OUTPUT,imgUri)
                        startActivityForResult(intent,1001)




//                        val file = FileUtil.createCameraFile("user_head_photo")
//                         fileUri = Uri.fromFile(file)
//
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
//                            fileUri = FileProvider.getUriForFile(
//                                this,
//                                "${BuildConfig.APPLICATION_ID}",
//                                file!!
//                            )
//                        }
//
//                        LogUtils.d(TAG, "获取的file 的uri" )
//
//
//                        var intentCamera =  Intent();
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                            intentCamera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
//                        }
//                        intentCamera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//                        //将拍照结果保存至photo_file的Uri中，不保留在相册中
//                        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
//                        startActivityForResult(intentCamera, 2)


                        //
                    }
                };


        }
    }

    override fun setUpView() {
        rxPermission = RxPermissions(this)
//        startActivity = registerForActivityResult(
//            ActivityResultContracts.StartActivityForResult()
//        ) {
//
//
//        }


    }

    override fun setUpData() {

    }

    override fun attachLayoutRes(): View {
        mBinding = ActivitySystemCameraBinding.inflate(layoutInflater)

        return mBinding.root
    }

    override fun setStatusBarView(): Int = 0

    override fun setNavigatorView(): Int = 0

    override fun setStateBarState() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && RESULT_OK == RESULT_OK) {
//            val bitmap = data.extras?.get("data") as Bitmap

            if (data != null && data.hasExtra("data")){

            }else{

                val uri2File = UriUtils.uri2File(imgUri)
                LogUtils.d(TAG, "获取的图片地址 ${uri2File.absolutePath}")

                Glide.with(this)
                    .asBitmap()
                    .load(imgUri)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(mBinding.ivPhoto);




                var intent = Intent()
                intent.action = Intent.ACTION_MEDIA_SCANNER_SCAN_FILE; //表示要将数据添加进系统的媒体资源库
//                intent.action = MediaStore.actionca
                intent.data = imgUri; //将照片文件的地址存入数据库
//
//                sendBroadcast(intent);

            }
        }
    }
}