package com.example.traficTicketapp_android;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DetailsDialog extends DialogFragment {

    ItemClickListenerDetailsDialog listener;

    Offender offender;

    TextView date;
    TextView firstName;
    TextView lastName;
    TextView offenderSpeed;
    TextView speedLimit;
    TextView finedAmount;

    boolean isSchoolOrConstructionZone;
    ImageView isSchoolOrConstructionZoneImg;

    Button deleteButton;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (ItemClickListenerDetailsDialog) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Implement CustomDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        Offender offender = bundle.getParcelable("offender");

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = inflater.inflate(R.layout.dialog_show_details, null, false);

        date = view.findViewById(R.id.dateTvDetailsScreen);
        firstName = view.findViewById(R.id.firsNameTvDetailsScreenDialog);
        lastName = view.findViewById(R.id.lastNameTvDetailsScreen);
        offenderSpeed = view.findViewById(R.id.speedTvDetailsScreenDialog);
        speedLimit = view.findViewById(R.id.speedLimitZoneTvDetailsScreenDialog);
        finedAmount = view.findViewById(R.id.findedAmountTvDetailsScreenDialog);
        isSchoolOrConstructionZoneImg = view.findViewById(R.id.imgViewIsSchoolOrConstructionZone);

        Button deleteButton = view.findViewById(R.id.btnDeleteEntry);

        date.setText(offender.fineDate);
        firstName.setText(offender.firstName);
        lastName.setText(offender.lastName);

        isSchoolOrConstructionZone = offender.isSchoolOrWorkZone;
        if(isSchoolOrConstructionZone){
            isSchoolOrConstructionZoneImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_special_zone));
        }else {
            isSchoolOrConstructionZoneImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_not_special_zone_24));

        }

        offenderSpeed.setText(String.valueOf(offender.offenderSpeed));
        speedLimit.setText(String.valueOf(offender.speedLimitZone));
        finedAmount.setText(String.valueOf(offender.fineAmount)+"$");

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClickDetails(offender);
            }
        });

        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        return alertDialog;
    }
    public interface ItemClickListenerDetailsDialog { void onItemClickDetails(Offender offender); }
}
