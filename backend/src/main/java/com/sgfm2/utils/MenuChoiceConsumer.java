package com.sgfm2.utils;

import java.util.function.Consumer;

public class MenuChoiceConsumer extends MenuChoiceBaseClass {
    private Consumer functionToCall;

    public MenuChoiceConsumer(String sTitle, char key, Consumer functionToCall) {
        super(sTitle, key);
        this.functionToCall = functionToCall;
    }

    public MenuChoiceConsumer(String sTitle, char key, Consumer functionToCall, Object parameter) {
        super(sTitle, key, parameter);
        this.functionToCall = functionToCall;
    }

    @Override
    public Object getFunctionToCall() {
        return functionToCall;
    } // getFunctionToCall

}
