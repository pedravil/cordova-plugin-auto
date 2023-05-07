var exec = require('cordova/exec');

var PLUGIN_NAME = 'AndroidAutoPlugin';

exports.initialize = function(successCallback, errorCallback) {
	exec(successCallback, errorCallback, PLUGIN_NAME, 'initialize', []);
};

exports.sendNotification = function(conversationId, from, title, message, successCallback, errorCallback) {
	exec(successCallback, errorCallback, PLUGIN_NAME, 'sendNotification', [conversationId, from, title, message]);
};
