package com.app.collow.allenums;

/**
 * Created by Harmis on 07/02/17.
 */

public enum ModificationOptions {


    ADD(1), EDIT(2) ,DELETE(3),VIEW(4),START_TIME(5),END_TIME(6);
    int operationIndex = 1;

    ModificationOptions(int operationIndex) {
        this.operationIndex = operationIndex;
    }

    public int getOperationIndex() {

        return this.operationIndex;
    }
}
