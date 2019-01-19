package com.example.minko.mp3cutter;

public class Songs {
    private long mSongID;
    private String mSongTitle;
    private String mSongArtist;
    private String mSongDuration;
    private String mSongSize;

    public Songs() {
    }

    public Songs(String mSongTitle, String mSongDuration) {
        this.mSongTitle = mSongTitle;
        this.mSongDuration = mSongDuration;
    }

    public Songs(String mSongTitle) {
        this.mSongTitle = mSongTitle;
    }

    public Songs(long mSongID, String mSongTitle, String mSongArtist, String mSongDuration, String mSongSize) {
        this.mSongID = mSongID;
        this.mSongTitle = mSongTitle;
        this.mSongArtist = mSongArtist;
        this.mSongDuration = mSongDuration;
        this.mSongSize = mSongSize;
    }

    public long getmSongID() {
        return mSongID;
    }

    public void setmSongID(long mSongID) {
        this.mSongID = mSongID;
    }

    public String getmSongTitle() {
        return mSongTitle;
    }

    public void setmSongTitle(String mSongTitle) {
        this.mSongTitle = mSongTitle;
    }

    public String getmSongArtist() {
        return mSongArtist;
    }

    public void setmSongArtist(String mSongArtist) {
        this.mSongArtist = mSongArtist;
    }

    public String getmSongDuration() {
        return mSongDuration;
    }

    public void setmSongDuration(String mSongDuration) {
        this.mSongDuration = mSongDuration;
    }

    public String getmSongSize() {
        return mSongSize;
    }

    public void setmSongSize(String mSongSize) {
        this.mSongSize = mSongSize;
    }

    @Override
    public String toString() {
        return "Songs{" +
                "mSongID=" + mSongID +
                ", mSongTitle='" + mSongTitle + '\'' +
                ", mSongArtist='" + mSongArtist + '\'' +
                ", mSongDuration='" + mSongDuration + '\'' +
                ", mSongSize='" + mSongSize + '\'' +
                '}';
    }
}