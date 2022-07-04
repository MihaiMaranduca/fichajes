package com.hiperusera.fichajes.screen.fichaje;


import com.hiperusera.fichajes.entity.Fichaje;
import io.jmix.ui.Dialogs;
import io.jmix.ui.component.Button;
import io.jmix.ui.executor.BackgroundTask;
import io.jmix.ui.executor.TaskLifeCycle;
import io.jmix.ui.model.CollectionContainer;
import io.jmix.ui.model.DataContext;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


@UiController("fich_Fichaje.browse")
@UiDescriptor("fichaje-browse.xml")
@LookupComponent("fichajesTable")

public class FichajeBrowse extends StandardLookup<Fichaje> {

    @Autowired
    private CollectionContainer<Fichaje> fichajesDc;
    @Autowired
    private Dialogs dialogs;
    @Autowired
    private DataContext dataContext;

    @Subscribe("button_quitarDuplicados")
    public void onButton_quitarDuplicadosClick(Button.ClickEvent event) {

        BackgroundTask<Integer, Void> task = new duplicatesTask();

        dialogs.createBackgroundWorkDialog(this, task)
                .withCaption("Borrando datos")
                .withShowProgressInPercentage(true)
                .withCancelAllowed(true)
                .show();
    }

    private class duplicatesTask extends BackgroundTask<Integer, Void> {
        public duplicatesTask() {

            super(5);
        }

        @Override
        public Void run(TaskLifeCycle<Integer> taskLifeCycle) throws Exception {

            List<Fichaje> fichajeArrayList = new ArrayList<>(fichajesDc.getMutableItems());
            List<Fichaje> objetosAEliminar = getObjetosAeliminar(fichajeArrayList);

            for (Fichaje fi: objetosAEliminar) {
                //fichajesDc.getMutableItems().remove(fi);
                dataContext.remove(fi);
            }
            dataContext.commit();
            return null;
        }
    }

    private List<Fichaje> getObjetosAeliminar(List<Fichaje> fichajeArrayList) {
        List<Fichaje> objetosAEliminar = new ArrayList<>();
        for (int i = 0; i<fichajeArrayList.size(); i++){
            for (int j = i+1; j<fichajeArrayList.size(); j++){
                if (fichajeArrayList.get(i).getNumeroTarjeta().equals(fichajeArrayList.get(j).getNumeroTarjeta())){
                    LocalDateTime fecha1 = fichajeArrayList.get(i).getMarcaTiempo();
                    LocalDateTime fecha2 = fichajeArrayList.get(j).getMarcaTiempo();
                    long minutes = ChronoUnit.MINUTES.between(fecha1, fecha2);
                    if (minutes < 5 && minutes >= 0){
                        objetosAEliminar.add(fichajeArrayList.get(j));
                    }else if (minutes > -5 && minutes < 0){
                        objetosAEliminar.add(fichajeArrayList.get(i));
                    }
                }
            }
        }
        return objetosAEliminar;
    }


    @Install(to = "fichajesTable", subject = "styleProvider")
    private String fichajesTableStyleProvider(Fichaje entity, String property) {

        ArrayList<Fichaje> fichajeArrayList = new ArrayList<>(fichajesDc.getItems());

        HashSet<String> distintosNumerosEmpleados = new HashSet<>();


        int contador = 0;

        for (Fichaje fichaje: fichajeArrayList) {
            distintosNumerosEmpleados.add(fichaje.getNumeroTarjeta());
        }

        for (String nrTarjeta: distintosNumerosEmpleados ) {
            for (Fichaje fichaje: fichajeArrayList) {
                if (nrTarjeta.equals(fichaje.getNumeroTarjeta())){
                    contador ++;
                }
            }

            if (contador % 2!=0){
                if (nrTarjeta.equals(entity.getNumeroTarjeta())) {
                    try {
                        if (property.equalsIgnoreCase("numeroTarjeta")){
                            return "rojo";
                        }
                    }catch (NullPointerException e){
                    }
                }
            }
            contador = 0;
        }
        return null;
    }
}