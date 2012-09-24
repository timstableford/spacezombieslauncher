package cx.it.hyperbadger.spacezombies.launcher;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;

public class AuthGUI extends JFrame  {
	private JFrame f;
	private JPanel p; 
	private JButton launch, options;
	private JTextField name, pass;

public AuthGUI(){
f= new JFrame();
p= new JPanel();
name = new JTextField();
pass = new JTextField();
launch = new JButton();
options = new JButton();

f.add(p);
name.setSize(5,5); //check for best size later - this one just for quick example
pass.setSize(5,5);
p.add(name, pass);

launch.setText("Launch");
launch.setSize(5,5); //again, check for size later.
options.setText("Options");
options.setSize(5,5);
p.add( launch, options);
}


//get the name from textfield in String form
public String getName(){

String SName = new String();
SName = name.getText();
return SName;
}

//get the password from textfield in String form
public String getPass(){
	String SPass= new String();
	SPass = pass.getText();
	return SPass;
}

//supposed to launch a seperate window with options screen
public void optionsWindow(){
	
}

//button to initialize login sequence - take away the name and pass in string, send over to auth for validation, launch game if all is valid
public void logIn(){
	
}

/**
 * Instructions
 * MAKE A GODDAMN LISTENER.
 * 1.login field ~
 * 2.password field ~
 * 3.options button
 * 4.launch button
 * 	once 'launched' is pressed=true, take String from Login and String from Password + find exact match in database( pass to Auth class)
 *  options button needs to open a new window with options
 * 
 */
}
