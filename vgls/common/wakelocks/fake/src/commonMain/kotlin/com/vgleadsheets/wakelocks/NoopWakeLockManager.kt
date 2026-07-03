package com.vgleadsheets.wakelocks

/**
 * No-op WakeLockManager for platforms with no screen-wake concept (e.g. the desktop app). The android
 * impl toggles FLAG_KEEP_SCREEN_ON on the Activity window; here keep-screen-on requests are ignored.
 */
class NoopWakeLockManager : WakeLockManager {
    override fun keepScreenOn() = Unit

    override fun allowScreenOff() = Unit
}
