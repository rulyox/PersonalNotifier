# Personal Notifier

A simple app that can receive notifications via API.

![image_1](doc/image_1.png)

## Requirements

### Google Firebase

1. Create a project.

![firebase_1](doc/firebase_1.png)

2. Add an app (select Android).

![firebase_2](doc/firebase_2.png)

3. Download `google-services.json`.

![firebase_3](doc/firebase_3.png)

4. Go to `Project settings` - `Service accounts` and download the private key.

![firebase_4](doc/firebase_4.png)

### Android

1. Place the file downloaded in `Firebase Step 3` in `app/google-services.json`.

2. Build and install the app.

3. (Optional) Testing if your device can receive a Firebase message.
    - Go to `Engage` - `Messaging` - `Create your first campaign` - `Firebase Notification messages`.
    - Write something and click `Send test message`.
    - Paste your device's token and click  `Test`.
    - A message will be received.
    - The received message's title and body will be null because a custom format is being used in this app.

![test_1](doc/test_1.png)

![test_2](doc/test_2.png)

### AWS Lambda

1. Create a `Lambda` function using Node.js as the runtime.

![aws_1](doc/aws_1.png)

2. Create a `API Gateway` HTTP API and select the created Lambda as an Integration.

![aws_2](doc/aws_2.png)

![aws_3](doc/aws_3.png)

3. Rename the file downloaded in `Firebase Step 4` to `firebase_service_account.json` and place it in `lambda/firebase_service_account.json`.

4. Run `npm install` to install the required packages.

5. Zip the `lambda` directory and upload it to Lambda.

![aws_4](doc/aws_4.png)

## Usage

Send a request to your Lambda endpoint to push a message.

```
POST https://[Your Endpoint].execute-api.ap-northeast-2.amazonaws.com/
{
    "title": "Remote Notification",
    "body": "Hello, world!",
    "targetToken": "[Your Token]"
}
```

![image_2](doc/image_2.png)
