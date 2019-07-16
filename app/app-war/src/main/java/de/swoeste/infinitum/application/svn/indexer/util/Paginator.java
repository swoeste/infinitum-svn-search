/*-
 * Copyright (C) 2017 Sebastian Woeste
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
package de.swoeste.infinitum.application.svn.indexer.util;

import java.util.List;

/**
 * @author swoeste
 */
public class Paginator<T> {

    private final List<T> originalModel;

    private final int     totalRecords;
    private final int     recordsPerPage;
    private final int     totalPages;

    private int           currentPage;
    private List<T>       currentPageModel;

    public Paginator(final List<T> originalModel, final int recordsPerPage) {
        this.originalModel = originalModel;
        this.totalRecords = originalModel.size();
        this.recordsPerPage = ensureGreaterZero(recordsPerPage);
        this.totalPages = calculateTotalPages(this.totalRecords, this.recordsPerPage);
        this.currentPage = 1;
        updateModel();
    }

    private final int calculateTotalPages(final int totalRecords, final int recordsPerPage) {
        int pages = totalRecords / recordsPerPage;
        if ((this.totalRecords % recordsPerPage) > 0) {
            pages = pages + 1;
        }
        return pages;
    }

    private final int ensureGreaterZero(final int value) {
        return value > 0 ? value : 1;
    }

    private int getFirstRecordOfCurrentPage() {
        return (this.currentPage * this.recordsPerPage) - this.recordsPerPage;
    }

    private void updateModel() {
        int fromIndex = getFirstRecordOfCurrentPage();
        int toIndex = getFirstRecordOfCurrentPage() + this.recordsPerPage;

        if (toIndex > this.totalRecords) {
            toIndex = this.totalRecords;
        }

        this.currentPageModel = this.originalModel.subList(fromIndex, toIndex);
    }

    /**
     * The amount of records displayed on a single page.
     *
     * @return the amount of records
     */
    public int getRecordsPerPage() {
        return this.recordsPerPage;
    }

    /**
     * The total amount of records managed by this Paginator.
     *
     * @return the total amount of records
     */
    public int getRecordsTotal() {
        return this.totalRecords;
    }

    /**
     * The total amount of pages managed by this Paginator.
     *
     * @return the total amount of pages
     */
    public int getPages() {
        return this.totalPages;
    }

    /**
     * Returns the records of the current page.
     *
     * @return a list of records
     */
    public List<T> getCurrentPageModel() {
        return this.currentPageModel;
    }

    /**
     * Get the index (1-based, not 0-based) of the current page.
     *
     * @return the index of the current page
     */
    public int getPage() {
        return this.currentPage;
    }

    /**
     * Set the index (1-based, not 0-based) of the current page.<br/>
     * If the index is equals or less then 0, the current page will be set to 1.<br/>
     * If the index is greater then max index. the current page will be set to max index.
     *
     * @param pageIndex
     *            the index of the current page
     */
    public void setPage(final int pageIndex) {
        if (pageIndex > 0) {
            if (pageIndex <= this.totalPages) {
                this.currentPage = pageIndex;
            } else {
                this.currentPage = this.totalPages;
            }
        } else {
            this.currentPage = 1;
        }
    }

    /**
     * Check if the next page exist.
     *
     * @return true if a next page exists, false if not
     */
    public boolean hasNext() {
        return this.currentPage < this.totalPages;
    }

    /**
     * Go to next page.
     */
    public void next() {
        if (this.currentPage < this.totalPages) {
            this.currentPage++;
            updateModel();
        }
    }

    /**
     * Go to last page.
     */
    public void last() {
        if (this.currentPage < this.totalPages) {
            this.currentPage = this.totalPages;
            updateModel();
        }
    }

    /**
     * Check if the previous page exist.
     *
     * @return true if a previous page exists, false if not
     */
    public boolean hasPrev() {
        return this.currentPage > 1;
    }

    /**
     * Go to previous page.
     */
    public void prev() {
        if (this.currentPage > 1) {
            this.currentPage--;
            updateModel();
        }
    }

    /**
     * Go to first page.
     */
    public void first() {
        if (this.currentPage > 1) {
            this.currentPage = 1;
            updateModel();
        }
    }

}