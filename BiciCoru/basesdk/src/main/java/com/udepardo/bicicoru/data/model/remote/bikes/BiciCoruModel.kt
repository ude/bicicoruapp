package com.udepardo.bicicoru.data.model.remote.bikes

import com.squareup.moshi.Json

/**
 * Created by fernando.ude on 26/01/2018.
 */

data class BicicoruWrapper(@Json(name = "EstacionAdditionalInformationDto") val additionalInfo : List<BicicorunaStation>)
data class BicicorunaStation(
    @Json(name = "IdEstacion") val id: Int,
    @Json(name = "Nombre")val name: String,
    @Json(name = "Latitud")val lat: Double,
    @Json(name = "Longitud")val lon: Double,
    @Json(name = "Direccion")val address: String,
    @Json(name = "PuestosLibres")val free: Int,
    @Json(name = "BicisDisponibles")val occupied: Int,
    @Json(name = "IsOnline")val online: String
)