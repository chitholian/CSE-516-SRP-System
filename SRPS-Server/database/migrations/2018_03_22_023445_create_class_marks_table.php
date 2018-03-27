<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateClassMarksTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('class_marks', function (Blueprint $table) {
            $table->unsignedInteger("exam_id");
            $table->string("course_code", 10);
            $table->unsignedInteger("student_id");
            $table->float("marks")->nullable();
            $table->timestamps();

            $table->primary(['exam_id', 'student_id','course_code']);
            $table->foreign('exam_id')->references('id')->on('exams');
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
        Schema::dropIfExists('class_marks');
    }
}
