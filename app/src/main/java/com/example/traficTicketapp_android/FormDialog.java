package com.example.traficTicketapp_android;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FormDialog extends DialogFragment implements AdapterView.OnItemSelectedListener {

    int MAX_SPEED = 260;

    Boolean isSchoolOrConstructionZone = false;
    int spinnerSelectionPosition;
    String spinnerSelectionValue;

    EditText firstName;
    EditText lastName;
    Spinner spinner;
    RadioButton radioButton;
    EditText speedTextInput;
    TextView amount;

    ProgressBar progressBar;

    ItemClickListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (ItemClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Implement CustomDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = inflater.inflate(R.layout.dialog_form, null, false);

        firstName = view.findViewById(R.id.firstNameTextInputDialog);
        lastName = view.findViewById(R.id.lastNameTextInputDialog);
        spinner = view.findViewById(R.id.spinnerDialog);
        radioButton = view.findViewById(R.id.radioButtonDialog);
        speedTextInput = view.findViewById(R.id.speedTextInputDialog);
        amount = view.findViewById(R.id.amountTvDialog);

        progressBar = view.findViewById(R.id.progressBar);

        firstName.requestFocus();

        firstName.addTextChangedListener(offenderInfos);
        lastName.addTextChangedListener(offenderInfos);
        speedTextInput.addTextChangedListener(offenderInfos);

        Button addButton = view.findViewById(R.id.addButtonFine);
        Button btnCancel = view.findViewById(R.id.btnDeleteEntry);

        ArrayAdapter<CharSequence> charSequenceArrayAdapter = ArrayAdapter.createFromResource(getContext(), R.array.fineTypes, android.R.layout.simple_spinner_item);
        charSequenceArrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(charSequenceArrayAdapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setDropDownWidth(500);

        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isSchoolOrConstructionZone = b;
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean allValid = false;

                EditText[] inputs = {firstName, lastName, speedTextInput};
                for(EditText editText: inputs){
                    if(editText.getText().toString().trim().isEmpty()){
                        editText.setBackground(getResources().getDrawable(R.drawable.text_field_error_style));
                        inputs[0].requestFocus();
                        editText.setError("Fields required");

                    }else {
                        allValid = true;
                    }
                }
                if (allValid) {
                    String offenderFirstName = firstName.getText().toString().trim();
                    String offenderLastName = lastName.getText().toString().trim();
                    int speedZoneNoted = parsStringToIn(spinnerSelectionValue);
                    boolean isOffenderSpeedingInSchoolOrConstructionZone = isSchoolOrConstructionZone;
                    int offenderSpeed = parsStringToIn(speedTextInput.getText().toString().trim());
                    int offenderFinedAmount = fineAmount(offenderSpeed, speedZoneNoted);
                    String date = returnTodayDate();


                    if(spinnerSelectionPosition == 0){

                        spinner.setBackground(getResources().getDrawable(R.drawable.text_field_error_style));
                        Toast.makeText(getActivity(), "Foo", Toast.LENGTH_LONG).show();

                    } else if(offenderSpeed <= speedZoneNoted){
                        speedTextInput.setBackground(getResources().getDrawable(R.drawable.text_field_error_style));
                        speedTextInput.setError("Speed must be superior to speed limit zone");

                    } else {

                        if(isOffenderSpeedingInSchoolOrConstructionZone){
                            offenderFinedAmount *= 2;
                        }

                        Offender offender = new Offender(offenderFirstName, offenderLastName, speedZoneNoted, isOffenderSpeedingInSchoolOrConstructionZone, offenderSpeed, offenderFinedAmount, date);
                        listener.onItemClick(offender);

                        amount.setText(String.valueOf(offenderFinedAmount)+"$");

                        progressBar.setProgress(2);

                        new CountDownTimer(2000, 1000) {
                            @Override
                            public void onTick(long l) {
                                progressBar.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onFinish() {
                                progressBar.setVisibility(View.GONE);
                                dismiss();
                            }
                        }.start();
                    }
                }
                }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Cancel button pressed" +
                        "", Toast.LENGTH_LONG).show();
                dismiss();
            }
        });
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        return alertDialog;
    }

    private TextWatcher offenderInfos = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            String offenderFirstName = firstName.getText().toString().trim();
            String offenderLastName = lastName.getText().toString().trim();
            String offenderSpeed = speedTextInput.getText().toString().trim();

            EditText[] inputs = {firstName, lastName, speedTextInput};
                for(EditText editText: inputs){
                    if(editText.getText().toString().trim().length() > 0){
                        editText.setBackground(getResources().getDrawable(R.drawable.text_field_style));
                    }
                }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        spinnerSelectionPosition = i;
        spinnerSelectionValue = spinner.getSelectedItem().toString();
//        Toast.makeText(getContext(), spinnerSelectionValue, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) { }

    public interface ItemClickListener { void onItemClick(Offender offender); }

    public int parsStringToIn(String valueToPars) {
        int intValue = 0;
        try {
            intValue = Integer.parseInt(valueToPars);
        } catch (NumberFormatException e) {
            //Log it if needed
        }
        return intValue;
    }

    public int fineAmount(Integer offenderSpeed, Integer speedLimitZone) {

        int[] speedDiff_10_60 = {4, 9, 10, 14, 19, 20, 24, 29, 30, 34, 39, 44, 45, 49, 54, 59, 60, 64, 69, 74, 79, 80, 84, 89, 94, 99, 100, 104, 109, 114, 119, 120, 124, 129, 134, 139, 140, 144, 149, 154, 159, 160};
        int[] speedDiff_10_60_Amount = {15, 25, 35, 35, 45, 55, 75, 90, 105, 135, 155, 350, 390, 480, 530, 630, 750, 810, 870, 930, 990, 990, 1050, 1110, 1170, 1230, 1230, 1290, 1350, 1410, 1470, 1470, 1470, 1530, 1590, 1650, 1710, 1710, 1770, 1830, 1890, 1950};

        int[] speedDiff_70_90 = {4, 9, 10, 14, 19, 20, 24, 29, 30, 34, 39, 44, 45, 49, 54, 59, 60, 64, 69, 74, 79, 80, 84, 89, 94, 99, 100, 104, 109, 114, 119, 120, 124, 129, 134, 139, 140, 144, 149, 154, 159, 160};
        int[] speedDiff_00_90_Amount = {15, 25, 35, 35, 45, 55, 75, 90, 105, 135, 155, 175, 195, 240, 530, 580, 630, 750, 810, 870, 930, 990, 990, 1050, 1110, 1170, 1230, 1230, 1290, 1350, 1410, 1470, 1470, 1530, 1590, 1650, 1710, 1710, 1770, 1830, 1890, 1950};

        int[] speedDiff_100_More = {4, 9, 10, 14, 19, 20, 24, 29, 30, 34, 39, 44, 45, 49, 54, 59, 60,  64, 69, 74, 79, 80,  84, 89, 94, 99, 100, 104, 109, 114, 119, 120, 124, 129, 134, 139, 140, 144, 149, 154, 159, 160};
        int[] speedDiff_100_More_Amount = {15, 25, 35, 35, 45, 55, 75, 90, 105, 135, 155, 175, 195, 240, 265, 290, 630, 750, 810, 870, 930, 990, 990, 1050, 1110, 1170, 1230, 1230, 1290, 1350, 1410, 1470, 1470, 1530, 1590, 1650, 1710, 1710, 1770, 1830, 1890, 1950};

        int fineAmount = 0;

        if((speedLimitZone >= 10 && speedLimitZone <= 60) && offenderSpeed != speedLimitZone){

            for (int i = 0; i <= speedDiff_10_60.length; i++) {
                if (offenderSpeed <= speedLimitZone + speedDiff_10_60[i]) {
                    fineAmount = speedDiff_10_60_Amount[i];
                    break;
                }
            }
        }else if(speedLimitZone >= 70 && speedLimitZone <= 90){
            for (int i = 0; i <= speedDiff_70_90.length; i++) {
                if (offenderSpeed <= speedLimitZone + speedDiff_70_90[i]) {
                    fineAmount = speedDiff_00_90_Amount[i];
                    break;
                }
            }

        }else if(speedLimitZone >= 90 && speedLimitZone <= 100){
            for (int i = 0; i <= speedDiff_100_More.length; i++) {
                if (offenderSpeed <= (speedLimitZone + speedDiff_100_More[i])) {
                    fineAmount = speedDiff_100_More_Amount[i];
                    break;
                }
            }
        }

        return fineAmount;
    }

    public String returnTodayDate() {
        Date fineDate = new Date();
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        return formattedDate;
    }

}
