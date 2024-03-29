package mirum.generate.invoice.rest.module.application;

import java.io.*;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

import javax.mail.internet.InternetAddress;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.styledxmlparser.css.media.MediaDeviceDescription;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.service.CommerceOrderLocalServiceUtil;
import com.liferay.dynamic.data.mapping.model.DDMFormInstanceRecord;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.dynamic.data.mapping.service.DDMFormInstanceRecordLocalServiceUtil;
import com.liferay.dynamic.data.mapping.service.DDMFormInstanceRecordServiceUtil;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.mail.kernel.model.MailMessage;
import com.liferay.mail.kernel.service.MailServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.*;
import freemarker.template.TemplateException;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import mirum.generate.invoice.rest.module.application.DTO.IdDTO;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.jaxrs.whiteboard.JaxrsWhiteboardConstants;

/**
 * @author root321
 */
@Component(
        property = {
                JaxrsWhiteboardConstants.JAX_RS_APPLICATION_BASE + "=/mirum",
                JaxrsWhiteboardConstants.JAX_RS_NAME + "=Greetings.Rest"
        },
        service = Application.class
)
public class MirumGenerateInvoiceRestModuleApplication extends Application {

    public Set<Object> getSingletons() {
        return Collections.<Object>singleton(this);
    }

    @GET
    @Path("/exportEntries")
    public String exportEntries() throws PortalException {

        String value =  null;
        try {
             //value = ExportFormEntries.exportEntries();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return value;
    }


    @POST
    @Path("/sendEmail")
    @Consumes(MediaType.APPLICATION_JSON)
    public static void sendEmail(@RequestBody IdDTO id) throws IOException, PortalException, TemplateException, URISyntaxException, FileNotFoundException {

        _log.info("inside send email method : ");
        CommerceOrder commerceOrder = CommerceOrderLocalServiceUtil.getCommerceOrder(id.getCommerceOrderId());
        ExpandoBridge expandoBridge = commerceOrder.getExpandoBridge();
        ByteArrayOutputStream file = null;
        File attachmentFile = null;
        if (Validator.isNotNull(expandoBridge)) {
            if (expandoBridge.getAttribute("paymentStatus").equals(false)) {
                file = generateInvoice(id);
                attachmentFile = validateFile(file);
                sendMailTemplate(attachmentFile, commerceOrder, id);
                expandoBridge.setAttribute("paymentStatus", true);
                commerceOrder.setExpandoBridgeAttributes(expandoBridge);
            }
        }

        expandoBridge.getAttributes().forEach((s, serializable) -> {
            _log.info(s + " ++ " + serializable.toString());
        });

    }

   /* @POST
    @Path("/download-temp")
    @Produces("application/pdf")
    public File createPdf() throws IOException, PortalException {
        URL url = null;
        url = new URL("http://localhost:8080/documents/d/jcc/payment_invoice_arabic");
        InputStream x = url.openStream();
        String templateContent = StringUtil.read(x);

        DecimalFormat decimalFormat = new DecimalFormat("#.00");

        CommerceOrder commerceOrder = CommerceOrderLocalServiceUtil.getCommerceOrder(45368);

        templateContent = templateContent.replaceAll("\\{\\{orderTotal\\}\\}", decimalFormat.format(commerceOrder.getTotal()));
        templateContent = templateContent.replaceAll("\\{\\{orderId\\}\\}", String.valueOf(commerceOrder.getCommerceOrderId()));
        templateContent = templateContent.replaceAll("\\{\\{orderName\\}\\}", String.valueOf(commerceOrder.getExpandoBridge().getAttribute("orderName")));
//       templateContent = templateContent.replaceAll("\\{\\{orderAccount\\}\\}", String.valueOf(commerceOrder.getCommerceAccountId()));
        //templateContent = templateContent.replaceAll("\\{\\{orderNote\\}\\}", commerceOrder.getPrintedNote());
//        templateContent = templateContent.replaceAll("\\{\\{externalReferenceCode\\}\\}", String.valueOf(commerceOrder.getExternalReferenceCode()));
        templateContent = templateContent.replaceAll("\\{\\{orderDate\\}\\}", String.valueOf(commerceOrder.getOrderDate()));
        templateContent = templateContent.replaceAll("\\{\\{transactionId\\}\\}", commerceOrder.getTransactionId());
        templateContent = templateContent.replaceAll("\\{\\{orderStatus\\}\\}", "Completed");

        templateContent = templateContent.replaceAll("\\{\\{productName\\}\\}", String.valueOf(commerceOrder.getCommerceOrderItems().get(0).getCPDefinition().getName()));
//        templateContent = templateContent.replaceAll("\\{\\{productSku\\}\\}", String.valueOf(commerceOrder.getCommerceOrderItems().get(0).getSku()));
        templateContent = templateContent.replaceAll("\\{\\{productPrice\\}\\}", decimalFormat.format(commerceOrder.getSubtotalMoney().getPrice()));
        templateContent = templateContent.replaceAll("\\{\\{productQuantity\\}\\}", String.valueOf(commerceOrder.getCommerceOrderItems().get(0).getQuantity()));
        templateContent = templateContent.replaceAll("\\{\\{productVAT\\}\\}", decimalFormat.format(commerceOrder.getTaxAmount()));
        templateContent = templateContent.replaceAll("\\{\\{productTotal\\}\\}", decimalFormat.format(commerceOrder.getTotal()));
        ConverterProperties properties = new ConverterProperties();
        MediaDeviceDescription mediaDeviceDescription =
                new MediaDeviceDescription(com.itextpdf.styledxmlparser.css.media.MediaType.PRINT);
        properties.setMediaDeviceDescription(mediaDeviceDescription);
        FontProvider fontProvider = new FontProvider();
        for (String font : FONTS) {
            FontProgram fontProgram = FontProgramFactory.createFont(font);
            fontProvider.addFont(fontProgram);
        }
        properties.setFontProvider(fontProvider);
        File file = new File("/home/root321/Documents/Projects/Mirum/workspace/mirum-gradle-workspace/modules/mirum-generate-invoice-rest-module/src/main/resources/temp-pdf/arabic.pdf");
//        HtmlConverter.convertToPdf(new File("/home/root321/Documents/Projects/Mirum/workspace/mirum-gradle-workspace/modules/mirum-generate-invoice-rest-module/src/main/resources/arabic/payment_invoice.ftl"), file, properties);
        HtmlConverter.convertToPdf(new File("/home/root321/Documents/Projects/Mirum/workspace/mirum-gradle-workspace/modules/mirum-generate-invoice-rest-module/src/main/resources/arabic/payment_invoice.ftl"), file, properties);
        return file;
    }
*/

    @POST
    @Path("/download")
    @Produces("application/pdf")
    public Response downloadPdf(@RequestBody IdDTO id) throws IOException, PortalException, TemplateException, URISyntaxException {

        _log.info("inside download pdf :");

        ByteArrayOutputStream file = generateInvoice(id);
        byte[] byteArray = file.toByteArray();

        return Response.ok(byteArray)
                .header("Content-Disposition", "attachment; filename=invoice.pdf")
                .build();
    }

    private static File validateFile(ByteArrayOutputStream file) throws IOException {
        _log.info("inside validate file method :");
        File attachmentFile = null;
        if (Validator.isNotNull(file)) {
            byte[] byteArray = file.toByteArray();
            String filePath = PropsUtil.get("invoice.file.path");
            _log.info("file path :" + filePath);
            if (Validator.isNotNull(filePath)) {
                _log.info("getting file path : ");
                attachmentFile = new File(filePath + new Date() + ".pdf");
            } else {
                _log.info("storing file at temp");
                attachmentFile = new File("temp/" + new Date() + ".pdf");
            }

            if (Validator.isNotNull(attachmentFile)) {
                _log.info("getting the file..");
                FileOutputStream fileOutputStream = null;
                try {
                    fileOutputStream = new FileOutputStream(attachmentFile);
                    fileOutputStream.write(byteArray);
                } catch (Exception e) {
                    _log.info(e);
                } finally {
                    if (Validator.isNotNull(fileOutputStream))
                        fileOutputStream.close();
                }
            }
        }
        return attachmentFile;
    }

    private static void sendMailTemplate(File attachmentFile, CommerceOrder commerceOrder, IdDTO id) throws NullPointerException{
        _log.info("inside sendMailTemplate method :");
        try {

            _log.info("sending email..");
            String subject = "";
//            URL url = new URL("https://liferay.upturn.mirummea.com/documents/d/jcc/email-template-final");
            URL url = new URL("https://www.jcci.org.sa/documents/d/jcc/email-template-3");
            String taskComments = "";
            String taskMessage = "";
            String adminEmailorText = "للتواصل والاستفسار ibrahim@gmail.com";

            if (id.getLanguageId().equals("en_US")) {
                subject = "Payment Success";
                taskMessage = "Your Order is Completed";
                taskComments = "Your Payment is completed successfully";
            } else {
                subject = "الدفع الناجح";
                taskMessage = "طلبك مكتمل";
                taskComments = "تم إتمام الدفع الخاص بك بنجاح";
            }
            InputStream x = url.openStream();
            String htmlTemplate = StringUtil.read(x);
            htmlTemplate = htmlTemplate.replaceAll("\\{\\{taskComments\\}\\}", taskComments);
            htmlTemplate = htmlTemplate.replaceAll("\\{\\{adminEmail\\}\\}", adminEmailorText);
            htmlTemplate = htmlTemplate.replaceAll("\\{\\{taskMessage\\}\\}", taskMessage);

            String toEmailAddress = null;
            long userId = commerceOrder.getUserId();
            User user = UserLocalServiceUtil.getUserById(userId);
            if (Validator.isNotNull(user)) {
                toEmailAddress = user.getEmailAddress();
                _log.info("toEmailAddress : " + toEmailAddress);
            }

            // Send the email
            _log.info("Sending mail with attachment " + attachmentFile.getAbsolutePath());
            InternetAddress fromAddress = new InternetAddress("jcservices@jcci.org.sa");
            _log.info("getting email in try catch : " + toEmailAddress);
            InternetAddress toAddress = new InternetAddress(toEmailAddress);
            MailMessage mailMessage = new MailMessage(
                    fromAddress,
                    toAddress,
                    subject,
                    htmlTemplate,
                    true
            );
            mailMessage.addFileAttachment(attachmentFile);
            MailServiceUtil.sendEmail(mailMessage);

        } catch (Exception e) {
            _log.info(e);
        }
    }

    private static ByteArrayOutputStream generateInvoice(IdDTO id) throws TemplateException, IOException, FileNotFoundException, URISyntaxException, PortalException {
        _log.info("inside converter method*****");
        URL url = null;
        if (id.getLanguageId().equals("ar_SA")) {
            url = new URL("http://localhost:8080/documents/d/jcc/payment_invoice_arabic");
        }
        else {
            url = new URL("http://localhost:8080/documents/d/jcc/payment_invoice_english");

        }
//        url = new URL("https://www.jcci.org.sa/documents/d/jcc/payment_invoice_english");

        InputStream x = url.openStream();
        String templateContent = StringUtil.read(x);

        DecimalFormat decimalFormat = new DecimalFormat("#.00");

        CommerceOrder commerceOrder = CommerceOrderLocalServiceUtil.getCommerceOrder(id.getCommerceOrderId());

        templateContent = templateContent.replaceAll("\\{\\{orderTotal\\}\\}", decimalFormat.format(commerceOrder.getTotal()));
        templateContent = templateContent.replaceAll("\\{\\{orderId\\}\\}", String.valueOf(commerceOrder.getCommerceOrderId()));
        templateContent = templateContent.replaceAll("\\{\\{orderName\\}\\}", String.valueOf(commerceOrder.getExpandoBridge().getAttribute("orderName")));
//       templateContent = templateContent.replaceAll("\\{\\{orderAccount\\}\\}", String.valueOf(commerceOrder.getCommerceAccountId()));
        //templateContent = templateContent.replaceAll("\\{\\{orderNote\\}\\}", commerceOrder.getPrintedNote());
//        templateContent = templateContent.replaceAll("\\{\\{externalReferenceCode\\}\\}", String.valueOf(commerceOrder.getExternalReferenceCode()));
        templateContent = templateContent.replaceAll("\\{\\{orderDate\\}\\}", String.valueOf(commerceOrder.getOrderDate()));
        templateContent = templateContent.replaceAll("\\{\\{transactionId\\}\\}", commerceOrder.getTransactionId());
        templateContent = templateContent.replaceAll("\\{\\{orderStatus\\}\\}", "Completed");

        templateContent = templateContent.replaceAll("\\{\\{productName\\}\\}", String.valueOf(commerceOrder.getCommerceOrderItems().get(0).getCPDefinition().getName()));
//        templateContent = templateContent.replaceAll("\\{\\{productSku\\}\\}", String.valueOf(commerceOrder.getCommerceOrderItems().get(0).getSku()));
        templateContent = templateContent.replaceAll("\\{\\{productPrice\\}\\}", decimalFormat.format(commerceOrder.getSubtotalMoney().getPrice()));
        templateContent = templateContent.replaceAll("\\{\\{productQuantity\\}\\}", String.valueOf(commerceOrder.getCommerceOrderItems().get(0).getQuantity()));
        templateContent = templateContent.replaceAll("\\{\\{productVAT\\}\\}", decimalFormat.format(commerceOrder.getTaxAmount()));
        templateContent = templateContent.replaceAll("\\{\\{productTotal\\}\\}", decimalFormat.format(commerceOrder.getTotal()));

        String language = id.getLanguageId();
        ConverterProperties properties = new ConverterProperties();
        FontProvider fontProvider = new FontProvider();
        MediaDeviceDescription mediaDeviceDescription =
                new MediaDeviceDescription(com.itextpdf.styledxmlparser.css.media.MediaType.PRINT);
//        if(language.equals("ar_SA")){
//            FontProgram fontProgram = FontProgramFactory.createFont(FONTS[0]);
//        }
        for (String font : FONTS) {
            FontProgram fontProgram = FontProgramFactory.createFont(font);
            fontProvider.addFont(fontProgram);
        }
        properties.setFontProvider(fontProvider);
//        properties.setCharset("utf-8");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        File file = new File();
        HtmlConverter.convertToPdf(templateContent.trim(), outputStream, properties);

        return outputStream;
    }

    private static final Log _log = LogFactoryUtil.getLog(MirumGenerateInvoiceRestModuleApplication.class.getName());

    public static final String[] FONTS = {
            "arabic/NotoNaskhArabic-Regular.ttf",
            "arabic/OpenSans-Regular.ttf"
    };
}
