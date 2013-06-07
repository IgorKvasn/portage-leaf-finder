package sk.kvasn

import java.io.{File, FileFilter}
import java.util.regex.Pattern

/**
 * @author Igo
 */
object Main {

  val digitPattern = Pattern.compile("-[0-9]")

  def main(args: Array[String]) {
    println("Sit tight, this might take a little longer (also don't be suprised by CPU usage)")
    println()
    val rootDir = new File("/var/db/pkg/")
    val packages = rootDir.listFiles(new DirFilter()).collect({
      case f => f.listFiles(new DirFilter())
    }).flatMap {
      case ar: Array[File] => ar
    }.map(trimPackageName)

    val leafPackages =  packages.filter(DependManager.isPortageLeaf(_,packages.length))

    println("---------------RESULTS---------")
    println("Leaf packages " + leafPackages.length)
    println(leafPackages.mkString("\n"))
  }


  private def trimPackageName(file: File): String = {
    val start = "/var/db/pkg/".length
    val end = indexOfRegex(file.getAbsolutePath)
    val name = file.getAbsolutePath.substring(start, end)
    if (name.count(_ == '/') != 1) throw new MyException("corrupted package name: " + name)
    name
  }

  private def indexOfRegex(s: String): Int = {
    val matcher = digitPattern.matcher(s)

    if (matcher.find()) matcher.start() else throw new MyException("could not find package name for: " + s)
  }

  class DirFilter extends FileFilter {
    def accept(pathname: File): Boolean = pathname.isDirectory
  }

  class MyException(msg: String) extends Exception {
  }

}
