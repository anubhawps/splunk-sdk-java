/*
 * Copyright 2011 Splunk, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"): you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.splunk.sdk.tests.com.splunk;

import com.splunk.*;
import com.splunk.sdk.Program;
import com.splunk.Service;

import java.io.IOException;

import junit.framework.TestCase;
import junit.framework.Assert;

import org.junit.*;

public class MessageTest extends TestCase {
    Program program = new Program();

    public MessageTest() {}

    Service connect() throws IOException {
        return new Service(
            program.host, program.port, program.scheme)
                .login(program.username, program.password);
    }

    @Before public void setUp() {
        this.program.init(); // Pick up .splunkrc settings
    }

    @Test public void testMessage() throws Exception {
        Service service = connect();

        EntityCollection messages = service.getMessages();

        if (messages.containsKey("sdk-test-message1")) {
            messages.remove("sdk-test-message1");
        }
        Assert.assertFalse(messages.containsKey("sdk-test-message1"));

        if (messages.containsKey("sdk-test-message2")) {
            messages.remove("sdk-test-message2");
        }
        Assert.assertFalse(messages.containsKey("sdk-test-message2"));

        Args args1 = new Args();
        args1.put("value", "hello.");
        messages.create("sdk-test-message1", args1);

        Assert.assertTrue(messages.containsKey("sdk-test-message1"));
        Message message = (Message)messages.get("sdk-test-message1");
        Assert.assertTrue(message.getKey().equals("sdk-test-message1"));
        Assert.assertTrue(message.getValue().equals("hello."));

        Args args2 = new Args();
        args2.put("value", "world.");
        messages.create("sdk-test-message2", args2);

        Assert.assertTrue(messages.containsKey("sdk-test-message2"));
        message = (Message)messages.get("sdk-test-message2");
        Assert.assertTrue(message.getKey().equals("sdk-test-message2"));
        Assert.assertTrue(message.getValue().equals("world."));

        messages.remove("sdk-test-message1");
        messages.remove("sdk-test-message2");
        Assert.assertFalse(messages.containsKey("sdk-test-message1"));
        Assert.assertFalse(messages.containsKey("sdk-test-message2"));
    }
}