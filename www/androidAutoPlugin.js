var exec = require('cordova/exec');

var PLUGIN_NAME = 'AndroidAutoPlugin';

exports.setAndroidAuto = function(successCallback, errorCallback) {
	exec(successCallback, errorCallback, PLUGIN_NAME, 'setAndroidAuto', []);
};

exports.sendNotificationAndroidAuto = function(conversationId, title, message, successCallback, errorCallback) {
	exec(successCallback, errorCallback, PLUGIN_NAME, 'sendNotificationAndroidAuto', [conversationId, title, message]);
};

exports.onWaitAnswerAndroidAuto = function(successCallback, errorCallback) {
	exec(successCallback, errorCallback, PLUGIN_NAME, 'onWaitAnswerAndroidAuto', []);
};
