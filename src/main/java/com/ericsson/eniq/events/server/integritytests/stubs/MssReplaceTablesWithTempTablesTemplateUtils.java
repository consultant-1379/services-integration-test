package com.ericsson.eniq.events.server.integritytests.stubs;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ericsson.eniq.events.server.templates.exception.ResourceInitializationException;
import com.ericsson.eniq.events.server.templates.utils.TemplateUtils;

/**
 * Place # before every table/view name in query
 * @author EECHCIK
 *
 */
public class MssReplaceTablesWithTempTablesTemplateUtils extends TemplateUtils {

    //need to ensure ordering
    private final static Map<String, String> keysToFindAndReplace = new LinkedHashMap<String, String>();

    static {
        keysToFindAndReplace.put("EVENT_E", "#EVENT_E");
        keysToFindAndReplace.put("GROUP_TYPE", "#GROUP_TYPE");
        keysToFindAndReplace.put("DIM_E_SGEH_HIER321", "#DIM_E_SGEH_HIER321");
        keysToFindAndReplace.put("DIM_Z_SGEH_HIER321", "#DIM_Z_SGEH_HIER321");
        //keysToFindAndReplace.put("DIM_E_SGEH_MCCMNC", "#DIM_E_SGEH_MCCMNC");
        keysToFindAndReplace.put("##", "#"); //for some 4G templates the parsing happened twice - cleanup by replacing the ##
        keysToFindAndReplace.put("DIM_E_MSS_FAULT_CODE", "#DIM_E_MSS_FAULT_CODE");
        keysToFindAndReplace.put("DIM_E_MSS_EVNTSRC", "#DIM_E_MSS_EVNTSRC");
        keysToFindAndReplace.put("DIM_E_MSS_EVENTTYPE", "#DIM_E_MSS_EVENTTYPE");
        keysToFindAndReplace.put("DIM_E_MSS_LCS_CLIENT_TYPE", "#DIM_E_MSS_LCS_CLIENT_TYPE");
        keysToFindAndReplace.put("DIM_E_MSS_TYPE_LOCATION_REQ", "#DIM_E_MSS_TYPE_LOCATION_REQ");
        keysToFindAndReplace.put("DIM_E_MSS_UNSUC_POSITION_REASON", "#DIM_E_MSS_UNSUC_POSITION_REASON");
        keysToFindAndReplace.put("GROUP_TYPE_E_EVNTSRC_CS", "#GROUP_TYPE_E_EVNTSRC_CS");
        keysToFindAndReplace.put("GROUP_TYPE_E_RAT_VEND_HIER3", "#GROUP_TYPE_E_RAT_VEND_HIER3");
        keysToFindAndReplace.put("GROUP_TYPE_E_RAT_VEND_HIER321", "#GROUP_TYPE_E_RAT_VEND_HIER321");
        keysToFindAndReplace.put("DIM_E_MSS_INTERNAL_CAUSE_CODE", "#DIM_E_MSS_INTERNAL_CAUSE_CODE");
        keysToFindAndReplace.put("DIM_E_SGEH_TAC", "#DIM_E_SGEH_TAC");
        keysToFindAndReplace.put("DIM_E_SGEH_RAT", "#DIM_E_SGEH_RAT");
    }

    @Override
    public String getQueryFromTemplate(final String templateFile, final Map<String, ?> parameters)
            throws ResourceInitializationException {
        final String queryFromTemplate = super.getQueryFromTemplate(templateFile, parameters);
        return findAndReplaceAllTablesWithTemporaryEquivalents(queryFromTemplate);
    }

    private String findAndReplaceAllTablesWithTemporaryEquivalents(final String queryFromTemplate) {
        String modifiedQuery = queryFromTemplate;
        for (final String key : keysToFindAndReplace.keySet()) {
            modifiedQuery = replaceAll(modifiedQuery, key, keysToFindAndReplace.get(key));
        }
        return modifiedQuery;
    }

    private String replaceAll(final String originalText, final String find, final String replace) {
        if (originalText.contains(replace)) {
            return originalText;
        }
        final Pattern pattern = Pattern.compile(find);
        final Matcher matcher = pattern.matcher(originalText);
        return matcher.replaceAll(replace);
    }
}
