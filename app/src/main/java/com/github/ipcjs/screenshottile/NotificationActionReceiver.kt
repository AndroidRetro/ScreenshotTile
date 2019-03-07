package com.github.ipcjs.screenshottile

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.widget.Toast
import com.github.ipcjs.screenshottile.Utils.p
import java.io.File


const val NOTIFICATION_ACTION_SHARE = "NOTIFICATION_ACTION_SHARE"
const val NOTIFICATION_ACTION_DELETE = "NOTIFICATION_ACTION_DELETE"
const val NOTIFICATION_ACTION_DATA_URI = "NOTIFICATION_ACTION_DATA_URI"
const val NOTIFICATION_ACTION_ID = "NOTIFICATION_ACTION_ID"

class NotificationActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.apply {
            when (intent?.action) {
                NOTIFICATION_ACTION_SHARE -> {
                    p("NotificationActionReceiver: ${intent.action}")

                    hideNotification(this, intent.getIntExtra(NOTIFICATION_ACTION_ID, 0))

                    val path = Uri.parse(intent.getStringExtra(NOTIFICATION_ACTION_DATA_URI))

                    val shareIntent = shareImageChooserIntent(this, path)
                    shareIntent.addFlags(FLAG_ACTIVITY_NEW_TASK)

                    if(ScreenshotTileService.instance != null) {
                        ScreenshotTileService.instance?.startActivityAndCollapse(shareIntent)
                    } else {
                        startActivity(shareIntent)
                    }
                }
                NOTIFICATION_ACTION_DELETE -> {
                    p("NotificationActionReceiver: ${intent.action}")

                    hideNotification(this, intent.getIntExtra(NOTIFICATION_ACTION_ID, 0))

                    val path = Uri.parse(intent.getStringExtra(NOTIFICATION_ACTION_DATA_URI))

                    if (deleteImage(this, File(path.path))) {
                        Toast.makeText(this, context.getString(R.string.screenshot_deleted), Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, context.getString(R.string.screenshot_delete_failed), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}