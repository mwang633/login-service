val akkaV = "2.3.14"
val sprayV = "1.3.3"

lazy val root = (project in file(".")).
  settings(
    name := "example-service",
    version := "1.0",
    scalaVersion := "2.11.7",
    scalacOptions ++= Seq("-unchecked", "-deprecation", "-encoding", "utf8"),
    libraryDependencies ++= Seq(
        "io.spray"            %%   "spray-servlet"     % sprayV,
        "io.spray"            %%   "spray-routing"     % sprayV,
        "io.spray"            %%   "spray-testkit"     % sprayV,
        "io.spray"            %%   "spray-client"      % sprayV,
        "io.spray"            %%   "spray-util"        % sprayV,
        "io.spray"            %%   "spray-caching"     % sprayV,
        "io.spray"            %%   "spray-can"         % sprayV,
        "io.spray"            %%   "spray-json"        % "1.3.1",
        "com.typesafe.akka"   %%  "akka-actor"         % akkaV,
        "com.typesafe.akka"   %%  "akka-testkit"       % akkaV,
        "com.typesafe.akka"   %%  "akka-slf4j"         % akkaV,
        "joda-time"           %   "joda-time"          % "2.9.2",
        "org.specs2"          %%  "specs2"             % "3.7" % "test",
        "org.apache.kafka"    %   "kafka-log4j-appender" % "0.9.0.1"
      )
  )