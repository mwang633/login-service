package com.example

import akka.actor.Actor
import org.joda.time.DateTime
import org.slf4j.LoggerFactory
import spray.http.StatusCodes._
import spray.httpx.SprayJsonSupport._
import spray.routing.{HttpService, _}
import spray.util.LoggingContext

import scala.concurrent.Future

// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class LoginServiceActor extends Actor with LoginService {

  def actorRefFactory = context

  def receive = runRoute(route)
}

// this trait defines our service behavior independently from the service actor
trait LoginService extends HttpService {

  import JsonSerializeProtocol._
  import Util._

  implicit def ec = actorRefFactory.dispatcher

  implicit def myExceptionHandler(implicit log: LoggingContext) =
    ExceptionHandler {
      case e: Exception =>
        requestUri { uri =>
          log.error("Unexpected Exception: " + e.toString, uri)
          complete(InternalServerError, e.toString)
        }
    }

  val log = LoggerFactory.getLogger(classOf[LoginService])
  val config = new Configuration()
  val authClient = new AuthClient(config, actorRefFactory)

  log.info("LoginService started")

  val route =
      get {
        pathPrefix("SecretEcho") {
          path(Segment) { s: String =>
            pathEnd {
              authenticate(authenticator) { session =>
                complete {
                  log.info(s"Echo $s")
                  s + session.toString
                }
              }
            }
          }
        }
      post {
        pathPrefix("Login") {
          pathEnd {
            formFields('userId, 'password) { (userId: String, password: String) => {
              val session = Session("TESTSESSIONID001", userId = "TestUser", userRoles = Array(1, 2, 5), expiresOn = DateTime.now.plusDays(10))
              setCookie(AuthCookies.sessionCookie(session, config.Authentication.domain, false)) {
                complete {
                  <html>
                    <body>
                      <h1>Logged in</h1>
                    </body>
                  </html>
                }
              }
            }
            }
          }
        } ~
          pathPrefix("Logout") {
            pathEnd {
              authenticate(authenticator) { session =>
                setCookie(AuthCookies.removeSessionCookie(config.Authentication.domain, false)) {
                  complete {
                    log.info(s"Logout $session")
                    <html>
                      <body>
                        <h1>Logged out</h1>
                      </body>
                    </html>
                  }
                }
              }
            }
          } ~
          pathPrefix("PostSecrete") {
            pathEnd {
              authenticate(xcsrfAuth) { session =>
                complete {
                  log.info(s"PostSecrete $session")
                  Seq("PostSecrete Success")
                }
              }
            }
          }
      }
}