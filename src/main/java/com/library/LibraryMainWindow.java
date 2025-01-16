package com.library;

import com.library.dao.BookDAO;
import com.library.model.Book;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class LibraryMainWindow extends JFrame {
    private JTable booksTable;
    private JButton addBookButton;
    private BookDAO bookDAO;

    public LibraryMainWindow() {
        setTitle("Library Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create table and buttons
        booksTable = new JTable();
        addBookButton = new JButton("Add Book");

        // Add components to frame
        add(new JScrollPane(booksTable), BorderLayout.CENTER);
        add(addBookButton, BorderLayout.SOUTH);

        // Load books into table
        loadBooks();
    }

    private void loadBooks() {
        try (Connection connection = com.library.dao.DatabaseConnection.getConnection()) {
            bookDAO = new BookDAO(connection);
            List<Book> books = bookDAO.getAllBooks();
            String[] columnNames = {"ID", "Title", "Author", "Available Copies"};
            String[][] data = new String[books.size()][4];
            for (int i = 0; i < books.size(); i++) {
                Book book = books.get(i);
                data[i][0] = String.valueOf(book.getId());
                data[i][1] = book.getTitle();
                data[i][2] = book.getAuthor();
                data[i][3] = String.valueOf(book.getAvailableCopies());
            }
            booksTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading books: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LibraryMainWindow mainWindow = new LibraryMainWindow();
            mainWindow.setVisible(true);
        });
    }
}
