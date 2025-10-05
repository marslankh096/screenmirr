package  com.sid.testscreenmirr.webcast.ui.activity

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.core.content.ContextCompat
import com.elvishew.xlog.XLog
import  com.sid.testscreenmirr.webcast.BaseApp
import com.sid.testscreenmirr.R
import com.sid.testscreenmirr.databinding.ActivityAppBinding
import  com.sid.testscreenmirr.webcast.info.dvkr.screenstream.data.other.getLog
import  com.sid.testscreenmirr.webcast.service.ServiceMessage
import  com.sid.testscreenmirr.webcast.service.helper.IntentAction
import  com.sid.testscreenmirr.webcast.ui.viewBinding

class AppActivity : PermissionActivity(R.layout.activity_app) {

    companion object {
        fun getAppActivityIntent(context: Context): Intent =
            Intent(context.applicationContext, AppActivity::class.java)

        fun getStartIntent(context: Context): Intent =
            getAppActivityIntent(context)
    }


    private val binding by viewBinding { activity -> ActivityAppBinding.bind(activity.findViewById(R.id.container))
    }

    private val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0.5f, 1f)
    private val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.5f, 1f)
    private val alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f)
    private var lastServiceMessage: ServiceMessage.ServiceState? = null

    private val prefListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
//        if (key == BaseApp.LOGGING_ON_KEY) setLogging()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setLogging()

        routeIntentAction(intent)
//        (application as BaseApp).sharedPreferences.registerOnSharedPreferenceChangeListener(prefListener)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
    }
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        routeIntentAction(intent)
    }
    override fun onDestroy() {
        (application as BaseApp).sharedPreferences.unregisterOnSharedPreferenceChangeListener(prefListener)
        super.onDestroy()
    }
    private fun routeIntentAction(intent: Intent?) {
        val intentAction = IntentAction.fromIntent(intent)
        intentAction != null || return
        XLog.d(getLog("routeIntentAction", "IntentAction: $intentAction"))
        if (intentAction is IntentAction.StartStream) {
            IntentAction.StartStream.sendToAppService(this)
        }
    }

    @SuppressLint("CheckResult")
//    private fun setLogging() {
//        val loggingOn = (application as BaseApp).isLoggingOn
//        binding.llActivityAppLogs.visibility = if (loggingOn) View.VISIBLE else View.GONE
//        binding.vActivityAppLogs.visibility = if (loggingOn) View.VISIBLE else View.GONE
//        if (loggingOn)
//            binding.bActivityAppSendLogs.setOnClickListener {
//
//            }
//        else
//            binding.bActivityAppSendLogs.setOnClickListener(null)
//    }


    override fun onServiceMessage(serviceMessage: ServiceMessage) {
        super.onServiceMessage(serviceMessage)

        if (serviceMessage is ServiceMessage.ServiceState) {
            lastServiceMessage != serviceMessage || return
            XLog.d(this@AppActivity.getLog("onServiceMessage", "$serviceMessage"))

            binding.title.text =
                if (serviceMessage.isStreaming) getString(R.string.bottom_menu_stop)
                else getString(R.string.bottom_menu_start)

            with(binding.fabActivityAppStartStop) {
                visibility = View.VISIBLE

                isEnabled = serviceMessage.isBusy.not()

                backgroundTintList = if (serviceMessage.isBusy) {
                    ContextCompat.getColorStateList(this@AppActivity, R.color.grey)
                } else {
                    ContextCompat.getColorStateList(this@AppActivity, R.color.blue)
                }

                contentDescription = if (serviceMessage.isStreaming) {
//                    setImageResource(R.drawable.ic_previous_play)
                    setOnClickListener { IntentAction.StopStream.sendToAppService(this@AppActivity) }
                    getString(R.string.bottom_menu_stop)
                } else {
//                    setImageResource(R.drawable.ic_play_button)
                    setOnClickListener { IntentAction.StartStream.sendToAppService(this@AppActivity) }
                    getString(R.string.bottom_menu_start)
                }
            }
            binding.backBtn.setOnClickListener {
                finish()
            }

            if (serviceMessage.isStreaming != lastServiceMessage?.isStreaming) {
                ObjectAnimator.ofPropertyValuesHolder(binding.fabActivityAppStartStop, scaleX, scaleY, alpha)
                    .apply { interpolator = OvershootInterpolator(); duration = 750 }
                    .start()
            }

            lastServiceMessage = serviceMessage
        }
    }
}