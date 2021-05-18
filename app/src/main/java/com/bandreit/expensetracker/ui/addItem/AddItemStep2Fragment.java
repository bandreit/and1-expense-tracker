package com.bandreit.expensetracker.ui.addItem;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bandreit.expensetracker.MainActivity;
import com.bandreit.expensetracker.R;
import com.bandreit.expensetracker.model.transactions.TransactionAmount;
import com.bandreit.expensetracker.model.transactions.TransactionType;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class AddItemStep2Fragment extends Fragment implements Validator.ValidationListener {

    private AddItemViewModel addItemViewModel;
    private TransactionType transactionType;
    private TransactionAmount transactionAmount;
    private String expenseTitle;
    private ImageView expenseTypeButton;
    private ImageView incomeTypeButton;
    @NotEmpty
    private EditText editTextAmount;
    @NotEmpty
    private EditText editTextName;
    private MaterialButton addExpense;
    @Pattern(regex = "(\\d+\\/?\\d?+)")
    private DatePickerDialog picker;
    @NotEmpty
    private EditText eText;
    private Validator validator;
    private Button addImageButton;
    private int selectedDay;
    private int selectedMonth;
    private int selectedYear;
    private Uri filePath;
    private Uri downloadUri;
    private final int PICK_IMAGE_REQUEST = 71;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addItemViewModel = AddItemViewModel.getInstance();
        validator = new Validator(this);
        validator.setValidationListener(this);

        View root = inflater.inflate(R.layout.fragment_add_item2, container, false);

        TextView textView = ((MainActivity) getActivity()).findViewById(R.id.fragment_title);
        textView.setText(R.string.enter_details);

        addItemViewModel.getCurrentExpenseItem().observe(getViewLifecycleOwner(), item -> {
//            debug.setText("category: " +  item.getCategory().getName());
        });

        editTextAmount = root.findViewById(R.id.editTextNumberDecimal);
        editTextName = root.findViewById(R.id.expense_item_name);

        expenseTypeButton = root.findViewById(R.id.expenseTypeExpense);
        incomeTypeButton = root.findViewById(R.id.expenseTypeIncome);
        addImageButton = root.findViewById(R.id.upload_photo);

        addExpense = root.findViewById(R.id.add_expense);

        int expenseButtonHeight = expenseTypeButton.getLayoutParams().height;
        int expenseButtonWidth = expenseTypeButton.getLayoutParams().width;
        int incomeButtonHeight = incomeTypeButton.getLayoutParams().height;
        int incomeButtonWidth = incomeTypeButton.getLayoutParams().width;

        transactionType = TransactionType.EXPENSE;

        expenseTypeButton.setOnClickListener(v -> {
            transactionType = TransactionType.EXPENSE;
            expenseTypeButton.getLayoutParams().width = (int) (expenseButtonWidth * 1.3);
            expenseTypeButton.getLayoutParams().height = (int) (expenseButtonHeight * 1.3);

            incomeTypeButton.getLayoutParams().width = (int) (incomeButtonWidth * 0.7);
            incomeTypeButton.getLayoutParams().height = (int) (incomeButtonHeight * 0.7);
            expenseTypeButton.requestLayout();
        });

        incomeTypeButton.setOnClickListener(v -> {
            transactionType = TransactionType.INCOME;
            incomeTypeButton.getLayoutParams().width = (int) (expenseButtonWidth * 1.3);
            incomeTypeButton.getLayoutParams().height = (int) (expenseButtonHeight * 1.3);

            expenseTypeButton.getLayoutParams().width = (int) (incomeButtonWidth * 0.7);
            expenseTypeButton.getLayoutParams().height = (int) (incomeButtonHeight * 0.7);
            expenseTypeButton.requestLayout();
        });

        eText = (EditText) root.findViewById(R.id.date_selector);
        eText.setInputType(InputType.TYPE_NULL);
        eText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                selectedDay = dayOfMonth;
                                selectedMonth = monthOfYear;
                                selectedYear = year;
                                eText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        addExpense.setOnClickListener(v -> {
            validator.validate();
        });

        return root;
    }

    private void uploadImage() {
        StorageReference storageReference = addItemViewModel.getStorageReference();
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
            UploadTask uploadTask = ref.putFile(filePath);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        downloadUri = task.getResult();
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void onValidationSucceeded() {
        Toast.makeText(getContext(), "Expense registered!", Toast.LENGTH_SHORT).show();
        transactionAmount = new TransactionAmount("DKK", Double.parseDouble(String.valueOf(editTextAmount.getText())));
        expenseTitle = String.valueOf(editTextName.getText());
        addItemViewModel.selectExpenseType(transactionType);
        addItemViewModel.selectAmount(transactionAmount);
        addItemViewModel.selectTitle(expenseTitle);
        addItemViewModel.selectDate(selectedDay, selectedMonth, selectedYear);
        addItemViewModel.setDownloadUri(downloadUri);
        addItemViewModel.addItem();
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.navigation_home);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getContext());
            // Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            uploadImage();
        }
    }
}
