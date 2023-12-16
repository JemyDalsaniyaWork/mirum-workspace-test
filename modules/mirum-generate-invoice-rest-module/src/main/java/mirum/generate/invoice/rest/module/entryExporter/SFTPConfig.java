package mirum.generate.invoice.rest.module.entryExporter;

import com.jcraft.jsch.*;
import com.liferay.portal.kernel.util.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class SFTPConfig {


    public static void sendFile(String host, int port, String username, String password, String sourceFolderPath, String destinationFolderPath) throws JSchException {

        try {
            JSch jsch = new JSch();

            // Create a session
            Session session = jsch.getSession(username, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            // Create an SFTP channel
            ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();

            try {
                // Check if the source folder exists
                File sourceFolder = new File(sourceFolderPath);
                if (!sourceFolder.exists() || !sourceFolder.isDirectory()) {
                    _log.info("Source folder does not exist: " + sourceFolderPath);
                    return;
                }

                // Check if the destination folder exists
                SftpATTRS attrs;
                try {
                    attrs = sftpChannel.stat(destinationFolderPath);
                } catch (SftpException e) {
                    attrs = null; // Folder does not exist
                }

                if (attrs == null) {
                   _log.info("Destination folder does not exist. Creating...");
                    // Create the destination folder
                    sftpChannel.mkdir(destinationFolderPath);
                }

                // Change to the destination folder on the SFTP server
                sftpChannel.cd(destinationFolderPath);

                // Upload the entire source folder to the SFTP server
                uploadFolder(sftpChannel, sourceFolder);

                System.out.println("Folder sent successfully!");
            }
            finally {
                // Disconnect the SFTP channel
                sftpChannel.disconnect();
            }

            // Disconnect the session
            session.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void uploadFolder(ChannelSftp sftpChannel, File localFolder) throws SftpException {
        // Create the subdirectory on the SFTP server


        SftpATTRS folderAttrs;
        try {
            folderAttrs = sftpChannel.stat(localFolder.getName());
        } catch (SftpException e) {
            folderAttrs = null; // Folder does not exist
        }

        // Create the folder if it doesn't exist
        if (folderAttrs == null) {
            try {
                sftpChannel.mkdir(localFolder.getName());
                _log.info("Folder created on the SFTP server: " + localFolder.getName());
            } catch (SftpException ex) {
                _log.info("Error creating folder on the SFTP server: " + ex.getMessage());
                ex.printStackTrace();
            }
        }

        // Change to the subdirectory on the SFTP server
        sftpChannel.cd(localFolder.getName());

        if (Validator.isNotNull(localFolder.listFiles())) {
            for (File file : localFolder.listFiles()) {
                if (file.isFile()) {
                    try (FileInputStream fis = new FileInputStream(file)) {
                        // Send the file to the SFTP server
                        sftpChannel.put(fis, file.getName(), ChannelSftp.OVERWRITE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (file.isDirectory()) {
                    // Recursively upload the contents of the subdirectory
                    uploadFolder(sftpChannel, file);
                }
            }
        }

        // Move back to the parent directory on the SFTP server
        sftpChannel.cd("..");
    }

    private static final Logger _log = LoggerFactory.getLogger(SFTPConfig.class);

}







