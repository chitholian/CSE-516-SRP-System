<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateAnswerScriptsTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('answer_scripts', function (Blueprint $table) {
            $table->unsignedInteger('code');
            $table->unsignedInteger('exam_id');
            $table->unsignedInteger('examiner_id');
            $table->float('marks');
            $table->enum('part', ['A', 'B']);
            $table->string('course_code', 10);
            $table->unsignedInteger('student_id');

            $table->primary('code');
            $table->unique(['exam_id', 'student_id', 'course_code', 'part']);
            $table->foreign('exam_id')->references('id')->on('exams');
            $table->foreign('examiner_id')->references('user_id')->on('teachers');
            $table->foreign('course_code')->references('code')->on('courses');
            $table->foreign('student_id')->references('id')->on('students');
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('answer_scripts');
    }
}
