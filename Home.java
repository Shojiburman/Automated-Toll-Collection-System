import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.imageio.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

class Home extends JFrame implements ActionListener, WindowListener {
	public Login login;
	public Signup signup;
	public EditProfile editProfile;
	public Admin admin;
	public AddVehicle vehicle;
	public Payment payment;
	public TollList tollList;
	public UsersList users;
	public Profile profile;
	public AddToll addToll;
	public UpdateToll updateToll;
	public TransactionList report;
	public UserTransaction userReport;
	public VehicleList vehicleList;
	public String salutation;

	public Home() {
		super("Home page");
		
		setLayout(new FlowLayout());
        ImageIcon logo = new ImageIcon("logo.png");
        this.setIconImage(logo.getImage());
		
		login = new Login(this);
		signup = new Signup(this);
		profile = new Profile(this);
		editProfile = new EditProfile(this);
		vehicle = new AddVehicle(this);
		payment = new Payment(this);
		tollList = new TollList(this);
		users = new UsersList(this);
		admin = new Admin(this);
		addToll = new AddToll(this);
		updateToll = new UpdateToll(this);
		report = new TransactionList(this);
		userReport = new UserTransaction(this);
		vehicleList = new VehicleList(this);

		
		Label l = new Label("Toll Collection Sytem");
		Label l2 = new Label("JOIN NOW");
		Label l3 = new Label("OR");
		Button b = new Button("Log in");
		Button signupButton = new Button("Sign up");
		
		b.setBackground(Color.DARK_GRAY);
		signupButton.setBackground(Color.DARK_GRAY);
		b.setForeground(Color.WHITE);
		signupButton.setForeground(Color.WHITE);

		add(l);add(l2);
		add(signupButton);add(l3);
		add(b);
		
		l.setBounds(0,0,480,100);
		l2.setBounds(50,120,300,40);
		signupButton.setBounds(50,160,380,40);
		l3.setBounds(0,200,480,40);
		b.setBounds(50,240,380,40);
		
		Font myFont = new Font("Serif",Font.BOLD,28);
		l.setFont(myFont);
		l.setAlignment(Label.CENTER);
		l3.setAlignment(Label.CENTER);
		Font myFont2 = new Font("Serif",Font.BOLD,14);
		l2.setFont(myFont2);
		l3.setFont(myFont2);
		Font myFont3 = new Font("Serif",Font.BOLD,12);
		b.setFont(myFont3);

		b.addActionListener(this);
		signupButton.addActionListener(this);
		
		addWindowListener(this);
		setLayout(null);
		setSize(500,500);
		setLocation(400,100);
	}

	// listens for button clicks
	public void actionPerformed(ActionEvent e) {
		String clickedButton = e.getActionCommand();
		if(clickedButton.equals("Log in")){
			first.addMovie.setVisible(true);
			this.setVisible(false);
			this.setVisible(false);
			login.setVisible(true);			
			login.setParent(this);
		}
		else if(clickedButton.equals("Sign up")) {
			this.setVisible(false);
			signup.setVisible(true);			
			signup.setParent(this);
		}
	}

	public void setUsername(String s) {
		//this.salutation = s; // maintaining pseudo session by storing username from login/signup
		this.salutation = Objects.requireNonNull(s);
	}

	public String getUsername() {
		return this.salutation; // serving the username 
	}

	public void windowActivated(WindowEvent e) {}

	public void windowClosed(WindowEvent e) {}

	public void windowClosing(WindowEvent e){
		System.exit(0);
	}
	public void windowDeactivated(WindowEvent e) {}

	public void windowDeiconified(WindowEvent e) {}

	public void windowIconified(WindowEvent e) {}

	public void windowOpened(WindowEvent e) {}
}