/**
 * Copyright © 2020, The Koala Computer Company Limited.
 * All Rights Reserved
 */
package ie.koala.lewis

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import ie.koala.lewis.adapter.StopInfoAdapter
import ie.koala.lewis.model.Stop
import ie.koala.lewis.model.StopInfo
import ie.koala.lewis.network.LuasForecastingApiWebService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalTime


class MainActivity : AppCompatActivity(), CoroutineScope by CoroutineScope(Dispatchers.Main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        displayForecast()

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Refreshing...", Snackbar.LENGTH_SHORT).show()
            displayForecast()
        }

    }

    fun directionToDisplay(): String {
        if (LocalTime.now().isAfter(LocalTime.of(12, 0))) {
            // When I open the app from 12:01 – 23:59
            // Then I should see trams forecast from Stillorgan LUAS stop towards Inbound
            return "Inbound"
        } else {
            // When I open the app from 00:00 – 12:00
            // Then I should see trams forecast from Marlborough LUAS stop towards Outbound
            return "Outbound"
        }
    }

    fun titleTodisplay(dir: String, stopCode: String): String {
        val stop: Stop = Stop.findByShortName(stopCode)
        if (dir == "Outbound") {
            return resources.getString(R.string.outbound_list_title, stop.longName)
        } else {
            return resources.getString(R.string.inbound_list_title, stop.longName)
        }
    }

    fun displayForecast() {
        val dir: String = directionToDisplay()
        val stop: String

        if (dir == "Outbound") {
            stop = LuasForecastingApiWebService.MARLBOROUGH
        } else {
            stop = LuasForecastingApiWebService.STILLORGAN
        }

        launch {
            val stopInfo: StopInfo = LuasForecastingApiWebService.getStopInfoAsync(stop).await()

            headerTextView.setText(titleTodisplay(dir, stop))
            if (stopInfo.created != null) {
                messageTextView.setTextColor(headerTextView.getTextColors())
                messageTextView.setText(stopInfo.message)
            } else {
                messageTextView.setTextColor(Color.RED)
                messageTextView.setText(getString(R.string.no_realtime_info))
            }
            cardsRecyclerView.setLayoutManager(LinearLayoutManager(this@MainActivity))
            cardsRecyclerView.setAdapter(StopInfoAdapter(stopInfo.findDirection(dir)))
        }
    }
}
