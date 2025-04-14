package jala.presentation.menus;

import jala.domain.Menu;

public class MenuManager {
    private Menu currenMenu;

    public MenuManager(Menu currenMenu) {
        this.currenMenu = currenMenu;
    }

    public void run(){
        while (currenMenu != null){
            currenMenu = currenMenu.run();
        }
    }
}
