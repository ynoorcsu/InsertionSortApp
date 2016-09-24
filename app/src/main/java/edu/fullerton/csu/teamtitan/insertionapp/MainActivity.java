package edu.fullerton.csu.teamtitan.insertionapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.text.Html;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    protected static final int MIN_NUM = 2;
    protected static final int MAX_NUM = 8;
    protected static final String CONFIRM_MSG = "Are you sure you want to exit?";
    protected static final String MSG_MIN_INPUT_SIZE = "Please enter at least 2 numbers between 0 and 9.";
    protected static final String MSG_MAX_INPUT_SIZE = "Please enter no more than 8 numbers between 0 and 9.";
    protected static final String MSG_INPUT_ERROR = "Please enter number from 0 to 9.";

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText inputDigits = (EditText)findViewById(R.id.txtInput);

        inputDigits.setFilters(new InputFilter[] {
            new InputFilter() {
                @Override
                public CharSequence filter(CharSequence cs, int start, int end,
                                           Spanned spanned, int dStart, int dEnd) {
                    // for backspace
                    if(cs.equals("")) {
                        return cs;
                    }

                    if(cs.toString().matches("[0-9]+")) {
                        return cs + " ".toString();
                    }

                    if(cs.toString().matches("[ ]?")) {
                        return cs;
                    }

                    return "";
                }
            }
        });

        toggleLabelDisplay(Boolean.FALSE);
    }

    protected void btnQuit_onclick_action(View v)
    {
        createConfirmationDialog(CONFIRM_MSG);
    }

    protected void btnClear_onclick_action(View v)
    {
        EditText inputDigits = (EditText)findViewById(R.id.txtInput);
        inputDigits.setText("");

        clearContentArea();
        toggleLabelDisplay(Boolean.FALSE);
    }

    protected void cmdApplySort(View v)
    {
        EditText inputDigits = (EditText)findViewById(R.id.txtInput);
        TextView lblInputArrayContent = (TextView)findViewById(R.id.lblInputArrayContent);

        clearContentArea();

        if (!inputDigits.getText().toString().trim().isEmpty()) {
            List<Integer> digitList = cleanupInput(inputDigits.getText().toString().split(" "));
            int[] digits = convertListToInt(digitList);
            int size = digits.length;

            if (size < MIN_NUM) {
                createDialog(MSG_MIN_INPUT_SIZE);
                clearContentArea();
                toggleLabelDisplay(Boolean.FALSE);
            } else if (size > MAX_NUM) {
                createDialog(MSG_MAX_INPUT_SIZE);
                clearContentArea();
                toggleLabelDisplay(Boolean.FALSE);
            } else {
                if (this.validateInput(digits)) {
                    lblInputArrayContent.setText(TextUtils.join(" ", digitList.toArray()));
                    insertionSort(digits);
                    toggleLabelDisplay(Boolean.TRUE);
                } else {
                    createDialog(MSG_INPUT_ERROR);
                    clearContentArea();
                    toggleLabelDisplay(Boolean.FALSE);
                }
            }
        } else {
            this.createDialog(MSG_MIN_INPUT_SIZE);
            inputDigits.setText("");
            clearContentArea();
            toggleLabelDisplay(Boolean.FALSE);
        }
    }

    protected List<Integer> cleanupInput(String[] input) {
        List<Integer> tempList = new ArrayList<Integer>();

        int j = 0;
        for (int i = 0; i < input.length; i++) {
            String value = input[i].toString().trim();

            if (!value.isEmpty()) {
                tempList.add(Integer.parseInt(value.toString()));
            }
        }

        return tempList;
    }

    protected int[] convertListToInt(List<Integer> list) {
        int[] digits = new int[list.size()];

        for(int i = 0; i < list.size(); i++) {
            try {
                digits[i] = list.get(i);
            } catch (IndexOutOfBoundsException e) {
                createDialog(e.getMessage());
            }
        }

        return digits;
    }

    protected void insertionSort(int[] input)
    {
        int temp;

        StringBuilder masterString = new StringBuilder();
        masterString.append(formatArray(input, 1) + "<br/>");

        for (int i = 1; i < input.length; i++) {
            for(int j = i ; j > 0 ; j--){
                if(input[j] < input[j-1]){
                    temp = input[j];
                    input[j] = input[j-1];
                    input[j-1] = temp;
                }
            }

            if (i < input.length) {
                masterString.append(formatArray(input, i + 1) + "<br/>");
            } else {
                masterString.append(formatArray(input, -1) + "<br/>");
            }

        }

        printArray(masterString.toString().trim());
    }

    protected String formatArray(int[] numbers, int boldPosition)
    {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < numbers.length; i++) {
            if (i == boldPosition) {
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

    protected void clearContentArea()
    {
        TextView lblInputArrayContent = (TextView)findViewById(R.id.lblInputArrayContent);
        TextView lblResults = (TextView)findViewById(R.id.lblResults);

        lblResults.setText("");
        lblInputArrayContent.setText("");
    }

    protected void toggleLabelDisplay(Boolean display)
    {
        TextView lblInputArray = (TextView)findViewById(R.id.lblInputArray);
        TextView lblSteps = (TextView)findViewById(R.id.lblSteps);

        if (display) {
            lblInputArray.setVisibility(View.VISIBLE);
            lblSteps.setVisibility(View.VISIBLE);
        } else {
            lblInputArray.setVisibility(View.INVISIBLE);
            lblSteps.setVisibility(View.INVISIBLE);
        }
    }
}
