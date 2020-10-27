/**
 * Copyright © 2020, The Koala Computer Company Limited.
 * All Rights Reserved
 */
package ie.koala.lewis.network

import ie.koala.lewis.util.Debug
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class LoggingInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val t1 = System.nanoTime()
        Debug.debug(String.format("Sending request %s", request.url()))
        val response = chain.proceed(request)
        val t2 = System.nanoTime()
        Debug.debug(String.format("Received response for %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6, response.headers()))
        return response
    }
}