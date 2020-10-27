/**
 * Copyright © 2020, The Koala Computer Company Limited.
 * All Rights Reserved
 */
package ie.koala.lewis.model

import java.time.LocalDateTime

class StopInfo {
    var stop: String? = null
    var created: LocalDateTime? = null
    var message: String? = null
    var stopAbv: String? = null
    var direction: MutableList<Direction> = mutableListOf()

    fun findDirection(dirStr: String): Direction {
        if (direction.size > 0) {
            for (dir: Direction in direction) {
                if (dir.name == dirStr) {
                    return dir
                }
            }
            // display first
            return direction[0]
        } else {
            return Direction()
        }
    }

    override fun toString(): String {
        return "StopInfo [stop = $stop, created = $created, message = $message, stopAbv = $stopAbv, direction = $direction]"
    }
}