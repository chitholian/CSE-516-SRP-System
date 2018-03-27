package cse.cu.srpsystem.data;

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
import cse.cu.srpsystem.entities.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ClientREST {

    @POST("login")
    Call<User> login(@Body CredentialModel credentials);

    @GET("scripts")
    Call<List<AnswerScript>> fetchScripts();

    @GET("class-marks")
    Call<List<ClassMarks>> fetchClassMarks();

    @GET("courses")
    Call<List<Course>> fetchCourses();

    @GET("course-teachers")
    Call<List<CourseTeacher>> fetchCourseTeachers();

    @GET("exams")
    Call<List<Exam>> fetchExams();

    @GET("semesters")
    Call<List<Semester>> fetchSemesters();

    @GET("students")
    Call<List<Student>> fetchStudents();

    @GET("teachers")
    Call<List<Teacher>> fetchTeachers();

    @GET("results")
    Call<List<Result>> fetchResults();

    @PUT("save/scripts/controller")
    Call<HttpResult> sendScripts(@Body List<AnswerScript> scripts);

    @PUT("save/results/controller")
    Call<HttpResult> sendResults(@Body List<Result> results);

    @POST("update/scripts/examiner")
    Call<HttpResult> sendScriptsAsExaminer(@Body List<AnswerScript> scripts);

    @POST("update/class-marks/teacher")
    Call<HttpResult> sendClassMarks(@Body List<ClassMarks> marks);

    @POST("results/publish/{id}")
    Call<HttpResult> publishResult(@Path("id") int id);
}
