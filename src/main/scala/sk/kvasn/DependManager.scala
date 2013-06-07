package sk.kvasn

/**
 * @author Igo
 */

import scala.sys.process._

object DependManager {
  var count = 0

  def isPortageLeaf(packageName: String, total: Int): Boolean = {
    count = count + 1
    print("Package " + count + " of " + total + ": " + packageName)
    val command = "equery d " + packageName
    try {
      val equeryResult = command.!!
      //    (equeryResult == " * These packages depend on " + packageName + ":")
      println()
      false
    } catch {
      case e: RuntimeException => println("---- LEAF"); true
    }
  }
}
