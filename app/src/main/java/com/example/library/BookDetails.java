package com.example.library;

import static com.example.library.MainActivity.IMAGE_URL_BASE;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class BookDetails extends AppCompatActivity {

    public final static String EXTRA_BOOK_DETAILS = "EXTRA_BOOK_DETAILS";
    private TextView bookTitle;
    private TextView bookAuthors;
    private TextView numberOfPages;
    private TextView publishDate;
    private TextView editions;
    private TextView isbn;
    private ImageView bookCover;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);


        bookTitle = findViewById(R.id.book_details_title);
        bookAuthors = findViewById(R.id.book_details_Author);
        numberOfPages = findViewById(R.id.book_details_number_of_pages);
        publishDate = findViewById(R.id.publish_date);
        editions = findViewById(R.id.edition_count);
        isbn = findViewById(R.id.isbn);
        bookCover = findViewById(R.id.book_details_cover);

        Book book = (Book) getIntent().getSerializableExtra(EXTRA_BOOK_DETAILS);

        bookTitle.setText(book.getTitle());
        bookAuthors.setText(getString(R.string.book_authors) + TextUtils.join(", ", book.getAuthors()));
        if(book.getNumberOfPages() != null){
            numberOfPages.setText(getString(R.string.pages_count) + book.getNumberOfPages());
        }else{
            numberOfPages.setText(getString(R.string.pages_count) + getString(R.string.no_info));
        }
        if(book.getPublishDate() != null) {
            publishDate.setText(getString(R.string.publish_date) + TextUtils.join("\r\n", book.getPublishDate()));
        }else{
            publishDate.setText(getString(R.string.publish_date) + getString(R.string.no_info));
        }
            editions.setText(getString(R.string.editions) + book.getEditionCount());
        if(book.getIsbn() != null){
            isbn.setText(getString(R.string.isbn_number) + TextUtils.join("\n\r", book.getIsbn()));
        }
        else{
            isbn.setText(getString(R.string.isbn_number) + getString(R.string.no_info));
        }
        if(book.getCover() != null){
            Picasso.with(getApplicationContext())
                    .load(IMAGE_URL_BASE + book.getCover() + "-L.jpg")
                    .placeholder(R.drawable.ic_baseline_book_24).into(bookCover);
        } else {
            bookCover.setImageResource(R.drawable.ic_baseline_book_24);

        }
    }
}