package com.scheduler.portlet;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.BaseMessageListener;
import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.scheduler.SchedulerEngineHelperUtil;
import com.liferay.portal.kernel.scheduler.SchedulerEntryImpl;
import com.liferay.portal.kernel.scheduler.SchedulerException;
import com.liferay.portal.kernel.scheduler.Trigger;
import com.liferay.portal.kernel.scheduler.TriggerFactoryUtil;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.GetterUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;

@Component(immediate = true, property = { "cron.expression= 0 * * ? * * " // scheduler runs every 1 min.
}, service = CsvScheduler.class)
public class CsvScheduler extends BaseMessageListener {

	private static final Log log = LogFactoryUtil.getLog(CsvScheduler.class.getName());
	@Override
	protected void doReceive(Message message) throws Exception {
	    try {
	        String timestamp = Long.toString(System.currentTimeMillis());
	        String newCsvFilePath = "/home/root321/Documents/Projects/Mirum/workspace/mirum-workspace/bundles/tomcat-9.0.56/form_entries/User_" + timestamp + ".csv";
	        File newCsvFile = new File(newCsvFilePath);
	        FileWriter fileWriter = new FileWriter(newCsvFilePath);
	        log.info("csvfile{}:"+newCsvFile);
	        CSVPrinter csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT.withHeader("UserId", "ScreenName", "EmailAddress"));

	        List<User> users = UserLocalServiceUtil.getUsers(-1, -1);

	        for (User user : users) {
	            String userId = String.valueOf(user.getUserId());

	            // Write each record
	            csvPrinter.printRecord(userId, user.getScreenName(), user.getEmailAddress());
	        }

	        // Close the CSV printer and the new file
	        csvPrinter.close();
	        fileWriter.close();

	        // Now that the new file is created and closed, delete the old ones
	        deleteOldFiles(newCsvFile);

	    } catch (SystemException | IOException e) {
	        e.printStackTrace();
	    }
	}

	// Delete old CSV files
	private void deleteOldFiles(File newCsvFile) {
	    String folderPath = "/home/root321/Documents/Projects/Mirum/workspace/mirum-workspace/bundles/tomcat-9.0.56/form_entries";
	    File folder = new File(folderPath);
	    File[] files = folder.listFiles();

	    if (files != null) {
	        for (File file : files) {
	            if (file.isFile() && file.getName().startsWith("User_") && file.getName().endsWith(".csv") && !file.equals(newCsvFile)) {
	                file.delete();
	            }
	        }
	    }
	}




	@org.osgi.service.component.annotations.Activate
	@Modified
	protected void activate(Map<String, Object> properties) throws SchedulerException {
		log.info("Scheduler Activated:::");

		try {
			String cronExpression = GetterUtil.getString(properties.get("cron.expression"), "cronExpression");

			String listenerClass = getClass().getName();
			Trigger jobTrigger = TriggerFactoryUtil.createTrigger(listenerClass, listenerClass, new Date(), null,
					cronExpression);

			SchedulerEntryImpl schedulerEntryImpl = new SchedulerEntryImpl(listenerClass, jobTrigger);

			SchedulerEngineHelperUtil.register(this, schedulerEntryImpl, DestinationNames.SCHEDULER_DISPATCH);

		} catch (Exception e) {
			log.error(e);
		}
	}

	@Deactivate
	protected void deactivate() {
	    try {
	        SchedulerEngineHelperUtil.unregister(this);
	    } catch (Exception e) {
	        log.error("Error while unregistering the scheduler: " + e.getMessage());
	    }
	}

}
