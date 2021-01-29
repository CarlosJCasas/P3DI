package com.example.p3di.UI;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.p3di.R;

import java.util.Calendar;
import java.util.Date;

public class AddFragment extends DialogFragment {
    EditText tituloTarea;
    EditText fechaVencimiento;
    Switch importante;
    long fechaLimite;
    public AddFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tituloTarea = view.findViewById(R.id.titulo_editText);
        fechaVencimiento = view.findViewById(R.id.fecha_vencimiento);
        importante = view.findViewById(R.id.switch1);
        tituloTarea.requestFocus();

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder alerdiaBuilder = new AlertDialog.Builder(getActivity());
        fechaVencimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        alerdiaBuilder.setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Añadir la tarea a la lista o mandar la tarea al activity para guardarlo en la lista
                //Check si el titulo y la fecha de vencimiento isEmpty o no
                if (tituloTarea.getText().toString().isEmpty() || fechaVencimiento.getText().toString().isEmpty()){
                    Toast toast = Toast.makeText(getContext(), R.string.agregar_campos, Toast.LENGTH_LONG);
                    toast.show();
                }else {
                    //No se como hacer para recibir la fecha en formato DATE, mas bien no se como pasasrlo con un Intent
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("SENDER", "sent")
                            .putExtra("TITULO", tituloTarea.getText())
                            .putExtra("FECHA",fechaLimite)
                            .putExtra("IMPORTANTE", importante.isChecked());
                    getActivity().startActivity(intent);
// Crear un bundle para pasar la info.
//                    Bundle bundle = new Bundle();
//                    bundle.putString("SENDER", "sent");
//                    bundle.putString("TITULO",tituloTarea.getText().toString());
//                    bundle.putLong("FECHA", fechaLimite);
//                    bundle.putBoolean("IMPORTANTE", importante.isChecked());

                }
            }
        });
        alerdiaBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        return alerdiaBuilder.create();
    }

    private void showDatePickerDialog(){
        int day, month, year;
        Calendar calendario = Calendar.getInstance();
        day = calendario.get(Calendar.DAY_OF_MONTH);
        month = calendario.get(Calendar.MONTH);
        year = calendario.get(Calendar.YEAR);
        DatePickerDialog datePickDiag = new DatePickerDialog(getContext(), (view, year1, month1, dayOfMonth) -> {
            calendario.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            calendario.set(Calendar.MONTH, month1);
            calendario.set(Calendar.YEAR, year1);

            String selectedDate = dayOfMonth+"/"+ month1 +"/"+ year1;
            fechaLimite = calendario.getTimeInMillis();
            fechaVencimiento.setText(selectedDate);
        },year,month,day);
        datePickDiag.show();
    }
}
