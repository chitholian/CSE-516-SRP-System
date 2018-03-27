<?php

namespace App;

class Teacher extends User
{
    protected $guarded = [];
    protected $hidden = ['user_id'];

    public function user(){
        return $this->belongsTo('\App\User');
    }
}
