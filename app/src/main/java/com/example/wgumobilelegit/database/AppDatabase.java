package com.example.wgumobilelegit.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.wgumobilelegit.Objects.Assessment;
import com.example.wgumobilelegit.Objects.Course;
import com.example.wgumobilelegit.Objects.Mentor;
import com.example.wgumobilelegit.Objects.Note;
import com.example.wgumobilelegit.Objects.Term;
import com.example.wgumobilelegit.dao.AssessmentDAO;
import com.example.wgumobilelegit.dao.CourseDAO;
import com.example.wgumobilelegit.dao.MentorDAO;
import com.example.wgumobilelegit.dao.NoteDAO;
import com.example.wgumobilelegit.dao.TermDAO;
@Database(entities = {Assessment.class, Course.class, Mentor.class, Term.class, Note.class}, version = 2, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract AssessmentDAO AssessmentDAO();

    public abstract CourseDAO courseDAO();

    public abstract MentorDAO mentorDAO();

    public abstract TermDAO termDAO();

    public abstract NoteDAO noteDAO();

    public static AppDatabase getDbInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,
                    "student_portal_db").allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}
