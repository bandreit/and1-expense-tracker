package com.bandreit.expensetracker.ui.edit.editItem;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.util.CurrencyAmount;
import android.net.Uri;
import android.os.Bundle;
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
import androidx.preference.PreferenceManager;

import com.bandreit.expensetracker.MainActivity;
import com.bandreit.expensetracker.R;
import com.bandreit.expensetracker.model.transactions.TransactionAmount;
import com.bandreit.expensetracker.model.transactions.TransactionItem;
import com.bandreit.expensetracker.model.transactions.TransactionType;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class EditItemStep2Fragment extends Fragment implements Validator.ValidationListener {

    private EditItemViewModel editItemViewModel;
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
    private int selectedDay = -1;
    private int selectedMonth = -1;
    private int selectedYear = -1;
    private Uri filePath;
    private Uri downloadUri;
    private TransactionAmount currentAmount;
    int expenseButtonHeight;
    int expenseButtonWidth;
    int incomeButtonHeight;
    int incomeButtonWidth;
    private final int PICK_IMAGE_REQUEST = 71;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        editItemViewModel = EditItemViewModel.getInstance();
        validator = new Validator(this);
        validator.setValidationListener(this);

        View root = inflater.inflate(R.layout.fragment_add_item2, container, false);

        TextView textView = ((MainActivity) getActivity()).findViewById(R.id.fragment_title);
        textView.setText(R.string.enter_details);

        TextView currency = root.findViewById(R.id.currency);
        currency.setText(PreferenceManager.getDefaultSharedPreferences(getContext()).getString("preferred_currency", "DKK"));

        editTextAmount = root.findViewById(R.id.editTextNumberDecimal);
        editTextName = root.findViewById(R.id.expense_item_name);

        expenseTypeButton = root.findViewById(R.id.expenseTypeExpense);
        incomeTypeButton = root.findViewById(R.id.expenseTypeIncome);
        addImageButton = root.findViewById(R.id.upload_photo);

        addExpense = root.findViewById(R.id.add_expense);
        addExpense.setText(R.string.edit_expense);

        expenseButtonHeight = expenseTypeButton.getLayoutParams().height;
        expenseButtonWidth = expenseTypeButton.getLayoutParams().width;
        incomeButtonHeight = incomeTypeButton.getLayoutParams().height;
        incomeButtonWidth = incomeTypeButton.getLayoutParams().width;

        editItemViewModel.getCurrentExpenseItem().observe(getViewLifecycleOwner(), this::setFields);

        expenseTypeButton.setOnClickListener(v -> {
            setExpenseType();
        });

        incomeTypeButton.setOnClickListener(v -> {
            setIncomeType();
        });

        eText = root.findViewById(R.id.date_selector);
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

    private void setIncomeType() {
        transactionType = TransactionType.INCOME;
        incomeTypeButton.getLayoutParams().width = (int) (expenseButtonWidth * 1.3);
        incomeTypeButton.getLayoutParams().height = (int) (expenseButtonHeight * 1.3);

        expenseTypeButton.getLayoutParams().width = (int) (incomeButtonWidth * 0.7);
        expenseTypeButton.getLayoutParams().height = (int) (incomeButtonHeight * 0.7);
        expenseTypeButton.requestLayout();
    }

    private void setExpenseType() {
        transactionType = TransactionType.EXPENSE;
        expenseTypeButton.getLayoutParams().width = (int) (expenseButtonWidth * 1.3);
        expenseTypeButton.getLayoutParams().height = (int) (expenseButtonHeight * 1.3);

        incomeTypeButton.getLayoutParams().width = (int) (incomeButtonWidth * 0.7);
        incomeTypeButton.getLayoutParams().height = (int) (incomeButtonHeight * 0.7);
        expenseTypeButton.requestLayout();
    }

    private void setFields(TransactionItem currentExpenseItem) {
        if (currentExpenseItem.getType() == TransactionType.EXPENSE) setExpenseType();
        else setIncomeType();
        editTextName.setText(currentExpenseItem.getTitle());
        if (currentAmount == null)
            currentAmount = currentExpenseItem.getAmount();
        editTextAmount.setText(String.valueOf(currentExpenseItem.getAmount().getCurrencyAmount()));
        if (selectedMonth == -1 && selectedYear == -1 && selectedDay == -1) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(currentExpenseItem.getTimestamp());
            selectedYear = calendar.get(Calendar.YEAR);
            selectedMonth = calendar.get(Calendar.MONTH);
            selectedDay = calendar.get(Calendar.DATE);
            eText.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
        }
    }

    private void uploadImage() {
        StorageReference storageReference = editItemViewModel.getStorageReference();
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
        Toast.makeText(getContext(), "Expense edited!", Toast.LENGTH_SHORT).show();
        transactionAmount = new TransactionAmount("DKK", Double.parseDouble(String.valueOf(editTextAmount.getText())));
        expenseTitle = String.valueOf(editTextName.getText());
        editItemViewModel.selectExpenseType(transactionType);
        editItemViewModel.selectAmount(transactionAmount);
        editItemViewModel.selectTitle(expenseTitle);
        editItemViewModel.selectDate(selectedDay, selectedMonth, selectedYear);
        editItemViewModel.setDownloadUri(downloadUri);
        editItemViewModel.editItem(currentAmount);
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
