package ui.commands;

import ui.Menu;

public class ShowMainMenuCommand implements Command {
    @Override
    public void execute() {
        new Menu().showMainMenu();
    }
}
