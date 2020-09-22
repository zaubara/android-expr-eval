package com.github.gianlucanitti.expreval;

import android.app.Activity;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;


import com.github.gianlucanitti.javaexpreval.UndefinedException;

public class ExprEvalIntent extends Activity {

    private boolean actionIsProcessText;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionIsProcessText = getIntent().getAction().equals(Intent.ACTION_PROCESS_TEXT);
        String expr = getIntent().getStringExtra(actionIsProcessText ? Intent.EXTRA_PROCESS_TEXT : "expression");

        TextView evalDialogLog = new TextView(this);
        evalDialogLog.setMovementMethod(new ScrollingMovementMethod());
        evalDialogLog.append(System.getProperty("line.separator"));
        TextViewExpressionContext ctx = new TextViewExpressionContext(evalDialogLog);
        ctx.setStopOnError(true);
        boolean failed = ctx.update(expr) == TextViewExpressionContext.Status.ERROR;

        if (failed) {
            result = getString(R.string.evalFailed);
        } else {
            try {
                result = Double.toString(ctx.getVariable("ans"));
            } catch(UndefinedException e){
                result = getString(R.string.evalFailed);
            }
        }

        setResult(RESULT_OK, new Intent().putExtra(actionIsProcessText ? Intent.EXTRA_PROCESS_TEXT : "result", result));
        finish();
    }
}
