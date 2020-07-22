package com.yqy.photo.choose

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.permissionx.guolindev.PermissionX
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropActivity
import com.zs.photo.R
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

/**
 * @desc
 * @author derekyan
 * @date 2020/7/10
 */
class InvisiblePhotoFragment: Fragment() {


    private var photoPath = ""
    private var cameraSavePath: File? = null    //拍照照片路径
    private var uri: Uri? = null    //照片uri
    private val requestCodeTake = 1
    private val requestCodeAlbum = 2

    private var bottomSheetDialog: BottomSheetDialog? = null

    /**
     * 动作类型 1：拍照 2：相册 3：保存
     */
    private var actionType = 0

    private lateinit var photoBuilder: PhotoBuilder

    fun showDialog(photoBuilder: PhotoBuilder) {
        this.photoBuilder = photoBuilder
        showHeadMenu()
    }

    /**
     * 打开弹窗
     */
    private fun showHeadMenu() {
        if (bottomSheetDialog == null) {
            bottomSheetDialog = BottomSheetDialog(requireContext())
            bottomSheetDialog?.setContentView(R.layout.photo_dialog_photo_menu)
            val tv_take = bottomSheetDialog?.findViewById<TextView>(R.id.tv_take)
            val tv_album = bottomSheetDialog?.findViewById<TextView>(R.id.tv_album)
            val tv_save = bottomSheetDialog?.findViewById<TextView>(R.id.tv_save)
            val tv_cancel = bottomSheetDialog?.findViewById<TextView>(R.id.tv_cancel)

            tv_take?.visibility = if (photoBuilder.getPhotoBean().isShowTakePhoto) View.VISIBLE else View.GONE
            tv_album?.visibility = if (photoBuilder.getPhotoBean().isShowPhotoAlbum) View.VISIBLE else View.GONE
            tv_save?.visibility = if (photoBuilder.getPhotoBean().isShowSaveToAlbum) View.VISIBLE else View.GONE

            tv_take?.setOnClickListener {
                actionType = 1
                checkCameraPermission()
                bottomSheetDialog?.dismiss()
            }
            tv_album?.setOnClickListener {
                actionType = 2
                checkCameraPermission()
                bottomSheetDialog?.dismiss()
            }
            tv_save?.setOnClickListener {
                actionType = 3
                savePhotoToAlbum()
                bottomSheetDialog?.dismiss()
            }
            tv_cancel?.setOnClickListener {
                photoBuilder.getChooseCallback()?.onCancel()
                bottomSheetDialog?.dismiss()
            }
        }
        bottomSheetDialog?.show()
    }

    /**
     * 检查相机权限 若都允许则继续
     */
    private fun checkCameraPermission() {
        PermissionX.init(requireActivity())
            .permissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
            .request { allGranted, _, _ ->
                if (allGranted) {
                    when (actionType) {
                        1 -> takePhoto()
                        2 -> selectPhoto()
                        3 -> savePhotoToAlbum()
                    }
                }
            }
    }

    /**
     * 拍照 调用系统相机
     */
    private fun takePhoto() {
        photoPath = photoBuilder.getPhotoBean().photoPath

        cameraSavePath = File(photoPath)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        uri = getFileUri(photoPath, intent)

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(intent, requestCodeTake)
    }

    /**
     * 相册 选择照片
     */
    private fun selectPhoto() {
        val intent = Intent()
        intent.action = Intent.ACTION_PICK
        intent.type = "image/*"
        startActivityForResult(intent, requestCodeAlbum)
    }

    /**
     * 保存到相册
     */
    private fun savePhotoToAlbum() {
        val name = System.currentTimeMillis().toString() + ".jpg"
        if (photoBuilder.getPhotoBean().photoBitmap != null) {
            val result = saveBitmapToAlbum(photoBuilder.getPhotoBean().photoBitmap!!, name)
            if (result) {
                photoBuilder.getChooseCallback()?.onSuccess("")
            } else {
                photoBuilder.getChooseCallback()?.onFailed(1, "保存失败")
            }
        } else {
            photoBuilder.getChooseCallback()?.onFailed(1, "保存失败 photoBitmap == null")
        }
    }

    /**
     * 调用裁剪
     */
    private fun gotoCrop(uri: Uri) {
        val photoPath = photoBuilder.getPhotoBean().cropPhotoPath
        //裁剪后保存到文件中
        val destinationUri = Uri.fromFile(File(photoPath))
        val uCrop = UCrop.of(uri, destinationUri)
        val options = UCrop.Options()
        //设置裁剪图片可操作的手势
        //一共三个参数，分别对应裁剪功能页面的“缩放”，“旋转”，“裁剪”界面，对应的传入NONE，就表示关闭了其手势操作
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.NONE, UCropActivity.NONE)
        //设置toolbar颜色
        options.setToolbarColor(Color.parseColor("#ffffff"))
        //设置状态栏颜色
        options.setStatusBarColor(Color.parseColor("#000000"))
        //是否能调整裁剪框
//        options.setFreeStyleCropEnabled(true);
        options.setHideBottomControls(true)
        uCrop.withOptions(options)

        // 裁剪比例
        if (photoBuilder.getPhotoBean().xCropRadio <= 0
            || photoBuilder.getPhotoBean().yCropRadio <= 0) {
            uCrop.withAspectRatio(1f,  1f) //默认比例
        } else {
            uCrop.withAspectRatio(photoBuilder.getPhotoBean().xCropRadio,  photoBuilder.getPhotoBean().yCropRadio) //比例
        }

        uCrop.start(requireActivity(), this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                requestCodeTake -> {
                    // 拍照
                    photoPath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        cameraSavePath?.path ?: ""
                    } else {
                        uri?.encodedPath ?: ""
                    }
                    if (photoBuilder.getPhotoBean().isNeedCrop)
                        gotoCrop(uri!!)
                    else
                        photoBuilder.getChooseCallback()?.onSuccess(photoBuilder?.getPhotoBean().photoPath)
                }
                requestCodeAlbum -> {
                    // 相册
                    if (data?.data != null) {
                        photoPath =
                            GetPhotoFromPhotoAlbum.getRealPathFromUri(requireActivity(), data.data!!) ?: ""

                        uri = getFileUri(photoPath, Intent())
                        if (photoBuilder.getPhotoBean().isNeedCrop)
                            gotoCrop(uri!!)
                        else
                            photoBuilder.getChooseCallback()?.onSuccess(GetPhotoFromPhotoAlbum.getRealPathFromUri(requireContext(), uri!!) ?: "")
                    } else {
                        photoBuilder.getChooseCallback()?.onFailed(0, "选择相册失败 data == null")
                    }
                }
                UCrop.REQUEST_CROP -> {
                    // 裁剪成功
                    val resultUri = UCrop.getOutput(data!!)
                    if (resultUri != null) {
                        photoBuilder.getChooseCallback()?.onSuccess(resultUri.path ?: "")
                    } else {
                        photoBuilder.getChooseCallback()?.onFailed(0, "裁剪失败 resultUri == null")
                    }
                }
                UCrop.RESULT_ERROR -> {
                    // 裁剪失败
                    if (data != null) {
                        val exception = UCrop.getError(data)
                        println(exception)
                        photoBuilder.getChooseCallback()?.onFailed(0, "裁剪失败:" + exception?.message )
                    } else {

                        photoBuilder.getChooseCallback()?.onFailed(0, "裁剪失败")
                    }
                }
            }
        }
    }


    /**
     * 获取uri
     */
    fun getFileUri(path: String, intent: Intent?): Uri {
        val uri: Uri
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(
                requireContext(),
                "com.zs.live.FileProvider",
                File(path)
            )
            intent?.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        } else {
            uri = Uri.fromFile(File(path))
        }
        return uri
    }

    private fun saveBitmapToAlbum(bitmap: Bitmap, bitName: String): Boolean {
        val file: File
        val fileName: String = if (Build.BRAND == "Xiaomi") { // 小米手机
            Environment.getExternalStorageDirectory()
                .path + "/DCIM/Camera/" + bitName
        } else { // Meizu 、Oppo
            Environment.getExternalStorageDirectory().path + "/DCIM/" + bitName
        }
        file = File(fileName)
        if (file.exists()) {
            file.delete()
        }
        val out: FileOutputStream
        try {
            out = FileOutputStream(file)
            // 格式为 JPEG，照相机拍出的图片为JPEG格式的，PNG格式的不能显示在相册中
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)) {
                out.flush()
                out.close()
                // 插入图库
                MediaStore.Images.Media.insertImage(
                    requireActivity().getContentResolver(),
                    file.absolutePath,
                    bitName,
                    null
                )
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return false
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
        // 发送广播，通知刷新图库的显示
        requireActivity().sendBroadcast(
            Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.parse("file://$fileName")
            )
        )
        return true;
    }

}