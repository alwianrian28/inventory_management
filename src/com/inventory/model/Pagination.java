package com.inventory.model;

/**
 *
 * @author Dearclaudia
 */
public class Pagination {
    private int currentPage;
    private int totalItems;
    private int pageSize;
    
    public Pagination(int pageSize) {
        this.pageSize = pageSize;
        this.currentPage = 1;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getTotalPages() {
        return (int) Math.ceil((double) totalItems / pageSize);
    }

    public int getStartIndex() {
        return (currentPage - 1) * pageSize;
    }

    public int getItemsPerPage() {
        return pageSize;
    }

    public void firstPage() {
        currentPage = 1;
    }

    public void nextPage() {
        if (currentPage < getTotalPages()) {
            currentPage++;
        }
    }

    public void previousPage() {
        if (currentPage > 1) {
            currentPage--;
        }
    }

    public void lastPage() {
        currentPage = getTotalPages();
    }

    public int getCurrentPage() {
        return currentPage;
    }
}
