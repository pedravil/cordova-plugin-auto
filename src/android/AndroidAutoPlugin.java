package com.androidauto.messaging;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.ComponentName;
import android.content.IntentFilter;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import android.util.Log;

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
  
	private final static boolean DEBUG = true;
	private final static String TAG = AndroidAutoPlugin.class.getSimpleName();
	
	private Context pluginContext = null;
	private static CallbackContext callbackContext = null;
	
	Messenger androidAutoMessagingService = null;
	boolean isBound;
	
	
	public AndroidAutoPlugin() {}

		
	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

		
		if (DEBUG) Log.d(TAG, "Method: execute: " + action);
		
		if (action.equals("initialize")) {
			this.initialize(callbackContext);
			return true;
		}
		
		if (action.equals("sendNotification")) {
			this.sendNotification(callbackContext, Integer.parseInt(args.getString(0)), args.getString(1), args.getString(2), args.getString(3));
			return true;
		}
		
		return false;
	}
	
  	private ServiceConnection serviceConnection = new ServiceConnection() {
		
		public void onServiceConnected(ComponentName className, IBinder service) {
			androidAutoMessagingService = new Messenger(service);
			isBound = true;
		}

		public void onServiceDisconnected(ComponentName className) {
			androidAutoMessagingService = null;
			isBound = false;
		}
	};

	private void initialize(CallbackContext callbackContext) {
	
		this.pluginContext = this.cordova.getActivity().getApplicationContext();
		AndroidAutoPlugin.callbackContext = callbackContext;

		Intent intent = new Intent(pluginContext, AndroidAutoMessagingService.class);
        pluginContext.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
	
	}
	
	public static void getNotificationFromFCM(Bundle bundle) {

		final CallbackContext callbackContext = AndroidAutoPlugin.callbackContext;

		if (callbackContext != null && bundle != null) {
		
		JSONObject json = new JSONObject();

		Set<String> keys = bundle.keySet();

		for (String key : keys) {
			try {

				json.put(key, bundle.get(key));

			} catch (JSONException e) { return; }
		}

		PluginResult pluginresult = new PluginResult(PluginResult.Status.OK, json);

		pluginresult.setKeepCallback(true);

		callbackContext.sendPluginResult(pluginresult);

	}
  
	private void sendNotification(CallbackContext callbackContext, int conversationId, String from, String title, String body) {
		
		if (DEBUG) Log.d(TAG, "Method: sendNotification");
		
		if (DEBUG) Log.d(TAG, "isBound: " + isBound);
		
		if (!isBound) return;

		try {

			IntentFilter filterReplyAction = new IntentFilter(AndroidAutoMessagingService.REPLY_ACTION);
			IntentFilter filterReadAction = new IntentFilter(AndroidAutoMessagingService.READ_ACTION);

			pluginContext.registerReceiver(new AndroidAutoMessagingReplyReceiver(callbackContext), filterReplyAction);
			pluginContext.registerReceiver(new AndroidAutoMessagingReadReceiver(), filterReadAction);
			
			Message message = Message.obtain();
			
			Bundle data = new Bundle();

			data.putString(AndroidAutoMessagingService.CONVERSATION_ID, String.valueOf(conversationId));
			data.putString(AndroidAutoMessagingService.FROM, from);
			data.putString(AndroidAutoMessagingService.TITLE, title);
			data.putString(AndroidAutoMessagingService.BODY, body);

			message.setData(data);

			androidAutoMessagingService.send(message);

		} catch (Exception e) {
		    e.printStackTrace();
		}
		
	}
  
}
