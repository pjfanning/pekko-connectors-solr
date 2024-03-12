ThisBuild / version := "1.0.0-SNAPSHOT"

val scala213Version = "2.13.13"
ThisBuild / scalaVersion := scala213Version
ThisBuild / crossScalaVersions := Seq(scala213Version, "2.12.19", "3.3.3")

ThisBuild / resolvers += Resolver.ApacheMavenSnapshotsRepo

val PekkoVersion = "1.0.2"
val SolrjVersion = "8.11.3"

lazy val root = (project in file("."))
  .settings(
    name := "pekko-connectors-solr",
    licenses := List(License.Apache2),
    libraryDependencies ++= Seq(
      "org.apache.pekko" %% "pekko-actor" % PekkoVersion,
      "org.apache.pekko" %% "pekko-stream" % PekkoVersion,
      "org.apache.solr" % "solr-solrj" % SolrjVersion,
      ("org.apache.solr" % "solr-test-framework" % SolrjVersion % Test)
        .exclude("org.apache.logging.log4j", "log4j-slf4j-impl"),
      "org.slf4j" % "log4j-over-slf4j" % "1.7.36" % Test,
      "org.apache.pekko" %% "pekko-stream-testkit" % PekkoVersion % Test,
      "org.apache.pekko" %% "pekko-slf4j" % PekkoVersion % Test,
      "ch.qos.logback" % "logback-classic" % "1.2.13" % Test,
      "org.scalatest" %% "scalatest" % "3.2.18" % Test,
      "com.dimafeng" %% "testcontainers-scala-scalatest" % "0.41.0" % Test,
      "com.novocode" % "junit-interface" % "0.11" % Test,
      "junit" % "junit" % "4.13.2" % Test
    )
  )
