/**
 * Copyright © 2020, The Koala Computer Company Limited.
 * All Rights Reserved
 */
package ie.koala.lewis.xml

import ie.koala.lewis.model.Direction
import ie.koala.lewis.model.StopInfo
import ie.koala.lewis.model.Tram
import ie.koala.lewis.util.Debug
import org.xml.sax.Attributes
import org.xml.sax.InputSource
import org.xml.sax.SAXException
import org.xml.sax.helpers.DefaultHandler
import java.io.IOException
import java.io.StringReader
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.xml.parsers.ParserConfigurationException
import javax.xml.parsers.SAXParserFactory

//<?xml version="1.0" encoding="UTF-8"?>
//<stopInfo created="2020-10-25T17:13:04" stop="Marlborough" stopAbv="MAR">
//   <message>Green Line services operating normally</message>
//   <direction name="Inbound">
//      <tram destination="No Northbound Service" dueMins="" />
//   </direction>
//   <direction name="Outbound">
//      <tram dueMins="6" destination="Bride's Glen" />
//      <tram dueMins="18" destination="Bride's Glen" />
//   </direction>
//</stopInfo>

//<?xml version="1.0" encoding="UTF-8"?>
//<stopInfo created="2020-10-25T17:07:09" stop="Stillorgan" stopAbv="STI">
//   <message>Green Line services operating normally</message>
//   <direction name="Inbound">
//      <tram dueMins="7" destination="Broombridge" />
//      <tram dueMins="18" destination="Broombridge" />
//   </direction>
//   <direction name="Outbound">
//      <tram dueMins="2" destination="Bride's Glen" />
//      <tram dueMins="17" destination="Bride's Glen" />
//   </direction>
//</stopInfo>

object ParseXml {

    fun parseDoc(xml: String?): StopInfo {
        try {
            val parser = XmlParser(StopInfo())
            return parser.parseDocument(xml)
        } catch (error: IOException) {
            Debug.exception("init io exception ", error)
        }
        return StopInfo()
    }

    class XmlParser(var stopInfo: StopInfo) : DefaultHandler() {
        private var s: String = ""
        var direction: Direction = Direction()
        var tram: Tram = Tram()

        fun parseDocument(xml: String?): StopInfo {
            val spf = SAXParserFactory.newInstance()
            try {
                val sp = spf.newSAXParser()
                sp.parse(InputSource(StringReader(xml)), this);
            } catch (se: SAXException) {
                se.printStackTrace()
            } catch (pce: ParserConfigurationException) {
                pce.printStackTrace()
            } catch (ie: IOException) {
                ie.printStackTrace()
            }
            return stopInfo
        }

        @Throws(SAXException::class)
        override fun startElement(
            uri: String,
            localName: String,
            qName: String,
            attributes: Attributes
        ) {
            s = ""

            when (NavigationTag.findByText(qName)) {
                NavigationTag.STOP_INFO -> {
                    stopInfo = StopInfo();

                    // see https://docs.oracle.com/javase/8/docs/api/index.html?java/time/format/DateTimeFormatter.html
                    val formatter =
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'H:mm:ss", Locale.ENGLISH)
                    stopInfo.created =
                        LocalDateTime.parse(attributes.getValue("created"), formatter)

                    stopInfo.stop = attributes.getValue("stop")
                    stopInfo.stopAbv = attributes.getValue("stopAbv")
                }
                NavigationTag.DIRECTION -> {
                    direction = Direction()
                    direction.name = attributes.getValue("name")
                }
                NavigationTag.TRAM -> {
                    tram = Tram()
                    tram.destination = attributes.getValue("destination")
                    val dueMins = attributes.getValue("dueMins")
                    tram.dueMins = attributes.getValue("dueMins")
                }
                else -> {
                    // nothing to do at the start of this tag
                }
            }
        }

        @Throws(SAXException::class)
        override fun endElement(uri: String, localName: String, qName: String) {
            // pop stack and add to 'parent' element, which is next on the stack
            // important to pop stack first, then peek at top element!
            when (NavigationTag.findByText(qName)) {
                NavigationTag.DIRECTION -> {
                    stopInfo.direction.add(direction)
                }
                NavigationTag.TRAM -> {
                    direction.tram.add(tram)
                }
                NavigationTag.MESSAGE -> {
                    stopInfo.message = s
                }
                else -> {
                    // nothing to do at the end of this tag
                }
            }
        }

        @Throws(SAXException::class)
        override fun characters(ch: CharArray, start: Int, length: Int) {
            s = String(ch, start, length)
        }

    }

    enum class NavigationTag(val text: String) {
        STOP_INFO("stopInfo"),
        CREATED("created"),
        STOP("stop"),
        STOP_ABV("stopAbv"),
        MESSAGE("message"),
        DIRECTION("direction"),
        NAME("name"),
        TRAM("tram"),
        DUE_MINS("dueMins"),
        DESTINATION("destination"),
        UNKNOWN("Unknown");

        companion object {
            fun findByText(text: String): NavigationTag {
                for (navigationTag in values()) {
                    if (text.equals(navigationTag.text, ignoreCase = true)) {
                        return navigationTag
                    }
                }
                return UNKNOWN
            }
        }

        override fun toString(): String {
            return text
        }
    }
}