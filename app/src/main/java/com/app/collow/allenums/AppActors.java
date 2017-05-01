package com.app.collow.allenums;

/**
 * Created by Harmis on 07/02/17.
 */

public enum AppActors {
    ADMIN(1), USER(2) ,MANAGEMENT(3);
    int actorIndex = 1;

    AppActors(int actorIndex) {
        this.actorIndex = actorIndex;
    }

    public int getActorIndex() {

        return this.actorIndex;
    }
}
