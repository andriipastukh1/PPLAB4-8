package ui.commands;

public class ExitCommand implements Command {
    @Override
    public void execute() {
        System.out.println("Exit. Goodbye.");
        System.exit(0);
    }
}
