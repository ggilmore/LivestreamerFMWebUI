package Util

/**
 * Created by gmgilmore on 3/12/15.
 */
sealed trait WebUIException

case object URLNotFoundException extends WebUIException
case object URLAlreadyPresentException extends WebUIException