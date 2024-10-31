import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.HashMap;
import java.util.List;

public class Main extends JFrame {
    private JTextField txtTitle;
    private JTextField txtAuthor;
    private JTextField txtIsbn;
    private JTextPane txtResults;
    private JButton SAVEButton;
    private JPanel MainPanel;
    private JButton DELETEButton;
    public String title, author, isbn, delText;

    public static void main(String[] args) {
        new Main();
    }

    public Main(){
        setTitle("My Book List");
        setContentPane(MainPanel);
        setMinimumSize(new Dimension(700, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        SAVEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                title = txtTitle.getText();
                author = txtAuthor.getText();
                isbn = txtIsbn.getText();
                saveInfo();
            }
        });
        DELETEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                delText = JOptionPane.showInputDialog("Enter title or ISBN");
                DeleteInfo(delText);

            }
        });
    }

    private void DeleteInfo(String delText) {
        String url = "jdbc:mysql://localhost:3306/booklist";
        String user = "root";
        String pass = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pass);
            String sql = "SELECT * FROM books WHERE title = '"+delText+"' OR isbn = '"+delText+"' ";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                String rsTitle = rs.getString("title");
                String rsISBN = rs.getString("isbn");
                if (rsTitle.equals(delText) || rsISBN.equals(delText)) {
                    String query = "DELETE FROM books WHERE title = '" + delText + "' OR isbn = '" + delText + "'";
                    PreparedStatement pstmt = con.prepareStatement(query);
                    pstmt.executeUpdate();
                    JOptionPane.showInputDialog(this, "Entry Deleted", "Success",JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showInputDialog(this, "Results not found", "error", JOptionPane.ERROR_MESSAGE);
                }
            }
            rs.close();
        } catch (Exception e ){
            System.out.println(e);
        }
    }

    private void saveInfo() {
        txtResults.setText(txtResults.getText()+"\n");
        txtResults.setText(txtResults.getText()+"Title: "+title+"\n");
        txtResults.setText(txtResults.getText()+"Author: "+author+"\n");
        txtResults.setText(txtResults.getText()+"ISBN: "+isbn+"\n");
        txtResults.setText(txtResults.getText()+"\n");
        String url = "jdbc:mysql://localhost:3306/booklist";
        String user = "root";
        String pass = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,pass);
            String query = "INSERT INTO books (title,author,isbn) VALUES ('"+title+"','"+author+"','"+isbn+"')";
            PreparedStatement pstmt = con.prepareStatement(query);
            if (title.equals(" ") || author.equals(" ") || isbn.equals(" ")){
                JOptionPane.showMessageDialog(this,"Add missing info", "ERROR :(",JOptionPane.ERROR_MESSAGE);
            } else {
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this,"Book Added to your List", "SUCCESS :)",JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e ){
            System.out.println(e);
        }
    }
}
