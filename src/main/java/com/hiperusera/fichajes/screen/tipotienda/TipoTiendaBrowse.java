package com.hiperusera.fichajes.screen.tipotienda;

import io.jmix.ui.screen.*;
import com.hiperusera.fichajes.entity.TipoTienda;

@UiController("fich_TipoTienda.browse")
@UiDescriptor("tipo-tienda-browse.xml")
@LookupComponent("tipoTiendasTable")
public class TipoTiendaBrowse extends StandardLookup<TipoTienda> {
}