package com.example.p3di.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p3di.DDBB.TareaLab;
import com.example.p3di.R;
import com.example.p3di.core.Tarea;

import java.util.ArrayList;

public class ListaCompletaFragment extends Fragment implements CustomAdapter.ItemClickListener , CustomAdapter.ItemLongClickListener{

    public ArrayList<Tarea> listaCompleta = new ArrayList<>();
    public ArrayList<Tarea> listaSeleccionadas = new ArrayList<>();
    public TareaLab myTareaLab;
    public View rootview;
    public CustomAdapter myAdapter;
    public RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.lista_completa_fragment, container, false);
        myTareaLab = TareaLab.get(getContext());

        listaCompleta = (ArrayList<Tarea>) myTareaLab.getTareasNoCompletadas();

        recyclerView = rootview.findViewById(R.id.recy_listacompleta);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        myAdapter = new CustomAdapter(getContext(), listaCompleta);
        myAdapter.setClickListener(this);
        recyclerView.setAdapter(myAdapter);

        return rootview;

    }

    @Override
    public void onResume() {
        super.onResume();
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemCLick(View view, int position) {
        //Que hacer cuando se hace click en el Item
    }

    @Override
    public void onItemLongClick(View view, int position) {
        //Qeu hacer cuando se hace longclick en el item

    }
    public void actualizarLista(){
        myAdapter.actualizarLista((ArrayList<Tarea>) TareaLab.get(getContext()).getTareasNoCompletadas());
    }


}
