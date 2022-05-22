package com.example.traficTicketapp_android;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity /*implements AdapterView.OnItemSelectedListener*/ implements FormDialog.ItemClickListener, DetailsDialog.ItemClickListenerDetailsDialog {

    boolean isDialogDisplayed = false;
    String dialogDialog = "dialogDisplayed";

    private final String OFFENDER_LIST = "offender_list";
    private final String OFFENDER = "offender";

    FloatingActionButton addButton;
    AlertDialog alertDialog;

    ArrayList<Offender> offenderList;
    Offender offender;

    int positionClickedItemRecyclerView = 0;

    CustomAdapter customAdapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = findViewById(R.id.noDataImage);

        if(savedInstanceState != null){
            offenderList = savedInstanceState.getParcelableArrayList(OFFENDER_LIST);
            offender = savedInstanceState.getParcelable(OFFENDER);

        }else {
            offenderList = new ArrayList<>();
            view.setVisibility(View.VISIBLE);
        }
                addButton = findViewById(R.id.addFlButton);
                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isDialogDisplayed = !isDialogDisplayed;
                        showDialog();
                    }
                });

//        Offender offender = new Offender("Ismail", "Yassine", 60, false, 70, 90, "09-May-2022");
//        offenderList.add(0, offender);

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        customAdapter = new CustomAdapter(offenderList, new CustomAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Offender offender) {
               positionClickedItemRecyclerView = offenderList.indexOf(offender);

                Bundle bundle = new Bundle();
                bundle.putParcelable("offender", offender);
                DialogFragment dialog = new DetailsDialog();
                dialog.show(getSupportFragmentManager(), "DetailsDialog");
                dialog.setArguments(bundle);
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(customAdapter);
    }

    @Override
    public void onItemClick(Offender offender) {
        offenderList.add(0, offender);
        customAdapter.notifyItemInserted(0);
        view.setVisibility(View.GONE);
        recyclerView.getLayoutManager().scrollToPosition(0);
    }


    void showDialog(){
        DialogFragment dialog = new FormDialog();
        dialog.show(getSupportFragmentManager(), "DialogForm");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    private  void showError(int id, boolean show){
        View view = findViewById(id);
        if(show){
            view.setVisibility(View.VISIBLE);
        }else{
            view.setVisibility((View.INVISIBLE));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelableArrayList(OFFENDER_LIST, offenderList);

        if (offender != null){
            savedInstanceState.putParcelable(OFFENDER, offender);
        }
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onItemClickDetails(Offender offender) {
        int pos = offenderList.indexOf(offender);
        offenderList.remove(pos);
        if (offenderList.size() == 0){
            view.setVisibility(View.VISIBLE);
        }
        customAdapter.notifyItemRemoved(pos);

    }
}