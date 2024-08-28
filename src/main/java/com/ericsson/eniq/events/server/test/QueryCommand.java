package com.ericsson.eniq.events.server.test;

import java.util.Map;

import com.ericsson.eniq.events.server.logging.ServicesLogger;
import com.ericsson.eniq.events.server.query.QueryParameter;
import com.ericsson.eniq.events.server.services.impl.DataServiceBean;
import com.ericsson.eniq.events.server.templates.utils.TemplateUtils;

public class QueryCommand {

    private Map<String, String> templateParameters;

    private Map<String, QueryParameter> queryParameters;

    private TemplateUtils templateUtils;

    private DataServiceBean dataServiceBean;

    private String templateName;

    public void setTemplateParameters(final Map<String, String> templateParameters) {
        this.templateParameters = templateParameters;
    }

    public void setQueryParameters(final Map<String, QueryParameter> queryParameters) {
        this.queryParameters = queryParameters;
    }

    public void setTemplateUtils(final TemplateUtils templateUtils) {
        this.templateUtils = templateUtils;
    }

    public void setDataServiceBean(final DataServiceBean dataServiceBean) {
        this.dataServiceBean = dataServiceBean;
    }

    public void setTemplateName(final String templateName) {
        this.templateName = templateName;
    }

    public String execute() throws Exception {
        final String query = templateUtils.getQueryFromTemplate(this.templateName, this.templateParameters);
        ServicesLogger.info(getClass().getName(), "execute", query);
        return dataServiceBean.getGridData("requestID", query, queryParameters, null, "+0100");
    }
}
