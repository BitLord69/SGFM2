package com.sgfm2.utils;

public abstract class MenuChoiceBaseClass {
    private char key;
    private String sTitle;
    private Object parameter;

    public MenuChoiceBaseClass(String sTitle, char key, Object parameter) {
        this.key = key;
        this.sTitle = sTitle;
        this.parameter = parameter;
    } // MenuChoice

    public MenuChoiceBaseClass(String sTitle, char key) {
        this(sTitle,key, null);
    } // MenuChoice


    public char getKey() {
        return key;
    } // getKey

    protected String getTitle() {
        return sTitle;
    } // getTitle

    protected String getFullTitle() {
        return getKey() + ". " + getTitle();
    } // getFullTitle

    public Object getParameter() { return parameter; }

    public abstract Object getFunctionToCall();
} // class MenuChoice
