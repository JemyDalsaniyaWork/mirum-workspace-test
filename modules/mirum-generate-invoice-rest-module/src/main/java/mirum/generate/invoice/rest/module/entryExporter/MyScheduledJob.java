//emglisj arabic working


package mirum.generate.invoice.rest.module.entryExporter;

import com.liferay.dispatch.executor.BaseDispatchTaskExecutor;
import com.liferay.dispatch.executor.DispatchTaskExecutor;
import com.liferay.dispatch.executor.DispatchTaskExecutorOutput;
import com.liferay.dispatch.model.DispatchTrigger;
import com.liferay.dynamic.data.mapping.model.*;
import com.liferay.dynamic.data.mapping.service.DDMFormInstanceLocalServiceUtil;
import com.liferay.dynamic.data.mapping.service.DDMFormInstanceRecordLocalServiceUtil;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.Validator;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Component(
        immediate = true,
        property = {
                /* can use a resource bundle key for the name */
                "dispatch.task.executor.name=My Scheduled Job",
                "dispatch.task.executor.type=dnebing.job-01"
        },
        service = DispatchTaskExecutor.class
)
public class MyScheduledJob extends BaseDispatchTaskExecutor {

    /**
     * doExecute: Invoked to complete the work of the scheduled task.
     *
     * @param dispatchTrigger            Trigger for the scheduled job.
     * @param dispatchTaskExecutorOutput Used to send details for an admin to review
     *                                   for job status.
     * @throws Exception in case of failure.
     */
    @Override
    public void doExecute(DispatchTrigger dispatchTrigger,
                          DispatchTaskExecutorOutput dispatchTaskExecutorOutput) throws Exception {


        _log.info("Scheduled task executed...");
        long groupId = Long.parseLong(dispatchTrigger.getDispatchTaskSettingsUnicodeProperties().getProperty("groupId"));
        String host = dispatchTrigger.getDispatchTaskSettingsUnicodeProperties().getProperty("host");
        int port = Integer.parseInt(dispatchTrigger.getDispatchTaskSettingsUnicodeProperties().getProperty("port"));
        String username = dispatchTrigger.getDispatchTaskSettingsUnicodeProperties().getProperty("username");
        String password = dispatchTrigger.getDispatchTaskSettingsUnicodeProperties().getProperty("password");
        String destinationFolderPath = dispatchTrigger.getDispatchTaskSettingsUnicodeProperties().getProperty("destinationFolderPath");
        String createFolderPath = dispatchTrigger.getDispatchTaskSettingsUnicodeProperties().get("folderPath");

        _log.info("Scheduled task properties : GroupId = " + groupId);
        _log.info("Scheduled task properties : host = " + host);
        _log.info("Scheduled task properties : port = " + port);
        _log.info("Scheduled task properties : username = " + username);
        _log.info("Scheduled task properties : password = " + password);
        _log.info("Scheduled task properties : destinationFolderPath = " + destinationFolderPath);
        _log.info("Scheduled task properties : folderPath = " + createFolderPath);

        try {
            String sourceFolderPath = getFormRecords(groupId, createFolderPath);
//             getFormRecords(groupId);
            //SFTPConfig.sendFile(host, port, username, password, sourceFolderPath, destinationFolderPath);
        } catch (SystemException e) {
            e.printStackTrace();
        }


        dispatchTaskExecutorOutput.setOutput("Scheduled task executed successfully.");
    }

    /**
     * getName: Returns the name for the scheduled job.
     *
     * @return String The name for the job, can be a message key for a resource
     * bundle.
     */
    @Override
    public String getName() {
        return "My Scheduled Job";
    }

    private static void deleteOldFiles(File newCsvFile, String folderPath) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().startsWith("form_entries") && file.getName().endsWith(".csv") && !file.equals(newCsvFile)) {
                    file.delete();
                }
            }
        }
    }

    public static String getFormRecords(long groupId, String createFolderPath) {
        try {
            List<DDMFormInstance> ddmFormInstances = DDMFormInstanceLocalServiceUtil.getFormInstances(groupId);
            String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String folderPath = createFolderPath + currentDate + "/";
            Path folder = Paths.get(folderPath);

            try {
                if (!Files.exists(folder)) {
                    Files.createDirectories(folder);
                    _log.info("Folder created successfully");
                } else {
                    _log.info("Folder already exists");
                }
            } catch (IOException e) {
                _log.info("Error creating folder", e);
            }

            if (Validator.isNotNull(ddmFormInstances)) {
                for (DDMFormInstance ddmFormInstance : ddmFormInstances) {
                    Locale defaultLocale = ddmFormInstance.getDDMForm().getDefaultLocale(); // Set the default locale as needed
                    long formInstanceId = ddmFormInstance.getFormInstanceId();

                    // Create a folder and CSV file with the current date
                    folderPath = createFolderPath + currentDate + "/";
                    String newCsvFilePath = folderPath + "form_entries_" + ddmFormInstance.getName(defaultLocale) + ".csv";
                    _log.info("Created file path: " + newCsvFilePath);
                    File newCsvFile = new File(newCsvFilePath);
                    FileWriter fileWriter = null;
                    CSVPrinter csvPrinter = null;

                    try {
                        fileWriter = new FileWriter(newCsvFilePath);
                        csvPrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT);

                        Map<String, Map<Integer, String>> csvMapData = new HashMap<>();
                        int csvIndex = 0;

                        List<DDMFormField> ddmFormFieldList = ddmFormInstance.getDDMForm().getDDMFormFields();
                        for (DDMFormField ddmFormField : ddmFormFieldList) {
                            if (!csvMapData.containsKey(ddmFormField.getName()) && !ddmFormField.getType().equals("separator")) {
                                Map<Integer, String> map = new HashMap<>();
                                map.put(csvIndex++, ddmFormField.getLabel().getString(ddmFormField.getLabel().getDefaultLocale()));
                                csvMapData.put(ddmFormField.getName(), map);
                            }
                        }

                        String[] headerArr = new String[csvMapData.size()];
                        csvMapData.forEach((key, value) -> value.forEach((innerKey, innerValue) -> headerArr[innerKey] = innerValue));

                        List<String> headerList = Arrays.stream(headerArr).collect(Collectors.toList());
                        csvPrinter.printRecord(headerList);

                        List<DDMFormInstanceRecord> records = DDMFormInstanceRecordLocalServiceUtil.getFormInstanceRecords(formInstanceId, 0, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);

                        Calendar twentyFourHoursAgo = (Calendar) Calendar.getInstance().clone();
                        twentyFourHoursAgo.add(Calendar.DAY_OF_YEAR, -1);

                        List<DDMFormInstanceRecord> filteredRecords;
                        filteredRecords = records.stream()
                                .filter(record -> record.getModifiedDate().after(twentyFourHoursAgo.getTime()))
                                .collect(Collectors.toList());
                        _log.info("Filtered records isEmpty: " + filteredRecords.isEmpty());

                        for (DDMFormInstanceRecord ddmFormInstanceRecord : filteredRecords) {
                            Map<String, String> csvValueMapData = new HashMap<>();
                            List<DDMFormFieldValue> ddmFormFieldValueList = ddmFormInstanceRecord.getDDMFormValues().getDDMFormFieldValues();
                            String[] data = new String[csvMapData.size()];
                            for (DDMFormFieldValue ddmFormFieldValue : ddmFormFieldValueList) {

                                Value val = ddmFormFieldValue.getValue();
                                if (Validator.isNotNull(val)) {

                                    csvValueMapData.put(ddmFormFieldValue.getName(), getFormFieldValueAsString(ddmFormFieldValue, val));
                                    if (Validator.isNotNull(csvMapData.get(ddmFormFieldValue.getName()))) {
                                        csvMapData.get(ddmFormFieldValue.getName()).forEach((key, value) ->
                                                data[key] = getFormFieldValueAsString(ddmFormFieldValue, val).trim());
                                    }
                                }

                            }
                            List<String> listData = Arrays.stream(data).collect(Collectors.toList());
                            csvPrinter.printRecord(listData);
                        }
                        // Delete old files
                        // deleteOldFiles(newCsvFile, folderPath);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (Validator.isNotNull(csvPrinter)) {
                            csvPrinter.flush();
                            csvPrinter.close();
                            fileWriter.close();
                        }
                    }
                }
                return folderPath;
            }
        } catch (PortalException | IOException e) {
            _log.info("Error getting form instances", e);
        }
        return "";
    }

    private static String getFormFieldValueAsString(DDMFormFieldValue ddmFormFieldValue, Value value) {
        if (ddmFormFieldValue != null) {
            String type = ddmFormFieldValue.getType();
            if (type.equals("date")) {
                return "date :" + value.getString(value.getDefaultLocale());
            } else if ("select".equals(type) || "radio".equals(type) || "checkbox_multiple".equals(type)) {
                String optionValue = value.getString(value.getDefaultLocale());
                optionValue = optionValue.replaceAll("[\\[\\]\"]", "");

                String[] optionArray = optionValue.split(",");
                return Arrays.stream(optionArray)
                        .map(String::trim)
                        .filter(Validator::isNotNull)
                        .map(option -> ddmFormFieldValue.getDDMFormField().getDDMFormFieldOptions().getOptionLabels(option).getString(ddmFormFieldValue.getDDMFormValues().getDefaultLocale()))
                        .collect(Collectors.joining(", "));
            } else {
                return value.getString(value.getDefaultLocale());
            }
        }
        return "";
    }

    private static final Logger _log = LoggerFactory.getLogger(MyScheduledJob.class);
}
