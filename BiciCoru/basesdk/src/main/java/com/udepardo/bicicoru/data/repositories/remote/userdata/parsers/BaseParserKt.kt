package com.udepardo.bicicoru.data.repositories.remote.userdata.parsers

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.lang.NullPointerException


open class BaseParserKt(html: String){
    var form: Document
    init {
        form = Jsoup.parseBodyFragment(html)
    }


    open fun updateForm(newHtml: String){
        form = Jsoup.parseBodyFragment(newHtml)
    }

    @Throws(NullPointerException::class)
    fun getValueString(attrId: String) = form.getElementById(attrId).attr("value")?.let { it } ?: throw NullPointerException("Attr. $attrId null")

    fun getString(elementId: String) = form.getElementById(elementId).text()

    @Throws(NullPointerException::class)
    fun getAttValEq(attr: String, value: String, eqs: String) =  form.getElementById(attr).attr(value)?.let { it == eqs } ?: throw NullPointerException("Attr. $attr null")

    fun getAllElementsByClass(className: String) = form.getElementsByClass(className)

}
