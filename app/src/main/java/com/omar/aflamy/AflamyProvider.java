package com.omar.aflamy;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;

import java.net.URISyntaxException;

public class AflamyProvider extends ContentProvider {

    // Creates a UriMatcher object.
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private AflamyDB dbHelper; //database helper object
    private SQLiteDatabase db; //database object

    static {
        uriMatcher.addURI(Aflamy.CONTENT_AUTHORITY, Aflamy.MOVIES_TABLE, Aflamy.MOVIES_MATCH);
        uriMatcher.addURI(Aflamy.CONTENT_AUTHORITY, Aflamy.MOVIES_TABLE+"/#", Aflamy.MOVIE_MATCH);
        //uriMatcher.addURI(Aflamy.CONTENT_AUTHORITY, Aflamy.MOVIES_TABLE+"/*", Aflamy.FAVORITES_MATCH);
        uriMatcher.addURI(Aflamy.CONTENT_AUTHORITY, Aflamy.TRAILERS_TABLE+"/#", Aflamy.TRAILERS_MATCH);
        uriMatcher.addURI(Aflamy.CONTENT_AUTHORITY, Aflamy.REVIEWS_TABLE+"/#", Aflamy.REVIEWS_MATCH);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new AflamyDB(getContext());
        return true;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)){
            case Aflamy.MOVIE_MATCH:
                db.delete(Aflamy.MOVIES_TABLE,Aflamy.ID_COLUMN+" == "+uri.getLastPathSegment(),null);
                db.delete(Aflamy.TRAILERS_TABLE,Aflamy.ID_COLUMN+" == "+uri.getLastPathSegment(),null);
                db.delete(Aflamy.REVIEWS_TABLE,Aflamy.ID_COLUMN+" == "+uri.getLastPathSegment(),null);
                getContext().getContentResolver().notifyChange(uri, null);
                return 1;
            default:
                throw new UnsupportedOperationException("URI not matched!");
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        db = dbHelper.getWritableDatabase();
        Uri newUri = null;
        switch (uriMatcher.match(uri)){
            case Aflamy.MOVIES_MATCH:{
                long id = db.insert(Aflamy.MOVIES_TABLE,null,contentValues);
                if(id>0){
                    newUri = ContentUris.withAppendedId(Aflamy.MOVIES_URI, id);
                    getContext().getContentResolver().notifyChange(newUri, null);
                    break;
                }
            }
            case Aflamy.TRAILERS_MATCH:{
                long id = db.insert(Aflamy.TRAILERS_TABLE,null,contentValues);
                if(id>0){
                    newUri = ContentUris.withAppendedId(Aflamy.TRAILERS_URI, id);
                    getContext().getContentResolver().notifyChange(newUri, null);
                    break;
                }
            }
            case Aflamy.REVIEWS_MATCH:{
                long id = db.insert(Aflamy.REVIEWS_TABLE,null,contentValues);
                if(id>0){
                    newUri = ContentUris.withAppendedId(Aflamy.REVIEWS_URI, id);
                    getContext().getContentResolver().notifyChange(newUri, null);
                    break;
                }
            }
            default: throw new UnsupportedOperationException("URI not matched!");
        }
        db.close();
        return newUri;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        db = dbHelper.getReadableDatabase();
        Cursor newCursor = null;
        switch (uriMatcher.match(uri)){
            case Aflamy.MOVIES_MATCH:{
                newCursor = db.query(Aflamy.MOVIES_TABLE,projection,selection,selectionArgs,null,null,sortOrder);
                newCursor.setNotificationUri(getContext().getContentResolver(), uri);
                break;
            }
            case Aflamy.MOVIE_MATCH:{
                newCursor = db.query(Aflamy.MOVIES_TABLE, projection, Aflamy.ID_COLUMN+" == "+uri.getLastPathSegment(),selectionArgs,null,null,sortOrder);
                newCursor.setNotificationUri(getContext().getContentResolver(), uri);
                break;
            }
            case Aflamy.TRAILERS_MATCH:{
                newCursor = db.query(Aflamy.TRAILERS_TABLE, projection, Aflamy.ID_COLUMN+" == "+uri.getLastPathSegment(),selectionArgs,null,null,sortOrder);
                newCursor.setNotificationUri(getContext().getContentResolver(), uri);
                break;
            }
            case Aflamy.REVIEWS_MATCH:{
                newCursor = db.query(Aflamy.REVIEWS_TABLE, projection, Aflamy.ID_COLUMN+" == "+uri.getLastPathSegment(),selectionArgs,null,null,sortOrder);
                newCursor.setNotificationUri(getContext().getContentResolver(), uri);
                break;
            }
            default: throw new UnsupportedOperationException("URI not matched!");
        }
        //db.close();   // causes crash!!
        return newCursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    /**********************************DB Helper***********************************/
    private class AflamyDB extends SQLiteOpenHelper{

        public AflamyDB(Context context) {
            super(context,Aflamy.DATABASE_NAME,null,Aflamy.DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL("CREATE TABLE "+Aflamy.MOVIES_TABLE+
                    " ("+Aflamy._ID_COLUMN+" INTEGER PRIMARY KEY AUTOINCREMENT, "+Aflamy.ID_COLUMN+" TEXT NOT NULL, "+
                    Aflamy.THUMB_COLUMN+" TEXT NOT NULL, "+Aflamy.TITLE_COLUMN+" TEXT NOT NULL, "+
                    Aflamy.DESCRIPTION_COLUMN+" TEXT NOT NULL, "+Aflamy.DATE_COLUMN+" TEXT NOT NULL, "+
                    Aflamy.VOTE_COLUMN+" TEXT NOT NULL, "+Aflamy.DURATION_COLUMN+" TEXT NOT NULL);");
            db.execSQL("CREATE TABLE "+Aflamy.TRAILERS_TABLE+
                    " ("+Aflamy._ID_COLUMN+" INTEGER PRIMARY KEY AUTOINCREMENT, "+Aflamy.ID_COLUMN+" TEXT NOT NULL, "+
                    Aflamy.KEY_COLUMN+" TEXT NOT NULL, "+Aflamy.NAME_COLUMN+" TEXT NOT NULL);");
            db.execSQL("CREATE TABLE "+Aflamy.REVIEWS_TABLE+
                    " ("+Aflamy._ID_COLUMN+" INTEGER PRIMARY KEY AUTOINCREMENT, "+Aflamy.ID_COLUMN+" TEXT NOT NULL, "+
                    Aflamy.AUTHOR_COLUMN+" TEXT NOT NULL, "+Aflamy.CONTENT_COLUMN+" TEXT NOT NULL);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL("DROP TABLE IF EXISTS " + Aflamy.MOVIES_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + Aflamy.REVIEWS_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + Aflamy.TRAILERS_TABLE);
            onCreate(db);
        }
    }
}
