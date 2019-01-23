package com.example.minko.mp3cutter.Model;

/**
 * Created by REYANSH on 4/8/2017.
 */

public class SongsModel {

    public String _ID;
    public String mSongsName;
    public String mArtistName;
    public String mDuration;
    public String mPath;
    public String mAlbum;
    public String mFileType;
    public String mAlbumId;

    public String get_ID() {
        return _ID;
    }

    public void set_ID(String _ID) {
        this._ID = _ID;
    }

    public String getmSongsName() {
        return mSongsName;
    }

    public void setmSongsName(String mSongsName) {
        this.mSongsName = mSongsName;
    }

    public String getmArtistName() {
        return mArtistName;
    }

    public void setmArtistName(String mArtistName) {
        this.mArtistName = mArtistName;
    }

    public String getmDuration() {
        return mDuration;
    }

    public void setmDuration(String mDuration) {
        this.mDuration = mDuration;
    }

    public String getmPath() {
        return mPath;
    }

    public void setmPath(String mPath) {
        this.mPath = mPath;
    }

    public String getmAlbum() {
        return mAlbum;
    }

    public void setmAlbum(String mAlbum) {
        this.mAlbum = mAlbum;
    }

    public String getmFileType() {
        return mFileType;
    }

    public void setmFileType(String mFileType) {
        this.mFileType = mFileType;
    }

    public String getmAlbumId() {
        return mAlbumId;
    }

    public void setmAlbumId(String mAlbumId) {
        this.mAlbumId = mAlbumId;
    }

    public SongsModel(String _ID,
                      String songsName,
                      String artistName,
                      String duration,
                      String album,
                      String path,
                      String albumId,
                      String fileType) {
        this._ID = _ID;
        mSongsName = songsName;
        mArtistName = artistName;
        mDuration = duration;
        mPath = path;
        mAlbum = album;
        mAlbumId=albumId;
        mFileType = fileType;
    }

}
