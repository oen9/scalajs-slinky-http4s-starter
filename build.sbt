val Http4sVersion = "0.20.15"
val LogbackVersion = "1.2.3"
scalaVersion := "2.12.10"

import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

lazy val sharedSettings = Seq(
  organization := "oen",
  scalaVersion := "2.12.10",
  version := "0.1.0-SNAPSHOT",
  libraryDependencies ++= Seq(
    "com.lihaoyi" %%% "scalatags" % "0.7.0",
    "org.typelevel" %% "cats-core" % "2.1.0",
    "io.circe" %%% "circe-generic" % "0.12.2",
    "io.circe" %%% "circe-literal" % "0.12.2",
    "io.circe" %%% "circe-generic-extras" % "0.12.2",
    "io.circe" %%% "circe-parser" % "0.12.2",
    "io.scalaland" %%% "chimney" % "0.3.2",
    "com.softwaremill.quicklens" %%% "quicklens" % "1.4.12"
  ),
  scalacOptions ++= Seq(
    "-Xlint",
    "-unchecked",
    "-deprecation",
    "-feature",
    "-Ypartial-unification",
    "-language:higherKinds"
  )
)

lazy val jsSettings = Seq(
  libraryDependencies ++= Seq(
    "me.shadaj" %%% "slinky-web" % "0.6.3",
    "com.lambdaminute" %%% "slinky-wrappers-react-router" % "0.4.1",
    "io.suzaku" %%% "diode" % "1.1.6"
  ),
  npmDependencies in Compile ++= Seq(
    "react" -> "16.12.0",
    "react-dom" -> "16.12.0",
    "react-popper" -> "1.3.6",
    "react-router-dom" -> "5.1.2",
    "path-to-regexp" -> "6.0.0",
    "bootstrap" -> "4.3.1",
    "jquery" -> "3.4.1"
  ),
  scalaJSUseMainModuleInitializer := true,
  localUrl := ("0.0.0.0", 12345),
  addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full),
  version.in(webpack) := "4.41.2",
  webpackBundlingMode := BundlingMode.LibraryAndApplication(),
  webpackBundlingMode.in(fastOptJS) := BundlingMode.LibraryOnly(),
  scalacOptions += "-P:scalajs:sjsDefinedByDefault"
)

lazy val jvmSettings = Seq(
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-effect" % "2.0.0",
    "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
    "org.http4s" %% "http4s-circe" % Http4sVersion,
    "org.http4s" %% "http4s-dsl" % Http4sVersion,
    "org.http4s" %% "http4s-blaze-client" % Http4sVersion,
    "ch.qos.logback" % "logback-classic" % LogbackVersion,
    "com.github.pureconfig" %% "pureconfig" % "0.11.0"
  ),
  target := baseDirectory.value / ".." / "target"
)

lazy val app =
  crossProject(JSPlatform, JVMPlatform)
    .crossType(CrossType.Full).in(file("."))
    .settings(sharedSettings)
    .jsSettings(jsSettings)
    .jvmSettings(jvmSettings)

lazy val appJS = app.js
  .enablePlugins(WorkbenchPlugin)
  .enablePlugins(ScalaJSBundlerPlugin)
  .disablePlugins(RevolverPlugin)

lazy val appJVM = app.jvm
  .enablePlugins(JavaAppPackaging)
  .settings(
    dockerExposedPorts := Seq(8080),
    dockerBaseImage := "oracle/graalvm-ce:19.3.0.2-java11",
    (unmanagedResourceDirectories in Compile) += (resourceDirectory in(appJS, Compile)).value,
    mappings.in(Universal) ++= webpack.in(Compile, fullOptJS).in(appJS, Compile).value.map { f =>
      f.data -> s"assets/${f.data.getName()}"
    },
    mappings.in(Universal) ++= Seq(
      (target in(appJS, Compile)).value / ("scala-" + scalaBinaryVersion.value) / "scalajs-bundler" / "main" / "node_modules" / "bootstrap" / "dist" / "css" / "bootstrap.min.css" -> "assets/bootstrap.min.css"
    ),
    bashScriptExtraDefines += """addJava "-Dassets=${app_home}/../assets""""
  )

disablePlugins(RevolverPlugin)
