package ui.commands;

public class ShowMainMenuCommand implements Command {

    @Override
    public void execute() {
        System.out.println("Returning to main menu...");
    }
}
