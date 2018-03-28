package cse.cu.srpsystem.dataaccesslayer;

import java.io.IOException;
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
import cse.cu.srpsystem.applicationlayer.StatusListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RemoteDAL {
    public static String SERVER_URL = "http://10.42.0.1/SRPS-Server/public/api/";
    private RESTAPIClient clientREST = new Retrofit.Builder().baseUrl(SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(RESTAPIClient.class);

    private RemoteDAL() {
    }

    public User loginUser(Credentials credentials) throws Exception {
        Call<User> userCall = clientREST.login(credentials);
        Response response = userCall.execute();
        System.out.println(response.message());
        return (User) response.body();
    }

    public StatusListener.Status sendScripts(List<AnswerScript> scripts) {
        Call<HttpResponse> request = clientREST.sendScripts(scripts);
        try {
            Response response = request.execute();
            if (response.isSuccessful()) return StatusListener.Status.SUCCESSFUL;
            System.out.println("Response is : " + response.errorBody().string());
            return StatusListener.Status.UNKNOWN_ERROR;
        } catch (IOException e) {
            e.printStackTrace();
            return StatusListener.Status.ERR_CONNECTION_FAILED;
        }
    }

    public StatusListener.Status sendScriptsAsExaminer(List<AnswerScript> scripts) {
        Call<HttpResponse> request = clientREST.sendScriptsAsExaminer(scripts);
        try {
            Response response = request.execute();
            if (response.isSuccessful()) return StatusListener.Status.SUCCESSFUL;
            System.out.println("Response is : " + response.errorBody().string());
            return StatusListener.Status.UNKNOWN_ERROR;
        } catch (IOException e) {
            e.printStackTrace();
            return StatusListener.Status.ERR_CONNECTION_FAILED;
        }
    }

    public void fetchData(EntityTypeName entityTypeName, final DataReceiver receiver) {
        switch (entityTypeName) {
            case ANSWER_SCRIPTS:
                Call<List<AnswerScript>> scripts = clientREST.fetchScripts();
                scripts.enqueue(new Callback<List<AnswerScript>>() {
                    @Override
                    public void onResponse(Call<List<AnswerScript>> call, Response<List<AnswerScript>> response) {
                        if (response.isSuccessful() && receiver != null) {
                            receiver.onReceive(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<AnswerScript>> call, Throwable t) {
                        t.printStackTrace();
                    }
                });

                break;
            case CLASS_MARKS:
                Call<List<ClassMarks>> marks = clientREST.fetchClassMarks();
                marks.enqueue(new Callback<List<ClassMarks>>() {
                    @Override
                    public void onResponse(Call<List<ClassMarks>> call, Response<List<ClassMarks>> response) {
                        if (response.isSuccessful() && receiver != null) {
                            receiver.onReceive(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ClassMarks>> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
                break;
            case COURSES:
                Call<List<Course>> courses = clientREST.fetchCourses();
                courses.enqueue(new Callback<List<Course>>() {
                    @Override
                    public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                        if (response.isSuccessful() && receiver != null) {
                            receiver.onReceive(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Course>> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
                break;
            case COURSE_TEACHERS:
                Call<List<CourseTeacher>> courseTeachers = clientREST.fetchCourseTeachers();
                courseTeachers.enqueue(new Callback<List<CourseTeacher>>() {
                    @Override
                    public void onResponse(Call<List<CourseTeacher>> call, Response<List<CourseTeacher>> response) {
                        if (response.isSuccessful() && receiver != null) {
                            receiver.onReceive(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CourseTeacher>> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
                break;
            case EXAMS:
                Call<List<Exam>> exams = clientREST.fetchExams();
                exams.enqueue(new Callback<List<Exam>>() {
                    @Override
                    public void onResponse(Call<List<Exam>> call, Response<List<Exam>> response) {
                        if (response.isSuccessful() && receiver != null) {
                            receiver.onReceive(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Exam>> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
                break;
            case SEMESTERS:
                Call<List<Semester>> semesters = clientREST.fetchSemesters();
                semesters.enqueue(new Callback<List<Semester>>() {
                    @Override
                    public void onResponse(Call<List<Semester>> call, Response<List<Semester>> response) {
                        if (response.isSuccessful() && receiver != null) {
                            receiver.onReceive(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Semester>> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
                break;
            case STUDENTS:
                Call<List<Student>> students = clientREST.fetchStudents();
                students.enqueue(new Callback<List<Student>>() {
                    @Override
                    public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                        if (response.isSuccessful() && receiver != null) {
                            receiver.onReceive(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Student>> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
                break;
            case TEACHERS:
                Call<List<Teacher>> teachers = clientREST.fetchTeachers();
                teachers.enqueue(new Callback<List<Teacher>>() {
                    @Override
                    public void onResponse(Call<List<Teacher>> call, Response<List<Teacher>> response) {
                        if (response.isSuccessful() && receiver != null) {
                            receiver.onReceive(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Teacher>> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
                break;
            case RESULTS:
                Call<List<Result>> results = clientREST.fetchResults();
                results.enqueue(new Callback<List<Result>>() {
                    @Override
                    public void onResponse(Call<List<Result>> call, Response<List<Result>> response) {
                        if (response.isSuccessful() && receiver != null) {
                            receiver.onReceive(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Result>> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
                break;
        }
    }

    public StatusListener.Status sendClassMarks(List<ClassMarks> marks) {
        Call<HttpResponse> request = clientREST.sendClassMarks(marks);
        try {
            Response response = request.execute();
            if (response.isSuccessful()) return StatusListener.Status.SUCCESSFUL;
            System.out.println("Response is : " + response.errorBody().string());
            return StatusListener.Status.UNKNOWN_ERROR;
        } catch (IOException e) {
            e.printStackTrace();
            return StatusListener.Status.ERR_CONNECTION_FAILED;
        }
    }

    public StatusListener.Status publishResult(int exam_id) {
        Call<HttpResponse> request = clientREST.publishResult(exam_id);
        try {
            Response response = request.execute();
            if (response.isSuccessful()) return StatusListener.Status.SUCCESSFUL;
            System.out.println("Response is : " + response.errorBody().string());
            return StatusListener.Status.UNKNOWN_ERROR;
        } catch (IOException e) {
            e.printStackTrace();
            return StatusListener.Status.ERR_CONNECTION_FAILED;
        }
    }

    public StatusListener.Status sendResults(List<Result> allResults) {
        Call<HttpResponse> request = clientREST.sendResults(allResults);
        try {
            Response response = request.execute();
            if (response.isSuccessful()) return StatusListener.Status.SUCCESSFUL;
            System.out.println("Response is : " + response.errorBody().string());
            return StatusListener.Status.UNKNOWN_ERROR;
        } catch (IOException e) {
            e.printStackTrace();
            return StatusListener.Status.ERR_CONNECTION_FAILED;
        }
    }

    private static class Helper {
        private static final RemoteDAL instance = new RemoteDAL();
    }

    public static RemoteDAL getInstance() {
        return Helper.instance;
    }
}
