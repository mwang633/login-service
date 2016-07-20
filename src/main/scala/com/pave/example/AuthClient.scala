package com.example

import akka.actor.ActorRefFactory
import org.joda.time.DateTime
import org.slf4j.LoggerFactory
import spray.client.pipelining._
import spray.http.{FormData, HttpRequest, HttpResponse}
import spray.httpx.SprayJsonSupport._
import spray.json.DefaultJsonProtocol

import scala.concurrent.Future
/*
* An example rest client to connect to another rest service
*
* */

object AuthClient {
  case class AuthResponse(error: Option[String] = None,
                          session : Option[Session] = None)

  case class Session(token : String, userId : String, userEmail : String, expiresOn : DateTime)

  case class User(userId : String, userEmail : String, password : String)
}


class AuthClient(config : Configuration, implicit val system: ActorRefFactory) {
  private object JsonProtocol extends DefaultJsonProtocol {
    implicit val exampleResponseFormat = jsonFormat2(ExampleResponse)
  }

  import JsonProtocol._
  import system.dispatcher

  private val log = LoggerFactory.getLogger(this.getClass)

  private val logRequest: HttpRequest => HttpRequest = { r => log.debug(r.toString); r }
  private val logResponse: HttpResponse => HttpResponse = { r => log.debug(r.toString); r }

  private val jsonQuery = logRequest ~> sendReceive ~> logResponse

  def requestFuture(id : String) : Future[ExampleResponse] = {
    val pipeline = jsonQuery ~> unmarshal[ExampleResponse]

    pipeline {
      Get(s"${config.ExampleRemoteServer.url}/getUser", FormData(Seq("id" -> id)))
    }
  }
}
