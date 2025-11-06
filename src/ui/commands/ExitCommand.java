package ui.commands;

public class ExitCommand implements Command {
    @Override
    public void execute() {
        System.out.println("exitinng See ya");
        System.exit(0);
    }
}
