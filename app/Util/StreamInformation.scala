package Util

import play.api.libs.json._

/**
 * Created by gmgilmore on 3/16/15.
 */
case class StreamInformation(urlPath:String, port: Int, uniqueID:Long, process:sys.process.Process,
                             var hasError:Boolean = false)

object StreamInformation {
  implicit val streamInfoWrites = new Writes[StreamInformation] {
    def writes(streamInfo: StreamInformation) = Json.obj(
      "urlPath" -> streamInfo.urlPath,
      "port" -> streamInfo.port,
      "id" -> streamInfo.uniqueID,
      "errorOrEnded" -> streamInfo.hasError
    )
  }
}

