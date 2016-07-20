package com.example

import java.io.File
import java.util.concurrent.TimeUnit

import com.typesafe.config.{ConfigFactory, Config}
import org.joda.time.Duration

class Configuration() {
  val config : Config = {
    val configDefaults = ConfigFactory.load(this.getClass.getClassLoader, "application.conf")

    scala.sys.props.get("application.config") match {
      case Some(filename) => ConfigFactory.parseFile(new File(filename)).withFallback(configDefaults)
      case None => configDefaults
    }
  }

  object ExampleRemoteServer {
    val url: String = config.getString("ExampleRemoteServer.url")
  }

  object Authentication {
    val domain : String = config.getString("authentication.domain")
    val cookieLife : Duration = new Duration(config.getDuration("authentication.cookieLife", TimeUnit.MILLISECONDS))
  }
}
