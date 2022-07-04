package com.hiperusera.fichajes.app;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

@Service
public class FtpConfig{
    public String server = "128.0.13.68";
    public int port = 21;
    public String user = "ftp@hiperusera";
    public String password = "nowin2";
    public FTPClient ftp;
    public void open() throws IOException{
        ftp = new FTPClient();

        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

        ftp.connect(server, port);
        int reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)){
            ftp.disconnect();
            throw new IOException("Failed connecting to FTP server");
        }
        ftp.login(user, password);
    }

    public void close() throws IOException{
        if (ftp.isConnected()){
            ftp.logout();
            ftp.disconnect();
        }
    }



   public void putFileToPath(File file, String path) throws IOException{
        ftp.storeFile(path, new FileInputStream(file));
   }
}

