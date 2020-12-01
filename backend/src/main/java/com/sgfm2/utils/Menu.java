package com.sgfm2.utils;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Function;

abstract public class Menu {
    protected ArrayList<MenuChoiceBaseClass> currentMenu;

    abstract public ArrayList<MenuChoiceBaseClass> setInitialMenu();

    protected void setMenu(Object o) {
        currentMenu = (ArrayList<MenuChoiceBaseClass>) o;
    } // setMenu

    private void printMenu() {
        System.out.println("");
        for (MenuChoiceBaseClass m : currentMenu) {
            System.out.printf("%s%n", m.getFullTitle());
        } // for m...
        System.out.print("Choose wisely: ");
    } // printMenu

    // Hämta användarens val
    private MenuChoiceBaseClass getMenuChoice() {
        String sChoice;
        Scanner scan = new Scanner(System.in);

        // Se till så att det finns ett menyval och inte en tomrad (blir så efter nextDouble)
        do
            sChoice = scan.nextLine();
        while (sChoice.length() == 0);

        // Loopa igenom och returnera rätt menyval
        for (MenuChoiceBaseClass m : currentMenu) {
            if (m.getKey() == sChoice.charAt(0))
                return m;
        } // for...
        return null;
    } // getMenuChoice

    public void handleConsumerMenu() {
        currentMenu = setInitialMenu();

        MenuChoiceBaseClass m;
        boolean bStop = false;
        while (!bStop) {
            printMenu();

            m = getMenuChoice();
            if (m == null)
                System.out.println("No such choice, please try again!");
            else {
                System.out.printf("You selected: %s%n", m.getTitle());
                bStop = m.getFunctionToCall() == null;
                if (!bStop) {
                    Consumer consumer = (Consumer)m.getFunctionToCall();
                    consumer.accept(m.getParameter());
                } // if bStop...
            } // else
        } // while
    } // handleConsumerMenu

    public Object handleFunctionMenu(boolean skipRun) {
        currentMenu = setInitialMenu();

        MenuChoiceBaseClass m;
        boolean bStop = false;
        while (!bStop) {
            printMenu();

            m = skipRun ? currentMenu.get(0) : getMenuChoice();

            if (m == null)
                System.out.println("No such choice, please try again!");
            else {
                System.out.printf("You selected: %s%n", m.getTitle());
                bStop = m.getFunctionToCall() == null;
                if (!bStop) {
                    Function function = (Function)m.getFunctionToCall();
                    return function.apply(m.getParameter());
                } // if bStop...
            } // else
        } // while

        return null;
    } // handleFunctionMenu
} // class Menu
