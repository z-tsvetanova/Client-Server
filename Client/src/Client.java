import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;
public class Client {
	
	final static int PORT=6001;
    final static String LOCALHOST="127.0.0.1";

	public static void main(String[] args) throws IOException
	{
		
        Socket S = new Socket(LOCALHOST,PORT);  //Създаване на конекция към сървър който върви на LocalHost който върви на порт 6001
        System.out.println("CONNECTION COMPLETED");  //Ще се изпълни ако конекцията се е осъществила
        BufferedReader in = new BufferedReader(new InputStreamReader(S.getInputStream())); // Изграждане на входящия канал за четене от сървъра (in)
        PrintWriter out = new PrintWriter(S.getOutputStream(), true); //Изграждане на изходящия канал за предаване към сървъра (out)
        Scanner scanner = new Scanner(System.in); 
        
        
        Thread messageReaderThread = new Thread(() -> { //Паралелно обработване на съобщение //Създаваме обект от клас Thread конструктора му приема като параметър функция тази функция описва тялото на нишката
            try {
                String serverMessage;
                while ((serverMessage = in.readLine()) != null) {
                    System.out.println("Server says: " + serverMessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        messageReaderThread.start();  //Стартираме нишката 
        
        String name=scanner.next();  //Въвеждане на име на потребител
        out.println(name);   //Пращане на името към сървъра
        System.out.println("Enter your guess (or type 'end' to exit): ");
        name=scanner.next();
        try {
            while (true) {
               
                String input = scanner.nextLine();  //Въвеждаме стойност от клавиатурата

                if ("end".equalsIgnoreCase(input)) {  //Проверяваме дали стойността е равна на end 
                   
                    break;  //Ако input-а е end прекратяваме връзката 
                }

                try {
                    int number = Integer.parseInt(input);  //Променяме тип input в тип int и я записваме в int променлива
                    out.println(number);  //Праща на сървъра числото

                    String response = in.readLine();  //Приемане на резултат от сървъра
                   

                    if (response.equals("Congratulations you guessed the number!")) {  //Проверка дали сме оцелили числото, ако го уцелим сървъра ще върне този низ
                    	System.out.println("Do you want to keep playing");
                    	System.out.println("Enter 1 to continue enter 0 to stop.");
                    	int answear=scanner.nextInt();  //Пращане на отговор дали искаме да продължим
                    	out.println(answear);
                    	if(answear==0)
                    	{
                    		break;  //Прекратяване на връзката при нежелание да продължим
                    	}
                    	else
                    	{
                    		continue;   //Продължаване на играта
                    	}
                        
                    }
                } 
                catch //Обработка на изключение в случай на въведено на друго освен число
                (NumberFormatException e)
                {
                    System.out.println("Invalid input. Please enter a number."); 
                    out.println("0");  //Пращане на служебен отговор при грешка - 0
                }
            }
        } finally {
            S.close();  //Затваряне на комуникационния канал
            scanner.close();  //Затваряне на  scanner
        }
        
	}

}
