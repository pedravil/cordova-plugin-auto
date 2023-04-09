var exec = require('cordova/exec');

exports.sendNotificationAndroidAuto = function(title, message, successCallback, errorCallback) {
	exec(successCallback, errorCallback, 'AndroidAutoPlugin', 'sendNotification', [title, message]);
};

exports.onWaitAnswerAndroidAuto = function(successCallback, errorCallback) {
	exec(successCallback, errorCallback, 'AndroidAutoPlugin', 'onWaitAnswerAndroidAuto', []);
};
