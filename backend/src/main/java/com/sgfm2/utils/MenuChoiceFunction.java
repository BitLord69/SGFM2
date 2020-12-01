package com.sgfm2.utils;

import java.util.function.Function;

public class MenuChoiceFunction extends MenuChoiceBaseClass {
    private Function functionToCall;

    public MenuChoiceFunction(String sTitle, char key, Function functionToCall) {
        super(sTitle, key);
        this.functionToCall = functionToCall;
    }

    public MenuChoiceFunction(String sTitle, char key, Function functionToCall, Object parameter) {
        super(sTitle, key, parameter);
        this.functionToCall = functionToCall;
    }

    @Override
    public Object getFunctionToCall() {
        return functionToCall;
    } // getFunctionToCall
}
