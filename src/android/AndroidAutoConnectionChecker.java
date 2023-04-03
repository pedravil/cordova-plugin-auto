import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.google.android.gms.car.Car;
import com.google.android.gms.car.CarConnectionCallback;
import com.google.android.gms.car.CarInfo;
import com.google.android.gms.car.CarNotConnectedException;

public class AndroidAutoConnectionChecker {

    private Car mCar;
    private boolean mIsConnected;

    /**
     * Check if the phone is connected to Android Auto.
     *
     * @param activity the current activity
     * @return true if the phone is connected to Android Auto, false otherwise
     */
    public boolean isAndroidAutoConnected(Activity activity) {
        Context context = activity.getApplicationContext();
        mCar = Car.createCar(context, mCarConnectionCallback);
        mCar.connect();
        while (!mIsConnected) {
            // Wait for the connection to be established
        }
        boolean isConnected = mIsConnected;
        mCar.disconnect();
        return isConnected;
    }

    private CarConnectionCallback mCarConnectionCallback = new CarConnectionCallback() {
        @Override
        public void onConnected(Car car) {
            try {
                CarInfo carInfo = car.getCarInfo();
                if (carInfo != null && carInfo.getCarConnectionType() == CarInfo.CONNECTION_TYPE_ANDROID_AUTO) {
                    mIsConnected = true;
                }
            } catch (CarNotConnectedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDisconnected(Car car) {
            mIsConnected = false;
        }
    };
}
