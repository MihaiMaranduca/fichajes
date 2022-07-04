package com.hiperusera.fichajes.screen.fichaje;

import com.hiperusera.fichajes.entity.Fichaje;
import io.jmix.ui.screen.*;

@UiController("fich_Fichaje.edit")
@UiDescriptor("fichaje-edit.xml")
@EditedEntityContainer("fichajeDc")
public class FichajeEdit extends StandardEditor<Fichaje> {


    @Subscribe
    public void onInitEntity(InitEntityEvent<Fichaje> event) {

    }
}