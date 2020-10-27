/**
 * Copyright © 2020, The Koala Computer Company Limited.
 * All Rights Reserved
 */
package ie.koala.lewis.model

class Tram {
    var destination: String? = null
    var dueMins: String? = null     // turns out that one possible value is "DUE"

    override fun toString(): String {
        return "Tram [destination = $destination, dueMins = $dueMins]"
    }
}