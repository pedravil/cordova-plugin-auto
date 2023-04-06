/*global cordova, module*/

exports.AndroidAutoSendNotification = function(successCallback, errorCallback, title, message) {
	cordova.exec(successCallback, errorCallback, 'AndroidAutoNotificationSender', 'sendNotification', [title, message]);
};

    
exports.AndroidAutoGetSpeechAnswer = function(successCallback, errorCallback) {
	cordova.exec(successCallback, errorCallback, 'AndroidAutoSpeechAnswerRetriever', 'getSpeechAnswer', []);
};


exports.IsAndroidAutoConnected =  function(successCallback, errorCallback) {
	cordova.exec(successCallback, errorCallback, "AndroidAutoConnectionChecker", "isConnected", []);    
};
