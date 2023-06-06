const axios = require('axios');
const { google } = require('googleapis');

const FIREBASE_SERVICE_ACCOUNT_JSON = './firebase_service_account.json';

const getGoogleToken = async (email, key) => {
    const scopes = ['https://www.googleapis.com/auth/firebase.messaging'];
    const jwtClient = new google.auth.JWT(
        email,
        null,
        key,
        scopes,
        null,
    );
    const credentials = await jwtClient.authorize();
    return credentials.access_token;
};

const sendMessage = async (title, body, targetToken) => {
    const firebaseAccount = require(FIREBASE_SERVICE_ACCOUNT_JSON);

    const googleToken = await getGoogleToken(firebaseAccount.client_email, firebaseAccount.private_key);
    const response = await axios({
        method: 'POST',
        url: `https://fcm.googleapis.com/v1/projects/${firebaseAccount.project_id}/messages:send`,
        headers: {
            'Authorization': `Bearer ${googleToken}`,
            'Content-Type': 'application/json',
        },
        data: {
            'message': {
                'data': {
                    'title': title,
                    'body': body,
                },
                'token': targetToken,
            }
        }
    });
}

exports.handler = async (event) => {
    const request = JSON.parse(event.body);
    const title = request.title;
    const body = request.body;
    const targetToken = request.targetToken;

    if (!title || !body || !targetToken) {
        return {
            statusCode: 400,
            body: 'Wrong body.',
        };
    }

    await sendMessage(title, body, targetToken);

    return {
        statusCode: 200,
        body: 'Message successfully sent.',
    };
};
