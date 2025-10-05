package  com.sid.testscreenmirr.webcast.receiver


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.elvishew.xlog.XLog
import  com.sid.testscreenmirr.webcast.info.dvkr.screenstream.data.other.getLog
import  com.sid.testscreenmirr.webcast.info.dvkr.screenstream.data.settings.SettingsReadOnly
import  com.sid.testscreenmirr.webcast.service.helper.IntentAction
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BootReceiver : BroadcastReceiver(), KoinComponent {

    private val settingsReadOnly: SettingsReadOnly by inject()

    override fun onReceive(context: Context, intent: Intent) {
        XLog.d(getLog("onReceive", "Invoked"))

        if (runBlocking { settingsReadOnly.startOnBootFlow.first().not() }) Runtime.getRuntime().exit(0)

        if (
            intent.action == "android.intent.action.BOOT_COMPLETED" ||
            intent.action == "android.intent.action.QUICKBOOT_POWERON"
        ) {
            IntentAction.StartOnBoot.sendToAppService(context)
        }
    }
}