package mirum.generate.invoice.rest.module.application;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.liferay.dynamic.data.mapping.model.DDMFormInstance;
import com.liferay.dynamic.data.mapping.model.DDMFormInstanceRecord;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.dynamic.data.mapping.model.Value;
import com.liferay.dynamic.data.mapping.service.DDMFormInstanceLocalServiceUtil;
import com.liferay.dynamic.data.mapping.service.DDMFormInstanceRecordLocalServiceUtil;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class ExportFormEntries  {

    public static String exportEntries() throws PortalException, JsonProcessingException {

        List<DDMFormInstance> ddmFormInstances = DDMFormInstanceLocalServiceUtil.getFormInstances(43855);
        List<DDMFormInstanceRecord> filteredRecords = null;
        List<DDMFormInstanceRecord> records = null;

        for (DDMFormInstance ddmFormInstance : ddmFormInstances) {

            long formInstanceId = ddmFormInstance.getFormInstanceId();
            int getFormInstanceRecordsCount = DDMFormInstanceRecordLocalServiceUtil.getFormInstanceRecordsCount(formInstanceId, 0);
            records = DDMFormInstanceRecordLocalServiceUtil.getFormInstanceRecords(formInstanceId, 0, -1, getFormInstanceRecordsCount, null);

            Calendar currentDate = Calendar.getInstance();

            //Filter based on Days
            Calendar thirtyDaysAgo = (Calendar) currentDate.clone();
            thirtyDaysAgo.add(Calendar.DAY_OF_YEAR, -2);
            //Filter based on Month
            Calendar sixMonthsAgo = (Calendar) currentDate.clone();
            sixMonthsAgo.add(Calendar.MONTH, -6);
            //Filter based on Week
            Calendar twoWeeksAgo = (Calendar) currentDate.clone();
            twoWeeksAgo.add(Calendar.WEEK_OF_YEAR, -2);

            filteredRecords = records.stream()
                    .filter(record -> record.getModifiedDate().before(thirtyDaysAgo.getTime()))
                    .collect(Collectors.toList());

            for (DDMFormInstanceRecord ddmFormInstanceRecord : filteredRecords) {
                DDMFormValues ddmFormValues = ddmFormInstanceRecord.getDDMFormValues();
                _log.info("Default Locale==" + ddmFormValues.getDefaultLocale());

                for(DDMFormFieldValue ddmFormFieldValue : ddmFormValues.getDDMFormFieldValues()) {
                    String name = ddmFormFieldValue.getFieldReference();
                    Value value =  ddmFormFieldValue.getValue();
                    _log.info("Field name:" + name + " value:" + value.getString(value.getDefaultLocale()));
                    _log.info("Form Instance Id:" + ddmFormInstanceRecord.getFormInstanceId());
                }
                _log.info("******************************************************");
            }
        }

        return null;
    }
    private static final Log _log = LogFactoryUtil.getLog(MirumGenerateInvoiceRestModuleApplication.class.getName());

}
