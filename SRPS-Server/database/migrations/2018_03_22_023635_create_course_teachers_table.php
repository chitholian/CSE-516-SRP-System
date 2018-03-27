<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateCourseTeachersTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('course_teachers', function (Blueprint $table) {
            $table->unsignedInteger('teacher_id');
            $table->unsignedInteger('semester_id');
            $table->string('course_code', 10);
            $table->timestamps();

            $table->primary(['teacher_id', 'semester_id', 'course_code']);
            $table->foreign('semester_id')->references('id')->on('semesters');
            $table->foreign('teacher_id')->references('user_id')->on('teachers');
            $table->foreign('course_code')->references('code')->on('courses');
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('course_teachers');
    }
}
