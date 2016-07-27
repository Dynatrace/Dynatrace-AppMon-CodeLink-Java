/*
 * Dynatrace CodeLink Wrapper
 * Copyright (c) 2008-2016, DYNATRACE LLC
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *  Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *  Neither the name of the dynaTrace software nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */

package com.dynatrace.codelink;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class DynatraceClientVersionTest {

    @Test
    public void compareTest() {
        ClientVersion version = new ClientVersion(1, 2, 3, 4);
        assertTrue(new ClientVersion(1, 2, 3, 4).compareTo(version) == 0);
        assertTrue(new ClientVersion(2, 2, 3, 4).compareTo(version) == 1);
        assertTrue(new ClientVersion(1, 3, 3, 4).compareTo(version) == 1);
        assertTrue(new ClientVersion(0, 1, 2, 3).compareTo(version) == -1);
        assertTrue(new ClientVersion(1, 2, 3, 3).compareTo(version) == -1);
        assertTrue(new ClientVersion(1, 3, 4, 5).compareTo(version) == 1);
    }


    @Test
    public void fromStringGivenValidVersion() throws Exception {
        assertTrue(ClientVersion.fromString("6.3.5.13223").equals(new ClientVersion(6, 3, 5, 13223)));
        assertTrue(ClientVersion.fromString("0.3.5.13223").equals(new ClientVersion(0, 3, 5, 13223)));
        assertTrue(!ClientVersion.fromString("5.3.5.13223").equals(new ClientVersion(0, 3, 5, 13223)));
        assertTrue(!ClientVersion.fromString("5.3.5.0").equals(new ClientVersion(0, 3, 5, 0)));
    }

    @Test
    public void fromStringGivenInvalidVersion() {
        try {
            ClientVersion.fromString("1.2.3");
            fail("Exception not thrown when expected to do");
        } catch(IllegalArgumentException e) {
        }
        try {
            ClientVersion.fromString("");
            fail("Exception not thrown when expected to do");
        } catch(IllegalArgumentException e) {
        }
        try {
            ClientVersion.fromString("1.2.3");
            fail("Exception not thrown when expected to do");
        } catch(IllegalArgumentException e) {
        }
        try {
            ClientVersion.fromString("string.21.23.2");
            fail("Exception not thrown when expected to do");
        } catch(NumberFormatException e) {
        }
    }
}
