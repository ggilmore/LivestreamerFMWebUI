package Utilities

/**
 * Simple container classes for messages that are sent to the streamHandler actor 
 */
sealed trait StreamHandlerRequest


/**
 * Meant to be sent to the actor when you want to create a new stream at this URL 
 * @param url the url to start the stream with 
 */
case class GiveURL(url:String) extends StreamHandlerRequest

/**
 * Meant to be sent to the actor when you want to mark a paritcular stream as closed (no longer running)
 * for whatever reason
 * @param url the url to mark as closed
 */
case class MarkURLAsClosed(url:String) extends StreamHandlerRequest


/**
 * Meant to be sent to the actor when you want to close every stream and clear the table.
 */
case object ClearStreams extends StreamHandlerRequest