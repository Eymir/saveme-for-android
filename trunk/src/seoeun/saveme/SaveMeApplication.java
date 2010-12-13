package seoeun.saveme;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import seoeun.saveme.data.ContactsData;
import seoeun.saveme.data.MessagesData;
import android.app.Application;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.telephony.SmsManager;

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
        // TODO think about that the following line is OK with being here.
        // this line is added for updating location in early stage cause the process takes long time.
        locationHelper.requestLocationUpdates(this);
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
            SaveMeLogger.d("No action triggered cause one has been triggered.");
        } else {
            List<Long> contacts = ContactsData.get(this);
            if(contacts != null && !contacts.isEmpty()) {
            	SaveMeLogger.d("Trigger action.");
                start();
            } else {
            	SaveMeLogger.d("No action triggered cause no contact is set.");
            }
        }
    }
    
    /* package */ void cancel() {
    	SaveMeLogger.d("Cancel action.");
        stop(true);
    }
    
    private void fire() {
        sendMessage();
        stop(false);
    }
    
    private void sendMessage() {
        List<Long> contacts = ContactsData.get(this);
        if(contacts != null && !contacts.isEmpty()) {
            // message
            String msg = MessagesData.get(this);
            if(msg == null || msg.length() <= 0) {
            	msg = getString(R.string.msg_save_me_default_message);
            }
            
            // location
            String loc;
            Location location = locationHelper.getBestLocation();
            if(location != null) {
            	double latitude = location.getLatitude();
            	double longitude = location.getLongitude();
            	Geocoder coder = new Geocoder(this);
            	List<Address> addresses;
				try {
					addresses = coder.getFromLocation(latitude, longitude, 1);
	            	if(addresses != null && !addresses.isEmpty()) {
	            		Address address = addresses.get(0);
	            		if(address != null) {
	            			loc = address.getThoroughfare();
	            		} else {
	            			loc = null;
	            		}
	            	} else {
	            		loc = null;
	            	}
				} catch (IOException e) {
					loc = null;
					e.printStackTrace();
				}
            	
				if(loc != null && loc.length() > 0) {
	            	loc = loc + " " + getString(R.string.url_maps, latitude, longitude);
				} else {
					loc = getString(R.string.url_maps, latitude, longitude);
				}
            } else {
            	loc = getString(R.string.msg_save_me_unknown_location);
            }
            
            msg = getString(R.string.msg_save_me, msg, loc);
            SaveMeLogger.d("Send message: " + msg);
            String[] temp = new String[2];
            for (Long contact : contacts) {
                ContactsData.query(getContentResolver(), contact, temp);
                String dst = temp[1];
                try {
                    SmsManager.getDefault().sendTextMessage(dst, null, msg, null, null);
                    SaveMeLogger.d("Success to send message to " + dst);
                } catch(Exception e) {
                	// try once more
                	try {
                        SmsManager.getDefault().sendTextMessage(dst, null, msg, null, null);
                        SaveMeLogger.d("Success to send message to " + dst);
                	} catch(Exception ex) {
                		SaveMeLogger.d("Failed to send message to " + dst);
                		ex.printStackTrace();
                	}
                }
            }
        }
    }
    
    private void start() {
    	SaveMeLogger.d("Start timer.");
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
    	SaveMeLogger.d("Stop timer.");
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
