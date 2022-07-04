package com.hiperusera.fichajes.screen.tipotienda;

import io.jmix.ui.screen.*;
import com.hiperusera.fichajes.entity.TipoTienda;

@UiController("fich_TipoTienda.edit")
@UiDescriptor("tipo-tienda-edit.xml")
@EditedEntityContainer("tipoTiendaDc")
public class TipoTiendaEdit extends StandardEditor<TipoTienda> {
}