package com.library.dao;

import com.library.model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    private final Connection connection;

    public BookDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Book> getAllBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM books";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("available_copies")
                ));
            }
        }
        return books;
    }

    public boolean addBook(Book book) throws SQLException {
        String query = "INSERT INTO books (title, author, available_copies) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setInt(3, book.getAvailableCopies());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean updateBookCopies(int bookId, int newCopies) throws SQLException {
        String query = "UPDATE books SET available_copies = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, newCopies);
            pstmt.setInt(2, bookId);
            return pstmt.executeUpdate() > 0;
        }
    }
}
