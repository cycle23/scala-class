

// emulate this: https://github.com/TrueCar/mleap/blob/master/build.sbt

// Set sub-project on SBT start: http://stackoverflow.com/a/22240142/1007926
// onLoad in Global := {
//   Command.process("project distributions", _: State)
// } compose (onLoad in Global).value

// Task 5h

lazy val `root` = project.in(file("."))
   .settings(Common.commonSettings)
   .aggregate(`distributions`, `plotting`)

lazy val `distributions` = project.in(file("distributions"))
   .settings(Common.commonSettings)
   
lazy val `plotting` = project.in(file("plotting"))
   .settings(Common.commonSettings)
   .settings(libraryDependencies ++= Dependencies.plottingDependencies)
   .dependsOn(`distributions`)

