package org.nearbyshops.shopkeeperapp.ModelItemSubmission.Endpoints;


import org.nearbyshops.shopkeeperapp.ModelItemSubmission.ItemImageSubmission;

import java.util.List;

/**
 * Created by sumeet on 30/6/16.
 */
public class ItemImageSubmissionEndPoint {

    private int itemCount;
    private int offset;
    private int limit;
    private int max_limit;
    private List<ItemImageSubmission> results;


    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getMax_limit() {
        return max_limit;
    }

    public void setMax_limit(int max_limit) {
        this.max_limit = max_limit;
    }


    public List<ItemImageSubmission> getResults() {
        return results;
    }

    public void setResults(List<ItemImageSubmission> results) {
        this.results = results;
    }
}
