package com.yqy.common.util

import android.annotation.TargetApi
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.zs.common.BuildConfig

/**
 * 通知辅助类
 *
 * @author derekyan
 * @date 2020/6/11
 */
object NotificationHelper {
    private const val TAG = "NotificationUtils"
    var isShowLog = BuildConfig.ENV_TEST

    /**
     * 创建并显示默认通知
     */
    fun createAndShow(
        context: Context,
        channelId: String,
        notificationId: Int,
        tag: String? = null,
        whenTime: Long? = System.currentTimeMillis(),
        smallIconResId: Int? = 0,
        largeIconBitmap: Bitmap? = null,
        contentTitle: String? = "",
        contentText: String? = "",
        pendingIntent: PendingIntent? = null,
        defaults: Int? = null
    ) {
        // 创建通知
        val notification = create(
            context,
            channelId,
            whenTime,
            smallIconResId,
            largeIconBitmap,
            contentTitle,
            contentText,
            defaults,
            pendingIntent
        )

        // 显示通知
        show(context, notificationId, notification, tag)
    }

    /**
     * 创建默认通知
     *
     * @param context 上下文
     * @param channelId 渠道id
     * @param whenTime 通知时间
     * @param smallIconResId 通知小图标
     * @param largeIconBitmap 通知大图
     * @param contentTitle 标题
     * @param contentText 内容
     * @param defaults 设置
     * @param pendingIntent 点击意图
     */
    fun create(
        context: Context,
        channelId: String,
        whenTime: Long? = System.currentTimeMillis(),
        smallIconResId: Int? = 0,
        largeIconBitmap: Bitmap? = null,
        contentTitle: String? = "",
        contentText: String? = "",
        defaults: Int? = null,
        pendingIntent: PendingIntent? = null
    ): Notification? {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            if (!isChannelAlreadyCreated(context, channelId)) {
                // 通知渠道已创建
                log("创建通知失败: 渠道(channelId:${channelId})未创建")
                return null
            }
            if (!isChannelsEnabled(context, channelId, false)) {
                // 通知渠道未打开
                log("创建通知失败: 通知渠道(channelId:$channelId)未打开")
                return null
            }
        }

        // 适配通知栏build的创建方式
        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder(context, channelId)
        } else {
            NotificationCompat.Builder(context)
        }

        // set
        if (whenTime != null) builder.setWhen(whenTime)
        if (smallIconResId != null && smallIconResId > 0) builder.setSmallIcon(smallIconResId)
        if (largeIconBitmap != null) builder.setLargeIcon(largeIconBitmap)
        if (contentTitle != null) builder.setContentTitle(contentTitle)
        if (contentText != null) builder.setContentText(contentText)
        if (defaults != null) builder.setDefaults(defaults)
        if (pendingIntent != null) builder.setContentIntent(pendingIntent)

        return  builder.build()
    }

    /**
     * 打开通知
     */
    fun show(context: Context, notificationId: Int, notification: Notification?, tag: String? = null) {
        if (notification != null) {
            NotificationManagerCompat.from(context).notify(tag, notificationId, notification)
        }
    }

    /**
     * 关闭指定 id 的通知
     */
    fun cancel(context: Context, notificationId: Int, tag: String? = null) {
        NotificationManagerCompat.from(context).cancel(tag, notificationId)

    }

    /**
     * 关闭所有通知
     */
    fun cancelAll(context: Context) {
        NotificationManagerCompat.from(context).cancelAll()
    }

    /**
     * 创建多个通知渠道
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun createChannels(context: Context, channelList: List<NotificationChannel>) {
        if (channelList.isNotEmpty()) {
            // 创建多个通知渠道
            // 校验每个通知渠道的渠道组是否创建 若其中一个渠道组未创建则任务结束
            channelList.forEach {
                if (it.group != null
                    && it.group.isNotEmpty()
                    && !isChannelGroupAlreadyCreated(context, it.group)
                ) {
                    // 渠道组未创建
                    log("多渠道创建失败：渠道组(groupId:${it.group})未创建")
                    return
                }
            }
            NotificationManagerCompat.from(context).createNotificationChannels(channelList)
        }
    }

    /**
     * 创建单个通知渠道
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun createChannel(context: Context, channelBean: NotificationChannel) {
        if (!checkChannelParam(channelBean)) {
            return
        }
        if (isChannelAlreadyCreated(context, channelBean.id)) {
            // 通知渠道已创建
            log("渠道(channelId:${channelBean.id})创建失败：已创建")
            return
        }
        if (channelBean.group != null
            && channelBean.group.isNotEmpty()
            && !isChannelGroupAlreadyCreated(context, channelBean.group)
        ) {
            // 渠道组未创建
            log("渠道(channelId:${channelBean.id})创建失败：" +
                    "渠道组(groupId:${channelBean.group})未创建")
            return
        }
        // 创建通知渠道
        NotificationManagerCompat.from(context).createNotificationChannel(channelBean)
    }

    /**
     * 创建单个通知渠道
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun createChannel(
        context: Context,
        channelId: String,
        channelName: String,
        importance: Int,
        groupId: String? = ""
    ) {
        val channel = NotificationChannel(channelId, channelName, importance)
        if (!groupId.isNullOrEmpty())
            channel.group = groupId
        createChannel(context, channel)
    }

    /**
     * 删除单个渠道
     */
    fun deleteChannel(context: Context, channelId: String) {
        NotificationManagerCompat.from(context).deleteNotificationChannel(channelId)
    }

    /**
     * 删除多个渠道
     */
    fun deleteChannels(context: Context, channelIds: List<String>) {
        channelIds.forEach { deleteChannel(context, it) }
    }

    /**
     * 创建单个渠道组
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun createChannelGroup(context: Context, channelGroup: NotificationChannelGroup) {
        NotificationManagerCompat.from(context).createNotificationChannelGroup(channelGroup)
    }

    /**
     * 创建多个渠道组
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun createChannelGroups(context: Context, channelGroupList: List<NotificationChannelGroup>) {
        channelGroupList.forEach {
            createChannelGroup(context, it)
        }
    }

    /**
     * 删除单个渠道组
     */
    fun deleteChannelGroup(context: Context, groupId: String) {
        NotificationManagerCompat.from(context).deleteNotificationChannelGroup(groupId)
    }

    /**
     * 删除多个渠道组
     */
    fun deleteChannelGroups(context: Context, groupIds: List<String>) {
        groupIds.forEach { deleteChannelGroup(context, it) }
    }

    /**
     * 创建 NotificationChannel 对象
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun buildChannel(
        channelId: String,
        channelName: String,
        importance: Int,
        groupId: String? = ""
    ): NotificationChannel {
        val channel = NotificationChannel(channelId, channelName, importance)
        if (!groupId.isNullOrEmpty()) {
            channel.group = groupId
        }
        return channel
    }

    /**
     * 创建 NotificationChannelGroup 对象
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun buildChannelGroup(groupId: String, groupName: String): NotificationChannelGroup {
        return NotificationChannelGroup(groupId, groupName)
    }

    /**
     * 检查 channelBean 参数是否合法
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun checkChannelParam(channelBean: NotificationChannel): Boolean {
        if (channelBean.id.isEmpty() || channelBean.name.isEmpty()) {
            log("渠道(channelId:${channelBean.id})创建失败：参数错误")
            return false
        }
        if (channelBean.importance != NotificationManagerCompat.IMPORTANCE_NONE
            && channelBean.importance != NotificationManagerCompat.IMPORTANCE_MIN
            && channelBean.importance != NotificationManagerCompat.IMPORTANCE_LOW
            && channelBean.importance != NotificationManagerCompat.IMPORTANCE_DEFAULT
            && channelBean.importance != NotificationManagerCompat.IMPORTANCE_HIGH
            && channelBean.importance != NotificationManagerCompat.IMPORTANCE_MAX
        ) {
            log("渠道(channelId:${channelBean.id})创建失败：importance参数错误")
            return false
        }
        return true
    }

    /**
     * 渠道是否已经创建
     *
     * @return true 已创建 false 未创建
     */
    private fun isChannelAlreadyCreated(context: Context, channelId: String): Boolean {
        var mChannel: NotificationChannel? = null
        try {
            // 获取通知渠道
            mChannel = NotificationManagerCompat.from(context).getNotificationChannel(channelId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return mChannel != null
    }

    /**
     * 渠道组是否创建
     */
    private fun isChannelGroupAlreadyCreated(context: Context, groupId: String): Boolean {
        var mChannelGroup: NotificationChannelGroup? = null
        try {
            mChannelGroup = NotificationManagerCompat.from(context).getNotificationChannelGroup(groupId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return mChannelGroup != null
    }

    /**
     * 指定渠道是否打开
     *
     * @param isIntentSetting: true 跳转到通知设置页
     */
    @TargetApi(Build.VERSION_CODES.O)
    fun isChannelsEnabled(
        context: Context,
        channelId: String,
        isIntentSetting: Boolean
    ): Boolean {
        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = manager.getNotificationChannel(channelId)
            if (channel != null && channel.importance == NotificationManager.IMPORTANCE_NONE) {
                if (isIntentSetting) {
                    val intent =
                        Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.putExtra(
                        Settings.EXTRA_APP_PACKAGE,
                        context.packageName
                    )
                    intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel.id)
                    context.startActivity(intent)
                }
                return false
            }
        }
        return true
    }

    /**
     * 通知权限是否打开
     */
    fun areNotificationsEnabled(context: Context): Boolean {
        return NotificationManagerCompat.from(context).areNotificationsEnabled()
    }

    /**
     * 打开通知设置
     *
     * 需要适配不同版本
     */
    fun openSetting(context: Context) {
        val intent = Intent()
        when {
            Build.VERSION.SDK_INT >= 26 -> {
                // android 8.0引导
                intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                intent.putExtra("android.provider.extra.APP_PACKAGE", context.packageName)
            }
            Build.VERSION.SDK_INT >= 21 -> {
                // android 5.0-7.0
                intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                intent.putExtra("app_package", context.packageName)
                intent.putExtra("app_uid", context.applicationInfo.uid)
            }
            else -> {
                // 其他
                intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                intent.data = Uri.fromParts("package", context.packageName, null)
            }
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun log(content: String) {
        if (isShowLog)
            Log.e(TAG, content)
    }
}