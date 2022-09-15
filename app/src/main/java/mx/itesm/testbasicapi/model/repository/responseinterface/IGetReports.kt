package mx.itesm.testbasicapi.model.repository.responseinterface

import mx.itesm.testbasicapi.model.entities.Report

interface IGetReports : IBasicResponse  {
    fun onSuccess(reports: List<Report>?)
}