import sbt._

class MovieStoreProject(info: ProjectInfo) extends DefaultProject(info) {

  val scalacheck = "org.scalacheck" % "scalacheck" % "1.7" % "test" from
    "http://scalacheck.googlecode.com/files/scalacheck_2.8.0-1.7.jar"
  val junit = "org.junit" % "junit" % "4.8.2" % "test" from
        "http://github.com/KentBeck/junit/downloads/junit-4.8.2.jar"
  val scalatest = "org.scalatest" % "scalatest" % "1.2" % "test"
  val testng = "org.testng" % "testng" % "5.13" % "test"
  val specs = "org.scala-tools.testing" % "specs_2.8.0" % "1.6.5" % "test"
  val mockito = "org.mockito" % "mockito-all" % "1.8.5" % "test"


}
