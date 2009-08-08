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
package org.apache.commons.configuration2.base;

import junit.framework.TestCase;

import org.apache.commons.configuration2.expr.ConfigurationNodeHandler;
import org.apache.commons.configuration2.tree.ConfigurationNode;
import org.apache.commons.configuration2.tree.DefaultConfigurationNode;

/**
 * Test class for {@link InMemoryConfigurationSource}.
 *
 * @author Commons Configuration team
 * @version $Id$
 */
public class TestInMemoryConfigurationSource extends TestCase
{
    /** The source to be tested. */
    private InMemoryConfigurationSource source;

    /**
     * Initializes the configuration source with the following structure: tables
     * table name fields field name field name
     */
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        source = new InMemoryConfigurationSource();
    }

    /**
     * Helper method for checking an empty root node.
     */
    private void checkEmptyRoot()
    {
        ConfigurationNode root = source.getRootNode();
        assertNull("Root node has a name", root.getName());
        assertNull("Root node has a value", root.getValue());
        assertNull("Root node has a reference", root.getReference());
        assertEquals("Root node has attributes", 0, root.getAttributeCount());
        assertEquals("Root node has children", 0, root.getChildrenCount());
    }

    /**
     * Tests a newly created instance.
     */
    public void testInit()
    {
        checkEmptyRoot();
    }

    /**
     * Tests setting a new root node.
     */
    public void testSetRootNode()
    {
        DefaultConfigurationNode node = new DefaultConfigurationNode();
        source.setRootNode(node);
        assertSame("Root node was not changed", node, source.getRootNode());
    }

    /**
     * Tests setting the root node to null. This should cause an exception.
     */
    public void testSetRootNodeNull()
    {
        try
        {
            source.setRootNode(null);
            fail("Could set null root node!");
        }
        catch (IllegalArgumentException iex)
        {
            // ok
        }
    }

    /**
     * Tests the clear() implementation.
     */
    public void testClear()
    {
        source.getRootNode().setValue("Test");
        source.getRootNode().addChild(
                new DefaultConfigurationNode("test", "value"));
        source.getRootNode().addAttribute(
                new DefaultConfigurationNode("attr", "attrValue"));
        source.clear();
        checkEmptyRoot();
    }

    /**
     * Tests the node handler used by the model.
     */
    public void testGetNodeHandler()
    {
        assertTrue("Wrong node handler",
                source.getNodeHandler() instanceof ConfigurationNodeHandler);
    }
}
