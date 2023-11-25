import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Server {
	 
	final static int PORT=6001;  //Деклариране на константа порт на който сървъра ще върви
	static List<MyThread> MyList = new ArrayList<>();  //Деклариране на лист масив в който ще бъдат всички клиенти
    static int n=0;  //Деклариране на число което ще бъде случайно
    static boolean a=false;
    public static boolean IsTheGameOver=false;
	public static void main(String[] args) throws IOException
	{
	
					
	    ServerSocket S = new ServerSocket(PORT);  //Създаване и стартиране на сървър
	    Random random = new Random(); 
	     n = random.nextInt(100); //Задаване на случайна стойност от 0 до 100
	    System.out.println(n);
	   
	    
		
		while(true)
		{
			 Socket Socket = S.accept();  //accept() Блокиращ метод който чака заявка от клиент и при получена заявка връща сокет който е комуникационен канал към клиента
			 System.out.println("CONNECTION");
			 MyThread Thread = new MyThread(Socket,n);  //Деклариране на нишка която ще описва клиента
			 MyList.add(Thread);  //Добавяне на клиента към листа с клиенти
			 
			 
			 Thread.start();  //Стартиране на нишката
			 
			
		     
		     
			
		}
	}
	public static void Broadcast(String message)  //Метод който праща съобщение към всеки един клиент който се е свързал към сървъра
	{
		for( MyThread Client: MyList)   //Обхождаме масива
		{
			PrintWriter out = Client.Getout();  //Вземане на изходящия канал на конкретния клиент
			out.println(message);  //Пращане на съобщение на конкретен клиент
			
		}
	}
	public static synchronized int Guess(MyThread thread) throws IOException  //Синхронен метод който обработва отговор от клиент и го променя към число 
	{ 
		if(!IsTheGameOver)  //Проверка дали играта не е спечелена
		{
			String name = thread.Getmyname();  //Взимане на името на клиента
			BufferedReader in = thread.Getin(); //Взимане на входящия канал на клиента
			PrintWriter out = thread.Getout(); //Взимане на изходяшия канал на клиента
			String message="Its "+ name +" turn at the moment";
		    thread.Getout().println("Its your turn now");
			Broadcast(message);  //Пращане на съобщение към всички за ред
			String reader = in.readLine(); //Приема на резултат от клиента
			int result = Integer.parseInt(reader); //Парсване на резултата към число
			return result; //Връщане на резултата
		}
		else
		{   
			//Връщане на служебен резултат ако играта е приключила
			return 0;
		}
		
	}
	public static void NewGame(MyThread thread)  //Генериране на нова игра
	{  
	    if(a==false)  //Проверка дали вече числото е променено
	    {
	    	a=true;
	    	Setn();  //Промяна на числото което трябва да се отгатне
	    	IsTheGameOver=false;  //IsTheGameOver променливата сочи дали играта е приключила
	    }
	    for(MyThread Client: MyList)  //Обхождане на масив
	    {
	    
	    	Client.Setnumber(Getn());  //Промяна на конкретната стойност на числото на всеки един клиент
	    }
	   
	}
	public static int Getn()
	{
		return n;
	}
	public static void Setn()
	{
		    Random random = new Random();
			n=random.nextInt(100);
		    System.out.println(n);
		    
	}
}
