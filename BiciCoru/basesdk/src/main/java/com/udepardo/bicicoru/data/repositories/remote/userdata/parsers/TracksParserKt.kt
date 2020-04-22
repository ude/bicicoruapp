package com.udepardo.bicicoru.data.repositories.remote.userdata.parsers

import com.udepardo.bicicoru.data.model.db.UserTrackKt
import org.jsoup.nodes.Element
import java.text.SimpleDateFormat
import java.util.*

class TracksParserKt(html: String) : BaseParserKt(html) {
    private val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
    private val tracks = mutableListOf<UserTrackKt>()

    override fun updateForm(newHtml: String) {
        tracks.clear()
        super.updateForm(newHtml)
    }

    fun parse(): List<UserTrackKt> {
        tracks.run{
            addAll(getAllElementsByClass("gridRowStyle").map { parseTrack(it) })
            addAll(getAllElementsByClass("gridAlternatingStyle").map { parseTrack(it) })
        }

        return tracks

    }

    fun parseTrack(element: Element) = UserTrackKt(
        simpleDateFormat.parse(element.getElementsByAttributeValue("align", "center")[2].text()).time,
        simpleDateFormat.parse(element.getElementsByAttributeValue("align", "center")[3].text()).time,
        element.getElementsByAttributeValue("align", "center")[0].text(),
        element.getElementsByAttributeValue("align", "center")[1].text()
    )

    fun getTotalCount () = Integer.parseInt(getString("ctl00_ContentPlaceHolderLeft_DataGridGenerico1_lTotalPaginas"))

}