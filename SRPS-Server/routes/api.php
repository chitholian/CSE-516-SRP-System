<?php

use Illuminate\Http\Request;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

Route::middleware('auth:api')->get('/user', function (Request $request) {
    return $request->user();
});

Route::post('/login', 'AuthController@login');


Route::get('/scripts/exam/{exam}', function (Request $request) {
    return \App\AnswerScript::where('exam_id', '=', $request->exam)->get();
});
Route::get('/class-marks/exam/{exam}', function (Request $request) {
    return \App\ClassMarks::where('exam_id', '=', $request->exam)->get();
});

Route::get('/class-marks', function () {
    return \App\ClassMarks::all();
});
Route::get('/scripts', function () {
    return \App\AnswerScript::all();
});
Route::get('/courses', function () {
    return \App\Course::all();
});
Route::get('/course-teachers', function () {
    return \App\CourseTeacher::all();
});
Route::get('/exams', function () {
    return \App\Exam::all();
});
Route::get('/semesters', function () {
    return \App\Semester::all();
});
Route::get('/students', function () {
    return \App\User::select('students.id', 'name','session','date_of_birth','hall_name')->join('students', 'students.user_id','=','users.id')->get();
});
Route::get('/teachers', function () {
    return \App\User::select('id', 'name','designation')->join('teachers', 'teachers.user_id','=','users.id')->get();
});
Route::get('/results', function () {
    return \App\Result::all();
});

Route::put('/save/scripts/controller', "AnswerScriptController@store");
Route::put('/save/results/controller', "ResultController@store");

Route::post('/update/scripts/examiner', "AnswerScriptController@update");

Route::post('/update/class-marks/teacher', "ClassMarksController@update");
Route::post('/results/publish/{id}', "ExamController@update");


