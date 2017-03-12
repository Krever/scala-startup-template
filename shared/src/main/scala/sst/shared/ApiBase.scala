package sst.shared

import endpoints.algebra.Endpoints

/**
  * Created by wpitula on 3/11/17.
  */
trait ApiBase extends Endpoints {

  def apiBasePath: Path[Unit]

}
