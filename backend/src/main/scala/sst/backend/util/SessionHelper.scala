package sst.backend.util

import akka.http.scaladsl.server.{Directive0, Directive1}
import com.softwaremill.session._
import com.typesafe.config.Config
import sst.shared.Session

/**
  * Created by wpitula on 6/11/17.
  */
class SessionHelper(config: Config) {

  implicit val serializer = JValueSessionSerializer.caseClass[Session]
  implicit val encoder = new JwtSessionEncoder[Session]
  implicit val manager = new SessionManager(SessionConfig.fromConfig(config))

  import SessionOptions._

  def setSession(session: Session): Directive0 = SessionDirectives.setSession(oneOff, usingHeaders, session)

  val requireSession: Directive1[Session] = SessionDirectives.requiredSession(oneOff, usingHeaders)

  val optionalSession: Directive1[Option[Session]] = SessionDirectives.optionalSession(oneOff, usingHeaders)

}
