package mx.itesm.testbasicapi.model.repository.responseinterface

import mx.itesm.testbasicapi.model.entities.Report

interface IAddReport : IBasicResponse {
    fun onSuccess(report: Report?)
}