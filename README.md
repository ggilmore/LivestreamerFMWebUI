# LivestreamerFMWebui
 
This is a web interface for [LivestreamerFM](www.github.com/ggilmore/livestreamerFM), written using [Scala Play](https://www.playframework.com/) and [Scala.js](http://www.scala-js.org/).

##Requirements

LivestreamerFMWebui shares all the same system and software prerequisites as LivestreamerFM, which you can view [here](https://github.com/ggilmore/LivestreamerFM). 

If you want to build this from source, you must download Scala Play, which you can find [here](https://www.playframework.com/download). 

## Running LivestreamerFMWebui:

1. Download the `tgz` file, and extract it (either using your native system tools or some program like [7-Zip](http://www.7-zip.org/)).

2. Navigate to `EXTRACTED_FOLDER_NAME/bin/` and then: 

* For Windows users, run the `.bat` file inside the folder, either by double clicking it, or via Command Prompt / Powershell with some command like `./livestreamerfmwebui.bat`

* For Linux/OSX users, run the bash script file inside the folder with some command like: 

`./livestreamerfmwebui`

###Changing the Default Port #:
By default, LivestreamerFMWebui runs on port 9000. If you want to change that, either pass this argument: `-Dhttp.port=PORT_#_HERE` when running the start script, or create a configuration file called `LIVESTREAMERFMWEBUI_config.txt` in the same folder as the start script, and have it contain `-Dhttp.port=PORT_#_HERE`. For more information, see [this Stack Overflow Post](http://stackoverflow.com/questions/8205067/how-do-i-change-the-default-port-9000-that-play-uses-when-i-execute-the-run).

## Using LivestreamerFMWebui:

LivestreamerFMWebui binds to [any IPv6 address avaialable on your local machine](https://en.wikipedia.org/wiki/0.0.0.0). So, open up your web browser and navigate to:
	YOUR_MACHINE'S_IP_ADDRESS:YOUR_SELECTED_PORT(OR_9000_BY_DEFAULT)

You'll then see a page with a text box, the buttons `Create Audio Stream` and `Clear and Close All Streams`, and a table below with two columns: `STREAM URL` and `PORT #`.

* Paste the link that you want to play (the list of supported links are the same as [LivestreamerFM's](https://github.com/ggilmore/LivestreamerFM)) into the text box, and click the `Create Audio Stream` button. This will create the audio stream, and a new entry will appear in the table. The `STREAM URL` shows the exact link that the server is generating the audio stream from, and the `PORT #` column is the port that the audio stream that LivestreamerFMWebui created is located at. 

* The entry's row will be white if the generated audio stream is currently playing, or red if the stream has ended or if there was some issue when trying to create the stream (such as trying to play an unsupported link. The table will automatically update as more streams are created and the stream's status changes. 

* If you want to close all the streams and clear all the entries from the table, press the `Clear and Close All Streams` button. 

###Example Usage:

Say you wanted to create an audio stream from `twitch.tv/arteezy`. Just paste `twitch.tv/arteezy` into the text box, and click the `Create Audio Stream` button. Then, some entry like `twitch.tv/arteezy	48664` should appear in the table below. Then, use some program like [VLC](https://www.videolan.org/vlc/index.html) to play the audio stream located at `http://YOUR_MACHINE'S_IP_ADDRESS:48664` and listen to your heart's content. 

##TODO: 

* figure out why pressing the `Create Audio Stream` sometimes throws an exception (the program still functions fine despite this, though)

* testing? I have no experience with doing this for a web application 

* figure out how to hook into something like the gosugamer's api so that it's easy to new streams (no pasting of the link required).
