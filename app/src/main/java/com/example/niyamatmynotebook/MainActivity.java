package com.example.niyamatmynotebook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int EDIT_NOTE_REQUEST_CODE = 101;

    private static final String PREFS_NAME = "NotesPrefs";
    private static final String KEY_NOTE_COUNT = "NotesCount";


    private LinearLayout notesContainer;
    private List<Notes> notesList;
    private Notes note;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notesContainer = findViewById(R.id.notesContainer);
        Button saveButton = findViewById(R.id.saveButton);

        ToggleButton toggleButton = findViewById(R.id.toggleButton);
        notesList = new ArrayList<>();
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            toggleButton.setChecked(true);
        }
        toggleButton.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    // Dark mode
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    // Light mode
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                recreate(); // Apply the new theme
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote();
            }

        });

        loadNotesFromPreferences();
        displayNotes();
        Toast.makeText(this, "Welcome to Niyamat My Note", Toast.LENGTH_SHORT).show();
    }


    private void displayNotes() {
        for (Notes notes: notesList){
            createNoteView(notes);
        }

    }

    private void loadNotesFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int noteCount = sharedPreferences.getInt(KEY_NOTE_COUNT, 0);

        for (int i = 0; i < noteCount; i++){
            String title = sharedPreferences.getString("note_title_" + i, "");
            String content = sharedPreferences.getString("note_content_" + i, "");

            Notes notes = new Notes();
            notes.setTitle(title);
            notes.setContent(content);

            notesList.add(notes);
        }
    }

    private void saveNote() {
        EditText titleEditText = findViewById(R.id.titleEditText);
        EditText contentEditText = findViewById(R.id.contentEditText);

        String title = titleEditText.getText().toString();
        String content = contentEditText.getText().toString();


        if (!title.isEmpty() && !content.isEmpty()){
            Notes notes = new Notes();
            notes.setTitle(title);
            notes.setContent(content);

            notesList.add(notes);

            saveNotesToPreferences();

            createNoteView(notes);
            clearInputFields();

        }
        Toast.makeText(this, "Note Added SuccessFull", Toast.LENGTH_SHORT).show();
    }

    private void clearInputFields() {
        EditText titleEditText = findViewById(R.id.titleEditText);
        EditText contentEditText = findViewById(R.id.contentEditText);

        titleEditText.getText().clear();
        contentEditText.getText().clear();
    }

    private void createNoteView(final Notes note) {
        View noteview = getLayoutInflater().inflate(R.layout.note_item, null);
        TextView titleTextView = noteview.findViewById(R.id.titleTextView);
        TextView contentTextView = noteview.findViewById(R.id.contentTextView);

        titleTextView.setText(note.getTitle());
        contentTextView.setText(note.getContent());
        noteview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editNote(note);
                Intent i=new Intent(MainActivity.this, NoteDetailsActivity.class);
                i.putExtra("title", note.getTitle());
                i.putExtra("content", note.getContent());
                startActivity(i);
            }
        });

        noteview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showDeleteDialog(note);

                return true;
            }
        });
        notesContainer.addView(noteview);
    }

    private void showDeleteDialog(final Notes note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure you want to delete this note?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteNoteAndRefresh(note);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void deleteNoteAndRefresh(Notes note) {
        notesList.remove(note);
        saveNotesToPreferences();
        refreshNoteViews();
        Toast.makeText(this, "Note Deleted SuccessFull", Toast.LENGTH_SHORT).show();
    }

    private void refreshNoteViews() {
        notesContainer.removeAllViews();
        displayNotes();
    }
    private void editNote(final Notes note){
        Intent intent = new Intent(MainActivity.this, NoteDetailsActivity.class);
        intent.putExtra("title", note.getTitle());
        intent.putExtra("content", note.getContent());
        intent.putExtra("position", position);
        startActivityForResult(intent, EDIT_NOTE_REQUEST_CODE);
    }
@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == EDIT_NOTE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
        String updatedTitle = data.getStringExtra("updated_title");
        String updatedContent = data.getStringExtra("updated_content");

        // Update the note in the list
        int position = data.getIntExtra("position", -1);
        if (position != -1) {
            Notes note = notesList.get(position);
            note.setTitle(updatedTitle);
            note.setContent(updatedContent);
            // Update note view
            updateNoteView(position, note);
            // Save updated notes
            saveNotesToPreferences();
        }
        Toast.makeText(this, "Note Edit SuccessFull", Toast.LENGTH_SHORT).show();
    }
}
    private void updateNoteView(int position, Notes note) {
        View noteView = notesContainer.getChildAt(position);
        if (noteView != null) {
            TextView titleTextView = noteView.findViewById(R.id.titleTextView);
            TextView contentTextView = noteView.findViewById(R.id.contentTextView);
            titleTextView.setText(note.getTitle());
            contentTextView.setText(note.getContent());
        }
    }

    private void saveNotesToPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_NOTE_COUNT, notesList.size());

        for (int i = 0; i < notesList.size(); i++){
            Notes notes = notesList.get(i);
            editor.putString("note_title_" + i, notes.getTitle());
            editor.putString("note_content_" + i, notes.getContent());
        }
        editor.apply();
    }



    @Override
    public void onBackPressed() {
        if (canGoBack())
            goBack();
        else
        {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.create();
            dialog.setTitle("Confirm to Exits");
            dialog.setMessage("Are You Confirm To Exits Niyamat My Personal Note?");
            dialog.setCancelable(false);
            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            dialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialog.setCancelable(true);
                }
            });
            dialog.show();
        }
    }

    private void goBack() {
        finish();
    }

    private static boolean canGoBack() {
        return false;
    }

    private void editNote() {
            editNote(null, 0);
        }

    private void editNote(@NonNull final Notes note, final int position) {
            this.note = note;
            this.position = position;
            Intent intent = new Intent(MainActivity.this, NoteDetailsActivity.class);
            intent.putExtra("title", note.getTitle());
            intent.putExtra("content", note.getContent());
            intent.putExtra("position", position);
            startActivityForResult(intent, EDIT_NOTE_REQUEST_CODE);
        }
    }
