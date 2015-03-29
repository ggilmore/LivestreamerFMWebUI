package Utilities

import play.api.libs.json._

/**
 * Container class used to store and pass around information about an individual stream
 * @param urlPath the URL of a stream to play
 * @param port the port that this stream is currently playing on
 * @param uniqueID a long which uniquely identifies this stream instance
 * @param process the actual LivestreamerFM process instacance that controlls this stream
 * @param hasError true if "process" closed becaused there was an error with the stream or the stream ended,
 *                 false otherwise
 */
case class StreamInformation(urlPath:String, port: Int, uniqueID:Long, process:sys.process.Process,
                             var hasError:Boolean = false)

object StreamInformation {
  /**
   * Decribes how to write the JSON for a StreamInformation instance
   */
  implicit val streamInfoWrites = new Writes[StreamInformation] {
    def writes(streamInfo: StreamInformation) = Json.obj(
      "urlPath" -> streamInfo.urlPath,
      "port" -> streamInfo.port,
      "id" -> streamInfo.uniqueID,
      "errorOrEnded" -> streamInfo.hasError
    )
  }
}

