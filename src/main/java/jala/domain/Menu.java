package jala.domain;

public interface Menu {
    /**
     * Executes the current menu
     * @return the follow menu to execute or null to exit.
     */
    Menu run() throws Exception;
}
