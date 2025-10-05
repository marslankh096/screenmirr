package  com.sid.testscreenmirr.webcast


import android.content.Context

import androidx.multidex.MultiDexApplication
import com.elvishew.xlog.flattener.ClassicFlattener
import com.elvishew.xlog.printer.file.FilePrinter
import com.elvishew.xlog.printer.file.clean.FileLastModifiedCleanStrategy
import  com.sid.testscreenmirr.webcast.di.baseKoinModule
import  com.sid.testscreenmirr.webcast.logging.DateSuffixFileNameGenerator
import  com.sid.testscreenmirr.webcast.logging.getLogFolder
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import java.util.*

abstract class BaseApp : MultiDexApplication() {

    protected val filePrinter: FilePrinter by lazy {
        FilePrinter.Builder(getLogFolder())
            .fileNameGenerator(DateSuffixFileNameGenerator(this@BaseApp.hashCode().toString()))
            .cleanStrategy(FileLastModifiedCleanStrategy(86400000)) // One day
            .flattener(ClassicFlattener())
            .build()
    }

    val lastAdLoadTimeMap: MutableMap<String, Long> = mutableMapOf()

  /*  override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(ContextWrapper(base!!.setAppLocale("fr")))
    }*/

    abstract fun initLogger()

    override fun onCreate() {
        super.onCreate()
        SharedPrefer.init(applicationContext)
        val lang: String = SharedPrefer.read("lang", "hi")
//        setLocales(lang)
        initLogger()
        appClass = this
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@BaseApp)
            modules(baseKoinModule)
        }


//        FirebaseApp.initializeApp(this)


    }


    fun Context.setAppLocale(language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        return createConfigurationContext(config)
    }

    open fun setLocales(lang: String?) {
        val myLocale = Locale(lang)
        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.locale = myLocale
        res.updateConfiguration(conf, dm)
    }

    internal val sharedPreferences by lazy(LazyThreadSafetyMode.NONE) {
        getSharedPreferences("logging.xml", MODE_PRIVATE)
    }

    internal var isLoggingOn: Boolean
        get() = sharedPreferences.getBoolean(LOGGING_ON_KEY, false)
        set(value) {
            sharedPreferences.edit().putBoolean(LOGGING_ON_KEY, value).commit()
        }

    internal companion object {
        const val LOGGING_ON_KEY = "loggingOn"

        var appClass: BaseApp? = null



        fun getAppClassIns(): BaseApp? {
            return appClass
        }
    }
}