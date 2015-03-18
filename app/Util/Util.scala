package Util

import java.net.ServerSocket

/**
 * Created by gmgilmore on 3/12/15.
 */
object Util {

  val CURRENT_RUNNING_PATH: String = new java.io.File( "." ).getCanonicalPath

  //TODO: Write config file parser for server so that this could be changed
  val CURRENT_VLC_LOCATION = core.OperatingSystem.getOS.getDefaultVLCLocation

  def findOpenPort: Int = {
    val socket:ServerSocket = new ServerSocket(0) //binds to any available port
    val openPort = socket.getLocalPort
    socket.close()
    openPort
  }



}
