import _root_.sbt.Keys._
import _root_.sbt.Project
import _root_.sbtrelease.ReleaseStateTransformations
import _root_.sbtrelease.ReleaseStateTransformations._
import sbtrelease._
import ReleaseStateTransformations._
import ReleaseKeys._
import xerial.sbt.Sonatype.SonatypeKeys
import com.typesafe.sbt.SbtGit.{GitKeys => git}

site.settings

ghpages.settings

site.includeScaladoc()

val nameLiteral = "ftypes-array"

organization := s"com.bryghts.${nameLiteral.toLowerCase}"

git.gitRemoteRepo := s"git@github.com:marcesquerra/$nameLiteral.git"

name := nameLiteral

scalaVersion := "2.11.6"

crossScalaVersions := Seq("2.10.4", "2.11.6")

publishMavenStyle := true

sonatypeProfileName  := "com.bryghts"

publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
        Some("snapshots" at nexus + "content/repositories/snapshots")
    else
        Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

publishArtifact in Test := false

pomExtra := (
    <url>http://www.brights.com</url>
        <licenses>
            <license>
                <name>mit</name>
            </license>
        </licenses>
        <scm>
            <url>git@github.com:marcesquerra/{nameLiteral}.git</url>
            <connection>scm:git:git@github.com:marcesquerra/{nameLiteral}.git</connection>
        </scm>
        <developers>
            <developer>
                <name>Marc Esquerrà i Bayo</name>
                <email>esquerra@bryghts.com</email>
            </developer>
        </developers>
    )


releaseProcess := Seq[ReleaseStep](
    checkSnapshotDependencies,                    // : ReleaseStep
    inquireVersions,                              // : ReleaseStep
    runClean,                                     // : ReleaseStep
    runTest,                                      // : ReleaseStep
    setReleaseVersion,                            // : ReleaseStep
    commitReleaseVersion,                         // : ReleaseStep, performs the initial git checks
    tagRelease,                                   // : ReleaseStep
    ReleaseStep(action = Command.process("publishSigned", _), enableCrossBuild = true),
    setNextVersion,                               // : ReleaseStep
    commitNextVersion,                            // : ReleaseStep
    ReleaseStep(action = Command.process("sonatypeReleaseAll", _)),
    pushChanges                                   // : ReleaseStep, also checks that an upstream branch is properly configured
)




lazy val root = project.in(file(".")).
    aggregate(fooJS, fooJVM).
    settings(
        publish := {},
        publishLocal := {}
    )

lazy val foo = crossProject.in(file(".")).
    settings(
        name := nameLiteral,
        libraryDependencies ++= Seq( "com.bryghts.ftypes-vals" %%% "ftypes-vals" % "0.1.13" )
    ).
    jvmSettings(
        // Add JVM-specific settings here
    ).
    jsSettings(
        // Add JS-specific settings here
    )

lazy val fooJVM = foo.jvm
lazy val fooJS = foo.js
