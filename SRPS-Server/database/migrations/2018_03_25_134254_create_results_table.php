<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateResultsTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('results', function (Blueprint $table) {
            $table->unsignedInteger('exam_id');
            $table->unsignedInteger('student_id');
            $table->string('course_code', 10);
            $table->float('gpa', 3, 2);
            $table->string('comment', 32);

            $table->primary(['exam_id', 'student_id', 'course_code']);
            $table->foreign('student_id')->references('id')->on('students');
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
        Schema::dropIfExists('results');
    }
}
