package edu.cuhk.csci3310.mediaplayer;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public class MediaModel implements Serializable {
    String imagePath;
    String path;
    String title;
    String duration;

    public MediaModel(String path, String title, String duration, String imagePath) {
        this.imagePath = imagePath;
        this.path = path;
        this.title = title;
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getFormteedDuration() {
        if (this.duration == null) {
            return "Time not available";
        }
        long millis = Long.parseLong(this.duration);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(minutes);
        return String.format("%02d:%02d", minutes, seconds);
    }

}