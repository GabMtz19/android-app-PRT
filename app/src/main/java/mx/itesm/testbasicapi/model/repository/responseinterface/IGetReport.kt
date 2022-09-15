package mx.itesm.testbasicapi.model.repository.responseinterface

import mx.itesm.testbasicapi.model.entities.Report

interface IGetReport : IBasicResponse {
    fun onSuccess(report: Report?)
}