import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
public class MyThread extends Thread 
{  
   private BufferedReader in; //Входящ канал декларация
   private PrintWriter out;  //Изходящ канал декларация
   private Socket socket;  //Декларация на сокет
   private int number;  //Декларация на число 
   private String name;  //Декларация на низ 
   public String Getmyname()  //Метод който чете името
   {
	   return name;
   }
   public BufferedReader Getin()  //Метод който взема входящия канал
   {
	   return in;
   }
   public PrintWriter Getout()     //Метод който взема изходящия канал
   {
	   return out;
   }
   public void Setnumber(int number)   //Записва стойността към полето number
   {
	   this.number=number;
   }
   public MyThread(Socket S,int number) throws IOException   //Разписване на конструктора с два параметъра комуникационния канал, сокет и случайното число което се предполага
   {
	   socket=S;
	   in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	   out = new PrintWriter(socket.getOutputStream(), true);
	   this.number=number;
	   
   }
   public void run() //Метод описващ нишката
   {
	   out.println("Type your name below:");
	   try
	   {
		name=in.readLine();  //Приемане на резултат на клиента  неговото име и го записваме в променлива name
	   } 
	   catch (IOException e) //Обработване на грешката
	   {	
		e.printStackTrace();
	   }
	    while(true)
	    {
	    	
		    int input = 0;
			try 
			{
				input = Server.Guess(this); //Викане на Guess метода и записване на резултата а именно числото въведено от клиента в променлива input
				Server.Broadcast( name + " Made a guess ");  //Пращане на съобщение че някой е направил предположение на числото
			} 
			catch (IOException e)
			{
				
				e.printStackTrace();
			}
		    if(input>number)  //Логически проверки за подсказване на конкретния клиент
		    {
		    	 out.println("The number is less!");
		    }
		    else if(input<number)
		    {
		    	out.println("The number is greater!");
		    }
		    else  //Познаване на числото
		    {  
		    	Server.a=false;  //Задаване на помощтни булеви
		    	Server.IsTheGameOver=true;
		    	out.println("Congratulations you guessed the number!");	
		    	String test = name + " Guessed the number ";
		    	Server.Broadcast(test);
		    	Server.Broadcast("Do you want to play again?");		//Запитване на клиента дали иска да играе нова игра    	
		    	try 
		    	{ 
		    		int answear=Integer.parseInt(in.readLine());  
		    		if(answear==1)  //Ако въведено едно ще генерира нова игра
		    		{   
		    			Server.NewGame(this);  
		    			System.out.println("Continue");
		    		}
		    		else   //В противен случай се прекратява връзката на комуникационния канал
		    		{ 
		    			System.out.println("Disconnect");
		    			socket.close();
						break;
		    		}
					
				} 
		    	catch (IOException e)
		    	{					
					e.printStackTrace();
				}
		    }		     
	    }	    
   }
}
