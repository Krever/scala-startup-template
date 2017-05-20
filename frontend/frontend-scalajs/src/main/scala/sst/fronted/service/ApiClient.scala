package sst.fronted.service

/**
  * Created by wpitula on 3/12/17.
  */
object ApiClient
    extends sst.shared.ApiEndpoints
    with endpoints.xhr.Endpoints
    with endpoints.xhr.thenable.Endpoints
    with endpoints.xhr.CirceEntities
