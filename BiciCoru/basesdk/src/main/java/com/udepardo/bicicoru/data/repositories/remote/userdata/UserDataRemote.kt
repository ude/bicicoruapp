package com.udepardo.bicicoru.data.repositories.remote.userdata

import android.content.SharedPreferences
import android.util.Log
import com.udepardo.bicicoru.data.model.db.GraphCoord
import com.udepardo.bicicoru.data.model.db.UserProfileKt
import com.udepardo.bicicoru.data.model.db.UserTrackKt
import com.udepardo.bicicoru.data.repositories.ProfileDatasource
import com.udepardo.bicicoru.data.repositories.remote.userdata.parsers.ProfileParserKt
import com.udepardo.bicicoru.data.repositories.remote.userdata.parsers.TracksParserKt
import com.udepardo.bicicoru.data.repositories.remote.userdata.parsers.UserStatsParserKt
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.security.auth.login.LoginException

class UserDataRemote (val prefs: SharedPreferences, val client: OkHttpClient) : ProfileDatasource{
    private val baseUrl = "https://www.bicicoruna.es"
    private val defaultUrl = "$baseUrl/Default.aspx"
    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())


    @Throws(IOException::class, LoginException::class)
    override fun getUserProfile(): UserProfileKt {
        val profileRequest = Request.Builder()
                .url("$baseUrl/User/Profile.aspx")
                .build()

        client.newCall(profileRequest).execute().run {
            this.body()?.let {
                return (when (this.code()) {
                    200 -> ProfileParserKt(it.string()).parseProfile()
                    302 -> {
                        getLoggedClient().newCall(profileRequest).execute().body()?.let {
                            ProfileParserKt(it.string()).parseProfile()
                        } ?: throw IOException("Server error")

                    }
                    else -> throw IOException("Server error")
                })

            } ?: throw IOException()

        }

    }

    @Throws(IOException::class)
    override fun getUserStats(): List<GraphCoord> {
        val profileRequest = Request.Builder()
                .url("$baseUrl/User/ofc_handler.aspx?chartjson=ctl00_ContentPlaceHolderLeft_OFCUsoSistema&ec=0")
                .build()

        client.newCall(profileRequest).execute().run {
            this.body()?.let {
                return (when (this.code()) {
                    200 -> UserStatsParserKt(it.string()).parse()
                    302 -> {
                        getLoggedClient().newCall(profileRequest).execute().body()?.let {
                            UserStatsParserKt(it.string()).parse()
                        } ?: throw IOException("Server error")

                    }
                    else -> throw IOException("Server error")
                })
            } ?: throw IOException()

        }
    }

    override fun getUserTracks(beginDate: Long, endDate: Long): MutableList<UserTrackKt> {
        val tracksRequest = Request.Builder().url("$baseUrl/User/Tracks.aspx").build()

        var responseBody = client.newCall(tracksRequest).execute()

        when (responseBody.code()) {
            200 -> Log.d("UDE", "All right!")
            302 -> responseBody = getLoggedClient().newCall(tracksRequest).execute()
            else -> throw IOException("Server error")
        }

        val form = Jsoup.parseBodyFragment(responseBody.body()?.string().let {
            it
        } ?: throw IOException("Server error"))

        var validation = form.body().getElementById("__EVENTVALIDATION").attr("value")
        var viewState = form.body().getElementById("__VIEWSTATE").attr("value")
        var viewStateGenerator = form.body().getElementById("__VIEWSTATEGENERATOR").`val`()

        val formBody = FormBody.Builder()
                .add("ctl00\$ContentPlaceHolderLeft\$TextBox1", dateFormatter.format(beginDate))
                .add("ctl00\$ContentPlaceHolderLeft\$TextBox2", dateFormatter.format(endDate))

                .add("ctl00\$ContentPlaceHolderLeft\$Button1", "Filtrar")
                .add("ctl00\$ContentPlaceHolderLeft\$DataGridGenerico1\$filterField", "EstacionOrigen")
                .add("ctl00\$ContentPlaceHolderLeft\$DataGridGenerico1\$txtSearch", "")

                .add("__LASTFOCUS", "")
                .add("__EVENTTARGET", "")
                .add("__EVENTARGUMENT", "")
                .add("__VIEWSTATEGENERATOR", viewStateGenerator)

                .add("__EVENTVALIDATION", validation)
                .add("__VIEWSTATE", viewState).build()


        var filteredTracksResponse = client.newCall(Request.Builder()
                .url("$baseUrl/User/Tracks.aspx")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36")
                .post(formBody)
                .build()).execute().body()?.let {
            it
        } ?: throw IOException()

        var response = filteredTracksResponse.string()
        val parser = TracksParserKt(response)

        val pages = parser.getTotalCount()
        val result = mutableListOf<UserTrackKt>()

        val newForm = Jsoup.parseBodyFragment(response)
        validation = newForm.body().getElementById("__EVENTVALIDATION").attr("value")
        viewState = newForm.body().getElementById("__VIEWSTATE").attr("value")

        var lastFocus = ""
        var eventArg = ""
        var counter = 0


        for (i in 1..pages) {
            val another = Jsoup.parseBodyFragment(response)

            val parts = another.childNode(0).childNode(1).childNode(4).toString().split("\\|")

            for (n in 0 until parts.size) {
                if (parts[n] == "__VIEWSTATE") {
                    viewState = parts[n + 1]
                } else if (parts[n] == "__VIEWSTATE") {
                    validation = parts[n + 1]
                }
            }


            val newBody = FormBody.Builder()
                    .add("ctl00\$ScriptManager1",
                            "ctl00\$ContentPlaceHolderLeft\$DataGridGenerico1\$UpdatePanel1|ctl00\$ContentPlaceHolderLeft\$DataGridGenerico1\$DataGrid1\$ctl34\$ctl" + formatValue(
                                    counter))
                    .add("__LASTFOCUS", lastFocus)
                    .add("__EVENTTARGET", "ctl00\$ContentPlaceHolderLeft\$DataGridGenerico1\$DataGrid1\$ctl34\$ctl" + formatValue(counter))
                    .add("__EVENTARGUMENT", eventArg)
                    .add("__VIEWSTATE", viewState)
                    .add("__EVENTVALIDATION", validation)
                    .add("ctl00\$ContentPlaceHolderLeft\$TextBox1", dateFormatter.format(beginDate))
                    .add("ctl00\$ContentPlaceHolderLeft\$TextBox2", dateFormatter.format(endDate))
                    .add("ctl00\$ContentPlaceHolderLeft\$DataGridGenerico1\$filterField", "EstacionOrigen")
                    .add("ctl00\$ContentPlaceHolderLeft\$DataGridGenerico1\$txtSearch", "")
                    .add("__ASYNCPOST", "true").build()

            val pageTracks = Request.Builder()
                    .url("$baseUrl/User/Tracks.aspx")
                    .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36")
                    .header("Accept", "*/*")
                    .header("Cache-Control", "no-cache")
                    .post(newBody)
                    .build()

            response = client.newCall(pageTracks).execute().body()?.let {
                it.string()
            } ?: throw IOException()

            if ((i - 1) % 10 == 0) {
                counter = 1
            } else {
                counter++
            }
            parser.updateForm(response)
            result.addAll(parser.parse())
        }


        return result


    }


    private fun formatValue(ent: Int): String {
        return if (ent < 10) {
            "0$ent"
        } else {
            ent.toString()
        }
    }


    @Throws(IOException::class, LoginException::class)
    private fun getLoggedClient(): OkHttpClient {
        val login = prefs.getString("login", "")?:""
        val password = prefs.getString("password", "")?:""
//        val login = "33336181G"
//        val password = "isademivida"

        val defaultRequest = Request.Builder().url(defaultUrl).build()

        val form = client.newCall(defaultRequest).execute().body()?.string().let {
            Jsoup.parseBodyFragment(it)
        } ?: throw (LoginException("Error"))

        val validation = form.body().getElementById("__EVENTVALIDATION").attr("value")
        val viewState = form.body().getElementById("__VIEWSTATE").attr("value")
        val viewStateGenerator = form.body().getElementById("__VIEWSTATEGENERATOR").`val`()


        val formBody = FormBody.Builder()
                .add("ctl00${'$'}Login1${'$'}TextBoxLogin", login)
                .add("ctl00${'$'}Login1${'$'}TextBoxPassword", password)
                .add("ctl00${'$'}Login1${'$'}ButtonValidar", "Aceptar")
                .add("__LASTFOCUS", "")
                .add("__EVENTTARGET", "")
                .add("__EVENTARGUMENT", "")
                .add("__VIEWSTATEGENERATOR", viewStateGenerator)

                .add("__EVENTVALIDATION", validation)
                .add("__VIEWSTATE", viewState)
                .build()


        client.newCall(Request.Builder().url(defaultUrl)
                .post(formBody)
                .build()).execute().let {
            if (it.header("location") == null) throw (LoginException("Error"))

            client.newCall(Request.Builder()
                    .url(baseUrl + it.header("location"))
                    .build()).execute().let { response ->
                if (response.message().equals("ok", true)) {
                    prefs.edit().putBoolean("loggedin", true).commit()
                    return client
                } else {
                    prefs.edit().putBoolean("loggedin", false).commit()
                    throw IOException("Wrong response")
                }
            }
        }
    }

}
