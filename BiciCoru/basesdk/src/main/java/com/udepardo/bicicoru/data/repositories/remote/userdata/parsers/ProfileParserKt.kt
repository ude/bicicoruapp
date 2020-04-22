package com.udepardo.bicicoru.data.repositories.remote.userdata.parsers

import com.udepardo.bicicoru.data.model.db.UserProfileKt

class ProfileParserKt(html: String) : BaseParserKt(html) {

    @Throws(NullPointerException::class)
    fun parseProfile() =
        UserProfileKt(
            getValueString("ctl00_ContentPlaceHolderLeft_txtCardNumber"),
            getValueString("ctl00_ContentPlaceHolderLeft_txtDni"),
            getValueString("ctl00_ContentPlaceHolderLeft_txtName"),
            getValueString("ctl00_ContentPlaceHolderLeft_txtSurname1"),
            getValueString("ctl00_ContentPlaceHolderLeft_txtSurname2"),
            getValueString("ctl00_ContentPlaceHolderLeft_txtFechaNacimiento"),
            getValueString("ctl00_ContentPlaceHolderLeft_txtAddress"),
            getValueString("ctl00_ContentPlaceHolderLeft_txtCity"),
            getValueString("ctl00_ContentPlaceHolderLeft_txtPostcode"),
            getValueString("ctl00_ContentPlaceHolderLeft_txtPhone"),
            getValueString("ctl00_ContentPlaceHolderLeft_txtMobile"),
            getValueString("ctl00_ContentPlaceHolderLeft_txtEmail"),
            if (getAttValEq("ctl00_ContentPlaceHolderLeft_rbWoman", "checked", "checked"))  0 else 1)
    }