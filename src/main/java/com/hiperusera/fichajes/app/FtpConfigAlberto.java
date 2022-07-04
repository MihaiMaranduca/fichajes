package com.hiperusera.fichajes.app;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class FtpConfigAlberto {
    public static final String NAME = "fichajes_ComunicacionFtpBean";

    String servidor = "128.0.13.68";
    int puerto = 21;
    String usuario = "ftp@hiperusera";
    String pass = "nowin2";
    Boolean modoPasivo = true;
    Boolean modoBinario = true;

    private FTPClient ftpClient;

   StringBuilder logServidorFtp = new StringBuilder();


    //metodo para conexión servidor
    public List<Object> conexionServidorFtp (StringBuilder logServidorFtp, String directorioLocal, String directorioRemoto) {


        try {
            List<Object> respuesta = new ArrayList<>();

            this.logServidorFtp = logServidorFtp;
            ftpClient = new FTPClient();
            int respuestaServidor;
            ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

            ftpClient.connect(servidor, puerto);
            this.respuestasServidorFtp();
            //codigo devuelto servidor
            respuestaServidor = ftpClient.getReplyCode();

            if (!FTPReply.isPositiveCompletion(respuestaServidor)) {
                logServidorFtp.append(respuestaServidor + "\n");
                this.cerrarConexionServidorFtp();
            } else {
                logServidorFtp.append(respuestaServidor + "\n");
            }

            boolean loginOk = ftpClient.login(usuario, pass);
            this.respuestasServidorFtp();
            if (!loginOk) {
                logServidorFtp.append("Error en login en servidor ftp" + "\n");
            } else {
                logServidorFtp.append("Login Ok en servidor ftp" + "\n");
            }

            if (modoPasivo) {
                ftpClient.enterLocalActiveMode();
            } else {
                ftpClient.enterLocalPassiveMode();
            }
            if (modoBinario) {
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            } else {
                ftpClient.setFileType(FTP.ASCII_FILE_TYPE);
            }

            List<Object> resultadoCheckDirectorio = checkDirectorioRemotoYlocal(ftpClient, directorioRemoto, directorioLocal, logServidorFtp);
            if (resultadoCheckDirectorio.get(0) == Boolean.TRUE) {
                respuesta.add(true);
                respuesta.add(ftpClient);
                respuesta.add(logServidorFtp);
                return respuesta;

            } else {
                respuesta.add(false);
                respuesta.add(ftpClient);
                respuesta.add(logServidorFtp);
                return respuesta;
            }

        } catch (Exception ex) {
            logServidorFtp.append("Error general en conexión al servidor ftp " + "\n" + ex + "\n");
            this.cerrarConexionServidorFtp();
            List<Object> respuesta = new ArrayList<>();
            respuesta.add(false);
            respuesta.add(ftpClient);
            respuesta.add(logServidorFtp);
            return respuesta;
        }

    }


    private void cerrarConexionServidorFtp() {

        try {
            if (ftpClient.isConnected()) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        } catch (IOException ex) {
            logServidorFtp.append("Error desconectando usuario y sesion del servidor ftp: " + ex + "\n");
        }
    }

    private void respuestasServidorFtp() {

        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply: replies) {
                logServidorFtp.append(aReply + "\n");
            }
        }
    }


    public List<Object> enviarFichero(FTPClient ftpClient, String directorioLocal, String directorioRemoto, String ficheroRemoto, String ficheroLocal) {

        InputStream inputStream = null;
        OutputStream outputStream = null;
        List<Object> respuesta = new ArrayList<>();

        try {
            String pathFicheroRemoto = directorioRemoto + "/" + ficheroRemoto;
            File pathFicheroLocal = new File(directorioLocal + "/" + ficheroLocal);
            //leo el fichero local en un inputstream
            inputStream = new FileInputStream(pathFicheroLocal);
            //puedo utilizar el storeFileStream o solo storeFile, la diferencia es q con el stream puedo controlar los bytes enviados
            outputStream = ftpClient.storeFileStream(pathFicheroRemoto);
            this.respuestasServidorFtp();
            //compruebo si encontramos el lugar en el remoto donde dejar el fichero en su directorio
            if (outputStream == null) {
                logServidorFtp.append("Directorio o Fichero remoto no existente o inaccesible " + directorioRemoto + ficheroRemoto + " \n");

                if (inputStream != null) {
                    inputStream.close();
                }

                respuesta.add(false);
                respuesta.add(logServidorFtp);
                return respuesta;
            }
            byte[] bytesIn = new byte[4096];
            int read = 0;
            while ((read = inputStream.read(bytesIn)) != -1) {
                outputStream.write(bytesIn, 0, read);
            }
            outputStream.close();
            inputStream.close();
            Boolean success = ftpClient.completePendingCommand();
            this.respuestasServidorFtp();

            if (success) {
                //compruebo el fichero para renombrarlo de extensión .tmp a .ped
                if (comprobarFicheroFtpRemoto(pathFicheroRemoto, pathFicheroLocal, ftpClient)) {
                    logServidorFtp.append("Fichero Enviado Correctamente: " + directorioLocal + "/" + ficheroLocal + "\n");
                    respuesta.add(true);
                    respuesta.add(logServidorFtp);
                    return respuesta;
                } else {
                    respuesta.add(false);
                    respuesta.add(logServidorFtp);
                    return respuesta;
                }
            } else {
                logServidorFtp.append("Fichero Enviado con Error: " + directorioLocal + "/" + directorioLocal + "\n");
                respuesta.add(false);
                respuesta.add(logServidorFtp);
                return respuesta;
            }


        } catch (Exception ex) {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logServidorFtp.append("Error!!!, al cerrar el inputStream en el envio del fichero" + "\n");
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    logServidorFtp.append("Error!!!, al cerrar el outputStream en el envio del fichero" + "\n");
                }
            }
            logServidorFtp.append("Error al enviar el fichero: " + directorioLocal + "/" + ficheroLocal + "\n");
            ex.printStackTrace();
            respuesta.add(false);
            respuesta.add(logServidorFtp);
            return respuesta;
        }

    }

    private boolean comprobarFicheroFtpRemoto(String ficheroRemoto, File ficheroLocal, FTPClient ftpClient){


        try {
            //renombrar fichero, viene con extensión .tmp y para ser procesado tiene que ser ped
            //primero copio el nombre y sustituyo la extensión para luego renombrar
            String ficheroRemotoNuevaExtension = ficheroRemoto.replace("fichajes_temporal", "fichajes");
            boolean renombrado = ftpClient.rename(ficheroRemoto, ficheroRemotoNuevaExtension);
            if (!renombrado) {
                logServidorFtp.append("Error renombrando fichero en servidor ftp, origen: " + ficheroRemoto + " destino: " + ficheroRemotoNuevaExtension);
                return false;
            }
            logServidorFtp.append("Correcto renombrando fichero en servidor ftp, origen: " + ficheroRemoto + " destino: " + ficheroRemotoNuevaExtension);
            return true;

        } catch (IOException e) {
            logServidorFtp.append("Error en la comprobacion del fichero enviado: " + ficheroRemoto);
            e.printStackTrace();
            return false;
        }
    }

    public boolean comprobarQueSeHayaEnviado(String ficheroRemoto, String ficheroLocal){
        return !Objects.equals(ficheroLocal, ficheroRemoto);
    }


    private List<Object> checkDirectorioRemotoYlocal(FTPClient ftpClient, String directorioRemoto, String directorioLocal, StringBuilder logServidorFtp ) {

        try {
            List<Object> respuesta = new ArrayList<>();
            int returnCode;
            //comprobamos que exista el directorio remoto donde esta el fichero
            ftpClient.changeWorkingDirectory(directorioRemoto);
            returnCode = ftpClient.getReplyCode();

            if (returnCode == 550) {
                logServidorFtp.append("Error, no existe el directorio remoto: " + directorioRemoto + "\n");
                respuesta.add(false);
                respuesta.add("Error, no existe el directorio remoto: " + directorioRemoto);
                return respuesta;

            }

            if (!new File(directorioLocal).exists()) {
                logServidorFtp.append("Error, no existe el directorio local: " + directorioLocal + "\n");
                respuesta.add(false);
                respuesta.add("Error, no existe el directorio local: " + directorioLocal);
                return respuesta;
            }

            logServidorFtp.append("El Directorio remoto actual es: " + ftpClient.printWorkingDirectory() + "\n");
            logServidorFtp.append("El Directorio local es valido: " + directorioLocal + "\n");

            respuesta.add(true);
            return respuesta;

        } catch (Exception ex) {
            logServidorFtp.append("Error, no existe directorio \n");
            List<Object> respuesta = new ArrayList<>();
            respuesta.add(false);
            respuesta.add("Error, no existe directorio");
            return respuesta;
        }
    }



}