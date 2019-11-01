# RemoteSMS
A small tool that allow you to remotely receive (or send, in the future) SMS on another cell phone


## Before compiling
First, you MUST edit the `app/src/main/assets/config.yaml` file before compiling or running. In particular ,the `endpoint` should be specified correctly
    ```
    webhook:
      ...
      endpoint: https://oapi.dingtalk.com/robot/send?access_token=YOUR_TOKEN
    ```
