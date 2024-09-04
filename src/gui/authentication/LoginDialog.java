package gui.authentication;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import app.Application;
import debug.Debug;
import gui.MainFrame;
import managers.UserManager;
import net.miginfocom.swing.MigLayout;

public class LoginDialog extends JDialog{
	private static final long serialVersionUID = 1168439930459370895L;

	public LoginDialog() {
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

	private void initDialog() { // Login dialog je sličan onom sa vežbi
		MigLayout layout = new MigLayout("wrap 2" + Debug.debugCons(), "[][]", "[]20[][]20[]");
		this.setLayout(layout);

		JTextField tfUsername = new JTextField(20);
		JPasswordField pfPassword = new JPasswordField(20);
		JButton btnOk = new JButton("Potvrdi");
		JButton btnCancel = new JButton("Otkaži");
		JButton btnRegister = new JButton("Registuj se");

		this.getRootPane().setDefaultButton(btnOk);

		this.add(new JLabel("Dobrodošli u aplikaciju našeg salona. Unesite vaše podatke: "), "span 2");
		this.add(new JLabel("Korisničko ime:"));
		this.add(tfUsername);
		this.add(new JLabel("Lozinka:"));
		this.add(pfPassword);
		this.add(btnOk, "split 3, span, grow 33");
		this.add(btnCancel, "grow 33");
		this.add(btnRegister, "grow 33");
		
		// Login
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String username = tfUsername.getText().trim();
				if (username.equals("")) {
					JOptionPane.showMessageDialog(null, "Niste uneli korisničko ime!", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				String password = new String(pfPassword.getPassword()).trim();
				if (password.equals("")) {
					JOptionPane.showMessageDialog(null, "Niste uneli lozinku!", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				boolean success = UserManager.getInstance().login(username, password);
				if (!success) {
					JOptionPane.showMessageDialog(null, "Korisničko ime ili lozinka nisu validni!", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				LoginDialog.this.setVisible(false);
				LoginDialog.this.dispose();
				MainFrame.createInstance();
			}
		});
		
		// Cancel
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LoginDialog.this.setVisible(false);
				LoginDialog.this.dispose();
			}
		});
		
		// Register
		btnRegister.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LoginDialog.this.setVisible(false);
				LoginDialog.this.dispose();
				new RegisterDialog();
			}
		});
	}

}
