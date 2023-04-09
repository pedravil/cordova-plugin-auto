package com.androidauto.messaging;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class AndroidAutoPlugin extends CordovaPlugin {
  
	Messenger androidAutoMessagingService = null;
	boolean isBound;
	
	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

		if (action.equals("onWaitAnswerAndroidAuto")) {
			this.waitAnswerAndroidAuto(callbackContext);
			return true;
		}
		
		if (action.equals("sendNotificationAndroidAuto")) {
			this.sendNotificationAndroidAuto(callbackContext, args.getString(0), args.getString(1), args.getString(2));
			return true;
		}

	}
	
  	private ServiceConnection serviceConnection = new ServiceConnection() {
		
		public void onServiceConnected(ComponentName className, IBinder service) {
			androidAutoMessagingService = new Messenger(service);
			isBound = true;
		};

		public void onServiceDisconnected(ComponentName className) {
			androidAutoMessagingService = null;
			isBound = false;
		};
	}
  
	private void sendNotificationAndroidAuto(CallbackContext callbackContext, String conversationId, String title, String description) throws JSONException {

		Intent intent = new Intent(getApplicationContext(), AndroidAutoMessagingService.class);

        	bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
		
		if (!isBound) return;
        
        	Message message = Message.obtain();
        
		Bundle bundle = new Bundle();
		bundle.putString("MyString", "This is a message");

		message.setData(bundle);

		try {

			androidAutoMessagingService.send(message);

		} catch (RemoteException e) {
		    e.printStackTrace();
		}
		
	}
  
	private void waitAnswerAndroidAuto(CallbackContext callbackContext) {
		
	}
	
}
