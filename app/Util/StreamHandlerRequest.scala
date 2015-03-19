package Util

/**
 * Created by gmgilmore on 3/16/15.
 */
sealed trait StreamHandlerRequest


case class GiveURL(url:String) extends StreamHandlerRequest
case class DeleteURL(url:String) extends StreamHandlerRequest