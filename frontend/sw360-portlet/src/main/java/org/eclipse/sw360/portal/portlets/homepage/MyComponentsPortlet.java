/*
 * Copyright Siemens AG, 2013-2015. Part of the SW360 Portal Project.
 * With modifications by Bosch Software Innovations GmbH, 2016.
 *
 * SPDX-License-Identifier: EPL-1.0
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.sw360.portal.portlets.homepage;

import org.eclipse.sw360.datahandler.common.CommonUtils;
import org.eclipse.sw360.datahandler.thrift.components.Component;
import org.eclipse.sw360.datahandler.thrift.users.User;
import org.eclipse.sw360.portal.portlets.Sw360Portlet;
import org.eclipse.sw360.portal.users.UserCacheHolder;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.osgi.service.component.annotations.ConfigurationPolicy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.*;

import static org.apache.log4j.Logger.getLogger;
import static org.eclipse.sw360.portal.common.PortalConstants.MY_COMPONENTS_PORTLET_NAME;

@org.osgi.service.component.annotations.Component(
    immediate = true,
    properties = {
        "/org/eclipse/sw360/portal/portlets/base.properties",
        "/org/eclipse/sw360/portal/portlets/user.properties"
    },
    property = {
        "com.liferay.portlet.header-portlet-javascript=/webjars/jquery-ui/jquery-ui.min.js",
        "com.liferay.portlet.header-portlet-javascript=/webjars/datatables.net/js/jquery.dataTables.min.js",

        "javax.portlet.name=" + MY_COMPONENTS_PORTLET_NAME,

        "javax.portlet.display-name=My Components",
        "javax.portlet.info.short-title=My Components",
        "javax.portlet.info.title=My Components",

        "javax.portlet.init-param.view-template=/html/homepage/mycomponents/view.jsp",
    },
    service = Portlet.class,
    configurationPolicy = ConfigurationPolicy.REQUIRE
)
public class MyComponentsPortlet extends Sw360Portlet {

    private static final Logger log = getLogger(MyComponentsPortlet.class);

    @Override
    public void doView(RenderRequest request, RenderResponse response) throws IOException, PortletException {
        List<Component> components;
        try {
            final User user = UserCacheHolder.getUserFromRequest(request);
            components = thriftClients.makeComponentClient().getMyComponents(user);
        } catch (TException e) {
            log.error("Could not fetch your components from backend", e);
            components = new ArrayList<>();
        }
        request.setAttribute("components",  CommonUtils.nullToEmptyList(components));
        super.doView(request, response);
    }
}
