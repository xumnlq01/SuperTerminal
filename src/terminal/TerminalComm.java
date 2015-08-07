package terminal;

import java.io.*;
import java.util.*;

import gnu.io.*;


public class TerminalComm {
	//private ReadPort reader;
	//private WritePort writer;
	private OutputStream out;
	private SerialPort port;
	private InputStream in;
	
	public static ArrayList<String> listPortChoices() {
        CommPortIdentifier portId;
        ArrayList<String> arr=new ArrayList<String>();
        @SuppressWarnings("unchecked")
		Enumeration<CommPortIdentifier> en = CommPortIdentifier.getPortIdentifiers();
        // iterate through the ports.
        while (en.hasMoreElements()) {
            portId = (CommPortIdentifier) en.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
            //System.out.println(portId.getName());
            	arr.add(portId.getName());
            }
        }
        //portChoice.select(parameters.getPortName());
        return arr;
    }
	
	
	
	private SerialPort openSerialPort(String portname,int baudrates,int databits,int stopbits,int parity) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException{
		
			CommPortIdentifier commId = CommPortIdentifier.getPortIdentifier(portname);
			
			if (commId.isCurrentlyOwned()){
				//System.out.println("Error:"+ portname+" is currently in use");
				throw new PortInUseException();
			}
			if(commId.getPortType()!=CommPortIdentifier.PORT_SERIAL){
				System.out.println("Only Support Serial Port");
			}
			
			SerialPort port=(SerialPort)commId.open(this.getClass().getName(), 5000);
			port.setSerialPortParams(baudrates, databits, stopbits, parity);
			
			return port;
	}
	
	public void write(String s){
		//s=strip(s);
		try {
			
			this.out.write(s.getBytes());
			//System.out.println("Write:"+s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void write(int c){
		try {
			this.out.write(c);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String read(){
		//System.out.println("Reading");
		byte[] buffer=new byte[1024];
		try {
			this.in.read(buffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("Read:"+new String(buffer));
		String s=new String(buffer);
		s=strip(s);
		//s=s.trim();
		return s;
		
	}
	public void addDataReadyListener(SerialPortEventListener lsn){
		try {
			this.port.addEventListener(lsn);
		} catch (TooManyListenersException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		port.notifyOnDataAvailable(true);
	}
	
	
	public void open(String portname,int baudrates,int databits,int stopbits,int parity){
		try {
			port=openSerialPort(portname, baudrates, databits, stopbits, parity);
			//reader=new ReadPort(port);
			this.in=port.getInputStream();
			//writer=new WritePort(openSerialPort(portname2, baudrates, databits, stopbits, parity));
			
			//this.out=openSerialPort(portname2, baudrates, databits, stopbits, parity).getOutputStream();
			this.out=port.getOutputStream();
		} catch (NoSuchPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PortInUseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedCommOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//new Thread(reader).start();
		
	}
	public void close(){
		port.close();
	}
	private static String strip(String s){
		String ns="";
		int i;
		for(i=0;i<s.length();i++){
			if(s.charAt(i)!='\0'){
				break;
			}
		}
		for(;i<s.length();i++){
			if(s.charAt(i)=='\0'){
				break;
			}
			ns+=s.charAt(i);
		}
		return ns;
	}
	
}

