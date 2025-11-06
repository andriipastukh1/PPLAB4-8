import ui.Menu;
import java.util.Scanner;

public class Main {



    public static void main(String[] args) {


        Menu menu = new Menu();

        Scanner sc = new Scanner(System.in);

        int choice;

        do {
            menu.showMainMenu();


            System.out.print("Your Choice: ");


            choice = sc.nextInt();
            menu.handleUserChoice(choice);




        } while (choice != 0);





    }






}
