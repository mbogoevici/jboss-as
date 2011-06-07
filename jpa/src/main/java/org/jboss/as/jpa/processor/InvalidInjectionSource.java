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
package org.jboss.as.jpa.processor;

import org.jboss.as.ee.component.InjectionSource;
import org.jboss.as.naming.ManagedReferenceFactory;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.msc.inject.Injector;
import org.jboss.msc.service.ServiceBuilder;

/**
 * An injection source that represents a broken Persistence Unit reference
 *
 * It is possible for classes to contain PersistenceUnit/PersistenceContext
 * references, even if they are not managed and injected by the application
 * server.
 *
 * This InjectionSource allows to mark references as broken at deployment
 * time. They will cause failures only when the application server will try
 * to honour them, which may not happen.
 *
 * @author Marius Bogoevici
 */
class InvalidInjectionSource extends InjectionSource {
    private final AnnotationInstance annotation;
    private final DeploymentUnit deploymentUnit;

    public InvalidInjectionSource(AnnotationInstance annotation, DeploymentUnit deploymentUnit) {
        this.annotation = annotation;
        this.deploymentUnit = deploymentUnit;
    }

    @Override
    public void getResourceValue(ResolutionContext resolutionContext, ServiceBuilder<?> serviceBuilder, DeploymentPhaseContext phaseContext, Injector<ManagedReferenceFactory> injector) throws DeploymentUnitProcessingException {
        throw new IllegalStateException("Can't find a deployment unit named " + annotation.value("unitName").asString() + " at " + deploymentUnit);
    }

    @Override
    public boolean equalTo(InjectionSource other, DeploymentPhaseContext phaseContext) {
        return false;
    }
}
