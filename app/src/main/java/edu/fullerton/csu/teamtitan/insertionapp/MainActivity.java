package edu.fullerton.csu.teamtitan.insertionapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.text.Html;


public class MainActivity extends AppCompatActivity {
    protected static final int MIN_NUM = 2;
    protected static final int MAX_NUM = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText txtInput = (EditText)findViewById(R.id.txtInput);
        txtInput.setFilters(new InputFilter[] {
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence cs, int start, int end, Spanned spanned, int dStart, int dEnd) {
                        if(cs.equals("")){ // for backspace
                            return cs;
                        }
                        if(cs.toString().matches("[0-9 ]+")){
                            return cs;
                        }
                        return "";
                    }
                }
        });
    }

    protected void cmdQuitAction(View v) 
    {
        createConfirmationDialog("Are you sure you want to exit?");
    }

    protected void cmdInsertionSort(View v) 
    {
        EditText txtInput = (EditText)findViewById(R.id.txtInput);
        TextView lblInputOutput = (TextView)findViewById(R.id.lblInputOutput);
        TextView lblErrorMsg = (TextView)findViewById(R.id.lblErrorMsg);

        resetElements();

        if (!txtInput.getText().toString().trim().isEmpty()) {
            String[] inputArrayString = txtInput.getText().toString().split(" ");
            int[] inputArrayInt = new int[inputArrayString.length];

            for(int i = 0; i < inputArrayString.length; i++) {
                try {
                    inputArrayInt[i] = Integer.parseInt(inputArrayString[i]);
                } catch (NumberFormatException e) {
                    createDialog(e.getMessage());
                }
            }

            int size = inputArrayInt.length;

            if (this.validateInput(inputArrayInt)) {
                if (size < MIN_NUM) {
                    createDialog("Please enter at least 2 numbers between 0 and 9.");
                    resetElements();
                } else if (size > MAX_NUM) {
                    createDialog("Please enter no more than 8 numbers between 0 and 9.");
                    resetElements();
                } else {
                    lblInputOutput.setText(txtInput.getText().toString());
                    insertionSort(inputArrayInt);
                }
            } else {
                createDialog("Please enter number from 0 to 9.");
                resetElements();
            }
        } else {
            this.createDialog("Please enter numbers between 0 and 9.");
            //lblErrorMsg.setText("Please enter numbers between 0 and 9.");
            txtInput.setText("");
            resetElements();
        }
    }

    protected void insertionSort(int[] input)
    {
        int temp;

        StringBuilder masterString = new StringBuilder();
        masterString.append(formatArray(input, -1) + "<br/>");

        for (int i = 1; i < input.length; i++) {
            for(int j = i ; j > 0 ; j--){
                if(input[j] < input[j-1]){
                    temp = input[j];
                    input[j] = input[j-1];
                    input[j-1] = temp;
                }
            }

            masterString.append(formatArray(input, i) + "<br/>");
        }

        printArray(masterString.toString().trim());
    }

    protected String formatArray(int[] numbers, int boldPosition) 
    {
        StringBuilder sb = new StringBuilder();

        for (int i=0; i < numbers.length; i++) {
            if (i == boldPosition && i+1 < numbers.length) {
                sb.append("<strong>" + numbers[i] + "</strong> ");
            } else {
                sb.append(numbers[i] + " ");
            }
        }

        return sb.toString().trim();
    }

    protected void printArray(String output) 
    {
        TextView lblResults = (TextView)findViewById(R.id.lblResults);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            lblResults.setText(Html.fromHtml(output.toString().trim(),Html.FROM_HTML_MODE_LEGACY));
        } else {
            lblResults.setText(Html.fromHtml(output.toString().trim()));
        }
    }

    protected Boolean validateInput(int[] inputs) 
    {
        Boolean flag = Boolean.TRUE;

        for (int num: inputs) {
            if (num >= 0 && num <= 9) {
                continue;
            } else {
                flag = Boolean.FALSE;
                break;
            }
        }

        return flag;
    }

    protected void createConfirmationDialog(String msg) 
    {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg)
            .setCancelable(false)
            .setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        if (android.os.Build.VERSION.SDK_INT >= 21) {
                            finishAndRemoveTask();
                        } else {
                            finish();
                        }
                        int pid = android.os.Process.myPid();
                        android.os.Process.killProcess(pid);
                    }
                }
            )
            .setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }
            );

        AlertDialog alert = builder.create();
        dialog = alert;
        dialog.show();
    }


    protected void createDialog(String msg) 
    {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg)
        .setCancelable(false)
        .setPositiveButton("ok",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int id) {
                    dialog.dismiss();
                }
            }
        );

        AlertDialog alert = builder.create();
        dialog = alert;
        dialog.show();
    }

    protected void resetElements() 
    {
        TextView lblInputOutput = (TextView)findViewById(R.id.lblInputOutput);
        TextView lblResults = (TextView)findViewById(R.id.lblResults);

        lblResults.setText("");
        lblInputOutput.setText("");
    }
}
