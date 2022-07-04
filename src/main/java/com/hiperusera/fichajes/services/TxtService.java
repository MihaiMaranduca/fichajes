package com.hiperusera.fichajes.services;
import com.hiperusera.fichajes.app.FtpConfigAlberto;
import com.hiperusera.fichajes.entity.Fichaje;
import com.hiperusera.fichajes.entity.TipoFichaje;
import io.jmix.core.DataManager;
import io.jmix.core.querycondition.LogicalCondition;
import io.jmix.core.querycondition.PropertyCondition;
import io.jmix.core.security.Authenticated;
import org.apache.commons.net.ftp.FTPClient;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class TxtService implements Job {
    @Autowired
    private DataManager dataManager;
    String directorioRemoto = "/telecom/comerciales";
    String directorioLocal = "C:/Users/upcinf067/IdeaProjects/fichajes";
    StringBuilder logServidorFtp = new StringBuilder();
    @Autowired
    private FtpConfigAlberto ftpConfigAlberto;



    public void encontrarListosParaEnviar() throws IOException {

        String fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyy_HHmmss"));
        //si se pone en botón es posible que se pierdan datos al pulsar dos veces seguidas
        // porque se sobreescrebiría el archivo

        List<Fichaje> fichajeList = new ArrayList<>(dataManager.load(Fichaje.class)
                .condition(
                        LogicalCondition.and(
                                PropertyCondition.equal("enviado", false),
                                PropertyCondition.equal("revisado", true)
                        )).list());

///MIRAR porque las condiciones no son solo estas. Depende de la respuesta que obtengo al revisar si existe el archivo en remoto.
        if (fichajeList.size()>0){

            File txt = new File("fichajes_temporal" +fileName+ ".txt");
            FileWriter myWriter = new FileWriter(txt);

            for (Fichaje f : fichajeList) {

                String numeroTarjeta = f.getNumeroTarjeta();
                String localDate = (f.getMarcaTiempo().toLocalDate().format(DateTimeFormatter.ofPattern("ddMMyyyy")));
                String localTime = (f.getMarcaTiempo().toLocalTime().format(DateTimeFormatter.ofPattern("HHmmss")));
                String nrTerminal = String.valueOf(f.getNumeroSerie());
                String numeroTipoFichaje;
                if (f.getTipoFichaje()==TipoFichaje.ENTRADA || f.getTipoFichaje()==TipoFichaje.SALIDA){
                    numeroTipoFichaje = "1";
                }else {
                    numeroTipoFichaje = "2";
                }

                try {
                    myWriter.write(numeroTarjeta);
                    myWriter.write(";");
                    myWriter.write(localDate);
                    myWriter.write(";");
                    myWriter.write(localTime);
                    myWriter.write(";");
                    myWriter.write(nrTerminal);
                    myWriter.write(";");
                    myWriter.write(numeroTipoFichaje);
                    myWriter.write("\n");
                } catch (IOException e) {
                }
            }
            myWriter.close();

            List<Object> resultado = ftpConfigAlberto.conexionServidorFtp(logServidorFtp, directorioLocal, directorioRemoto);

            //------------------------------------
            //Envío del fichero
            //------------------------------------
            if (resultado.get(0) == Boolean.TRUE) {
                List<Object> resultadoEnvioFtp = ftpConfigAlberto.enviarFichero(((FTPClient) resultado.get(1)), directorioLocal, directorioRemoto, String.valueOf(txt), String.valueOf(txt));

                int ultimoRegistroDelLog = resultadoEnvioFtp.size() - 1;
                if (resultadoEnvioFtp.get(ultimoRegistroDelLog).toString().contains("Correcto")) {
                    for (Fichaje f : fichajeList) {
                        f.setEnviado(true);
                        dataManager.save(f);
                        txt.delete();
                    }
                }
            }
        }
    }

    @Authenticated
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            encontrarListosParaEnviar();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}