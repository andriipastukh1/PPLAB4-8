package ui.commands;

public interface Command {
    void execute();
    default String getDesc(){
        return "No description";
    }
}
