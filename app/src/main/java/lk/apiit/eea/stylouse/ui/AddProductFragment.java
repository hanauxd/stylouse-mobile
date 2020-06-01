package lk.apiit.eea.stylouse.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.adapters.FileAdapter;
import lk.apiit.eea.stylouse.apis.ApiResponseCallback;
import lk.apiit.eea.stylouse.application.StylouseApp;
import lk.apiit.eea.stylouse.databinding.FragmentAddProductBinding;
import lk.apiit.eea.stylouse.di.AuthSession;
import lk.apiit.eea.stylouse.models.Category;
import lk.apiit.eea.stylouse.models.requests.ProductRequest;
import lk.apiit.eea.stylouse.models.responses.ProductResponse;
import lk.apiit.eea.stylouse.services.CategoryService;
import lk.apiit.eea.stylouse.services.ProductService;
import lk.apiit.eea.stylouse.utils.PermissionManager;
import okhttp3.MultipartBody.Part;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Intent.ACTION_GET_CONTENT;
import static android.content.Intent.createChooser;
import static android.text.TextUtils.isEmpty;
import static android.widget.Toast.LENGTH_SHORT;
import static com.pranavpandey.android.dynamic.toasts.DynamicToast.makeError;
import static com.pranavpandey.android.dynamic.toasts.DynamicToast.makeSuccess;
import static com.pranavpandey.android.dynamic.toasts.DynamicToast.makeWarning;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static lk.apiit.eea.stylouse.databinding.FragmentAddProductBinding.inflate;
import static lk.apiit.eea.stylouse.utils.Constants.PICK_IMAGE_REQUEST;
import static lk.apiit.eea.stylouse.utils.DocumentHelper.getPath;
import static okhttp3.MediaType.parse;
import static okhttp3.MultipartBody.Part.createFormData;
import static okhttp3.RequestBody.create;

public class AddProductFragment extends RootBaseFragment {
    private MutableLiveData<Boolean> loading = new MutableLiveData<>(true);
    private MutableLiveData<String> error = new MutableLiveData<>(null);
    private ArrayList<String> categoryList = new ArrayList<>(singletonList(""));
    private FragmentAddProductBinding binding;
    private String selectedCategory = "";
    private FileAdapter adapter;

    @Inject
    AuthSession session;
    @Inject
    ProductService productService;
    @Inject
    CategoryService categoryService;
    @Inject
    PermissionManager permissionManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((StylouseApp) activity.getApplication()).getAppComponent().inject(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) this.activity).getSupportActionBar().setTitle("Add Product");
        permissionManager.requestExternalStoragePermission(activity);
        bindToView();
        fetchCategories();
    }

    private void fetchCategories() {
        error.setValue(null);
        categoryService.getCategories(categoryFetchCallback, session.getAuthState().getJwt());
    }

    private void bindToView() {
        loading.observe(getViewLifecycleOwner(), this::onLoadingChange);
        error.observe(getViewLifecycleOwner(), this::onErrorChange);
        binding.btnChooseFile.setOnClickListener(this::onChooseFileClick);
        binding.btnSave.setOnClickListener(this::onSaveClick);
        adapter = new FileAdapter(this::setFileCount, activity);
        binding.fileList.setAdapter(adapter);
    }

    private void onErrorChange(String error) {
        binding.setError(error);
    }

    private void onLoadingChange(Boolean loading) {
        binding.setLoading(loading);
    }

    private void setFileCount(int count) {
        binding.setFileCount(count);
    }

    private void onChooseFileClick(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(ACTION_GET_CONTENT);
        startActivityForResult(createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void onSaveClick(View view) {
        if (adapter.getItemCount() < 1) {
            makeWarning(activity, "Choose a file before upload.", LENGTH_SHORT)
                    .show();
            return;
        }

        if (product() != null) {
            binding.btnSave.startAnimation();
            productService.createProduct(createProductCallback,
                    session.getAuthState().getJwt(),
                    product(),
                    imageFiles());
        }
    }

    private List<Part> imageFiles() {
        return adapter
                .getFiles()
                .stream()
                .map(file -> createFormData("file",
                        file.getName(),
                        create(parse("image/*"), file)))
                .collect(toList());
    }

    private ProductRequest product() {
        String name = binding.productName.getText().toString();
        String description = binding.productDescription.getText().toString();
        String price = binding.productPrice.getText().toString();
        String quantity = binding.productQuantity.getText().toString();

        if (isEmpty(name) || isEmpty(description) || isEmpty(price)
                || isEmpty(quantity) || isEmpty(selectedCategory)) {
            makeWarning(activity, "Fields cannot be empty").show();
            return null;
        }

        List<String> categories = new ArrayList<>(singletonList(selectedCategory));
        return new ProductRequest(name, parseInt(quantity), parseDouble(price), description, categories);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            addFileToAdapter(data.getData());
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void addFileToAdapter(Uri uri) {
        String filePath = getPath(activity, uri);
        if (filePath == null || filePath.isEmpty()) {
            makeWarning(activity, "File does not exist").show();
            return;
        }

        File file = new File(filePath);
        if (file.exists()) {
            adapter.addFile(file);
        }
    }

    private void resetInputFields() {
        binding.productName.setText("");
        binding.productQuantity.setText("");
        binding.productPrice.setText("");
        binding.productDescription.setText("");
        adapter.clearFiles();
    }

    private ApiResponseCallback createProductCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(Response<?> response) {
            ProductResponse product = (ProductResponse) response.body();
            if (product != null) {
                makeSuccess(activity, format("Product %s added successfully", product.getName())).show();
            }
            resetInputFields();
            binding.btnSave.revertAnimation();
        }

        @Override
        public void onFailure(String message) {
            binding.btnSave.revertAnimation();
            makeError(activity, message).show();
        }
    };

    private ApiResponseCallback categoryFetchCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(Response<?> response) {
            List<Category> categories = (List<Category>) response.body();
            if (categories != null) {
                categories.forEach(category -> {
                    categoryList.add(category.getCategory());
                });
                setAdapterToSpinner();
            }
            loading.setValue(false);
        }

        private void setAdapterToSpinner() {
            binding.spinner.setOnItemSelectedListener(categorySelectedCallback);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, categoryList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spinner.setAdapter(adapter);
        }

        @Override
        public void onFailure(String message) {
            loading.setValue(false);
            error.setValue(message);
        }
    };

    private AdapterView.OnItemSelectedListener categorySelectedCallback = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selectedCategory = categoryList.get(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };
}