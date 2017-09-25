package com.example.zhouzhou.mynote.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by zhouzhou on 2017/9/20.
 */

public class NoteProvider extends ContentProvider{
    private NoteDAO mNoteDAO;
    @Override
    public boolean onCreate() {
        mNoteDAO = new NoteDAO(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Log.v("LOG","NoteProvider -- query()");
        Cursor c = mNoteDAO.queryNote(selection, selectionArgs);
        // Log.v("LOG", "id=" + c.getInt(1) + " content=" + c.getString(2));
        return c;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        mNoteDAO.insertNote(values);
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return mNoteDAO.deleteNote(selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return mNoteDAO.updateNote(values, selection, selectionArgs);
    }
}
