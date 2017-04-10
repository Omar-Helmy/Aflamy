package com.omar.aflamy;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.net.Uri;

/**
 * Created by Omar on 31/07/2016.
 */
public abstract class Aflamy {

    /*****************Movies API*******************/
    private static final String API_KEY = "4b3e9ea2eb637b5831c06e63bac47120";
    public static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185/";
    public static final String SORT_POPULARITY = "popular";
    public static final String SORT_PREF_KEY = "sort_list_pref";
    public static String ID;
    public static String[] IDs,THUMBs;
    public static  boolean TABLET_MODE = false;
    public static String TRAILER;

    /***************Content Provider and DB*************/
    // Database:
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "aflamy.db";
    public static final String MOVIES_TABLE = "movies";
    public static final String TRAILERS_TABLE = "favorites";
    public static final String REVIEWS_TABLE = "reviews";
    // database columns:
    public static final String _ID_COLUMN = "_id";
    public static final String ID_COLUMN = "id";
    public static final String THUMB_COLUMN = "thumb";
    public static final String TITLE_COLUMN = "title";
    public static final String DESCRIPTION_COLUMN = "description";
    public static final String DATE_COLUMN = "date";
    public static final String VOTE_COLUMN = "vote";
    public static final String DURATION_COLUMN = "duration";
    public static final String FAVORITE_COLUMN = "favorite";
    public static final String KEY_COLUMN = "key";
    public static final String NAME_COLUMN = "name";
    public static final String AUTHOR_COLUMN = "author";
    public static final String CONTENT_COLUMN = "content";
    // Content Provider:
    public static final String CONTENT_AUTHORITY = "com.omar.aflamy.provider";
    public static final String CONTENT_URI = "content://"+CONTENT_AUTHORITY+"/";
    public static final int MOVIE_MATCH = 0, MOVIES_MATCH = 1, FAVORITES_MATCH = 2, TRAILERS_MATCH = 3, REVIEWS_MATCH = 4;
    // UriMatcher:
    public static final Uri MOVIES_URI = Uri.parse(CONTENT_URI+MOVIES_TABLE);
    public static final Uri TRAILERS_URI = Uri.parse(CONTENT_URI+TRAILERS_TABLE);
    public static final Uri REVIEWS_URI = Uri.parse(CONTENT_URI+REVIEWS_TABLE);


    public static String getMovieUrl(){
        return "http://api.themoviedb.org/3/movie/"+ID+"?api_key="+API_KEY+"&append_to_response=trailers,reviews";
    }

    public static String getMoviesUrl(String sortBy) {
        return "http://api.themoviedb.org/3/movie/"+sortBy+"?api_key="+API_KEY;

    }

}
/*******************************Tutorials**********************************
 *
 * https://guides.codepath.com/android/Handling-Scrolls-with-CoordinatorLayout
 * https://guides.codepath.com/android/Heterogenous-Layouts-inside-RecyclerView
 * https://guides.codepath.com/android/using-the-recyclerview
 * http://blog.grafixartist.com/pinterest-masonry-layout-staggered-grid/
 * https://mzgreen.github.io/2015/02/15/How-to-hideshow-Toolbar-when-list-is-scroling(part1)/
 * http://saulmm.github.io/mastering-coordinator
 * http://bartinger.at/listview-with-sectionsseparators/

 * **/
