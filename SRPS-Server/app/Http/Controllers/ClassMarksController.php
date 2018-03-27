<?php

namespace App\Http\Controllers;

use App\ClassMarks;
use Illuminate\Http\Request;

class ClassMarksController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        //
    }

    /**
     * Show the form for creating a new resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function create()
    {
        //
    }

    /**
     * Store a newly created resource in storage.
     *
     * @param  \Illuminate\Http\Request $request
     * @return \Illuminate\Http\Response
     */
    public function store(Request $request)
    {
        //
    }

    /**
     * Display the specified resource.
     *
     * @param  \App\ClassMarks $classMarks
     * @return \Illuminate\Http\Response
     */
    public function show(ClassMarks $classMarks)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  \App\ClassMarks $classMarks
     * @return \Illuminate\Http\Response
     */
    public function edit(ClassMarks $classMarks)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request $request
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request)
    {
        foreach ($request->all() as $classMarks) {
            ClassMarks::where([
                ['course_code', '=', $classMarks['course_code']],
                ['exam_id', '=', $classMarks['exam_id']],
                ['student_id', '=', $classMarks['student_id']]])->
            update(["marks" => $classMarks['marks']]);
        }
        return response(['status' => 200, 'message' => "OK"], 200);
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  \App\ClassMarks $classMarks
     * @return \Illuminate\Http\Response
     */
    public function destroy(ClassMarks $classMarks)
    {
        //
    }
}
