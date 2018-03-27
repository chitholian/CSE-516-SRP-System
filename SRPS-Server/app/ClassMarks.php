<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class ClassMarks extends Model
{
    protected $guarded = ['marks'];
    protected $hidden = ['created_at', 'updated_at'];
}
