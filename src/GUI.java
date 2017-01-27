import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;

import java.awt.Font;

public class GUI {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setFont(new Font("Simplified Arabic Fixed", Font.ITALIC, 16));
		frame.setBounds(100, 100, 538, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JButton btnNewButton = new JButton("Compress");
		btnNewButton.setBounds(145, 170, 105, 34);
		btnNewButton.setFont(new Font("Comic Sans MS", Font.ITALIC, 16));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int s = Integer.parseInt(textField.getText());
				String path = textField_1.getText();
				int h = Integer.parseInt(textField_2.getText());
				int w = Integer.parseInt(textField_3.getText());
				Compress c = new Compress(w, h, s, path);
				c.compress();
				c.Final();
				try {
					c.files();
				} catch (IOException e1) {
				}
				JOptionPane.showMessageDialog(null, "The Text has been Compressed");
			}
		});

		textField = new JTextField();
		textField.setBounds(264, 42, 86, 20);
		textField.setColumns(10);

		JButton btnNewButton_1 = new JButton("Decompress");
		btnNewButton_1.setBounds(289, 171, 123, 33);
		btnNewButton_1.setFont(new Font("Comic Sans MS", Font.ITALIC, 16));
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Decompress d = new Decompress();
				try {
					d.decompress("");
				} catch (IOException e1) {
				}
				JOptionPane.showMessageDialog(null, "The Text has been DeCompressed");
			}
		});

		JLabel lblEnterTheText = new JLabel("Enter the number of vectors:");
		lblEnterTheText.setBounds(10, 44, 140, 17);

		JButton btnExit = new JButton("Exit");
		btnExit.setBounds(22, 216, 51, 23);
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		textField_1 = new JTextField();
		textField_1.setBounds(264, 132, 248, 20);
		textField_1.setColumns(10);

		JLabel lblEnterPathOf = new JLabel("Enter Path of Image:");
		lblEnterPathOf.setBounds(10, 135, 152, 14);

		textField_2 = new JTextField();
		textField_2.setBounds(264, 73, 86, 20);
		textField_2.setColumns(10);

		JLabel lblEnterVectorWidth = new JLabel("Enter vector height:");
		lblEnterVectorWidth.setBounds(10, 103, 121, 17);

		JLabel lblEnterVectorWidth_1 = new JLabel("Enter vector width:");
		lblEnterVectorWidth_1.setBounds(10, 75, 121, 17);

		textField_3 = new JTextField();
		textField_3.setBounds(264, 101, 86, 20);
		textField_3.setColumns(10);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(btnExit);
		frame.getContentPane().add(lblEnterVectorWidth_1);
		frame.getContentPane().add(lblEnterPathOf);
		frame.getContentPane().add(lblEnterTheText);
		frame.getContentPane().add(lblEnterVectorWidth);
		frame.getContentPane().add(textField_1);
		frame.getContentPane().add(textField_3);
		frame.getContentPane().add(textField);
		frame.getContentPane().add(textField_2);
		frame.getContentPane().add(btnNewButton);
		frame.getContentPane().add(btnNewButton_1);
	}
}
