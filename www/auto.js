/*global cordova, module*/

module.exports = {
    
    AndroidAutoSendNotification: function(title, message) {
        cordova.exec(null, null, 'AndroidAutoNotificationSender', 'sendNotification', [title, message]);
    },
    AndroidAutoGetSpeechAnswer: function(successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, 'AndroidAutoSpeechAnswerRetriever', 'getSpeechAnswer', []);
    },	
	IsAndroidAutoConnected: function(successCallback, errorCallback) {
			cordova.exec(successCallback, errorCallback, "AndroidAutoConnectionChecker", "isConnected", []);
	}
    
};
