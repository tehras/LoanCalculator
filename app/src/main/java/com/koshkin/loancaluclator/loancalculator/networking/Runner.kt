package com.koshkin.loancaluclator.loancalculator.networking

import android.os.AsyncTask
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody

/**
 * Created by koshkin on 7/4/16.
 *
 * Runner
 */
class Runner : AsyncTask<Request, Void, Response>() {

    var defaultMediaType = MediaType.parse("application/json; charset=utf-8")

    override fun onPostExecute(result: Response?) {
        if (request != null)
            request!!.networkResponse.onResponse(result!!, request!!)
    }

    var request: Request? = null

    override fun doInBackground(vararg request: Request?): Response {
        val req = request[0]
        val response = Response(req!!.parsingObject)
        this.request = req

        when (req.requestType) {
            Request.Type.GET -> response.httpGet(req)
            Request.Type.POST -> response.httpPost(req)
            Request.Type.DELETE -> response.httpDelete(req)
            Request.Type.PUT -> response.httpPut(req)
        }

        return response
    }

    private fun Response.httpPut(request: Request) {
        val client = getClient()

        val builder = defaultBuilder(request)!!.put(RequestBody.create(defaultMediaType, request.requestData))
        val okResponse = client.newCall(builder.build()).execute()

        parseResponse(this, okResponse)
    }

    private fun Response.httpPost(request: Request) {
        val client = getClient()

        val builder = defaultBuilder(request)!!.post(RequestBody.create(defaultMediaType, request.requestData))
        val okResponse = client.newCall(builder.build()).execute()

        parseResponse(this, okResponse)
    }

    private fun Response.httpDelete(request: Request) {
        val client = getClient()

        val builder = defaultBuilder(request)!!.delete()
        val okResponse = client.newCall(builder.build()).execute()

        parseResponse(this, okResponse)
    }

    private fun Response.httpGet(request: Request) {
        val client = getClient()

        val builder = defaultBuilder(request)!!.get()
        val okResponse = client.newCall(builder.build()).execute()

        parseResponse(this, okResponse)
    }

    private fun parseResponse(response: Response, okResponse: okhttp3.Response) {
        if (okResponse.isSuccessful) response.status = Response.ResponseStatus.SUCCESS
        else response.status = Response.ResponseStatus.FAILURE

        if (response.status == Response.ResponseStatus.SUCCESS)
            response.parsingObject.parse(okResponse.body().string())

    }

    private fun defaultBuilder(request: Request): okhttp3.Request.Builder? {
        return okhttp3.Request.Builder()
                .url(request.url)
    }

    private fun getClient(): OkHttpClient {
        if (client == null)
            client = OkHttpClient()

        return client as OkHttpClient
    }

}

var client: OkHttpClient? = null
