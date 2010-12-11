package seoeun.saveme;


class TimeHelper {
    
    private long time = -1;
    
    /* package */ TimeHelper() {
    }
    
    /* package */ boolean isStarted() {
        return time >= 0;
    }
    
    /* package */ long getEllapsedTime() {
        return System.currentTimeMillis() - time;
    }
    
    /* package */ void startTimer() {
        this.time = System.currentTimeMillis();
    }
    
    /* package */ void stopTimer() {
        this.time = -1;
    }
    
}
