addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full)

lazy val tagless = (project in file("tagless"))
	.settings(
		name := "scala-tagless",
		version := "0.0.1",
		libraryDependencies ++= Seq(
			"com.typesafe.slick" %% "slick" % "3.3.2",
			"com.h2database" % "h2" % "1.4.200"
		)
	)
    .settings(commonSettings)

lazy val lang = (project in file("lang"))
	.settings(
		name := "scala-lang",
		version := "0.0.1"
	)
    .settings(commonSettings)

lazy val commonSettings = Seq(
	scalaVersion := "2.13.2",
	scalacOptions ++= Seq(
		"-target:jvm-1.8",
		"-encoding", "UTF-8",
		"-unchecked",
		"-feature",
		"-language:existentials",
		"-language:higherKinds",
		"-language:implicitConversions",
		"-language:experimental.macros",
		"-language:reflectiveCalls",
		"-language:postfixOps",
		"-Ymacro-annotations"
	),
	libraryDependencies ++= Seq(
		"org.scalatest" %% "scalatest" % "3.1.1" % Test,
		"org.scalacheck" %% "scalacheck" % "1.14.3" % Test,
		"org.scala-lang" % "scala-compiler" % scalaVersion.value
	)
)