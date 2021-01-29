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

public class ListaImportantesFragment extends Fragment implements CustomAdapter.ItemClickListener , CustomAdapter.ItemLongClickListener {
    ArrayList<Tarea> listaImportante = new ArrayList<>();
    TareaLab myTareaLab;
    View rootview;
    CustomAdapter myAdapter;
    RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.lista_importantes_fragment, container, false);
        myTareaLab = TareaLab.get(getContext());

        listaImportante = (ArrayList<Tarea>) myTareaLab.getTareasFavoritas();

        recyclerView = rootview.findViewById(R.id.recy_listaImportante);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        myAdapter = new CustomAdapter(getContext(), listaImportante);
        myAdapter.setClickListener(this::onItemCLick);
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

    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
    public void actualizarLista(){
        myAdapter.actualizarLista((ArrayList<Tarea>) TareaLab.get(getContext()).getTareasFavoritas());
    }


}
