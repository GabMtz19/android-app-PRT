package mx.itesm.testbasicapi.model.repository.responseinterface

import mx.itesm.testbasicapi.model.entities.JwtToken
import mx.itesm.testbasicapi.model.entities.User

interface ICreateUser : IBasicResponse {
    fun onSuccess(token: JwtToken?)
}