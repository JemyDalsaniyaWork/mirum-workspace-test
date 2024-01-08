package calendar.custom.module.portlet;

import calendar.custom.module.constants.CalendarCustomModulePortletKeys;

import com.liferay.commerce.account.model.CommerceAccount;
import com.liferay.commerce.account.service.CommerceAccountLocalServiceUtil;
import com.liferay.commerce.context.CommerceContext;
import com.liferay.commerce.context.CommerceContextFactory;
import com.liferay.commerce.currency.model.CommerceCurrency;
import com.liferay.commerce.currency.service.CommerceCurrencyLocalServiceUtil;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.product.model.*;
import com.liferay.commerce.product.service.*;
import com.liferay.commerce.product.type.grouped.model.CPDefinitionGroupedEntryTable;
import com.liferay.commerce.service.CommerceOrderItemLocalServiceUtil;
import com.liferay.commerce.service.CommerceOrderLocalService;

import com.liferay.dynamic.data.mapping.model.DDMFormInstanceRecord;
import com.liferay.object.model.ObjectEntry;
import com.liferay.object.service.ObjectEntryLocalServiceUtil;
import com.liferay.petra.sql.dsl.DSLQueryFactoryUtil;
import com.liferay.petra.sql.dsl.query.DSLQuery;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;

import javax.portlet.*;

import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;


/**
 * @author root321
 */
@Component(
        property = {
                "com.liferay.portlet.display-category=category.sample",
                "com.liferay.portlet.header-portlet-css=/css/main.css",
                "com.liferay.portlet.instanceable=true",
                "javax.portlet.display-name=CalendarCustomModule",
                "javax.portlet.init-param.template-path=/",
                "javax.portlet.init-param.view-template=/view.jsp",
                "javax.portlet.name=" + CalendarCustomModulePortletKeys.CALENDARCUSTOMMODULE,
                "javax.portlet.resource-bundle=content.Language",
                "javax.portlet.security-role-ref=power-user,user"
        },
        service = Portlet.class
)
public class CalendarCustomModulePortlet extends MVCPortlet {

    @Reference
    private CommerceOrderLocalService _commerceOrderService;


    public void createOrder(ActionRequest actionRequest, ActionResponse actionResponse)
            throws IOException, PortletException, PortalException {

        try {


            ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(
                    WebKeys.THEME_DISPLAY);

            ServiceContext serviceContext = ServiceContextFactory.getInstance(
                    DDMFormInstanceRecord.class.getName(), actionRequest);

            long groupId = ParamUtil.getLong(actionRequest, "groupId");
            User _user = UserLocalServiceUtil.fetchUser(themeDisplay.getUserId());

            CommerceAccount account =
                    CommerceAccountLocalServiceUtil.getPersonalCommerceAccount(
                            _user.getUserId());

            CommerceCurrency commerceCurrency =
                    CommerceCurrencyLocalServiceUtil.fetchPrimaryCommerceCurrency(
                            themeDisplay.getCompanyId());

            CommerceChannel commerceChannel =
                    CommerceChannelLocalServiceUtil.fetchCommerceChannelBySiteGroupId(
                            themeDisplay.getSiteGroupId());

            CommerceOrder commerceOrder = _commerceOrderService.addCommerceOrder(_user.getUserId(), commerceChannel.getGroupId(), account.getCommerceAccountId(),
                    commerceCurrency.getCommerceCurrencyId(), 0);

            CPInstance cpInstance = null;
            CommerceContext commerceContext = _commerceContextFactory.create(
                    themeDisplay.getCompanyId(),
                    _commerceChannelLocalService.getCommerceChannelGroupIdBySiteGroupId(
                            themeDisplay.getSiteGroupId()),
                    themeDisplay.getUserId(), commerceOrder.getCommerceOrderId(),
                    account.getCommerceAccountId());

//		String currentLanguage = ParamUtil.getString(actionRequest, "languageId");
            String currentLanguage = themeDisplay.getLanguageId();


            List<Object[]> cpDefinitions = null;
            CPInstance cpInstanceDynamic = null;
            List<Object> results = null;
            String[] productType = ParamUtil.getStringValues(actionRequest, "firstName");
            String eventDate = ParamUtil.getString(actionRequest, "date");
            List<String> productList = new ArrayList<>(Arrays.asList(productType));
            Map<String, Serializable> values = new HashMap<>();
            values.put("testfield", (Serializable) productList);
            values.put("eventDate", eventDate);

            ObjectEntryLocalServiceUtil.addObjectEntry(_user.getUserId(), 0, 48940, values, serviceContext);

            cpDefinitions = getCPDefinitionFromDSL(currentLanguage, productType, cpDefinitions);

            if (Validator.isNotNull(cpDefinitions)) {
                long cpDefinitionId = 0;
                for (Object[] cpDefinition : cpDefinitions) {
                    cpDefinitionId = (long) cpDefinition[0];
                    cpInstanceDynamic = CPInstanceLocalServiceUtil.getCPDefinitionApprovedCPInstances(cpDefinitionId).get(0);
                    cpInstance = CPInstanceLocalServiceUtil.getCPInstance(cpDefinitionId, cpInstanceDynamic.getSku());
                    CommerceOrderItemLocalServiceUtil.addCommerceOrderItem(commerceOrder.getCommerceOrderId(), cpInstance.getCPInstanceId(), "[]", 1, 1, commerceContext, serviceContext);
                }
            }

            System.out.println("adding item to commerce order !!!");
//            Map<String, String> valueMap = getObjectEntries();
//            actionRequest.setAttribute("eventValues", valueMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private List<Object[]> getCPDefinitionFromDSL(String currentLanguage, String[] productType, List<Object[]> cpDefinitions) {
        try {
            DSLQuery query = DSLQueryFactoryUtil
                    .selectDistinct(CPDefinitionTable.INSTANCE.CPDefinitionId, CPDefinitionLocalizationTable.INSTANCE.name, CPInstanceTable.INSTANCE.price)
                    .from(CPDefinitionTable.INSTANCE)
                    .innerJoinON(CPDefinitionLocalizationTable.INSTANCE, CPDefinitionLocalizationTable.INSTANCE.CPDefinitionId.eq(CPDefinitionTable.INSTANCE.CPDefinitionId))
                    .innerJoinON(CPInstanceTable.INSTANCE, CPInstanceTable.INSTANCE.CPDefinitionId.eq(CPDefinitionTable.INSTANCE.CPDefinitionId))
                    .where(CPDefinitionLocalizationTable.INSTANCE.name.in(productType).and(CPDefinitionLocalizationTable.INSTANCE.languageId.eq(currentLanguage)).and(CPInstanceTable.INSTANCE.status.eq(0)));


            cpDefinitions = CPDefinitionLocalServiceUtil.dslQuery(query);
            System.out.println("cpDefinitions ============ : " + cpDefinitions);
            _log.info("product size : " + cpDefinitions.size());

        } catch (Exception e) {
            _log.info("Error Occurred while getting Product Details based on the form " + e);
        }
        return cpDefinitions;
    }


    public static List<Object> getProductNames() {

        DSLQuery query2 = DSLQueryFactoryUtil
                .select(CPDefinitionLocalizationTable.INSTANCE.name)
                .from(CPDefinitionLocalizationTable.INSTANCE)
                .innerJoinON(CProductTable.INSTANCE, CProductTable.INSTANCE.publishedCPDefinitionId.eq(CPDefinitionLocalizationTable.INSTANCE.CPDefinitionId))
                .innerJoinON(CPDefinitionGroupedEntryTable.INSTANCE, CPDefinitionGroupedEntryTable.INSTANCE.entryCProductId.eq(CProductTable.INSTANCE.CProductId))
                .innerJoinON(CPDefinitionTable.INSTANCE, CPDefinitionGroupedEntryTable.INSTANCE.CPDefinitionId.eq(CPDefinitionTable.INSTANCE.CPDefinitionId))
                .where(CPDefinitionTable.INSTANCE.productTypeName.eq("grouped"));


        List<Object> result = CPDefinitionLocalServiceUtil.dslQuery(query2);
        _log.info("Name results" + result);
        return result;
    }


    public static List<Map<String, String>> getObjectEntryValues() throws PortalException {
        List<Map<String, String>> entryList = new ArrayList<>();

        try {
            List<ObjectEntry> objectEntries = ObjectEntryLocalServiceUtil.getObjectEntries(0, 48940, -1, -1);
            _log.info("count : " + ObjectEntryLocalServiceUtil.getObjectEntriesCount(0, 48940));

            for (ObjectEntry entry : objectEntries) {
                Map<String, String> entryMap = new HashMap<>();
                Map<String, Serializable> eventValues = entry.getValues();
                String name = (String) eventValues.get("testfield");
                _log.info("name : " + name);
                Timestamp date = (Timestamp) eventValues.get("eventDate");
                _log.info("date : " + date);

                if (Validator.isNotNull(name) && Validator.isNotNull(date)) {
                    entryMap.put("testfield", name);
                    entryMap.put("eventDate", String.valueOf(date));
                    entryList.add(entryMap); // Add each entry to the list
                }
            }
        } catch (PortalException e) {
            e.printStackTrace();
        }

        _log.info("multiValueMap outside for : " + entryList);
        return entryList;
    }


    private static final Log _log = LogFactoryUtil.getLog(
            CalendarCustomModulePortlet.class);


    @Reference
    private CommerceContextFactory _commerceContextFactory;

    @Reference
    private CommerceChannelLocalService _commerceChannelLocalService;
}
