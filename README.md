# RemoteSMS
A small tool that allow you to remotely receive (or send, in the future) SMS on another cell phone


## Before compiling
First, you MUST edit the `app/src/main/assets/config.yaml` file before compiling or running. In particular ,the `endpoint` should be specified correctly

    ```
    webhook:
      ...
      endpoint: https://oapi.dingtalk.com/robot/send?access_token=YOUR_TOKEN
    ```

## Tips and TroubleShooting
### Instruction for XiaoMi/MIUI users 
If you are using Xiaomi, there is a dedicated permission "access notification SMS" in addition to the regular "access SMS". Plus, it defaults to "disabled" 
![image](https://user-images.githubusercontent.com/8603485/120455102-0b465900-c3c7-11eb-9d39-ae30f0f01c8d.png)

Therefore, if you are unable to access the 6-digit Verification Code, just check the permission for this App, and make sure it's turned on

Read more https://www.zhangshengrong.com/p/v710PWml1M/

Additionally, some regular SMS will be mis-categorized as "promo msgs （推广短信）"， so you might need to turn on the "Notification for promo msgs （推广短信通知）" option


## Alternatives

There are alternatives available. e.g.

#### bogomolov
* Intro: https://bogomolov.tech/android-sms-forwarding/
* Github Repo: https://github.com/bogkonstantin/android_income_sms_gateway_webhook

#### ppscn
* Github Repo: https://github.com/pppscn/SmsForwarder

