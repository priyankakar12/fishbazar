package com.pk.fishmarket.Utils

import android.app.Activity

import android.content.ContentResolver

import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.pk.fishmarket.R




fun Activity.nextFragment(id: Int, bundle: Bundle? = null) {
    Navigation.findNavController(this, R.id.nav_host_fragment_activity_main).navigate(id, bundle)
}

fun Activity.popFragment() {
    Navigation.findNavController(this, R.id.nav_host_fragment_activity_main).popBackStack()
}

fun Fragment.showToast(message: String?) {
    if (message != null) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }
}


fun Activity.showToast(message: String?) {
    if (message != null) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

fun Activity.isNotificationServiceRunning(): Boolean {
    val contentResolver: ContentResolver = this.contentResolver
    val enabledNotificationListeners: String =
        Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
    val packageName: String = this.packageName
    return enabledNotificationListeners.contains(packageName)
}