package com.overlay.plugins.camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONException;

import static com.overlay.plugins.camera.OverlayCameraActivity.ERROR_MESSAGE;
import static com.overlay.plugins.camera.OverlayCameraActivity.FILENAME;
import static com.overlay.plugins.camera.OverlayCameraActivity.IMAGE_URI;
import static com.overlay.plugins.camera.OverlayCameraActivity.QUALITY;
import static com.overlay.plugins.camera.OverlayCameraActivity.RESULT_ERROR;
import static com.overlay.plugins.camera.OverlayCameraActivity.TARGET_HEIGHT;
import static com.overlay.plugins.camera.OverlayCameraActivity.TARGET_WIDTH;


public class OverlayCamera extends CordovaPlugin {

    private CallbackContext callbackContext;

	@Override
    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {
	    if (!hasRearFacingCamera()) {
	        callbackContext.error("No rear camera detected");
	        return false;
	    }
	    this.callbackContext = callbackContext;
	    Context context = cordova.getActivity().getApplicationContext();
	    Intent intent = new Intent(context, OverlayCameraActivity.class);
	    intent.putExtra(FILENAME, args.getString(0));
	    intent.putExtra(QUALITY, args.getInt(1));
	    intent.putExtra(TARGET_WIDTH, args.getInt(2));
	    intent.putExtra(TARGET_HEIGHT, args.getInt(3));
	    cordova.startActivityForResult(this, intent, 0);
        return true;
    }

	private boolean hasRearFacingCamera() {
	    Context context = cordova.getActivity().getApplicationContext();
	    return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
	}

	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
	    if (resultCode == Activity.RESULT_OK) {
	        callbackContext.success(intent.getExtras().getString(IMAGE_URI));
	    } else if (resultCode == RESULT_ERROR) {
	        String errorMessage = intent.getExtras().getString(ERROR_MESSAGE);
	        if (errorMessage != null) {
	            callbackContext.error(errorMessage);
	        } else {
	            callbackContext.error("Failed to take picture");
	        }
	    }
    }

}
