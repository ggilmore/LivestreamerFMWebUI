package models




import Utilities.{ClearStreams, GiveURL, MarkURLAsClosed, StreamInformation, Util}

import akka.actor.{Props, ActorRef, Actor}
import core._
import play.api.Play.current
import play.api.libs.concurrent.Akka
import play.api.libs.json.{JsValue, Json}

import scala.collection.concurrent.TrieMap

object StreamHandler{

  /**
   * A reference to an actor instance
   */
  val actorReference:ActorRef = Akka.system.actorOf(Props[StreamHandler], "Handler")

  /**
   * Where the magic happens.
   *
   * This is the data structure that keeps track of every stream that was loaded into the system. The key for the map
   * is the url of the stream (in this way we automatically filter out duplicates). The value is the stream information
   * associated with the stream.
   */
  private val processMapping = TrieMap[String, StreamInformation]()


  /**
   *
   *stores the various errors created during the operation of the StreamHandler actor
   */
  private val errorSet:scala.collection.mutable.Set[String] = scala.collection.mutable.Set[String]()

  /**
   *
   * @return a JSON snapshot of all the StreamInformation's that we are currently keeping track of
   */
  def toJson : JsValue = Json.toJson(processMapping.values)


  /**
   *
   * @return a snapshot of all the Errors that the server has currently generated
   */
  def getCurrentErrors:Seq[String] = errorSet.toSeq


  private val ANY_AVAILABLE_IP = "0.0.0.0"
  private val DEFAULT_NETWORK_DELAY = "5000"
  private val DEFAULT_CONFIGURATION_NAME = "livestreamerconfig.txt"
}


/**
 * Simple class that is created whenever a new Livestreamer process is created. This thread waits until the process
 * it's associated with exits, and then sends a message to the StreamHandler actor to mark the stream as closed in the
 * processMapping datastructure.
 *
 * @param info the StreamInformation instance associated with the stream that we are assigning this
 *             LivestreamerFMProcessExitDetector to
 */
case class LivestreamerFMProcessExitDetector(info:StreamInformation) extends Thread(new Runnable{
  def run(): Unit ={
    info.process.exitValue //blocks until the process exits, I don't care about the actual exit code @ the moment
    StreamHandler.actorReference ! MarkURLAsClosed(info.urlPath)
  }
})


class StreamHandler extends Actor{

  /**
   * The unique ID for each stream that is created
   */
  var id: Long = 0

  def receive = {
    case GiveURL(path) =>{
        createStreamFromURL(path)
    }
    case MarkURLAsClosed(path) => {
      StreamHandler.processMapping(path).hasError = true
    }

    case ClearStreams => {
      StreamHandler.processMapping.values.foreach(x => x.process.destroy)
      StreamHandler.processMapping.clear
    }

  }

  def createStreamFromURL(path:String) = {
    /**
     * Tasks that need to be preformed in order to create a stream.
     *
     * 1) Find an open port (or bail out)
     *
     * 2) check to see if processMapping hasn't seen this path before (or bail out)
     *
     * 3) configure LivestreamerFM
     *
     * 4) configure Livestreamer itself and create it's configuration file
     *
     * 5) Configure and create the livestreamer process
     *
     * 6) Put the new process into the "processMapping" data structure
     *
     * 7) Create the LivestreamerFMProcessExitDetector for the process
     */
    Util.findOpenPort match {

      /**
       * find open port
       */
      case Some(freePort) => {

        /**
         * 2) check to see if processMapping hasn't seen this path before (or bail out)
         */
        if (!StreamHandler.processMapping.keySet.contains(path)) {

          /**
           * 3) configure LivestreamerFM
           */
          val LSFMOptions = core.LSFMConfigOptions(core.OperatingSystem.getOS.getDefaultVLCLocation,
            s"${StreamHandler.ANY_AVAILABLE_IP}:" + freePort, StreamHandler.DEFAULT_NETWORK_DELAY,
            Util.CURRENT_RUNNING_PATH)

          core.Util.splitIpAndPort(LSFMOptions.ipAndPort) match {
            case Some((ip, port)) => {

              /**
               * 4) configure Livestreamer itself and create it's configuration file
               */
              val livestreamerOptions = core.LSConfigOptions(vlcLocation = OperatingSystem.getOS.getDefaultVLCLocation,
                fileLocation = Util.CURRENT_RUNNING_PATH, ip = ip, vlcPort = port)
              core.LivestreamerConfigFileWriter.writeNewConfigFile(livestreamerOptions)

              /**
               * 5) Configure and create the livestreamer process
               */

              val processInfo = core.ArgParser.LiveStreamerProcessInfo(configLocation =
                Util.CURRENT_RUNNING_PATH + StreamHandler.DEFAULT_CONFIGURATION_NAME, url = path,
                ip = livestreamerOptions.ip, port = port,
                audioOptionName = core.AudioSettings.getAudioSettingFromURL(path))

              core.ArgParser.createLiveStreamerProcess(processInfo) match {
                case Right(process) => {
                  id += 1
                  val info = StreamInformation(path, freePort, id, process)

                  /**
                   * 6) Put the new process into the "processMapping" data structure
                   */
                  StreamHandler.processMapping.putIfAbsent(path, info)


                  /**
                   * 7) Create the LivestreamerFMProcessExitDetector for the process
                   */
                  LivestreamerFMProcessExitDetector(info).start
                }
                case Left(err) => StreamHandler.errorSet + err.getErrorMessage
              }
            }
            case None => StreamHandler.errorSet +
              s"${LSFMOptions.ipAndPort} is incorrectly formatted. Unable to split ip and port."
          }
        }
      }
      case None => StreamHandler.errorSet + s"No free ports available on this system. How is this possible?"
      }

    }








}


