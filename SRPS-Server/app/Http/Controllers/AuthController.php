<?php

namespace App\Http\Controllers;

use App\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;

class AuthController extends Controller
{
    public static function login(Request $request)
    {
        if ($request->role == "Exam Controller")
            $request['role'] = 'Teacher';

        if (Auth::attempt($request->all(['email', 'password', 'role']))) {
            if ($request->role == "Student") {
                return User::select('students.id', 'name', 'role')->join('students', 'students.user_id',
                    '=', 'users.id')->where('user_id', '=', $request->user()->id)->first();
            }

            return User::select('user_id as id', 'name', 'role')->join('teachers', 'teachers.user_id',
                '=', 'users.id')->where('user_id', '=', $request->user()->id)->first();
        }

        return ['code' => 403, 'user' => null];
    }
}
