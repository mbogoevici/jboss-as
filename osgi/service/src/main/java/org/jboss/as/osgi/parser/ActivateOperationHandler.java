/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.as.osgi.parser;

import java.util.Locale;
import java.util.ResourceBundle;

import org.jboss.as.controller.AbstractRuntimeOnlyHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.descriptions.DescriptionProvider;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.descriptions.common.CommonDescriptions;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceController.Mode;
import org.jboss.osgi.framework.Services;

/**
 * Operation to activate the OSGi subsystem.
 *
 * @author David Bosschaert
 */
public class ActivateOperationHandler extends AbstractRuntimeOnlyHandler implements DescriptionProvider {
    static ActivateOperationHandler INSTANCE = new ActivateOperationHandler();

    private ActivateOperationHandler() {
    }

    @Override
    protected void executeRuntimeStep(OperationContext context, ModelNode operation) throws OperationFailedException {
        ServiceController<?> svc = context.getServiceRegistry(false).getRequiredService(Services.FRAMEWORK_ACTIVE);
        svc.setMode(Mode.ACTIVE);
        context.completeStep();
    }

    @Override
    public ModelNode getModelDescription(Locale locale) {
        ResourceBundle resourceBundle = OSGiSubsystemProviders.getResourceBundle(locale);
        return CommonDescriptions.getDescriptionOnlyOperation(resourceBundle, ModelConstants.ACTIVATE, ModelDescriptionConstants.SUBSYSTEM);
    }
}
