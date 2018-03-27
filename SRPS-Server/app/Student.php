<?php

namespace App;


class Student extends User
{
    protected $guarded = [];
    protected $hidden = ['user_id'];

    public function user(){
        return $this->belongsTo('\App\User');
    }
}
