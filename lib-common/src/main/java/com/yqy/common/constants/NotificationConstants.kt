package com.zs.common.constants

/**
 * @author derekyan
 * @desc 通知常量
 * @date 2020/4/22
 */
object NotificationConstants {
    // 通知渠道-聊天消息
    var NOTIFICATION_CHANNEL_ID_CHAT_MESSAGE = "channel_chat_message"
    // 通知渠道-系统通知
    var NOTIFICATION_CHANNEL_ID_SYSTEM_NOTICE = "channel_system_notice"
    var NOTIFICATION_CHANNEL_ID_SYSTEM = "channel_system"

    // 通知渠道-名称 在设置中显示
    var NOTIFICATION_CHANNEL_NAME_CHAT_MESSAGE_NAME = "聊天消息"
    var NOTIFICATION_CHANNEL_NAME_SYSTEM_NOTICE_NAME = "系统通知"
    var NOTIFICATION_CHANNEL_NAME_SYSTEM_NAME = "系统"

    var NOTIFICATION_ID_FLOAT_WINDOW = 1001 // 悬浮窗
    var NOTIFICATION_ID_CALLING = "1002" // 通话中
}