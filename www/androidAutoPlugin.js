var exec = require('cordova/exec');

var PLUGIN_NAME = 'AndroidAutoPlugin';

exports.initialize = function(successCallback, errorCallback) {
	exec(successCallback, errorCallback, PLUGIN_NAME, 'initialize', []);
};

exports.sendNotification = function(conversationId, title, message, successCallback, errorCallback) {
	exec(successCallback, errorCallback, PLUGIN_NAME, 'sendNotification', [conversationId, title, message]);
};

exports.waitAnswer = function(successCallback, errorCallback) {
	exec(successCallback, errorCallback, PLUGIN_NAME, 'waitAnswer', []);
};
