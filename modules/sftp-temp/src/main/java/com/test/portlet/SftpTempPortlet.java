package com.test.portlet;

import com.jcraft.jsch.*;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.util.Validator;
import com.test.constants.SftpTempPortletKeys;

//import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author root321
 */
@Component(
        property = {
                "com.liferay.portlet.display-category=category.sample",
                "com.liferay.portlet.header-portlet-css=/css/main.css",
                "com.liferay.portlet.instanceable=true",
                "javax.portlet.display-name=SftpTemp",
                "javax.portlet.init-param.template-path=/",
                "javax.portlet.init-param.view-template=/view.jsp",
                "javax.portlet.name=" + SftpTempPortletKeys.SFTPTEMP,
                "javax.portlet.resource-bundle=content.Language",
                "javax.portlet.security-role-ref=power-user,user"
        },
        service = Portlet.class
)
public class SftpTempPortlet extends MVCPortlet {

	@Override
	public void render(
			RenderRequest renderRequest, RenderResponse renderResponse)
			throws PortletException {

		String host = "192.168.1.52";
		int port = 22;
		String username = "test";
		String password = "test";
		String destinationFolderPath = "Files/";
		String sourceFolderPath = "/home/root321/Documents/Projects/Mirum/workspace/mirum-workspace/bundles/tomcat-9.0.56/form_entries";

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
					System.err.println("Source folder does not exist: " + sourceFolderPath);
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
					System.out.println("Destination folder does not exist. Creating...");
					// Create the destination folder
					sftpChannel.mkdir(destinationFolderPath);
				}

				// Change to the destination folder on the SFTP server
				sftpChannel.cd(destinationFolderPath);

				// Upload the entire source folder to the SFTP server
				uploadFolder(sftpChannel, sourceFolder);

				System.out.println("Folder sent successfully!");
			} finally {
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
		sftpChannel.mkdir(localFolder.getName());

		// Change to the subdirectory on the SFTP server
		sftpChannel.cd(localFolder.getName());

		if(Validator.isNotNull(localFolder.listFiles())){
			for (File file : localFolder.listFiles()) {
				if (file.isFile()) {
					try (FileInputStream fis = new FileInputStream(file)) {
						// Send the file to the SFTP server
						sftpChannel.put(fis, file.getName());
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
}