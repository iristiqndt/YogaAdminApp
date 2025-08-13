package com.tiqndt.yogaadminapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "YogaStudio.db";

    private static final String TABLE_COURSES = "courses";
    private static final String KEY_ID = "id";
    private static final String KEY_TYPE = "type";
    private static final String KEY_DAY = "day";
    private static final String KEY_TIME = "time";
    private static final String KEY_CAPACITY = "capacity";
    private static final String KEY_PRICE = "price";
    private static final String KEY_DURATION = "duration";
    private static final String KEY_DESCRIPTION = "description";

    private static final String TABLE_INSTANCES = "instances";
    private static final String KEY_INSTANCE_ID = "instance_id";
    private static final String KEY_INSTANCE_DATE = "instance_date";
    private static final String KEY_TEACHER = "teacher_name";
    private static final String KEY_COMMENTS = "comments";
    private static final String KEY_COURSE_ID_FK = "course_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_COURSES_TABLE = "CREATE TABLE " + TABLE_COURSES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TYPE + " TEXT,"
                + KEY_DAY + " TEXT,"
                + KEY_TIME + " TEXT,"
                + KEY_CAPACITY + " INTEGER,"
                + KEY_PRICE + " REAL,"
                + KEY_DURATION + " INTEGER,"
                + KEY_DESCRIPTION + " TEXT" + ")";
        db.execSQL(CREATE_COURSES_TABLE);

        String CREATE_INSTANCES_TABLE = "CREATE TABLE " + TABLE_INSTANCES + "("
                + KEY_INSTANCE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_INSTANCE_DATE + " TEXT,"
                + KEY_TEACHER + " TEXT,"
                + KEY_COMMENTS + " TEXT,"
                + KEY_COURSE_ID_FK + " INTEGER,"
                + "FOREIGN KEY(" + KEY_COURSE_ID_FK + ") REFERENCES " + TABLE_COURSES + "(" + KEY_ID + ")"
                + ")";
        db.execSQL(CREATE_INSTANCES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSTANCES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
        onCreate(db);
    }

    public void addCourse(Course course) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TYPE, course.getType());
        values.put(KEY_DAY, course.getDay());
        values.put(KEY_TIME, course.getTime());
        values.put(KEY_CAPACITY, course.getCapacity());
        values.put(KEY_PRICE, course.getPrice());
        values.put(KEY_DURATION, course.getDuration());
        values.put(KEY_DESCRIPTION, course.getDescription());
        db.insert(TABLE_COURSES, null, values);
        db.close();
    }

    public Course getCourse(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_COURSES,
                new String[] { KEY_ID, KEY_TYPE, KEY_DAY, KEY_TIME, KEY_CAPACITY, KEY_PRICE, KEY_DURATION,
                        KEY_DESCRIPTION },
                KEY_ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null);

        Course course = null;
        if (cursor != null && cursor.moveToFirst()) {
            course = new Course(
                    cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getInt(4), cursor.getDouble(5),
                    cursor.getInt(6), cursor.getString(7));
        }
        if (cursor != null) {
            cursor.close();
        }
        return course;
    }

    public List<Course> getAllCourses() {
        List<Course> courseList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_COURSES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Course course = new Course();
                course.setId(cursor.getInt(0));
                course.setType(cursor.getString(1));
                course.setDay(cursor.getString(2));
                course.setTime(cursor.getString(3));
                course.setCapacity(cursor.getInt(4));
                course.setPrice(cursor.getDouble(5));
                course.setDuration(cursor.getInt(6));
                course.setDescription(cursor.getString(7));
                courseList.add(course);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return courseList;
    }

    public List<Course> searchCourses(String keyword) {
        List<Course> courseList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_COURSES + " WHERE " +
                KEY_TYPE + " LIKE ? OR " +
                KEY_DAY + " LIKE ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[] { "%" + keyword + "%", "%" + keyword + "%" });

        if (cursor.moveToFirst()) {
            do {
                Course course = new Course();
                course.setId(cursor.getInt(0));
                course.setType(cursor.getString(1));
                course.setDay(cursor.getString(2));
                course.setTime(cursor.getString(3));
                course.setCapacity(cursor.getInt(4));
                course.setPrice(cursor.getDouble(5));
                course.setDuration(cursor.getInt(6));
                course.setDescription(cursor.getString(7));
                courseList.add(course);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return courseList;
    }

    public int updateCourse(Course course) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TYPE, course.getType());
        values.put(KEY_DAY, course.getDay());
        values.put(KEY_TIME, course.getTime());
        values.put(KEY_CAPACITY, course.getCapacity());
        values.put(KEY_PRICE, course.getPrice());
        values.put(KEY_DURATION, course.getDuration());
        values.put(KEY_DESCRIPTION, course.getDescription());
        int rowsAffected = db.update(TABLE_COURSES, values, KEY_ID + " = ?",
                new String[] { String.valueOf(course.getId()) });
        db.close();
        return rowsAffected;
    }

    public void deleteCourse(Course course) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COURSES, KEY_ID + " = ?",
                new String[] { String.valueOf(course.getId()) });
        db.close();
    }

    public void addInstance(ClassInstance instance) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_INSTANCE_DATE, instance.getDate());
        values.put(KEY_TEACHER, instance.getTeacherName());
        values.put(KEY_COMMENTS, instance.getComments());
        values.put(KEY_COURSE_ID_FK, instance.getCourseId());
        db.insert(TABLE_INSTANCES, null, values);
        db.close();
    }

    public ClassInstance getInstance(int instanceId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_INSTANCES,
                new String[] { KEY_INSTANCE_ID, KEY_INSTANCE_DATE, KEY_TEACHER, KEY_COMMENTS, KEY_COURSE_ID_FK },
                KEY_INSTANCE_ID + "=?", new String[] { String.valueOf(instanceId) }, null, null, null, null);

        ClassInstance instance = null;
        if (cursor != null && cursor.moveToFirst()) {
            instance = new ClassInstance();
            instance.setId(cursor.getInt(0));
            instance.setDate(cursor.getString(1));
            instance.setTeacherName(cursor.getString(2));
            instance.setComments(cursor.getString(3));
            instance.setCourseId(cursor.getInt(4));
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return instance;
    }

    @SuppressLint("Range")
    public List<ClassInstance> getAllInstancesForCourse(int courseId) {
        List<ClassInstance> instanceList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_INSTANCES + " WHERE " + KEY_COURSE_ID_FK + " = " + courseId;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                ClassInstance instance = new ClassInstance();
                instance.setId(cursor.getInt(cursor.getColumnIndex(KEY_INSTANCE_ID)));
                instance.setDate(cursor.getString(cursor.getColumnIndex(KEY_INSTANCE_DATE)));
                instance.setTeacherName(cursor.getString(cursor.getColumnIndex(KEY_TEACHER)));
                instance.setComments(cursor.getString(cursor.getColumnIndex(KEY_COMMENTS)));
                instance.setCourseId(cursor.getInt(cursor.getColumnIndex(KEY_COURSE_ID_FK)));
                instanceList.add(instance);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return instanceList;
    }

    public int updateInstance(ClassInstance instance) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_INSTANCE_DATE, instance.getDate());
        values.put(KEY_TEACHER, instance.getTeacherName());
        values.put(KEY_COMMENTS, instance.getComments());
        int rowsAffected = db.update(TABLE_INSTANCES, values, KEY_INSTANCE_ID + " = ?",
                new String[] { String.valueOf(instance.getId()) });
        db.close();
        return rowsAffected;
    }

    public void deleteInstance(int instanceId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_INSTANCES, KEY_INSTANCE_ID + " = ?",
                new String[] { String.valueOf(instanceId) });
        db.close();
    }

    @SuppressLint("Range")
    public List<ClassInstance> searchInstancesForCourse(String keyword, int courseId) {
        List<ClassInstance> instanceList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_INSTANCES + " WHERE " +
                KEY_COURSE_ID_FK + " = ? AND (" +
                KEY_TEACHER + " LIKE ? OR " +
                KEY_INSTANCE_DATE + " LIKE ?)";

        Cursor cursor = db.rawQuery(selectQuery, new String[] {
                String.valueOf(courseId),
                "%" + keyword + "%",
                "%" + keyword + "%"
        });

        if (cursor.moveToFirst()) {
            do {
                ClassInstance instance = new ClassInstance();
                instance.setId(cursor.getInt(cursor.getColumnIndex(KEY_INSTANCE_ID)));
                instance.setDate(cursor.getString(cursor.getColumnIndex(KEY_INSTANCE_DATE)));
                instance.setTeacherName(cursor.getString(cursor.getColumnIndex(KEY_TEACHER)));
                instance.setComments(cursor.getString(cursor.getColumnIndex(KEY_COMMENTS)));
                instance.setCourseId(cursor.getInt(cursor.getColumnIndex(KEY_COURSE_ID_FK)));
                instanceList.add(instance);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return instanceList;
    }
}