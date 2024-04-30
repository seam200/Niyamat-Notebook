package com.example.niyamatmynotebook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class NoteDetailsActivity extends AppCompatActivity {

    private EditText titleEditText;
    private EditText contentEditText;
    private int position;
//    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        titleEditText = findViewById(R.id.titleEditText);
        contentEditText = findViewById(R.id.contentEditText);
        Button saveButton = findViewById(R.id.saveButton);

        // Get the passed data
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        position = intent.getIntExtra("position", -1);

        // Set data to EditText fields
        titleEditText.setText(title);
        contentEditText.setText(content);

        // Set click listener for save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNoteChanges();
            }
        });
//        saveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Get updated text from EditText fields
//                String updatedTitle = titleEditText.getText().toString();
//                String updatedContent = contentEditText.getText().toString();
//
//                // Pass the updated data back to MainActivity
//                Intent resultIntent = new Intent();
//                resultIntent.putExtra("updated_title", updatedTitle);
//                resultIntent.putExtra("updated_content", updatedContent);
//                setResult(RESULT_OK, resultIntent);
//                finish();
//            }
        }

    private void saveNoteChanges() {
        String updatedTitle = titleEditText.getText().toString();
        String updatedContent = contentEditText.getText().toString();

        if (!updatedTitle.isEmpty() && !updatedContent.isEmpty()) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("updated_title", updatedTitle);
            resultIntent.putExtra("updated_content", updatedContent);
            resultIntent.putExtra("position", position);

            setResult(RESULT_OK, resultIntent);
            finish();
        } else {
            Toast.makeText(this, "Title and Content cannot be empty", Toast.LENGTH_SHORT).show();
       }
    }
}
