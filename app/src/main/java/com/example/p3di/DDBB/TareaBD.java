package com.example.p3di.DDBB;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.p3di.core.Tarea;
import com.example.p3di.core.TypeConverterDate;

@Database(entities = {Tarea.class}, version = 1, exportSchema = false)
@TypeConverters(TypeConverterDate.class)
public abstract class TareaBD extends RoomDatabase {
    public abstract TareaDao getTareaDao();
}
