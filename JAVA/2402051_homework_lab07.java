import java.util.*;

class LibraryBook {
    private int uniqueId;
    private String name;
    private String authorName;
    private boolean inStock;

    public LibraryBook(int uniqueId, String name, String authorName) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.authorName = authorName;
        this.inStock = true;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public String getName() {
        return name;
    }

    public String getAuthorName() {
        return authorName;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void lendBook() {
        if (inStock) {
            inStock = false;
        } else {
            System.out.println("The book is already checked out.");
        }
    }

    public void receiveBook() {
        inStock = true;
    }

    public void printDetails() {
        System.out.println("Book ID: " + uniqueId + ", Name: " + name + ", Author: " + authorName + ", In Stock: " + (inStock ? "Yes" : "No"));
    }
}

class LibraryMember {
    private int userId;
    private String userName;
    private List<Integer> borrowedBookList;

    public LibraryMember(int userId, String userName) {
        this.userId = userId;
        this.userName = userName;
        this.borrowedBookList = new ArrayList<>();
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public void addBorrowedBook(int bookId) {
        borrowedBookList.add(bookId);
    }

    public void removeBorrowedBook(int bookId) {
        borrowedBookList.remove((Integer) bookId);
    }

    public List<Integer> getBorrowedBooks() {
        return borrowedBookList;
    }
}

class LibraryManager {
    private int managerId;
    private String managerName;
    private List<LibraryBook> availableBooks;

    public LibraryManager(int managerId, String managerName) {
        this.managerId = managerId;
        this.managerName = managerName;
        this.availableBooks = new ArrayList<>();
    }

    public void addNewBook(LibraryBook book) {
        availableBooks.add(book);
    }

    public void displayAllBooks() {
        for (LibraryBook book : availableBooks) {
            book.printDetails();
        }
    }
}

abstract class LibraryTransaction {
    protected int transactionId;
    protected Date transactionDate;
    protected LibraryBook book;
    protected LibraryMember member;

    public LibraryTransaction(int transactionId, Date transactionDate, LibraryBook book, LibraryMember member) {
        this.transactionId = transactionId;
        this.transactionDate = transactionDate;
        this.book = book;
        this.member = member;
    }

    public abstract void completeTransaction();
}

class CheckoutTransaction extends LibraryTransaction {
    public CheckoutTransaction(int transactionId, Date transactionDate, LibraryBook book, LibraryMember member) {
        super(transactionId, transactionDate, book, member);
    }

    @Override
    public void completeTransaction() {
        if (book.isInStock()) {
            book.lendBook();
            member.addBorrowedBook(book.getUniqueId());
            System.out.println(member.getUserName() + " borrowed " + book.getName());
        } else {
            System.out.println("The book is currently unavailable.");
        }
    }
}

class ReturnTransaction extends LibraryTransaction {
    public ReturnTransaction(int transactionId, Date transactionDate, LibraryBook book, LibraryMember member) {
        super(transactionId, transactionDate, book, member);
    }

    @Override
    public void completeTransaction() {
        book.receiveBook();
        member.removeBorrowedBook(book.getUniqueId());
        System.out.println(member.getUserName() + " returned " + book.getName());
    }
}

public class LibraryMain {
    public static void main(String[] args) {
        LibraryManager manager = new LibraryManager(101, "Sophia");
        LibraryMember member = new LibraryMember(1001, "Charlie");
        LibraryBook bookA = new LibraryBook(301, "The Alchemist", "Paulo Coelho");
        LibraryBook bookB = new LibraryBook(302, "Brave New World", "Aldous Huxley");

        manager.addNewBook(bookA);
        manager.addNewBook(bookB);

        LibraryTransaction checkout = new CheckoutTransaction(201, new Date(), bookA, member);
        checkout.completeTransaction();

        LibraryTransaction returnBook = new ReturnTransaction(202, new Date(), bookA, member);
        returnBook.completeTransaction();
    }
}
