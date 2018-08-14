# RemoteSMS
A small tool that allow you to remotely receive (or send, in the future) SMS on another cell phone


## Before compiling
First, you need to create a `DefaultConfig.kt` file located under `app/src/main/java/li/x1ang/remotesms`, such file is ignored by Git.
It should look like:

   ```
   package li.x1ang.remotesms


   const val DINGTALK_ENDPOINT = "https://oapi.dingtalk.com/robot/send..."
   ```