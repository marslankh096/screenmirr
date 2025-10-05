package  com.sid.testscreenmirr.webcast.service

import  com.sid.testscreenmirr.webcast.info.dvkr.screenstream.data.model.AppError
import  com.sid.testscreenmirr.webcast.data.model.HttpClient
import  com.sid.testscreenmirr.webcast.info.dvkr.screenstream.data.model.NetInterface
import  com.sid.testscreenmirr.webcast.info.dvkr.screenstream.data.model.TrafficPoint

sealed class ServiceMessage {
    object FinishActivity : ServiceMessage()

    data class ServiceState(
        val isStreaming: Boolean, val isBusy: Boolean, val isWaitingForPermission: Boolean,
        val netInterfaces: List<NetInterface>,
        val appError: AppError?
    ) : ServiceMessage()

    data class Clients(val clients: List<HttpClient>) : ServiceMessage()
    data class TrafficHistory(val trafficHistory: List<TrafficPoint>) : ServiceMessage() {
        override fun toString(): String = javaClass.simpleName
    }

    override fun toString(): String = javaClass.simpleName
}