<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class AnswerScript extends Model
{
    protected $guarded = [];
    protected $hidden = ['created_at', 'updated_at'];
    public $timestamps = false;
}
