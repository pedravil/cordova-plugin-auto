/*global cordova, module*/

module.exports = {
    
    AndroidAutoSendNotification: function(title, message) {
        cordova.exec(null, null, 'AndroidAutoNotificationSender', 'sendNotification', [title, message]);
    }
    
};
