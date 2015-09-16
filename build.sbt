name := """play-reactjs"""

version := "1.0-SNAPSHOT"


lazy val app = (project in file("."))
    .enablePlugins(PlayScala)
    .dependsOn(js)

lazy val js = (project in file("javascripts"))
  .settings(
    version := "1.0-SNAPSHOT",
    scalaVersion := "2.11.7",
    libraryDependencies ++= Seq(
      "com.typesafe.play" %% "play" % "2.4.3"
    )
  )


scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  specs2 % Test
)

// resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
