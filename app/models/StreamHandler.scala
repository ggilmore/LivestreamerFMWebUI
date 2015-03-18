package models


import Util.{GiveURL, Util, GetAllUrls, StreamInformation, DeleteURL}

import akka.actor.{Props, ActorRef, Actor}
import core._
import play.api.Play.current
import play.api.libs.concurrent.Akka
import play.api.libs.json.{JsValue, Json}

import scala.collection.concurrent.TrieMap

/**
 * Created by gmgilmore on 3/11/15.
 *
 * Stuff I need to have happen when a user enters a url/
 * 1) check to see if the url still exists in the database, if so do nothing.
 * 2) find an open port
 * 3) store in the database the url and port number
 * 4) find a place to store a configuration file for LivestreamerFM,
 * then find a place for LivestreamerFM to write it's own config file for vlc
 * 5) start the livestreamerFM process
 * 6) have a quit watcher, delete corresponding row from database
 * 7) return the list of running streams to the user
 *
 */

object StreamHandler{
  val actorReference:ActorRef = Akka.system.actorOf(Props[StreamHandler], "Handler")
  private val processMapping = TrieMap[String, StreamInformation]()
  def toJson : JsValue = Json.toJson(processMapping.values)
}

case class ProcessExitDetector(info:StreamInformation) extends Thread(new Runnable{
  def run(): Unit ={
    info.process.exitValue
    StreamHandler.actorReference ! DeleteURL(info.urlPath)
  }
})


class StreamHandler extends Actor{

  var id: Long = 0

  def receive = {
    case GiveURL(path) =>{
        val freePort = Util.findOpenPort
        if (!StreamHandler.processMapping.keySet.contains(path)) {
          val LSFMOptions = core.LSFMConfigOptions(core.OperatingSystem.getOS.getDefaultVLCLocation, "0.0.0.0:"+freePort,
            "5000", Util.CURRENT_RUNNING_PATH)
          core.Util.splitIpAndPort(LSFMOptions.ipAndPort) match {
            case Some((ip, port)) => {
              val livestreamerOptions = core.LSConfigOptions(vlcLocation = OperatingSystem.getOS.getDefaultVLCLocation,
                fileLocation = Util.CURRENT_RUNNING_PATH, ip = ip, vlcPort = port)
              core.LivestreamerConfigFileWriter.writeNewConfigFile(livestreamerOptions)

              val processInfo = core.ArgParser.LiveStreamerProcessInfo(configLocation =
                Util.CURRENT_RUNNING_PATH + "livestreamerconfig.txt", url = path,
              ip = livestreamerOptions.ip , port = port,
                audioOptionName = core.AudioSettings.getAudioSettingFromURL(path))

              core.ArgParser.createLiveStreamerProcess(processInfo) match {
                case Right(process) => {
                  id = id + 1
                  val info = StreamInformation(path, freePort, id, process)
                  StreamHandler.processMapping.putIfAbsent(path, info)
                  ProcessExitDetector(info).start
                }
                case Left(err) => ???
             }
          }
            case None => ???
          }
      }
    }
    case DeleteURL(path) => {
      StreamHandler.processMapping.remove(path)
    }




  }







}


