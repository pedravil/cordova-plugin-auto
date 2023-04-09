var exec = require('cordova/exec');

exports.AndroidAutoSendNotification = function(title, message, successCallback, errorCallback) {
	exec(successCallback, errorCallback, 'AndroidAutoNotificationSender', 'sendNotification', [title, message]);
};
