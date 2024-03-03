//> using scala 3.3.1
//> using options -deprecation -feature -language:noAutoTupling -Werror -Wunused:all -Wvalue-discard -Wnonunit-statement
//> using publish.organization com.bluecatcode.example
//> using publish.name scala

//> using compileOnly.dep org.graalvm.sdk:graal-sdk:22.3.5
//> using dep org.scalameta:scalameta_2.13:4.9.1

import org.graalvm.nativeimage.IsolateThread
import org.graalvm.nativeimage.c.function.CEntryPoint

import scala.annotation.static

@main def main(): Unit = println("Test")

class EntryPoint
object EntryPoint:
  @CEntryPoint(name = "a")
  @static
  def a(thread: IsolateThread): Int = 1

