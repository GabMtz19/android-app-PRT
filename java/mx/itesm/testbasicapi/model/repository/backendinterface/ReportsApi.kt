package mx.itesm.testbasicapi.model.repository.backendinterface

import mx.itesm.testbasicapi.model.entities.Report
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ReportsApi {
    @GET("reports/{reportId}")
    fun getReport(@Path("reportId") reportId: String): Call<Report>

    @GET("reports/")
    fun getReports(): Call<List<Report>>

    @Multipart
    @POST("reports/")
    fun insertReport(
        @Part report: MultipartBody.Part,
        @Part reportPhoto: MultipartBody.Part?
    ): Call<Report>

    @Multipart
    @PUT("reports/{reportId}")
    fun updateReport(
        @Path("reportId") reportId: String,
        @Part report: MultipartBody.Part,
        @Part reportPhoto: MultipartBody.Part?
    ): Call<Report>

    @DELETE("reports/{reportId}")
    fun deleteReport(@Path("reportId") reportId: String): Call<Report>
}