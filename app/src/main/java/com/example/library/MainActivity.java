package com.example.library;

import static com.example.library.BookDetails.EXTRA_BOOK_DETAILS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

        public static final String IMAGE_URL_BASE = "http://covers.openlibrary.org/b/id/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final BookAdapter adapter = new BookAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.book_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                fetchBooksData(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        MenuItem clearItem = menu.findItem(R.id.menu_item_clear);
        clearItem.setOnMenuItemClickListener(v -> {
           searchView.setQuery("", false);
            setupBookListView(new ArrayList<Book>());
            return true;
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void fetchBooksData(String query){
        String finalQuery = prepareQuery(query);
        BookService bookService = RetrofitInstance.getRetrofitInstance().create(BookService.class);

        Call<BookContainer> booksApiCall = bookService.findBooks(finalQuery);

        booksApiCall.enqueue(new Callback<BookContainer>() {
            @Override
            public void onResponse(Call<BookContainer> call, Response<BookContainer> response) {
                if(response.body()!= null){
                    setupBookListView(response.body().getBookList());
                }
            }

            @Override
            public void onFailure(Call<BookContainer> call, Throwable t) {
                Snackbar.make(findViewById(R.id.main_view), R.string.fetch_failure, BaseTransientBottomBar.LENGTH_LONG).show();
            }
        });
    }

    private String prepareQuery(String query){
        String[] queryParts = query.split("\\s+");
        return TextUtils.join("+", queryParts);
    }

    private void setupBookListView(List<Book> books){
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final BookAdapter adapter = new BookAdapter();
        adapter.setBooks(books);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private boolean checkNullOrEmpty(String title) {
        return title != null && !TextUtils.isEmpty(title);
    }


    private class BookHolder extends RecyclerView.ViewHolder{
        private TextView bookTitle;
        private TextView authors;
        private TextView numberOfPages;
        private ImageView bookCover;


        public BookHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.book_list_item, parent, false));

            bookTitle = itemView.findViewById(R.id.book_title);
            bookCover = itemView.findViewById(R.id.img_cover);
            authors = itemView.findViewById(R.id.book_author);
            numberOfPages = itemView.findViewById(R.id.number_of_pages);

        }

        public void bind(Book book){

            if(book != null && checkNullOrEmpty(book.getTitle()) && book.getAuthors() != null) {
                bookTitle.setText(book.getTitle());
                authors.setText(TextUtils.join(", ", book.getAuthors()));
                numberOfPages.setText(book.getNumberOfPages());
                if (book.getCover() != null) {
                    Picasso.with(itemView.getContext()).load(IMAGE_URL_BASE + book.getCover() + "-S.jpg").placeholder(R.drawable.ic_baseline_book_24).into(bookCover);
                }
                else{
                    bookCover.setImageResource(R.drawable.ic_baseline_book_24);
                }

                View bookView = itemView.findViewById(R.id.single_book);
                bookView.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, BookDetails.class);
                    intent.putExtra(EXTRA_BOOK_DETAILS, book);
                    startActivity(intent);
                });


            }
        }
    }

    private class BookAdapter extends RecyclerView.Adapter<BookHolder>{
        private List<Book> books;
        @NonNull
        @Override
        public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new BookHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull BookHolder holder, int position) {
            if(books != null){
                Book book = books.get(position);
                holder.bind(book);
            }
            else{
                Log.d("MainActivity", "No books");
            }


        }

        @Override
        public int getItemCount() {
            if(books != null){
                return books.size();
            }
            else{
                return 0;
            }
        }

        void setBooks(List<Book> books){
            this.books = books;
            notifyDataSetChanged();
        }
    }
}