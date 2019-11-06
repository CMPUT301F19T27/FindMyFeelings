package com.example.findmyfeelings;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.util.Calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FindMyFeelingsStorage  {

    private Context context;
    private SQLiteDatabase db;
    private static final String TABLE_NAME_MOOD = "mood";
    private static final String TABLE_NAME_FOLLOWER = "follower";
    private static final String TABLE_NAME_FOLLOWING = "following";
    private static final String TABLE_NAME_USER = "user";
    private static final String TABLE_NAME_REQUEST = "request";


    public FindMyFeelingsStorage(Context ctx) {
        context = ctx;
        FindMyFeelingsStorageHelper callHelper = new FindMyFeelingsStorageHelper(context);
        db = callHelper.getWritableDatabase();
    }

    public void close() {
        db.close();
    }

    public ArrayList<User> getUsers() {

        ArrayList<User> users = new ArrayList<User>();

        Cursor c = db.query(TABLE_NAME_USER, null, null, null, null, null,
                "id DESC");

        while (c.moveToNext()) {
            User item = new User(c.getString(c.getColumnIndex("email")),
                    c.getString(c.getColumnIndex("username")),
                    c.getString(c.getColumnIndex("firstname")),
                    c.getString(c.getColumnIndex("lastname"))
                    );

            users.add(item);

            getFollowers(c.getLong(c.getColumnIndex("id")), item);

            getFollowings(c.getLong(c.getColumnIndex("id")), item);

            getRequests(c.getLong(c.getColumnIndex("id")), item);

            getMoods(c.getLong(c.getColumnIndex("id")), item);

        }
        c.close();

        return users;
    }

    private void getMoods(long id, User item) {
        Cursor c1 = db.rawQuery( "select  strftime('%Y.%m.%d %H:%M', mood.time) as time, mood.mood as mood, mood.reason as reason "
                        +" from " + TABLE_NAME_MOOD + " as mood "
                         + " where mood.user_id=?",
                new String[] { Long.toString(id) }
        );

        long tm = 0;
        Mood recentMode = null;

        while (c1.moveToNext()) {

            String s = c1.getString(c1.getColumnIndex("time") );
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm");
            try {
                Date date = formatter.parse(s);
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);


                Mood mood = new Mood(
                        cal.get(Calendar.DAY_OF_MONTH),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.HOUR),
                        cal.get(Calendar.MINUTE),
                        c1.getString(c1.getColumnIndex("mood")),
                        c1.getString(c1.getColumnIndex("reason"))
                );

                item.addMood(mood);

                if (tm<date.getTime()) {
                    recentMode = mood;
                    tm = date.getTime();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        c1.close();
        //item.setRecentMood(recentMode);
    }

    private void getFollowers(long id, User item) {
        Cursor c1 = db.rawQuery( "select follower.id as id, follower.email as email, strftime('%Y.%m.%d %H:%M', mood.time) as time, mood.mood as mood, mood.reason as reason "
                        +" from "  + TABLE_NAME_FOLLOWER + " as follower "
                        +" left join " + TABLE_NAME_MOOD + " as mood "
                        +" on follower.user_id=mood.user_id "
                        + " where follower.user_id=?",
                new String[] { Long.toString(id) }
        );
        while (c1.moveToNext()) {
            Follower follower = new Follower();
            follower.setEmail( c1.getString(c1.getColumnIndex("email") ));

            String s = c1.getString(c1.getColumnIndex("time") );
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm");
            try {
                Date date = formatter.parse(s);
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);

                Mood mood = new Mood(
                        cal.get(Calendar.DAY_OF_MONTH),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.HOUR),
                        cal.get(Calendar.MINUTE),
                        c1.getString(c1.getColumnIndex("mood")),
                        c1.getString(c1.getColumnIndex("reason"))
                );

                follower.setRecentMood( mood );

                item.addFollower(follower);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        c1.close();
    }

    private void getFollowings(long id, User item) {
        Cursor c1 = db.rawQuery( "select following.id as id, following.email as email, strftime('%Y.%m.%d %H:%M', mood.time) as time, mood.mood as mood, mood.reason as reason "
                        +" from "  + TABLE_NAME_FOLLOWING + " as following "
                        +" left join " + TABLE_NAME_MOOD + " as mood "
                        +" on following.user_id=mood.user_id "
                        + " where following.user_id=?",
                new String[] { Long.toString(id) }
        );
        while (c1.moveToNext()) {
            Following following = new Following();
            following.setEmail( c1.getString(c1.getColumnIndex("email") ));

            String s = c1.getString(c1.getColumnIndex("time") );
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm");
            try {
                Date date = formatter.parse(s);
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);

                Mood mood = new Mood(
                        cal.get(Calendar.DAY_OF_MONTH),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.HOUR),
                        cal.get(Calendar.MINUTE),
                        c1.getString(c1.getColumnIndex("mood")),
                        c1.getString(c1.getColumnIndex("reason"))
                );

                following.setRecentMood( mood.getMood() );

                item.addFollowing(following);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        c1.close();
    }

    private void getRequests(long id, User item) {
        Cursor c1 = db.rawQuery( "select request.id as id, request.request as request "
                        +" from "  + TABLE_NAME_REQUEST + " as request "
                        + " where request.user_id=?",
                new String[] { Long.toString(id) }
        );
        while (c1.moveToNext()) {
                item.addRequest(c1.getString(c1.getColumnIndex("request")));
        }

        c1.close();
    }

    public boolean addUser(User item) {
        if (item == null)
            return false;

        ContentValues values = new ContentValues();

        values.put("email", item.getEmail());
        values.put("username", item.getUsername());
        values.put("firstname", item.getFirstName());
        values.put("lastname", item.getLastName());

        int userID = (int) db.insert(TABLE_NAME_USER, null, values);

        addFollowers(userID, item.getFollowersList());
        addFollowings(userID, item.getFollowingList());
        addRequests(userID, item.getRequestList());
        addMoods(userID, item.getMyMoods());

        return userID>=0;
    }

    private void addMoods(int userID, ArrayList<Mood> moods) {
        for(Mood mood: moods) {
            ContentValues values1 = new ContentValues();

            Calendar cal = Calendar.getInstance();
            cal.set(mood.getDateYear(), mood.getDateMonth(), mood.getDateDay(), mood.getTimeHour(), mood.getTimeMinute()  );

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String s = dateFormat.format(cal.getTime());
            values1.put("time", s);
            values1.put("mood", mood.getMood());
            values1.put("reason", mood.getReason());
            values1.put("user_id", userID);

            db.insert(TABLE_NAME_MOOD, null, values1);
        }
    }

    private void addRequests(int userID, ArrayList<String> requestList) {
        for(String request: requestList) {
            ContentValues values1 = new ContentValues();

            values1.put("request", request);
            values1.put("user_id", userID);

            db.insert(TABLE_NAME_REQUEST, null, values1);
        }
    }

    private void addFollowings(int userID, ArrayList<Following> followings) {
        for(Following following: followings) {
            ContentValues values1 = new ContentValues();

            values1.put("email", following.getEmail());
            values1.put("user_id", userID);

            db.insert(TABLE_NAME_FOLLOWING, null, values1);
        }
    }

    private void addFollowers(int userID, ArrayList<Follower> followers) {
        for(Follower follower: followers) {
            ContentValues values1 = new ContentValues();

            values1.put("email", follower.getEmail());
            values1.put("user_id", userID);

            db.insert(TABLE_NAME_FOLLOWER, null, values1);
        }
    }

    public boolean removeUser(long userID) {

        removeMood(userID);
        removeFollower(userID);
        removeFollowing(userID);
        removeRequest(userID);

        int num = db.delete(TABLE_NAME_USER, "id=?", new String[] { Long.toString(userID) });

        return num>0;
    }

    public boolean removeMood(long userID) {

        int num =db.delete(TABLE_NAME_MOOD, "user_id=?", new String[] { Long.toString(userID) });

        return num>0;
    }

    public boolean removeFollower(long userID) {

        int num =db.delete(TABLE_NAME_FOLLOWER, "user_id=?", new String[] { Long.toString(userID) });

        return num>0;
    }

    public boolean removeFollowing(long userID) {

        int num =db.delete(TABLE_NAME_FOLLOWING, "user_id=?", new String[] { Long.toString(userID) });

        return num>0;
    }

    public boolean removeRequest(long userID) {

        int num =db.delete(TABLE_NAME_REQUEST, "user_id=?", new String[] { Long.toString(userID) });

        return num>0;
    }


    class FindMyFeelingsStorageHelper extends SQLiteOpenHelper {

        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "find_my_feelings";

        FindMyFeelingsStorageHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL("CREATE TABLE "
                    + TABLE_NAME_MOOD +
                    " ( id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "   time TimeStamp NOT NULL DEFAULT (datetime('now','localtime'))," +
                    "   mood TEXT NOT NULL," +
                    "   reason TEXT," +
                    "   user_id INTEGER NOT NULL" +
                    ");"
            );

            db.execSQL("CREATE TABLE "
                    + TABLE_NAME_FOLLOWER +
                    " ( id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "   email TEXT NOT NULL," +
                    "   user_id INTEGER NOT NULL" +
                    ");"
            );

            db.execSQL("CREATE TABLE "
                    + TABLE_NAME_FOLLOWING +
                    " ( id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "   email TEXT NOT NULL," +
                    "   user_id INTEGER" +
                    ");"
            );

            db.execSQL("CREATE TABLE "
                    + TABLE_NAME_USER +
                    " ( id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "   email TEXT NOT NULL," +
                    "   username TEXT NOT NULL," +
                    "   firstname TEXT," +
                    "   lastname TEXT" +
                    ");"
            );

            db.execSQL("CREATE TABLE "
                    + TABLE_NAME_REQUEST +
                    " ( id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "   request TEXT NOT NULL," +
                    "   user_id INTEGER" +
                    ");"
            );


        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //just a demo
            if (oldVersion<2) {
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MOOD + ";");
                onCreate(db);
            } else if (oldVersion<3) {
                db.execSQL("ALTER TABLE " + TABLE_NAME_MOOD + " ADD reason TEXT;");
            }
        }
    }


}
