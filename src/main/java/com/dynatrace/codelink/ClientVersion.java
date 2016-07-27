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

public class ClientVersion implements Comparable<ClientVersion> {
    public final int major;
    public final int minor;
    public final int revision;
    public final int build;

    public ClientVersion(int major, int minor, int revision, int build) {
        this.major = major;
        this.minor = minor;
        this.revision = revision;
        this.build = build;
    }

    public static ClientVersion fromString(String version) {
        String[] parts = version.split("\\.");
        if (parts.length != 4) {
            throw new IllegalArgumentException("Provided version is in invalid format, should be M.m.r.b");
        }
        return new ClientVersion(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
    }

    @Override
    public int hashCode() {
        int result = this.major;
        result = 31 * result + this.minor;
        result = 31 * result + this.revision;
        result = 31 * result + this.build;
        return result;
    }

    @Override
    public int compareTo(ClientVersion o) {
        if (o.major > this.major) {
            return -1;
        } else if (o.major < this.major) {
            return 1;
        }
        if (o.minor > this.minor) {
            return -1;
        } else if (o.minor < this.minor) {
            return 1;
        }
        if (o.revision > this.revision) {
            return -1;
        } else if (o.revision < this.revision) {
            return 1;
        }
        if (o.build > this.build) {
            return -1;
        } else if (o.build < this.build) {
            return 1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        ClientVersion that = (ClientVersion) o;

        if (this.major != that.major) return false;
        if (this.minor != that.minor) return false;
        if (this.revision != that.revision) return false;
        return this.build == that.build;
    }
}
