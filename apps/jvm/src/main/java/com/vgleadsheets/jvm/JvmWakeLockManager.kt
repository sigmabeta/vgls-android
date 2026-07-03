package com.vgleadsheets.jvm

import com.vgleadsheets.wakelocks.WakeLockManager

/**
 * Desktop no-op. The android WakeLockManagerImpl toggles FLAG_KEEP_SCREEN_ON on the Activity window;
 * there's no equivalent (or need) on the desktop, so keep-screen-on requests are ignored.
 */
class JvmWakeLockManager : WakeLockManager {
    override fun keepScreenOn() = Unit

    override fun allowScreenOff() = Unit
}
