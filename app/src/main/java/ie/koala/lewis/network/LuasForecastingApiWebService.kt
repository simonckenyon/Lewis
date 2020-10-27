/**
 * Copyright © 2020, The Koala Computer Company Limited.
 * All Rights Reserved
 */
package ie.koala.lewis.network

import ie.koala.lewis.model.StopInfo
import ie.koala.lewis.util.Debug
import ie.koala.lewis.xml.ParseXml
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

object LuasForecastingApiWebService : CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private val URL = "http://luasforecasts.rpa.ie/xml/get.ashx?action=forecast&encrypt=false&stop="

    val MARLBOROUGH = "mar"
    val STILLORGAN = "sti"

    /**
     * Use OkHttp3 to make a request to the server and parse the result.
     *
     * See https://square.github.io/okhttp/
     */
    fun getStopInfoAsync(stop: String): Deferred<StopInfo> {
        return async(Dispatchers.IO) {
            var stopInfo = StopInfo()
            val url = URL + stop
            val request = Request
                .Builder()
                .url(url)
                .get()
                .build()
            val client: OkHttpClient? = OkHttpClient
                .Builder()
                //.addInterceptor(LoggingInterceptor())     // print stats on web service call
                .build()
            if (client != null) {
                try {
                    client
                        .newCall(request)
                        .execute()
                        .use { response ->
                            try {
                                if (!response.isSuccessful) {
                                    Debug.error("got an error talking to the server")
                                    throw IOException("Unexpected response from server \"$response\"")
                                }
                                stopInfo = ParseXml.parseDoc(response.body()?.string())
                            } catch (error: Exception) {
                                Debug.exception(
                                    "exception processing response ",
                                    error
                                )
                            }
                        }
                } catch (error: Exception) {
                    Debug.exception("exception ", error)
                }
            } else {
                Debug.error("http client is null")
            }
            stopInfo
        }
    }
}