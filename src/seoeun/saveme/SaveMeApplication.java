package seoeun.saveme;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import seoeun.saveme.data.ContactsData;
import seoeun.saveme.data.MessagesData;
import android.app.Application;
import android.location.Location;
import android.telephony.SmsManager;
import android.util.Log;

public class SaveMeApplication extends Application {
    
    public static final long TIME = 5000;
    
    private TimeHelper timeHelper;

    private LocationHelper locationHelper;
    
    private Timer timer;
    
    private Listener listener;
    
    @Override
    public void onCreate() {
        super.onCreate();
        timeHelper = new TimeHelper();
        locationHelper = new LocationHelper();
    }
    
    @Override
    public void onTerminate() {
        stop(false);
        super.onTerminate();
    }
    
    /* package */ void setListener(Listener l) {
        this.listener = l;
    }
    
    /* package */ boolean isTriggered() {
        return timeHelper.isStarted();
    }
    
    /* package */ void trigger() {
        if(isTriggered()) {
            Log.d("SaveMe", "No action triggered cause one has been triggered.");
        } else {
            List<Long> contacts = ContactsData.get(this);
            if(contacts != null && !contacts.isEmpty()) {
                Log.d("SaveMe", "Trigger action.");
                start();
            } else {
                Log.d("SaveMe", "No action triggered cause no contact is set.");
            }
        }
    }
    
    /* package */ void cancel() {
        Log.d("SaveMe", "Cancel action.");
        stop(true);
    }
    
    private void fire() {
        sendMessage();
        stop(false);
    }
    
    private void sendMessage() {
        List<Long> contacts = ContactsData.get(this);
        if(contacts != null && !contacts.isEmpty()) {
            StringBuilder buf = new StringBuilder();
            String msg = MessagesData.get(this);
            if(msg != null) {
                buf.append(msg);
            } else {
                buf.append(getString(R.string.msg_save_me));
            }
            Location loc = locationHelper.getBestLocation();
            if(loc != null) {
                buf.append(" [").append(loc.toString()).append("]");
            } else {
                buf.append(" [Unknown Location]");
            }
            msg = buf.toString();
            Log.d("SaveMe", "Send message '" + msg + "'.");
            String[] temp = new String[2];
            for (Long contact : contacts) {
                ContactsData.query(getContentResolver(), contact, temp);
                String dst = temp[1];
                Log.d("SaveMe", "Send message to " + dst);
                SmsManager.getDefault().sendTextMessage(dst, null, msg, null, null);
            }
        }
    }
    
    private void start() {
        Log.d("SaveMe", "Start timer.");
        timeHelper.startTimer();
        locationHelper.requestLocationUpdates(this);
        
        final Listener l = this.listener;
        TimerTask task = new TimerTask() {
            public void run() {
                long remain = TIME - timeHelper.getEllapsedTime();
                if(remain > 0) {
                    if(l != null) {
                        l.onTick(remain);
                    }
                } else {
                    fire();
                    if(l != null) {
                        l.onFire();
                    }
                }
            }
        };
        Timer t = new Timer();
        t.schedule(task, 0, 1000);
        this.timer = t;
    }
    
    private void stop(boolean cancelled) {
        Log.d("SaveMe", "Stop timer.");
        Timer t = this.timer;
        if(t != null) {
            t.cancel();
            t.purge();
            this.timer = null;
        }
        if(cancelled && listener != null) {
            listener.onCancel();
        }
        
        locationHelper.stopLocationUpdates(this);
        timeHelper.stopTimer();
    }
    
    /* package */ interface Listener {
        
        void onTick(long remain);
        
        void onFire();
        
        void onCancel();
        
    }

}
