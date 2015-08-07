package terminal;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;

public class TerminalUI extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4931147088873183708L;

	private TerminalComm comm;
	//private DBConnection dbcon;
	private TerminalRecord record;
	
	private JTextArea cmdWindow;
	//private JTextField editWindow;
	private JLabel portLab,baudLab,databitsLab,stopbitsLab,parityLab;
	//private JTextField databaseTxt;
	private JComboBox<String> portTxt,baudTxt,databitsTxt,stopbitsTxt,parityTxt;
	private JButton apply,close;
	
	private String strBuf="";
	private int screenStat;//光标控制状态机
	private int moveCount;//光标控制
	
	//private ArrayList<String> cmdHistory;
	//private int historyIndex;
	//private int inputStat;
	//private int prePos;
	
	public TerminalUI(){
		
		this.comm=new TerminalComm();
		this.record=new TerminalRecord();
		
		cmdWindow=new JTextArea(30,40);
		//this.editWindow=new JTextField(40);
		
		//databaseLab=new JLabel("Database");
		portLab=new JLabel("Port");
		baudLab=new JLabel("Baud Rate");
		databitsLab=new JLabel("Data bits");
		stopbitsLab=new JLabel("Stop bits");
		parityLab=new JLabel("Parity");
		
		//databaseTxt=new JTextField(10);
		portTxt=new JComboBox<String>();
		baudTxt=new JComboBox<String>();
		databitsTxt=new JComboBox<String>();
		stopbitsTxt=new JComboBox<String>();
		parityTxt=new JComboBox<String>();
		
		apply=new JButton("Apply");
		close=new JButton("Close");
		
		this.setTitle("Super Terminal");
		
		this.setLayout(new GridBagLayout());
		manageLayout();
		
		//this.addKeyListenerForEditWindow();
		this.addKeyListenerForCmdWindow();
		//this.addDataReadyListener();
		this.addListenerForApplyBtn();
		this.addListenerForCloseBtn();
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
		
		screenStat=0;
		moveCount=0;
		
		//cmdHistory=new ArrayList<String>();
		//historyIndex=0;
		//inputStat=0;
	}
	
	private void manageLayout(){
		setCmdLayout();
		//setEditLayout();
		setLabelLayout();
		setOtherLayout();
		setApplyBtnLayout();
		setCloseBtnLayout();
		
	}
	/*private void setEditLayout(){
		GridBagConstraints constraints=new GridBagConstraints();
		
		constraints.gridx=0;
		constraints.gridy=10;
		constraints.gridwidth=5;
		constraints.gridheight=1;
		constraints.fill=GridBagConstraints.HORIZONTAL;
		constraints.weightx=0.8;
		this.add(editWindow,constraints);
		editWindow.setEnabled(false);
	}*/
	private void setCmdLayout(){
		GridBagConstraints constraints=new GridBagConstraints();
		
		constraints.gridx=0;
		constraints.gridy=0;
		constraints.gridwidth=5;
		constraints.gridheight=10;
		constraints.fill=GridBagConstraints.BOTH;
		constraints.weightx=0.8;
		constraints.weighty=1;
		
		
		cmdWindow.setFocusable(true);
		cmdWindow.setFocusTraversalKeysEnabled(false);
		
		cmdWindow.setEnabled(false);
		cmdWindow.setEditable(false);
		cmdWindow.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		cmdWindow.getCaret().setVisible(true);
		cmdWindow.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				cmdWindow.getCaret().setVisible(false);
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				cmdWindow.getCaret().setVisible(true);
			}
		});
		JScrollPane pane=new JScrollPane(cmdWindow);
		//pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.add(pane,constraints);
		
		return;
	}
	private void setLabelLayout(){
		GridBagConstraints constraints=new GridBagConstraints();
		constraints.gridx=6;
		constraints.gridheight=constraints.gridwidth=1;
		constraints.fill=GridBagConstraints.HORIZONTAL;
		constraints.weightx=0.05;
		constraints.weighty=0.1;
		
		constraints.gridy=0;
		//this.add(databaseLab,constraints);
		
		constraints.gridy=1;
		this.add(portLab, constraints);
		
		constraints.gridy=2;
		this.add(baudLab, constraints);
		
		constraints.gridy=3;
		this.add(databitsLab, constraints);
		
		constraints.gridy=4;
		this.add(stopbitsLab, constraints);
		
		constraints.gridy=5;
		this.add(parityLab, constraints);
		
	}
	
	private void setOtherLayout(){
		GridBagConstraints constraints=new GridBagConstraints();
		constraints.gridx=7;
		constraints.gridheight=1;
		constraints.gridwidth=2;
		constraints.fill=GridBagConstraints.HORIZONTAL;
		constraints.weightx=0.15;
		constraints.weighty=0.1;
		
		constraints.gridy=0;
		//this.add(databaseTxt,constraints);
		//databaseTxt.setText("localhost:3306");
		
		constraints.gridy=1;
		ArrayList<String> portname=TerminalComm.listPortChoices();
		for(Iterator<String> iter=portname.iterator();iter.hasNext();){
			portTxt.addItem(iter.next());
		}
		this.add(portTxt, constraints);
		
		constraints.gridy=2;
		baudTxt.addItem("9600");
		baudTxt.addItem("2400");
		baudTxt.addItem("4800");
		baudTxt.addItem("14400");
		baudTxt.addItem("19200");
		baudTxt.addItem("38400");
		baudTxt.addItem("57600");
		this.add(baudTxt, constraints);
		
		constraints.gridy=3;
		databitsTxt.addItem("DATABITS_8");
		databitsTxt.addItem("DATABITS_7");
		databitsTxt.addItem("DATABITS_6");
		databitsTxt.addItem("DATABITS_5");
		this.add(databitsTxt, constraints);
		
		constraints.gridy=4;
		stopbitsTxt.addItem("STOPBITS_1");
		stopbitsTxt.addItem("STOPBITS_2");
		stopbitsTxt.addItem("STOPBITS_1_5");
		this.add(stopbitsTxt, constraints);
		
		constraints.gridy=5;
		parityTxt.addItem("PARITY_NONE");
		parityTxt.addItem("PARITY_ODD");
		parityTxt.addItem("PARITY_EVEN");
		parityTxt.addItem("PARITY_MARK");
		parityTxt.addItem("PARITY_SPACE");
		this.add(parityTxt, constraints);
	}
	
	private void setApplyBtnLayout(){
		GridBagConstraints constraints=new GridBagConstraints();
		constraints.gridx=6;
		constraints.gridy=6;
		constraints.gridwidth=1;
		constraints.gridheight=1;
		constraints.anchor=GridBagConstraints.CENTER;
		//constraints.fill=GridBagConstraints.HORIZONTAL;
		constraints.weightx=0.1;
		constraints.weighty=0.1;
		
		this.add(apply,constraints);
	}
	private void setCloseBtnLayout(){
		GridBagConstraints constraints=new GridBagConstraints();
		constraints.gridx=7;
		constraints.gridy=6;
		constraints.gridwidth=1;
		constraints.gridheight=1;
		constraints.anchor=GridBagConstraints.CENTER;
		//constraints.fill=GridBagConstraints.HORIZONTAL;
		constraints.weightx=0.1;
		constraints.weighty=0.1;
		this.add(close,constraints);
	}
	
	private void printStringInBytes(String s){
		for(int i=0;i<s.length();i++){
			System.out.print((int)s.charAt(i));
			System.out.print(" ");
		}
		System.out.println();
		for(int i=0;i<s.length();i++){
			System.out.print(s.charAt(i));
			System.out.print(" ");
		}
		System.out.println();
	}
	
	
	/*
	\033[<N>A		- Move the cursor up N lines
	\033[<N>B		- Move the cursor down N lines
	\033[<N>C		- Move the cursor forward N columns	
	\033[<N>D		- Move the cursor backward N columns
	26				-Sub(Ctrl+Z)	
	 */
	
	public void putOnScreen(String s){
		byte[] b=s.getBytes();
		for(int i=0;i<b.length;i++){
			//状态转移
			switch(screenStat){
			case 0:
				if(b[i]==27){
					screenStat=1;
				}
				else{
					screenStat=6;
				}
				break;
			case 1:
				if(b[i]=='['){
					screenStat=2;
				}
				break;
			case 2:
				if(b[i]=='C'){
					screenStat=3;
					moveCount=1;
				}
				else if (b[i]=='D'){
					screenStat=5;
					moveCount=1;
				}
				else{
					screenStat=4;
					moveCount=0;
				}
				break;
			case 3:
			case 5:
			case 6:
				screenStat=0;
				break;
			case 4:
				if(b[i]=='C'){
					screenStat=3;
				}
				else if (b[i]=='D'){
					screenStat=5;
				}else if(b[i]=='J'){
					screenStat=7;
				}
				else{
					screenStat=4;
				}
				break;
				
			}
			int pos=cmdWindow.getCaretPosition();
			try {
				int len = cmdWindow.getCaretPosition()-cmdWindow.getLineStartOffset(cmdWindow.getLineOfOffset(pos));
				switch (screenStat) {
				case 3:
					//pos=cmdWindow.getCaretPosition();
					moveCount=(moveCount>len)?len:moveCount;
					pos+=moveCount;
					cmdWindow.setCaretPosition(pos);
					screenStat=0;
					break;
				case 5:
					//pos=cmdWindow.getCaretPosition();
					moveCount=(moveCount>len)?len:moveCount;
					pos-=moveCount;
					cmdWindow.setCaretPosition(pos);
					screenStat=0;
					break;
				case 6:
					char c=(char)b[i];
					if((Character.isISOControl((char)b[i]))&&c!='\n'&&c!=' '&&c!='\t'&&c!='\r'){
						break;
					}
					if(pos!=cmdWindow.getText().length())
						cmdWindow.replaceRange(""+(char)b[i], pos, pos+1);
					else {
						cmdWindow.append(""+(char)b[i]);
						
					}
					screenStat=0;
					break;
				case 4:
					moveCount=moveCount*10+b[i]-'0';
					break;
				case 7:
					cmdWindow.setText("");
					screenStat=0;
				default:
					break;
				}
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
/*	@SuppressWarnings("unused")
	private void putOnCmdWindow(String s){
		byte[] b=s.getBytes();
		int i=0;
		while(i<b.length){
			
			int pos=cmdWindow.getCaretPosition();
			if(b[i]==27){//\033  0x1b ESC
				int ct=0;
				if(b[++i]=='['){//91 0x5b
					i++;
					while(b[i]>='0'&&b[i]<='9'){//<N>
						ct=ct*10+b[i]-'0';
						i++;
					}
					if(ct==0) ct=1;//if <N>==1 then <N> is omitted
					//obtain cursor position
					
					
					//int line = cmdWindow.getLineOfOffset(pos);
					//int col=pos-cmdWindow.getLineStartOffset(line);
					try {
						int len=cmdWindow.getCaretPosition()-cmdWindow.getLineStartOffset(cmdWindow.getLineCount()-1);
						System.out.println("len:"+len+" ct:"+ct);
						switch(b[i]){
						case 'A'://up
							break;
						case 'B'://down 
							break;
						case 'C'://forward
							//ct=(ct>len)?len:ct;
							pos+=ct;
							System.out.println("ct:"+ct);
							cmdWindow.setCaretPosition(pos);
							break;
						case 'D'://backward
							//ct=(ct>len)?len:ct;
							pos-=ct;
							System.out.println("ct:"+ct);
							cmdWindow.setCaretPosition(pos);
							break;
						}
						System.out.println("ct:"+ct);
						
					} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					
				}
			}
			else {
				cmdWindow.replaceRange((""+(char)b[i]),pos,pos);
				
			}
			i++;
		}
		
		//cmdWindow.setCaretPosition(cmdWindow.getDocument().getLength());
	}*/
	private void addDataReadyListener(){
		comm.addDataReadyListener(new SerialPortEventListener() {
			
			@Override
			public void serialEvent(SerialPortEvent arg0) {
				// TODO Auto-generated method stub
				if(arg0.getEventType()!=SerialPortEvent.DATA_AVAILABLE){
					return;
				}
				else{
					String s=comm.read();
					
					printStringInBytes(s);
					
					if(s.length()==0)
						return;
					SwingUtilities.invokeLater(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							
							//cmdWindow.append(s);
							putOnScreen(s);
							//printStringInBytes(s);
							//cmdWindow.append(s);
							//cmdWindow.setCaretPosition(cmdWindow.getText().length());
						}
					});
					
					
				}
			}
			
		});
	}
	private void addKeyListenerForCmdWindow(){
		this.cmdWindow.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				char c=e.getKeyChar();
				comm.write(c);
				if(c!='\n')
					strBuf+=c;
				else{
					//dbcon.insert(strBuf);
					if(strBuf.length()!=0){
						record.append(strBuf+"\r\n");
						strBuf="";
					}
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
/*					char c=e.getKeyChar();
					if(inputStat==0){
						comm.write((int)c);
						if(c!='\n')
							strBuf+=c;
						else{
							//dbcon.insert(strBuf);
							if(strBuf.length()!=0){
								cmdHistory.add(strBuf);
								historyIndex=cmdHistory.size()-1;
								record.append(strBuf+"\r\n");
								strBuf="";
							}
							
						}
					}
					else{
						cmdWindow.setCaretPosition(cmdWindow.getText().length());
						comm.write(strBuf);
						
						inputStat=0;
					}*/
				

			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
/*				int code=e.getKeyCode();
				if(code==38||e.getKeyCode()==40){
					inputStat=1;
					if(code==38){
						if(historyIndex<0)
							return;
						strBuf=cmdHistory.get(historyIndex);
						//cmdWindow.append(strBuf);
						historyIndex--;
					}
					else if(code==40){
						if(historyIndex+1==cmdHistory.size()){
							return;
						}
						strBuf=cmdHistory.get(historyIndex+1);
						//cmdWindow.append(strBuf);
						historyIndex++;
						
					}
				}
				else{
					inputStat=0;
				}*/
				int code=e.getKeyCode();
				
				
				if(code==KeyEvent.VK_UP){
					comm.write(27);
					comm.write("[A");
					e.consume();
				}
				else if(code==KeyEvent.VK_DOWN){
					comm.write(27);
					comm.write("[B");
					e.consume();
				}
				else if(code==KeyEvent.VK_LEFT){
					comm.write(27);
					comm.write("[D");
					e.consume();
				}
				else if(code==KeyEvent.VK_RIGHT){
					comm.write(27);
					comm.write("[C");
					e.consume();
				}
				
				
				/*switch (inputStat) {
				case 0:
					if(code==38||code==40){
						inputStat=1;
						prePos=cmdWindow.getText().length();
						//cmdWindow.setEditable(true);
						//cmdWindow.setCaretPosition(cmdWindow.getText().length());
					}
					else{
						inputStat=2;
					}
					break;
				case 1:
					if(c=='\n'){
						//
						int len=cmdWindow.getText().length()-prePos;
						//cmdWindow.setEditable(false);
						try {
							strBuf=cmdWindow.getText(prePos, len);
						} catch (BadLocationException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						comm.write(strBuf+"\n");
						record.append(strBuf+"\r\n");
						cmdHistory.add(strBuf);
						historyIndex=cmdHistory.size()-1;
						inputStat=0;
						cmdWindow.replaceRange("", prePos, cmdWindow.getText().length());
					}
					break;
				case 2:
					break;
				default:
					break;
				}
				
				switch(inputStat){
				case 1:
					if(code==38){
						if(historyIndex<0)
							break;
						cmdWindow.replaceRange(cmdHistory.get(historyIndex), prePos, cmdWindow.getText().length());
						cmdWindow.setCaretPosition(cmdWindow.getText().length());
						historyIndex--;
					}else if(code==40){
						if(historyIndex+1==cmdHistory.size())
							break;
						cmdWindow.replaceRange(cmdHistory.get(historyIndex+1), prePos, cmdWindow.getText().length());
						cmdWindow.setCaretPosition(cmdWindow.getText().length());
						historyIndex++;
					}
					else{
						
					}
					break;
				case 2:
					comm.write((int)c);
					if(c!='\n')
						strBuf+=c;
					else {
						strBuf=strBuf.trim();
						if(strBuf.length()>0){
							cmdHistory.add(strBuf);
							historyIndex=cmdHistory.size()-1;
							record.append(strBuf+"\r\n");
							strBuf="";
						}
					}
					inputStat=0;
					break;
				}*/
				
			}
			
		});
	}
	
	/*@SuppressWarnings("unused")
	private void addKeyListenerForEditWindow(){
		this.editWindow.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				char c=e.getKeyChar();
				if(c=='\n'||c=='?'){
						//databaseTxt.setText(""+cmdWindow.getLineCount());
						//System.out.println("Write: PrePos:"+prePos);
						//String s=editWindow.getText().substring(prePos).trim()+"\n";
						String s=editWindow.getText();
						if(c=='\n')
							s=s+"\r\n";
						editWindow.setText("");
						//System.out.println("Write:"+s);
						//System.out.println(s.length());
						comm.write(s);
						
				}
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}*/
	
	private void addListenerForApplyBtn(){
		apply.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				comm.open(getCommId(), getBaudRate(), getDataBits(), getStopBits(), getParity());
				addDataReadyListener();
				cmdWindow.setEnabled(true);
				cmdWindow.setText("");
				//editWindow.setEnabled(true);
				
				//dbcon=new DBConnection(getDBUrl(), getCommId());
			}
		});
		
	}
	private void addListenerForCloseBtn(){
		close.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				comm.close();
				//editWindow.setEnabled(false);
				cmdWindow.setEnabled(false);
			}
		});
		
		//apply.removeAll();
	}
	
	private String getCommId(){
		return (String)portTxt.getSelectedItem();
	}
	/*@SuppressWarnings("unused")
	private String getDBUrl(){
		return databaseTxt.getText();
	}*/
	private int getBaudRate(){
		return Integer.parseInt((String)baudTxt.getSelectedItem());
	}
	
	private int getDataBits(){
		String s=(String)databitsTxt.getSelectedItem();
		return 	(s=="DATABITS_5")? SerialPort.DATABITS_5:
				(s=="DATABITS_6")? SerialPort.DATABITS_6:
				(s=="DATABITS_7")? SerialPort.DATABITS_7:
									SerialPort.DATABITS_8;
			
	}
	private int getStopBits(){
		String s=(String)stopbitsTxt.getSelectedItem();
		return (s=="STOPBITS_1")? SerialPort.STOPBITS_1:
				(s=="STOPBITS_2")? SerialPort.STOPBITS_2:
					SerialPort.STOPBITS_1_5;

	}
	private int getParity(){
		String s=(String)parityTxt.getSelectedItem();
		return 	(s=="PARITY_NONE")? SerialPort.PARITY_NONE:
				(s=="PARITY_ODD")? SerialPort.PARITY_ODD:
				(s=="PARITY_EVEN")? SerialPort.PARITY_EVEN:
				(s=="PARITY_MARK")? SerialPort.PARITY_MARK:
								SerialPort.PARITY_SPACE;
						
	}

	public static void main(String[] args) {
		// TODO Auto-generated method
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				new TerminalUI();
			}
		});
		
	}

}


