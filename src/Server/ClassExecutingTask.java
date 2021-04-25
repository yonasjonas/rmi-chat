package Server;

import java.rmi.ConnectIOException;
import java.rmi.RemoteException;
import java.util.Date;

import java.util.Timer;

import java.util.TimerTask;

// class to start the timer for checking if client is alive. Not working correctly
public class ClassExecutingTask {

    long delay = 10 * 1000; // delay in milliseconds
    LoopTask task = new LoopTask();
    Timer timer = new Timer("TaskName");

    public void start() {
        timer.cancel();
        timer = new Timer("TaskName");
        Date executionDate = new Date();
        timer.scheduleAtFixedRate(task, executionDate, delay);
    }

    public class LoopTask extends TimerTask {
        public void run() {
           
			ChatServer ch;    			
			try {
				System.out.print("tick 10 sec");
				ch = new ChatServer();
				ch.checkClients();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }

}