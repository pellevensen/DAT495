package w6_comparators;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Book {
	private final int publishedYear;
	private final String title;
	private final List<String> authorSurnames;
	private final List<String> authorFirstNames;

	public Book(Builder builder) {
		this.title = builder.title;
		this.publishedYear = builder.publishedYear;
		this.authorSurnames = new ArrayList<>(builder.authorSurnames);
		this.authorFirstNames = new ArrayList<>(builder.authorFirstNames);
	}

	public static class Builder {
		private final String title;
		private final int publishedYear;
		private final List<String> authorSurnames;
		private final List<String> authorFirstNames;

		private Builder(String title, int publishedYear) {
			this.title = title;
			this.publishedYear = publishedYear;
			this.authorSurnames = new ArrayList<>();
			this.authorFirstNames = new ArrayList<>();
		}

		public Builder addAuthor(String firstName, String surname) {
			this.authorFirstNames.add(Objects.requireNonNull(firstName));
			this.authorSurnames.add(Objects.requireNonNull(surname));
			return this;
		}

		public Book build() {
			return new Book(this);
		}

	}

	public static Comparator<Book> byYear() {
		return (o1, o2) -> o1.publishedYear - o2.publishedYear;
	}

	public static Comparator<Book> byTitle() {
		return (o1, o2) -> o1.title.compareToIgnoreCase(o2.title);
	}

	public static Builder builder(String title, int publishedYear) {
		return new Builder(title, publishedYear);
	}

	@Override
	public String toString() {
		List<String> authorNames = new ArrayList<>();
		for (int i = 0; i < this.authorSurnames.size(); i++) {
			authorNames.add(this.authorFirstNames.get(i) + " " + this.authorSurnames.get(i));
		}
		return "Book [title=\"" + this.title + "\", publishedYear=" + this.publishedYear + ", authors=["
				+ String.join(", ", authorNames) + "]";
	}

	public static void main(String[] args) {
		List<Book> books = new ArrayList<>();
		books.add(Book.builder("The art of computer programming vol 2 - Seminumerical algorithms, 3rd edition", 1998)
				.addAuthor("Donald Ervin", "Knuth").build());
		books.add(Book.builder("The art of computer programming vol 3 - Sorting and searching, 3rd edition", 1998)
				.addAuthor("Donald Ervin", "Knuth").build());
		books.add(Book.builder("Keys to infinity", 1995).addAuthor("Clifford Alan", "Pickover").build());
		books.add(Book.builder("Winning Ways for Your Mathematical Plays", 1982).addAuthor("Elwyn Ralph", "Berlekamp")
				.addAuthor("John Horton", "Conway").addAuthor("Richard Kenneth", "Guy").build());
		books.add(Book.builder("The Gospel of the Flying Spaghetti Monster", 2006).addAuthor("Bobby", "Henderson")
				.build());
		books.add(Book.builder("How not to be wrong", 2014).addAuthor("Jordan", "Ellenberg").build());
		books.add(Book.builder("Fahrenheit 451", 1953).addAuthor("Ray", "Bradbury").build());
		books.add(Book.builder("Practical cryptography", 2003).addAuthor("Niels", "Ferguson").addAuthor("Bruce", "Schneier").build());
		books.add(Book.builder("Handbook of applied cryptography", 1997).addAuthor("Alfred", "Menezes").
				addAuthor("Paul", "van Oorschot").addAuthor("Scott", "Vanstone").build());

		books.sort(Book.byYear().thenComparing(Book.byTitle()));

		books.forEach(x -> System.out.println(x));
	}
}
