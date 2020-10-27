/**
 * Copyright © 2020, The Koala Computer Company Limited.
 * All Rights Reserved
 */
package ie.koala.lewis.model

class Direction {
    var name: String? = null
    var tram: MutableList<Tram> = mutableListOf()

    override fun toString(): String {
        return "Direction [name = $name, tram = $tram]"
    }
}