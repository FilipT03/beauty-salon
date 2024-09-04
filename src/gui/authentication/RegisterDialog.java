package gui.authentication;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import app.Application;
import debug.Debug;
import gui.MainFrame;
import managers.ClientManager;
import net.miginfocom.swing.MigLayout;
import users.Client;
import users.Gender;

public class RegisterDialog extends JDialog{
	private static final long serialVersionUID = -6244714954398336928L;

	public RegisterDialog() {
		this.setTitle("Prijava");
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		initDialog();
		this.pack();
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				Application.saveAllData();
			}
		});
		
		this.setVisible(true);
	}

	private void initDialog() {
		MigLayout layout = new MigLayout("wrap 2" + Debug.debugCons(), "[][]", "[]20[][][][][][][]20[]");
		this.setLayout(layout);

		JTextField tfName = new JTextField(20);
		JTextField tfSurname = new JTextField(20);
		JTextField tfPhone = new JTextField(20);
		JTextField tfAdress = new JTextField(20);
		String[] genders = {"Muški", "Ženski"};
		JComboBox<String> cbGender = new JComboBox<String>(genders);
		JTextField tfUsername = new JTextField(20);
		JPasswordField pfPassword = new JPasswordField(20);
		JButton btnOk = new JButton("Potvrdi");
		JButton btnCancel = new JButton("Otkaži");
		JButton btnLogin = new JButton("Prijavi se");

		this.getRootPane().setDefaultButton(btnOk);

		this.add(new JLabel("Unesite vaše podatke da bi ste se registrovali: "), "span 2");
		this.add(new JLabel("Ime:"));
		this.add(tfName);
		this.add(new JLabel("Prezime:"));
		this.add(tfSurname);
		this.add(new JLabel("Adresa:"));
		this.add(tfAdress);
		this.add(new JLabel("Telefon:"));
		this.add(tfPhone);
		this.add(new JLabel("Pol:"));
		this.add(cbGender);
		this.add(new JLabel("Korisničko ime:"));
		this.add(tfUsername);
		this.add(new JLabel("Lozinka:"));
		this.add(pfPassword);
		this.add(btnOk, "split 3, span, grow 33");
		this.add(btnCancel, "grow 33");
		this.add(btnLogin, "grow 33");
		
		// Login
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = tfName.getText().trim();
				String surname = tfSurname.getText().trim();
				String adress = tfAdress.getText().trim();
				String phone = tfPhone.getText().trim();
				Gender gender = cbGender.getSelectedItem().equals("Muški") ? Gender.male : Gender.female;
				String username = tfUsername.getText().trim();
				String password = new String(pfPassword.getPassword()).trim();
				
				String error = ClientManager.getInstance().register(new Client(name, surname, phone, adress, username, password, gender, false), true);
				if (error != null) {
					JOptionPane.showMessageDialog(null, error, "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				RegisterDialog.this.setVisible(false);
				RegisterDialog.this.dispose();
				MainFrame.createInstance();
			}
		});
		
		// Cancel
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RegisterDialog.this.setVisible(false);
				RegisterDialog.this.dispose();
			}
		});
		
		// Register
		btnLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RegisterDialog.this.setVisible(false);
				RegisterDialog.this.dispose();
				new LoginDialog();
			}
		});
	}

}
