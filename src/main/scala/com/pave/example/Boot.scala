package com.example

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import spray.can.Http
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._

object Boot {
  // we need an ActorSystem to host our application in
  implicit val system = ActorSystem("login-service")

  // create and start our service actor
  val service = system.actorOf(Props[LoginServiceActor], "login-service")

  def main(args: Array[String]): Unit = {
    implicit val timeout = Timeout(30.seconds)

    IO(Http) ? Http.Bind(service, interface = "::0", port = 80)
  }
}
