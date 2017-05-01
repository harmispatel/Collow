package com.app.collow.allenums;

/**
 * Created by Harmis on 01/02/17.
 */

public enum CommunityInformationScreenEnum {
    FROM_COMMUNITY_SEARCH(1), FROM_COMMUNITY_SEARCH_HISTORY(2), NORMAL_SEARCH_LISTING(3);
    int methodIndex = 1;

    CommunityInformationScreenEnum(int fromWhereCalledScreenIndex) {
        this.methodIndex = methodIndex;
    }

    public int getIndexFromWhereCalledCommunityInformation() {

        return this.methodIndex;
    }
}
