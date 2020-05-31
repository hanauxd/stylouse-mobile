package lk.apiit.eea.stylouse.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.adapters.FileAdapter;
import lk.apiit.eea.stylouse.apis.ApiResponseCallback;
import lk.apiit.eea.stylouse.application.StylouseApp;
import lk.apiit.eea.stylouse.databinding.FragmentAddProductBinding;
import lk.apiit.eea.stylouse.di.AuthSession;
import lk.apiit.eea.stylouse.models.requests.ProductRequest;
import lk.apiit.eea.stylouse.models.responses.ProductResponse;
import lk.apiit.eea.stylouse.services.ProductService;
import lk.apiit.eea.stylouse.utils.DocumentHelper;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class AddProductFragment extends RootBaseFragment {
    public static final int PICK_IMAGE_REQUEST = 1;

    private FragmentAddProductBinding binding;
    private FileAdapter adapter;

    @Inject
    ProductService productService;
    @Inject
    AuthSession session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((StylouseApp) activity.getApplication()).getAppComponent().inject(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddProductBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnChooseFile.setOnClickListener(this::onChooseFileClick);
        binding.btnSave.setOnClickListener(this::onSaveClick);
        adapter = new FileAdapter(this::setFileCount);
        binding.fileList.setAdapter(adapter);
        addPermissionActivityCompat();
    }

    private void setFileCount(int count) {
        binding.setFileCount(count);
    }

    private void onChooseFileClick(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void onSaveClick(View view) {
        if (adapter.getItemCount() < 1) {
            DynamicToast.makeWarning(activity, "Choose a file before upload.", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        List<File> selectedFiles = adapter.getFiles();

        List<MultipartBody.Part> files = selectedFiles.stream().map(
                file -> MultipartBody.Part.createFormData(
                        "file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file)
                )).collect(Collectors.toList());

        String name = binding.productName.getText().toString();
        String description = binding.productDescription.getText().toString();
        double price = Double.parseDouble(binding.productPrice.getText().toString());
        int quantity = Integer.parseInt(binding.productQuantity.getText().toString());
        List<String> categories = new ArrayList<>(Collections.singletonList("Top"));

        ProductRequest product = new ProductRequest(name, quantity, price, description, categories);

        productService.createProduct(createCallback, session.getAuthState().getJwt(), product, files);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            addFileToAdapter(data.getData());
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void addPermissionActivityCompat() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission_group.STORAGE) != PackageManager.PERMISSION_GRANTED) {
            String[] list = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(activity, list, 1);
        }
    }

    private void addFileToAdapter(Uri uri) {
        String filePath = DocumentHelper.getPath(activity, uri);
        if (filePath == null || filePath.isEmpty()) {
            DynamicToast.makeWarning(activity, "File does not exist").show();
            return;
        }
        File file = new File(filePath);
        if (file.exists()) {
            adapter.addFile(file);
        }
    }

    private ApiResponseCallback createCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(Response<?> response) {
            ProductResponse productResponse = (ProductResponse) response.body();
            System.out.println("[ON SUCCESS] " + productResponse);
        }

        @Override
        public void onFailure(String message) {
            DynamicToast.makeError(activity, message).show();
        }
    };
}