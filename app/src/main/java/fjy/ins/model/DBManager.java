package fjy.ins.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import fjy.ins.model.Note;

import java.util.List;

/**
 * Created by XP on 2015/2/15.
 */
public class DBManager {
    private Context context;
    private NoteDBOpenHelper databaseOpenHelper;
    private SQLiteDatabase dbReader;
    private SQLiteDatabase dbWriter;
    private static DBManager instance;

    public DBManager(Context context) {
        this.context = context;
        databaseOpenHelper = new NoteDBOpenHelper(context);
        // 创建and/or打开一个数据库
        dbReader = databaseOpenHelper.getReadableDatabase();
        dbWriter = databaseOpenHelper.getWritableDatabase();
    }

    //getInstance单例
    public static synchronized DBManager getInstance(Context context) {
        if (instance == null) {
            instance = new DBManager(context);
        }
        return instance;
    }

    // 添加到数据库
    public void addToDB(String title, String content, String time, String path, String size, String url) {
        //  组装数据
        ContentValues cv = new ContentValues();
        cv.put(NoteDBOpenHelper.TITLE, title);
        cv.put(NoteDBOpenHelper.CONTENT, content);
        cv.put(NoteDBOpenHelper.TIME, time);
		cv.put(NoteDBOpenHelper.PATH, path);
		cv.put(NoteDBOpenHelper.SIZE, size);
		cv.put(NoteDBOpenHelper.URL, url);
        dbWriter.insert(NoteDBOpenHelper.TABLE_NAME, null, cv);
    }

    //  读取数据
    public void readFromDB(List<Note> noteList) {
        Cursor cursor = dbReader.query(NoteDBOpenHelper.TABLE_NAME, null, null, null, null, null, null);
        try {
            while (cursor.moveToNext()) {
                Note note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndex(NoteDBOpenHelper.ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndex(NoteDBOpenHelper.TITLE)));
                note.setContent(cursor.getString(cursor.getColumnIndex(NoteDBOpenHelper.CONTENT)));
                note.setTime(cursor.getString(cursor.getColumnIndex(NoteDBOpenHelper.TIME)));
				note.setPath(cursor.getString(cursor.getColumnIndex(NoteDBOpenHelper.PATH)));
				note.setSize(cursor.getString(cursor.getColumnIndex(NoteDBOpenHelper.SIZE)));
				note.setUrl(cursor.getString(cursor.getColumnIndex(NoteDBOpenHelper.URL)));
                noteList.add(note);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //  更新数据
    public void updateNote(int noteID, String title, String content, String time, String path, 
	String size, String url) {
        ContentValues cv = new ContentValues();
        cv.put(NoteDBOpenHelper.ID, noteID);
        cv.put(NoteDBOpenHelper.TITLE, title);
        cv.put(NoteDBOpenHelper.CONTENT, content);
        cv.put(NoteDBOpenHelper.TIME, time);
		cv.put(NoteDBOpenHelper.PATH, path);
		cv.put(NoteDBOpenHelper.SIZE, size);
		cv.put(NoteDBOpenHelper.URL, url);
        dbWriter.update(NoteDBOpenHelper.TABLE_NAME, cv, "_id = ?", new String[]{noteID + ""});
    }

    //  删除数据
    public void deleteNote(int noteID) {
        dbWriter.delete(NoteDBOpenHelper.TABLE_NAME, "_id = ?", new String[]{noteID + ""});
    }

    // 根据id查询数据
    public Note readData(int noteID) {
        Cursor cursor = dbReader.rawQuery("SELECT * FROM note WHERE _id = ?", new String[]{noteID + ""});
        cursor.moveToFirst();
        Note note = new Note();
        note.setId(cursor.getInt(cursor.getColumnIndex(NoteDBOpenHelper.ID)));
        note.setTitle(cursor.getString(cursor.getColumnIndex(NoteDBOpenHelper.TITLE)));
        note.setContent(cursor.getString(cursor.getColumnIndex(NoteDBOpenHelper.CONTENT)));
		note.setPath(cursor.getString(cursor.getColumnIndex(NoteDBOpenHelper.PATH)));
		note.setSize(cursor.getString(cursor.getColumnIndex(NoteDBOpenHelper.SIZE)));
		note.setUrl(cursor.getString(cursor.getColumnIndex(NoteDBOpenHelper.URL)));
        return note;
    }
}


