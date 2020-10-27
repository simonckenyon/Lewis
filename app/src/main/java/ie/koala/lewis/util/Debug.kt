/**
 * Copyright © 2020, The Koala Computer Company Ltd.
 * All Rights Reserved
 */
package ie.koala.lewis.util

import org.slf4j.LoggerFactory

/**
 * see https://stackoverflow.com/a/43080363
 */
object Debug {
    private val log = LoggerFactory.getLogger(Debug::class.java)

    private const val VERSION_NAME_KEY = "VERSION_NAME_KEY"
    private const val VERSION_CODE_KEY = "VERSION_CODE_KEY"
    private const val BUILD_TIME_KEY = "BUILD_TIME_KEY"
    private const val GIT_HASH_KEY = "GIT_HASH_KEY"

    private const val index = 4 // <== Index in call stack array
    private val methodNames = arrayOf("debug", "info", "warning", "error", "exception") // <== Name of method for public call

    private val callerName: String?
        get() {
            var caller = "NONE"
            val stacktrace = Thread.currentThread().stackTrace
            if (stacktrace.size >= index) {
                caller = stacktrace[index].methodName
            }
            return caller
        }

    private val tag: String?
        get() {
            var tag = "NONE"
            val stacktrace = Thread.currentThread().stackTrace
            for (i in stacktrace.indices) {
                if (stacktrace[i].methodName in methodNames) {
                    tag = "(${stacktrace[i + 1].fileName}:${stacktrace[i + 1].lineNumber}):${stacktrace[index].methodName}:"
                    return tag
                }
            }
            return tag
        }

    @kotlin.jvm.JvmStatic
    fun debug(s: String) {
        log.debug("$tag $s")
    }

    @kotlin.jvm.JvmStatic
    fun info(s: String) {
        log.info("$tag $s")
    }

    @kotlin.jvm.JvmStatic
    fun warning(s: String) {
        log.warn("$tag $s")
    }

    @kotlin.jvm.JvmStatic
    fun error(s: String) {
        log.error("$tag $s")
    }

    @kotlin.jvm.JvmStatic
    fun exception(s: String, e: Exception) {
        log.error("$tag $s: exception ${e.message}")
    }

    @kotlin.jvm.JvmStatic
    fun exceptionWithStacktrace(s: String, e: Exception) {
        log.error("$tag $s: exception ", e)
    }
}