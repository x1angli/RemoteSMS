# RemoteSMS
A small tool that allow you to remotely receive (or send, in the future) SMS on another cell phone


## Before compiling
First, you need to create a `extra.properties` file located under the project base, such file is ignored by Git.
It should look like:

   ```
        webhook.endpoint=https://oapi.dingtalk.com/robot/send?access_token=.....
        receiver.id=13012345678
   ```