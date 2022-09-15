package mx.itesm.testbasicapi.model.repository.responseinterface

import mx.itesm.testbasicapi.model.entities.JwtToken

interface ILogin : IBasicResponse {
    fun onSuccess(token: JwtToken?)
}