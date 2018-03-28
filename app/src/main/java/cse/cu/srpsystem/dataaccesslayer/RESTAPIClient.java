package cse.cu.srpsystem.dataaccesslayer;

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
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RESTAPIClient {

    @POST("login")
    Call<User> login(@Body Credentials credentials);

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
    Call<HttpResponse> sendScripts(@Body List<AnswerScript> scripts);

    @PUT("save/results/controller")
    Call<HttpResponse> sendResults(@Body List<Result> results);

    @POST("update/scripts/examiner")
    Call<HttpResponse> sendScriptsAsExaminer(@Body List<AnswerScript> scripts);

    @POST("update/class-marks/teacher")
    Call<HttpResponse> sendClassMarks(@Body List<ClassMarks> marks);

    @POST("results/publish/{id}")
    Call<HttpResponse> publishResult(@Path("id") int id);
}
