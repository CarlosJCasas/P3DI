package com.example.p3di.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.p3di.DDBB.TareaLab;
import com.example.p3di.R;
import com.example.p3di.core.Tarea;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    ArrayList<Tarea> listaTareas = new ArrayList<>();
    TareaLab myTareaLab;
    EditText fechaVencimiento, titulo;
    Switch check_importante;
    Long fechaLimite;
    boolean importante;
    boolean doubleBackToExitPressedOnce = false;
    Button botonAceptar, botonCancelar;
    FloatingActionButton addButton;
    ListaCompletaFragment listaCompletaFragment = new ListaCompletaFragment();
    ListaFinalizadasFragment listaFinalizadasFragment = new ListaFinalizadasFragment();
    ListaImportantesFragment listaImportantesFragment = new ListaImportantesFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Bsae de datos
        myTareaLab = TareaLab.get(this);
        listaTareas = (ArrayList<Tarea>) myTareaLab.getTareas();

        bottomNavigationView = findViewById(R.id.bottom_nav);
        addButton = findViewById(R.id.floatingActionButton);

        //Botón añadir
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Eliminar base de datos
//        myTareaLab.deleteAll();
        //Add fragment principal
        FragmentTransaction ftCompleta = getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragment, listaCompletaFragment, "fragment_completo");
        ftCompleta.commit();
        //Define los fragments con las listas
        bottomNavigationView.setBackgroundColor(Color.TRANSPARENT);
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.lista_completa:
                        FragmentTransaction ftCompleta = getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragment, listaCompletaFragment,"fragment_completo");
                        ftCompleta.commit();
                        return true;
                    case R.id.lista_importante:
                        FragmentTransaction ftImp = getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragment, listaImportantesFragment, "fragment_importantess");
                        ftImp.commit();
                        return true;
                    case R.id.lista_finalizada:
                        FragmentTransaction ftFin = getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragment, listaFinalizadasFragment, "fragment_finalizadas");
                        ftFin.commit();
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<String> tareasDescartadas = new ArrayList<>();
        ArrayList<String> tareasIds = new ArrayList<>();
        ArrayList<Integer> itemsSeleccionados = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        TextView title = new TextView(this);
        title.setText(R.string.tareasVencidas);
        title.setBackgroundColor(getResources().getColor(R.color.teal_200));
        title.setTextSize(25);
        title.setPadding(25,25,25,25);
        title.setTextColor(getResources().getColor(R.color.white));
        title.setGravity(Gravity.CENTER);
        builder.setCustomTitle(title);

        View customLayout = getLayoutInflater().inflate(R.layout.borrar_layout, null);
        builder.setView(customLayout);
        for(Tarea tarea : listaTareas){
            long fechaActual = Calendar.getInstance().getTimeInMillis();
            long fechaTarea = tarea.getFechaLimite();
            if(fechaActual >= fechaTarea && !tarea.isCompletado()){
              tareasDescartadas.add(tarea.getTitulo());
              tareasIds.add(tarea.getTareaId());
            }
        }
        CharSequence[] cs = tareasDescartadas.toArray(new CharSequence[tareasDescartadas.size()]);
        builder.setMultiChoiceItems(cs, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if(isChecked){
                    itemsSeleccionados.add(which);
                }else {
                    itemsSeleccionados.remove(Integer.valueOf(which));
                }
            }
        });
        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                builder1.setMessage("¿Eliminar tareas seleccionadas?");
                builder1.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(int i :itemsSeleccionados){
                            myTareaLab.delTarea(myTareaLab.getTarea(tareasIds.get(i)));
                            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_fragment);
                            if(currentFragment instanceof ListaCompletaFragment){
                                ((ListaCompletaFragment) currentFragment).actualizarLista();
                            }else if(currentFragment instanceof ListaImportantesFragment){
                                ((ListaImportantesFragment) currentFragment).actualizarLista();
                            }else if(currentFragment instanceof ListaFinalizadasFragment){
                                ((ListaFinalizadasFragment) currentFragment).actualizarLista();
                            }
                        }
                    }
                });
                builder1.setNegativeButton("Cancelar", null);
                builder1.create().show();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        if(!tareasDescartadas.isEmpty()){
            builder.create().show();
        }



    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Presiona atrás de nuevo para salir.", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
    public static String cambioLongString(long fechaLong){
        String dateString = new SimpleDateFormat("dd/MM/yyyy").format(new Date(fechaLong));
        return dateString;
    }

    public void addTask(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        TextView title = new TextView(this);
        title.setText(R.string.addTarea);
        title.setBackgroundColor(getResources().getColor(R.color.teal_200));
        title.setTextSize(25);
        title.setPadding(25,25,25,25);
        title.setTextColor(getResources().getColor(R.color.white));
        title.setGravity(Gravity.CENTER);
        builder.setCustomTitle(title);
        View customLayout = getLayoutInflater().inflate(R.layout.add_fragment, null);
        builder.setView(customLayout);

        //Crear los items para recoger los dstos y enviarlos a la lista
        titulo = customLayout.findViewById(R.id.titulo_editText);
        fechaVencimiento = customLayout.findViewById(R.id.fecha_vencimiento);
        fechaVencimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.fecha_vencimiento:
                        showDatePickerDialog();
                        break;
                }
            }
        });
        check_importante = customLayout.findViewById(R.id.switch1);
        check_importante.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    //do stuff when Switch is ON
                    importante = true;
                } else {
                    //do stuff when Switch if OFF
                    importante= false;
                }
            }
        });
        //Usar botones predeterminados
        builder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Tarea tarea = new Tarea(titulo.getText().toString(), fechaLimite, importante, false, false);
                myTareaLab.addTarea(tarea);
                //Comprobar que fragmento se muestra y notificar cambios
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_fragment);
                if(currentFragment instanceof ListaCompletaFragment){
                    ((ListaCompletaFragment) currentFragment).actualizarLista();
                }else if(currentFragment instanceof ListaImportantesFragment){
                    ((ListaImportantesFragment) currentFragment).actualizarLista();
                }else if(currentFragment instanceof ListaFinalizadasFragment){
                    ((ListaFinalizadasFragment) currentFragment).actualizarLista();
                }
            }
        });

        builder.setNegativeButton(R.string.cancelar, null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        //Boton aceptar
        botonAceptar = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        botonAceptar.setBackgroundColor(Color.TRANSPARENT);
        botonAceptar.setTextColor(Color.GREEN);
        //Boton cancelar
        botonCancelar = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        botonCancelar.setBackgroundColor(Color.TRANSPARENT);
        botonCancelar.setTextColor(Color.RED);

    }

    private void showDatePickerDialog(){
        int day, month, year;
        Calendar calendario = Calendar.getInstance();
        day = calendario.get(Calendar.DAY_OF_MONTH);
        month = calendario.get(Calendar.MONTH);
        year = calendario.get(Calendar.YEAR);
        DatePickerDialog datePickDiag = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            calendario.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            calendario.set(Calendar.MONTH, month1);
            calendario.set(Calendar.YEAR, year1);

            String selectedDate = dayOfMonth+"/"+ month1 +"/"+ year1;
            fechaLimite = calendario.getTimeInMillis();
            fechaVencimiento.setText(selectedDate);
        },year,month,day);
        datePickDiag.show();
    }
    public void actualizarLasListas(Context context){
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_fragment);
        if(currentFragment instanceof ListaCompletaFragment){
            ((ListaCompletaFragment) currentFragment).actualizarLista();
        }else if(currentFragment instanceof ListaImportantesFragment){
            ((ListaImportantesFragment) currentFragment).actualizarLista();
        }else if(currentFragment instanceof ListaFinalizadasFragment){
            ((ListaFinalizadasFragment) currentFragment).actualizarLista();
        }
    }
}