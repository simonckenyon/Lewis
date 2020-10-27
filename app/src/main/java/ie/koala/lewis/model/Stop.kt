/**
 * Copyright © 2020, The Koala Computer Company Limited.
 * All Rights Reserved
 */
package ie.koala.lewis.model


enum class Stop(val shortName: String, val longName: String) {
    MARLBOROUGH("mar", "Marlborough"),
    STILLORGAN("sti", "Stillorgan"),
    UNKNOWN("unk", "unknown");

    companion object {
        fun findByShortName(text: String): Stop {
            for (stop in values()) {
                if (text.equals(stop.shortName, ignoreCase = true)) {
                    return stop
                }
            }
            return UNKNOWN
        }
    }
}