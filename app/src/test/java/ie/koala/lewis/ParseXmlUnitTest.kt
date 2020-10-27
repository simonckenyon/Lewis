/**
 * Copyright © 2020, The Koala Computer Company Limited.
 * All Rights Reserved
 */
package ie.koala.lewis

import ie.koala.lewis.xml.ParseXml
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Test the parser
 */
class ParseXmlUnitTest {
    val xml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <stopInfo created="2020-10-25T17:13:04" stop="Marlborough" stopAbv="MAR">
               <message>Green Line services operating normally</message>
               <direction name="Inbound">
                  <tram destination="No Northbound Service" dueMins="" />
               </direction>
               <direction name="Outbound">
                  <tram dueMins="6" destination="Bride's Glen" />
                  <tram dueMins="18" destination="Bride's Glen" />
               </direction>
            </stopInfo>
        """.trimIndent()

    @Test
    fun created_isCorrect() {
        val stopInfo = ParseXml.parseDoc(xml)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'H:mm:ss", Locale.ENGLISH)
        assertEquals(stopInfo.created?.format(formatter), "2020-10-25T17:13:04")
    }

    @Test
    fun stop_isCorrect() {
        val stopInfo = ParseXml.parseDoc(xml)
        assertEquals(stopInfo.stop, "Marlborough")
    }

    @Test
    fun stopAbv_isCorrect() {
        val stopInfo = ParseXml.parseDoc(xml)
        assertEquals(stopInfo.stopAbv, "MAR")
    }

    @Test
    fun message_isCorrect() {
        val stopInfo = ParseXml.parseDoc(xml)
        assertEquals(stopInfo.message, "Green Line services operating normally")
    }

    @Test
    fun directions_length_isCorrect() {
        val stopInfo = ParseXml.parseDoc(xml)
        assertEquals(stopInfo.direction.size, 2)
    }

    @Test
    fun directions_inbound_isCorrect() {
        val stopInfo = ParseXml.parseDoc(xml)
        assertEquals(stopInfo.direction[0].name, "Inbound")
    }

    @Test
    fun directions_inbound_tram_isCorrect() {
        val stopInfo = ParseXml.parseDoc(xml)
        assertEquals(stopInfo.direction[0].tram.size, 1)
    }

    @Test
    fun directions_inbound_tram_destination_isCorrect() {
        val stopInfo = ParseXml.parseDoc(xml)
        assertEquals(stopInfo.direction[0].tram[0].destination, "No Northbound Service")
    }

    @Test
    fun directions_inbound_tram_dueMins_isCorrect() {
        val stopInfo = ParseXml.parseDoc(xml)
        assertEquals(stopInfo.direction[0].tram[0].dueMins, "")
    }

    @Test
    fun directions_outbound_isCorrect() {
        val stopInfo = ParseXml.parseDoc(xml)
        assertEquals(stopInfo.direction[1].name, "Outbound")
    }

    @Test
    fun directions_outbound_tram_isCorrect() {
        val stopInfo = ParseXml.parseDoc(xml)
        assertEquals(stopInfo.direction[1].tram.size, 2)
    }

    @Test
    fun directions_outbound_tram_first_destination_isCorrect() {
        val stopInfo = ParseXml.parseDoc(xml)
        assertEquals(stopInfo.direction[1].tram[0].destination, "Bride's Glen")
    }

    @Test
    fun directions_outbound_tram_first_dueMins_isCorrect() {
        val stopInfo = ParseXml.parseDoc(xml)
        assertEquals(stopInfo.direction[1].tram[0].dueMins, "6")
    }

    @Test
    fun directions_outbound_tram_second_destination_isCorrect() {
        val stopInfo = ParseXml.parseDoc(xml)
        assertEquals(stopInfo.direction[1].tram[1].destination, "Bride's Glen")
    }

    @Test
    fun directions_outbound_tram_second_dueMins_isCorrect() {
        val stopInfo = ParseXml.parseDoc(xml)
        assertEquals(stopInfo.direction[1].tram[1].dueMins, "18")
    }
}