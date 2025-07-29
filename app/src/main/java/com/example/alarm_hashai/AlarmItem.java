// AlarmItem.java
package com.example.alarm_hashai;

public class AlarmItem {
    private int id;
    private int hour;
    private int minute;
    private String audioUri;
    private String audioFileName;
    private boolean enabled;
    private long scheduledTime;

    public AlarmItem() {
        
    }

    public AlarmItem(int id, int hour, int minute, String audioUri, String audioFileName, boolean enabled, long scheduledTime) {
        this.id = id;
        this.hour = hour;
        this.minute = minute;
        this.audioUri = audioUri;
        this.audioFileName = audioFileName;
        this.enabled = enabled;
        this.scheduledTime = scheduledTime;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public String getAudioUri() {
        return audioUri;
    }

    public String getAudioFileName() {
        return audioFileName;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public long getScheduledTime() {
        return scheduledTime;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setAudioUri(String audioUri) {
        this.audioUri = audioUri;
    }

    public void setAudioFileName(String audioFileName) {
        this.audioFileName = audioFileName;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setScheduledTime(long scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public String getFormattedTime() {
        return String.format("%02d:%02d", hour, minute);
    }
}