package com.example.p3di.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p3di.DDBB.TareaLab;
import com.example.p3di.R;
import com.example.p3di.core.Tarea;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private ItemClickListener clickListener;
    private ItemLongClickListener itemLongClickListener;
    private TareaLab myTareaLab;
    Context context;
    private final LayoutInflater myInflater;
    private ArrayList<Tarea> listaTareas;



    public CustomAdapter(Context context, ArrayList<Tarea> listaTareas) {
        this.context = context;
        this.myInflater = LayoutInflater.from(context);
        this.listaTareas = listaTareas;

    }

    public void actualizarLista(ArrayList<Tarea> listaTareas){
        this.listaTareas = listaTareas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate XML del ViewHolder
        View view = myInflater.inflate(R.layout.recyclerview_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tarea tarea = listaTareas.get(position);
        holder.bind(tarea);
    }

    @Override
    public int getItemCount() {
        return listaTareas.size();
    }



    //ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener, View.OnLongClickListener{
        TextView tituloTv, fechaTv;
        CardView cardViewItem;
        ImageButton importanteButtonOff,importanteButtonOn, eliminarButton;
        CheckBox checkCompleto;

        ViewHolder(View itemView) {
            super(itemView);
            //Crear el viewHolder
            tituloTv = itemView.findViewById(R.id.titulo_textview);
            fechaTv = itemView.findViewById(R.id.fecha_textview);
            cardViewItem = itemView.findViewById(R.id.cardViewItem);
            importanteButtonOff = itemView.findViewById(R.id.boton_favorito_off);
            importanteButtonOn = itemView.findViewById(R.id.boton_favorito_on);
            eliminarButton = itemView.findViewById(R.id.boton_eliminar);
            checkCompleto = itemView.findViewById(R.id.checkbox_completado);
            itemView.setOnClickListener(this::onClick);
        }


        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.onItemCLick(v, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (itemLongClickListener != null) {
                itemLongClickListener.onItemLongClick(v, getAdapterPosition());
            }
            return true;
        }
        //Getting selected Items
        void bind (Tarea tarea){
            //Determinar si esta seleccionado o no y setear visible o no algun cambio.
            tituloTv.setText(tarea.getTitulo());
            String fechaString = MainActivity.cambioLongString(tarea.getFechaLimite());
            fechaTv.setText(fechaString);
            importanteButtonOff.setVisibility(tarea.isFavorito() ? View.INVISIBLE : View.VISIBLE);
            importanteButtonOn.setVisibility(tarea.isFavorito() ? View.VISIBLE : View.INVISIBLE);
            checkCompleto.setChecked(tarea.isCompletado());

            importanteButtonOff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    importanteButtonOff.setVisibility(View.INVISIBLE);
                    importanteButtonOn.setVisibility(View.VISIBLE);

                    tarea.setFavorito(true);
                    TareaLab.get(context).updateTarea(tarea);

                    //Tiene que actualizar las listas
                    actualizarLista((ArrayList<Tarea>) TareaLab.get(context).getTareasFavoritas());
                }
            });
            importanteButtonOn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    importanteButtonOn.setVisibility(View.INVISIBLE);
                    importanteButtonOff.setVisibility(View.VISIBLE);

                    tarea.setFavorito(false);
                    TareaLab.get(context).updateTarea(tarea);

                    //Tiene que actualizar las listas
                    actualizarLista((ArrayList<Tarea>) TareaLab.get(context).getTareasFavoritas());
                    actualizarLista((ArrayList<Tarea>) TareaLab.get(context).getTareasNoCompletadas());
                }
            });
            eliminarButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder confirmarEliminar = new AlertDialog.Builder(context);
                    confirmarEliminar.setMessage("¿Eliminar tarea?");
                    confirmarEliminar.setNegativeButton("Cancelar", null);
                    confirmarEliminar.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            TareaLab.get(context).delTarea(tarea);
                            //Tiene que actualizar las listas
                            actualizarLista((ArrayList<Tarea>) TareaLab.get(context).getTareasNoCompletadas());
                            actualizarLista((ArrayList<Tarea>) TareaLab.get(context).getTareasFavoritas());
                        }
                    });
                    confirmarEliminar.create().show();

                }
            });
            checkCompleto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        tarea.setCompletado(true);
                        TareaLab.get(context).updateTarea(tarea);
                        actualizarLista((ArrayList<Tarea>) TareaLab.get(context).getTareasNoCompletadas());
                        actualizarLista((ArrayList<Tarea>) TareaLab.get(context).getTareasFavoritas());
                    }else {
                        tarea.setCompletado(false);
                        TareaLab.get(context).updateTarea(tarea);
                        actualizarLista((ArrayList<Tarea>) TareaLab.get(context).getTareasCompletadas());
                    }
                }
            });



        }

    }

    //Get all items
    public ArrayList<Tarea> getAll(){
        return listaTareas;
    }

    //Get selected items
    public ArrayList<Tarea> getSelected(){
        ArrayList<Tarea> selected = new ArrayList<>();
        for (int i = 0; i < listaTareas.size() ; i++){
            if (listaTareas.get(i).isSelected()){
                selected.add(listaTareas.get(i));

            }
        }
        return selected;
    }


    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemCLick(View view, int position);
    }

    public void setLongClickListener(ItemLongClickListener itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
    }

    public interface ItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

}
