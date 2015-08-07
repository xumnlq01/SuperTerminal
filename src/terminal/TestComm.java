package terminal;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.util.Scanner;

public class TestComm {
	private TerminalComm comm;
	public TestComm(){
		comm=new TerminalComm();
		comm.open("COM5", 9600, SerialPort.DATABITS_8,SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		comm.addDataReadyListener(new SerialPortEventListener() {
			
			@Override
			public void serialEvent(SerialPortEvent arg0) {
				// TODO Auto-generated method stub
				System.out.print("Read:"+comm.read());
			}
		});
	}
	
	public void run(){
		Scanner sc=new Scanner(System.in);
		String s;
		int n=100;
		while(n-->0){
			s=sc.next();
			comm.write(s);
		}
		sc.close();
	}
	
	/*public static void main(String args[]){
		(new TestComm()).run();
	}*/
}
