/*
 * Copyright (C) 2013 Sebastian Woeste
 * 
 * Licensed to Sebastian Woeste under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership. I license this file to You under
 * the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License
 * at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package de.swoeste.infinitum.fw.core.bl.svn.indexer.config;

/**
 * @author swoeste
 */
public class SVNInformation {

    private final String  repositoryUrl;
    private final String  username;
    private final String  password;
    private final boolean authentication;

    public SVNInformation(final String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
        this.username = null;
        this.password = null;
        this.authentication = false;
    }

    public SVNInformation(final String repositoryUrl, final String username, final String password) {
        this.repositoryUrl = repositoryUrl;
        this.username = username;
        this.password = password;
        this.authentication = true;
    }

    /**
     * @return the repositoryUrl
     */
    public String getRepositoryUrl() {
        return this.repositoryUrl;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * @return the authentication
     */
    public boolean isAuthentication() {
        return this.authentication;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("nls")
    @Override
    public String toString() {
        return "SVNInformation [repositoryUrl=" + this.repositoryUrl + ", username=" + this.username + ", password=" + this.password + ", authentication=" + this.authentication
                + "]";
    }

}
