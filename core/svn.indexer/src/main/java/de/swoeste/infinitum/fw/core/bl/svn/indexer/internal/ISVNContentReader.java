/*
 * Copyright (C) 2012 Sebastian Woeste
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package de.swoeste.infinitum.fw.core.bl.svn.indexer.internal;

import org.tmatesoft.svn.core.SVNException;

import de.swoeste.infinitum.fw.core.bl.svn.indexer.action.IActionQueue;

/**
 * @author swoeste
 */
public interface ISVNContentReader {

    /**
     * Open a new connection.<br/>
     * Multiple calls of this method will not open multiple connections.
     * 
     * @throws SVNException
     */
    void openConnection() throws SVNException;

    /**
     * Close the current connection.
     */
    void closeConnection();

    /**
     * @param queue
     * @param startRevision
     * @param endRevision
     * @throws SVNException
     */
    void readRepository(final IActionQueue queue, final long startRevision, final long endRevision) throws SVNException;
}
