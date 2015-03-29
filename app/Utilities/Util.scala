package Utilities

import java.net.ServerSocket

/**
 * Random methods and values that don't really fit anywhere else...
 */
object Util {


  /**
   * Gives the current path for where the LivestreamerFMWebUI is running
   */
  val CURRENT_RUNNING_PATH: String = new java.io.File( "." ).getCanonicalPath


  /**
   * Gives the default VLC installation location for whatever Operating System that the LivestreamerFMWebUI is running
   */
  //TODO: Write config file parser for server so that this could be changed
  val CURRENT_VLC_LOCATION = core.OperatingSystem.getOS.getDefaultVLCLocation


  /**
   * Returns a random open port on this machine
   * @return Some()the random open port number, or None if there are no free ports on this machine.
   */
  def findOpenPort: Option[Int] = {
    try {
      val socket: ServerSocket = new ServerSocket(0) //binds to any available port
      val openPort = socket.getLocalPort
      socket.close()
      Some(openPort)
    } catch {
      case e: java.io.IOException => None
    }
  }



}
