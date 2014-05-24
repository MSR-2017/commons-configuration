/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.configuration.builder;

import static org.junit.Assert.assertNotNull;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.ex.ConfigurationException;
import org.apache.commons.configuration.reloading.ReloadingController;
import org.apache.commons.configuration.reloading.ReloadingDetector;
import org.apache.commons.configuration.reloading.ReloadingListener;
import org.easymock.EasyMock;
import org.junit.Test;

/**
 * Test class for {@code ReloadingBuilderSupportListener}.
 */
public class TestReloadingBuilderSupportListener
{
    /**
     * Tests that the builder is reset when a reloading event notification
     * occurs.
     */
    @Test
    public void testResetBuilderOnReloadingEvent()
    {
        ReloadingDetector detector =
                EasyMock.createMock(ReloadingDetector.class);
        EasyMock.expect(detector.isReloadingRequired()).andReturn(Boolean.TRUE);
        EasyMock.replay(detector);
        ReloadingController controller = new ReloadingController(detector);
        BasicConfigurationBuilder<Configuration> builder =
                new BasicConfigurationBuilder<Configuration>(
                        PropertiesConfiguration.class);
        BuilderEventListenerImpl builderListener =
                new BuilderEventListenerImpl();
        builder.addEventListener(ConfigurationBuilderEvent.ANY, builderListener);

        ReloadingBuilderSupportListener listener =
                ReloadingBuilderSupportListener.connect(builder, controller);
        assertNotNull("No listener returned", listener);
        controller.checkForReloading(null);
        builderListener.nextEvent(ConfigurationBuilderEvent.RESET);
        builderListener.assertNoMoreEvents();
    }

    /**
     * Tests that the controller's reloading state is reset when a new result
     * object is created.
     */
    @Test
    public void testResetReloadingStateOnResultCreation()
            throws ConfigurationException
    {
        ReloadingController controller =
                EasyMock.createMock(ReloadingController.class);
        controller.addReloadingListener(EasyMock
                .anyObject(ReloadingListener.class));
        controller.resetReloadingState();
        EasyMock.replay(controller);
        BasicConfigurationBuilder<Configuration> builder =
                new BasicConfigurationBuilder<Configuration>(
                        PropertiesConfiguration.class);

        ReloadingBuilderSupportListener listener =
                ReloadingBuilderSupportListener.connect(builder, controller);
        builder.getConfiguration();
        EasyMock.verify(controller);
    }
}
