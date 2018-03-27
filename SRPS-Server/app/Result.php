<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Result extends Model
{
    public $timestamps = false;
    protected $fillable = ['exam_id', 'student_id', 'course_code', 'gpa', 'comment'];
}
