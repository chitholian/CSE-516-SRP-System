package cse.cu.srpsystem.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cse.cu.srpsystem.entities.AnswerScript;
import cse.cu.srpsystem.entities.ClassMarks;
import cse.cu.srpsystem.entities.Course;
import cse.cu.srpsystem.entities.CourseTeacher;
import cse.cu.srpsystem.entities.Exam;
import cse.cu.srpsystem.entities.Result;
import cse.cu.srpsystem.entities.Semester;
import cse.cu.srpsystem.entities.Student;
import cse.cu.srpsystem.entities.Teacher;
import cse.cu.srpsystem.logics.GenerationLogic;

public class LocalDataHandler {
    private SQLiteDatabase rdb, wdb;
    private static LocalDataHandler handler;

    private LocalDataHandler(Context context) {
        DBHelper helper = new DBHelper(context);
        wdb = helper.getWritableDatabase();
        rdb = helper.getReadableDatabase();
    }

    public static LocalDataHandler getInstance(Context context) {
        if (handler == null)
            handler = new LocalDataHandler(context);
        return handler;
    }

    public List<Teacher> getTeachers() {
        Cursor cursor = rdb.rawQuery("SELECT id, name, designation FROM teachers ORDER BY name", null);
        ArrayList<Teacher> teachers = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Teacher teacher = new Teacher();
            teacher.id = cursor.getInt(0);
            teacher.name = cursor.getString(1);
            teacher.designation = cursor.getString(2);
            teachers.add(teacher);
        }
        cursor.close();
        return teachers;
    }

    public int getMaximumCode(int exam_id) {
        Cursor cursor = rdb.query("answer_scripts", new String[]{"MAX(code)"}, "exam_id = ?", new String[]{exam_id + ""}, null, null, null);
        cursor.moveToFirst();
        int maxCode = cursor.getInt(0);
        cursor.close();
        return maxCode;
    }

    public boolean addScript(AnswerScript script) {
        ContentValues values = new ContentValues(7);
        values.put("code", script.code);
        values.put("exam_id", script.exam_id);
        values.put("examiner_id", script.examiner_id);
        values.put("marks", -1);
        values.put("student_id", script.student_id);
        values.put("course_code", script.course_code);
        values.put("part", script.part + "");
        return wdb.insert("answer_scripts", null, values) != -1;
    }

    public List<Exam> getExams() {
        Cursor cursor = rdb.rawQuery("SELECT id, controller_id, title, description FROM exams ORDER BY title", null);
        ArrayList<Exam> exams = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Exam exam = new Exam();
            exam.id = cursor.getInt(0);
            exam.controller_id = cursor.getInt(1);
            exam.title = cursor.getString(2);
            exam.description = cursor.getString(3);
            exams.add(exam);
        }
        cursor.close();
        return exams;
    }

    public void updateLocalData(DataModel dataModel, List<?> list) {
        ContentValues values;
        switch (dataModel) {
            case ANSWER_SCRIPTS:
                for (Object answerScript : list) {
                    AnswerScript script = (AnswerScript) answerScript;
                    values = new ContentValues(7);
                    values.put("code", script.code);
                    values.put("exam_id", script.exam_id);
                    values.put("examiner_id", script.examiner_id);
                    values.put("marks", script.marks);
                    values.put("part", script.part + "");
                    values.put("course_code", script.course_code);
                    values.put("student_id", script.student_id);
                    if (wdb.insert("answer_scripts", null, values) == -1)
                        wdb.update("answer_scripts", values, "code = ? AND exam_id = ?",
                                new String[]{((AnswerScript) answerScript).code + "", ((AnswerScript) answerScript).exam_id + ""});
                }
                break;
            case CLASS_MARKS:
                for (Object classMarks : list) {
                    ClassMarks marks = (ClassMarks) classMarks;
                    values = new ContentValues(4);
                    values.put("exam_id", marks.exam_id);
                    values.put("course_code", marks.course_code);
                    values.put("student_id", marks.student_id);
                    values.put("marks", marks.marks);
                    if (wdb.insert("class_marks", null, values) == -1)
                        wdb.update("class_marks", values, "student_id = ? AND course_code = ? AND exam_id = ?",
                                new String[]{((ClassMarks) classMarks).student_id + "", ((ClassMarks) classMarks).course_code, ((ClassMarks) classMarks).exam_id + ""});
                }
                break;
            case COURSES:
                for (Object courseObj : list) {
                    Course course = (Course) courseObj;
                    values = new ContentValues(4);
                    values.put("code", course.code);
                    values.put("title", course.title);
                    values.put("type", course.type + "");
                    values.put("credits", course.credits);
                    wdb.insert("courses", null, values);
                }
                break;
            case COURSE_TEACHERS:
                for (Object courseTeacherObj : list) {
                    CourseTeacher courseTeacher = (CourseTeacher) courseTeacherObj;
                    values = new ContentValues(3);
                    values.put("course_code", courseTeacher.course_code);
                    values.put("semester_id", courseTeacher.semester_id);
                    values.put("teacher_id", courseTeacher.teacher_id);
                    wdb.insert("course_teachers", null, values);
                }
                break;
            case EXAMS:
                for (Object examObj : list) {
                    Exam exam = (Exam) examObj;
                    values = new ContentValues(5);
                    values.put("id", exam.id);
                    values.put("controller_id", exam.controller_id);
                    values.put("title", exam.title);
                    values.put("description", exam.description);
                    values.put("published", exam.published);
                    if (wdb.insert("exams", null, values) == -1)
                        wdb.update("exams", values, "id = ?", new String[]{exam.id + ""});
                }
                break;
            case SEMESTERS:
                for (Object semesterObj : list) {
                    Semester semester = (Semester) semesterObj;
                    values = new ContentValues(2);
                    values.put("id", semester.id);
                    values.put("title", semester.title);
                    wdb.insert("semesters", null, values);
                }
                break;
            case STUDENTS:
                for (Object studentObj : list) {
                    Student student = (Student) studentObj;
                    values = new ContentValues(5);
                    values.put("id", student.id);
                    values.put("name", student.name);
                    values.put("session", student.session);
                    values.put("date_of_birth", student.date_of_birth);
                    values.put("hall_name", student.hall_name);
                    wdb.insert("students", null, values);
                }
                break;
            case TEACHERS:
                for (Object teacherObj : list) {
                    Teacher teacher = (Teacher) teacherObj;
                    values = new ContentValues(3);
                    values.put("id", teacher.id);
                    values.put("name", teacher.name);
                    values.put("designation", teacher.designation);
                    wdb.insert("teachers", null, values);
                }
                break;
            case RESULTS:
                for (Object res : list) {
                    Result result = (Result) res;
                    values = new ContentValues(3);
                    values.put("exam_id", result.exam_id);
                    values.put("student_id", result.student_id);
                    values.put("course_code", result.course_code);
                    values.put("gpa", result.gpa);
                    values.put("comment", result.comment);
                    if (wdb.insert("results", null, values) == -1)
                        wdb.update("results", values, "exam_id = ? AND student_id = ? AND course_code = ?",
                                new String[]{result.exam_id + "", result.student_id + "", result.course_code});
                }
                break;
        }
    }

    public List<AnswerScript> getAnswerScriptsByController(int teacher_id) {
        Cursor cursor = rdb.rawQuery("SELECT code, exam_id, examiner_id, marks, part, course_code, student_id " +
                "FROM answer_scripts INNER JOIN exams ON exams.id = exam_id " +
                "INNER JOIN teachers ON controller_id = teachers.id WHERE controller_id = ?" +
                " ORDER BY code", new String[]{teacher_id + ""});
        ArrayList<AnswerScript> answerScripts = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            AnswerScript script = new AnswerScript();
            script.code = cursor.getInt(0);
            script.exam_id = cursor.getInt(1);
            script.examiner_id = cursor.getInt(2);
            script.marks = cursor.getInt(3);
            script.part = cursor.getString(4).charAt(0);
            script.course_code = cursor.getString(5);
            script.student_id = cursor.getInt(6);
            answerScripts.add(script);
        }
        cursor.close();
        return answerScripts;
    }

    public List<AnswerScript> getAnswerScriptsByExaminer(int teacher_id) {
        Cursor cursor = rdb.rawQuery("SELECT code, exam_id, marks " +
                "FROM answer_scripts WHERE examiner_id = ? AND marks >= 0" +
                " ORDER BY code", new String[]{teacher_id + ""});
        ArrayList<AnswerScript> answerScripts = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            AnswerScript script = new AnswerScript();
            script.code = cursor.getInt(0);
            script.exam_id = cursor.getInt(1);
            script.marks = cursor.getInt(2);
            answerScripts.add(script);
        }
        cursor.close();
        return answerScripts;
    }

    public List<Integer> getScriptCodesByExaminer(int teacher_id, int exam_id) {
        ArrayList<Integer> codes = new ArrayList<>();
        Cursor cursor = rdb.rawQuery("SELECT code FROM answer_scripts WHERE marks < 0 AND exam_id = ? AND examiner_id = ?",
                new String[]{exam_id + "", teacher_id + ""});
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            codes.add(cursor.getInt(0));
        }
        cursor.close();
        return codes;
    }

    public boolean updateScriptMarks(int exam_id, int code, float marks, int teacher_id) {
        ContentValues values = new ContentValues(1);
        values.put("marks", marks);
        return wdb.update("answer_scripts", values, "exam_id = ? AND code = ? AND examiner_id = ?",
                new String[]{exam_id + "", code + "", teacher_id + ""}) == 1;
    }

    public List<Exam> getExamsByExaminer(int teacher_id) {
        ArrayList<Exam> exams = new ArrayList<>();
        Cursor cursor = rdb.rawQuery("SELECT DISTINCT(exam_id), title FROM answer_scripts INNER JOIN exams ON exams.id = exam_id WHERE examiner_id = ? AND published = 0",
                new String[]{teacher_id + ""});
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Exam exam = new Exam();
            exam.id = cursor.getInt(0);
            exam.title = cursor.getString(1);
            exams.add(exam);
        }
        cursor.close();
        return exams;
    }

    public List<Course> getCoursesByTeacher(int teacher_id) {
        ArrayList<Course> courses = new ArrayList<>();
        Cursor cursor = rdb.rawQuery("SELECT DISTINCT(course_code), title, type, credits FROM course_teachers INNER JOIN courses ON code = course_code WHERE teacher_id = ?",
                new String[]{teacher_id + ""});
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Course course = new Course();
            course.code = cursor.getString(0);
            course.title = cursor.getString(1);
            try {
                course.type = cursor.getString(2).charAt(0);
            } catch (Exception e) {
            }
            course.credits = cursor.getInt(3);
            courses.add(course);
        }
        cursor.close();
        return courses;
    }

    public boolean addClassMarks(int student_id, int exam_id, String course_code, float marks) {
        ContentValues values = new ContentValues(1);
        values.put("marks", marks);
        return wdb.update("class_marks", values, "exam_id = ? AND student_id = ? AND course_code = ?",
                new String[]{exam_id + "", student_id + "", course_code}) == 1;
    }

    public List<Exam> getExamsOfCourseTeacher(int teacher_id) {
        ArrayList<Exam> exams = new ArrayList<>();
        Cursor cursor = rdb.rawQuery("SELECT DISTINCT(exam_id), exams.title FROM class_marks INNER JOIN course_teachers ON" +
                        " class_marks.course_code = course_teachers.course_code INNER JOIN exams ON exams.id = exam_id WHERE course_teachers.teacher_id = ? AND exams.published != 1",
                new String[]{teacher_id + ""});
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Exam exam = new Exam();
            exam.id = cursor.getInt(0);
            exam.title = cursor.getString(1);
            exams.add(exam);
        }
        cursor.close();
        return exams;
    }

    public List<Exam> getExamsByStudent(int student_id) {
        ArrayList<Exam> exams = new ArrayList<>();
        Cursor cursor = rdb.rawQuery("SELECT DISTINCT(exam_id), exams.title, published FROM class_marks INNER JOIN exams ON" +
                " exams.id = exam_id WHERE student_id = ?", new String[]{student_id + ""});
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Exam exam = new Exam();
            exam.id = cursor.getInt(0);
            exam.title = cursor.getString(1);
            exam.published = cursor.getShort(2);
            exams.add(exam);
        }
        cursor.close();
        return exams;
    }

    public List<Course> getCoursesByExam(int exam_id) {
        ArrayList<Course> courses = new ArrayList<>();
        Cursor cursor = rdb.rawQuery("SELECT DISTINCT(code), title, type FROM courses INNER JOIN class_marks ON course_code = courses.code WHERE exam_id = ?",
                new String[]{exam_id + ""});
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Course course = new Course();
            course.code = cursor.getString(0);
            course.title = cursor.getString(1);
            course.type = cursor.getString(2).charAt(0);
            courses.add(course);
        }
        cursor.close();
        return courses;
    }

    public void deleteAll() {
        wdb.execSQL("DELETE FROM answer_scripts");
        wdb.execSQL("DELETE FROM class_marks");
        wdb.execSQL("DELETE FROM courses");
        wdb.execSQL("DELETE FROM course_teachers");
        wdb.execSQL("DELETE FROM exams");
        wdb.execSQL("DELETE FROM semesters");
        wdb.execSQL("DELETE FROM students");
        wdb.execSQL("DELETE FROM teachers");
        wdb.execSQL("DELETE FROM results");
    }

    public List<?> getSemestersByExam(int exam_id) {
        ArrayList<Semester> semesters = new ArrayList<>(1);
        Semester semester = new Semester();
        semester.id = 5;
        semester.title = "5th Semester";
        semesters.add(semester);
        return semesters;
        //todo:incomplete.
    }

    public List<Exam> getExamsByController(int teacher_id) {
        ArrayList<Exam> exams = new ArrayList<>();
        Cursor cursor = rdb.rawQuery("SELECT id, title, published FROM exams WHERE controller_id = ?", new String[]{teacher_id + ""});
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Exam exam = new Exam();
            exam.id = cursor.getInt(0);
            exam.title = cursor.getString(1);
            exam.published = cursor.getShort(2);
            exams.add(exam);
        }
        cursor.close();
        return exams;
    }

    public List<Course> getTheoryCoursesByExam(int exam_id) {
        List<Course> courses = getCoursesByExam(exam_id), newCourses = new ArrayList<>();

        for (Course c : courses)
            if (c.type == 'T')
                newCourses.add(c);
        return newCourses;
    }

    public List<Integer> getStudentIDsByExam(int exam_id) {
        ArrayList<Integer> students = new ArrayList<>();
        Cursor cursor = rdb.rawQuery("SELECT DISTINCT(id) FROM students INNER JOIN class_marks ON class_marks.student_id = id" +
                " WHERE exam_id = ?", new String[]{exam_id + ""});
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            students.add(cursor.getInt(0));
        }
        cursor.close();
        return students;
    }

    public List<ClassMarks> getClassMarksByTeacher(int teacher_id) {
        ArrayList<ClassMarks> marks = new ArrayList<>();
        Cursor cursor = rdb.rawQuery("SELECT exam_id, class_marks.course_code, student_id, marks FROM class_marks INNER JOIN " +
                "course_teachers ON course_teachers.course_code = class_marks.course_code WHERE teacher_id = ?", new String[]{teacher_id + ""});
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            ClassMarks cm = new ClassMarks();
            cm.exam_id = cursor.getInt(0);
            cm.course_code = cursor.getString(1);
            cm.student_id = cursor.getInt(2);
            cm.marks = cursor.getFloat(3);
            marks.add(cm);
        }
        cursor.close();
        return marks;
    }

    public List<Integer> getStudentIDsByCourseTeacher(int teacher_id) {
        ArrayList<Integer> students = new ArrayList<>();
        Cursor cursor = rdb.rawQuery("SELECT DISTINCT(id) FROM students INNER JOIN class_marks ON class_marks.student_id = students.id" +
                " INNER JOIN course_teachers ON course_teachers.course_code = class_marks.course_code WHERE course_teachers.teacher_id = ? AND marks < 0", new String[]{teacher_id + ""});
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            students.add(cursor.getInt(0));
        }
        cursor.close();
        return students;
    }

    public void publishResult(int exam_id) {
        ContentValues values = new ContentValues(1);
        values.put("published", 1);
        wdb.update("exams", values, "id = ?", new String[]{exam_id + ""});
    }

    public List<Result> getResult(int student_id, int exam_id) {
        Cursor cursor = rdb.rawQuery("SELECT course_code, gpa FROM results WHERE student_id = ? AND exam_id = ?", new String[]{
                student_id + "", exam_id + ""
        });
        List<Result> results = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Result result = new Result();
            result.course_code = cursor.getString(0);
            result.gpa = cursor.getFloat(1);
            results.add(result);
        }
        cursor.close();
        return results;
    }

    public float getCredits(String course_code) {
        Cursor cursor = rdb.rawQuery("SELECT credits FROM courses WHERE code LIKE ? LIMIT 0, 1", new String[]{course_code});
        float cr = 0.0f;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            cr = cursor.getFloat(0);
        }
        cursor.close();
        return cr;
    }

    public List<Result> getMarksAsResult(int exam_id) {
        List<Result> resultList = new ArrayList<>();
        Cursor cursor = rdb.rawQuery("SELECT C.student_id, C.course_code, D.type, D.credits, ((CASE WHEN AM >= 0 THEN AM ELSE 0 END) + (CASE WHEN BM >= 0 THEN BM ELSE 0 END) + (CASE WHEN C.marks >= 0 THEN C.marks ELSE 0 END)) Marks FROM class_marks C LEFT JOIN (SELECT A.marks AM, B.marks BM, A.exam_id, A.student_id, A.course_code FROM answer_scripts A INNER JOIN answer_scripts B ON A.exam_id = B.exam_id AND A.student_id = B.student_id AND A.course_code = B.course_code AND A.part = 'A' AND B.part = 'B') E ON C.exam_id = E.exam_id AND C.student_id = E.student_id AND C.course_code = E.course_code INNER JOIN courses D ON D.code = C.course_code WHERE C.exam_id = ?",
                new String[]{exam_id + ""});

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Result result = new Result();
            result.exam_id = exam_id;
            result.student_id = cursor.getInt(0);
            result.course_code = cursor.getString(1);
            result.gpa = GenerationLogic.getGPA(cursor.getFloat(4), cursor.getFloat(3));
            result.comment = result.gpa <= 0 ? "Failed" : "Passed";
            resultList.add(result);
        }
        cursor.close();
        return resultList;
    }

    public List<Exam> getPublishedExamsByStudent(int student_id) {
        List<Exam> exams = getExamsByStudent(student_id), publishedExams = new ArrayList<>();
        for (Exam e : exams)
            if (e.published == 1)
                publishedExams.add(e);
        return publishedExams;
    }


    private static class DBHelper extends SQLiteOpenHelper {
        //        private static final String DB_NAME = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "srps.db";
        private static final String DB_NAME = "srps.db";
        private static final int DB_VERSION = 1;

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE `answer_scripts` (" +
                    "  `code` int(6)  NOT NULL," +
                    "  `exam_id` int(10)  NOT NULL," +
                    "  `examiner_id` int(10)  NOT NULL," +
                    "  `marks` double(8,2) DEFAULT NULL," +
                    "  `part` char(1) DEFAULT NULL," +
                    "  `course_code` varchar(10)  DEFAULT NULL," +
                    "  `student_id` int(10)  DEFAULT NULL," +
                    "   PRIMARY KEY(`code`, `exam_id`)," +
                    "   UNIQUE(`exam_id`,`student_id`,`course_code`,`part`))");
            db.execSQL("CREATE TABLE `class_marks` (" +
                    "  `exam_id` int(10)  NOT NULL," +
                    "  `course_code` varchar(10)  NOT NULL," +
                    "  `student_id` int(10)  NOT NULL," +
                    "  `marks` double(8,2) DEFAULT NULL," +
                    "  `created_at` timestamp NULL DEFAULT NULL," +
                    "  `updated_at` timestamp NULL DEFAULT NULL," +
                    "   PRIMARY KEY(`course_code`, `student_id`, `exam_id`))");
            db.execSQL("CREATE TABLE `courses` (" +
                    "  `code` varchar(10)  NOT NULL," +
                    "  `title` varchar(255)  NOT NULL," +
                    "  `type` char(1)  NOT NULL," +
                    "  `credits` tinyint(3)  NOT NULL," +
                    "   PRIMARY KEY(`code`))");
            db.execSQL("CREATE TABLE `course_teachers` (" +
                    "  `teacher_id` int(10)  NOT NULL," +
                    "  `semester_id` int(10)  NOT NULL," +
                    "  `course_code` varchar(10)  NOT NULL," +
                    "  `created_at` timestamp NULL DEFAULT NULL," +
                    "  `updated_at` timestamp NULL DEFAULT NULL," +
                    "   PRIMARY KEY(`teacher_id`, `semester_id`, `course_code`))");
            db.execSQL("CREATE TABLE `exams` (" +
                    "  `id` int(10)  NOT NULL," +
                    "  `controller_id` int(10)  NOT NULL," +
                    "  `title` varchar(255)  NOT NULL," +
                    "  `description` text ," +
                    "  `published` int(1)," +
                    "  `created_at` timestamp NULL DEFAULT NULL," +
                    "  `updated_at` timestamp NULL DEFAULT NULL," +
                    "   PRIMARY KEY(`id`))");
            db.execSQL("CREATE TABLE `semesters` (" +
                    "  `id` int(10)  NOT NULL," +
                    "  `title` varchar(255)  NOT NULL," +
                    "  `created_at` timestamp NULL DEFAULT NULL," +
                    "  `updated_at` timestamp NULL DEFAULT NULL," +
                    "   PRIMARY KEY(`id`))");
            db.execSQL("CREATE TABLE `students` (" +
                    "  `id` int(10)  NOT NULL," +
                    "  `name` varchar(64)  NOT NULL," +
                    "  `session` varchar(11)  NOT NULL," +
                    "  `date_of_birth` date NOT NULL," +
                    "  `hall_name` varchar(255)  DEFAULT NULL," +
                    "   PRIMARY KEY(`id`))");
            db.execSQL("CREATE TABLE `teachers` (" +
                    "  `id` int(10)  NOT NULL," +
                    "  `name` varchar(64)  NOT NULL," +
                    "  `designation` varchar(32)  NOT NULL," +
                    "   PRIMARY KEY(`id`))");
            db.execSQL("CREATE TABLE `results` (" +
                    "  `exam_id` int(10) NOT NULL," +
                    "  `student_id` int(10) NOT NULL," +
                    "  `course_code` varchar(10) NOT NULL," +
                    "  `gpa` double(3,2) NOT NULL," +
                    "  `comment` varchar(32) NOT NULL," +
                    "   PRIMARY KEY(exam_id, student_id, course_code))");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS teachers");
            db.execSQL("DROP TABLE IF EXISTS students");
            db.execSQL("DROP TABLE IF EXISTS semesters");
            db.execSQL("DROP TABLE IF EXISTS courses");
            db.execSQL("DROP TABLE IF EXISTS exams");
            db.execSQL("DROP TABLE IF EXISTS course_teachers");
            db.execSQL("DROP TABLE IF EXISTS answer_scripts");
            db.execSQL("DROP TABLE IF EXISTS class_marks");
            db.execSQL("DROP TABLE IF EXISTS results");
            onCreate(db);
        }
    }
}
