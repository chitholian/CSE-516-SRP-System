<?php

namespace App\Http\Controllers;

use App\AnswerScript;
use App\Exam;
use Illuminate\Contracts\Logging\Log;
use Illuminate\Http\Request;

class AnswerScriptController extends Controller
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
        $error = 0;
        foreach ($request->all() as $script) {
            if (Exam::where([['id', '=', $script['exam_id']], ['published', '=', false]])->count())
                try {
                    AnswerScript::create(['code' => $script['code'], 'exam_id' => $script['exam_id'], 'examiner_id' => $script['examiner_id'], 'marks' => -1,
                        'student_id' => $script['student_id'], 'course_code' => $script['course_code'], 'part' => $script['part']]);
                } catch (\Exception $exception) {
                    $error++;
                    //return response([$exception->getTraceAsString()], 500);
                }
        }
        return ($error = 0) > 0 ? response(['status' => 500, 'message' => "Error processing $error scripts.\n"], 500) : response(['status' => 200, 'message' => "OK"], 200);
    }

    /**
     * Display the specified resource.
     *
     * @param  \App\AnswerScript $answerScript
     * @return \Illuminate\Http\Response
     */
    public function show(AnswerScript $answerScript)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  \App\AnswerScript $answerScript
     * @return \Illuminate\Http\Response
     */
    public function edit(AnswerScript $answerScript)
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
        foreach ($request->all() as $script) {
            if (Exam::where([['id', '=', $script['exam_id']], ['published', '=', false]])->count())
                AnswerScript::where([['code', '=', $script['code']], ['exam_id', '=', $script['exam_id']]])->update(["marks" => $script['marks']]);
        }
        return response(['status' => 200, 'message' => "OK"], 200);
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  \App\AnswerScript $answerScript
     * @return \Illuminate\Http\Response
     */
    public function destroy(AnswerScript $answerScript)
    {
        //
    }
}
