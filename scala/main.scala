//> using scala 3.3.3
//> using options -deprecation -feature -Wunused:all -language:noAutoTupling -Wvalue-discard -Wnonunit-statement
//> using publish.organization com.bluecatcode.example
//> using publish.name libscala
//> using packaging.packageType graalvm
//> using packaging.graalvmArgs --no-fallback --shared -H:Name=libscala
//> using compileOnly.dep org.graalvm.sdk:graal-sdk:22.3.5
//> using dep org.scalameta:scalameta_2.13:4.9.0

import org.graalvm.nativeimage.IsolateThread
import org.graalvm.nativeimage.c.function.CEntryPoint

import scala.annotation.static

@main def main(): Unit = println("Test")

class EntryPoint
object EntryPoint:
  @CEntryPoint(name = "a")
  @static
  def a(thread: IsolateThread): Int = 1

