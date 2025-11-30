package ui.commands;


import ui.Menu;




import java.util.HashMap;
import java.util.Map;

public class MenuEnter extends Menu{
    private Map<Integer, Command> commands = new HashMap<>();


    public MenuEnter() {
        init();
    }


    @Override
    public void init(){
        commands.put(1, new ShowReportCommand());
        commands.put(2, new ShowIncomeFromFile());
    }



@Override
public void showMainMenu() {
    System.out.println("=== Main Menu ===");


    System.out.println("1. Enter Income");
    System.out.println("2. Enter Benefits");


}





}
