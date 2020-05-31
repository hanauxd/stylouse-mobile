package lk.apiit.eea.stylouse.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

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
    private File chosenFile;
    private Uri returnUri;

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
        addPermissionActivityCompat();
    }

    private void onChooseFileClick(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void onSaveClick(View view) {
        if (chosenFile == null) {
            Toast.makeText(activity, "Choose a file before upload.", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        MultipartBody.Part file = MultipartBody.Part.createFormData(
                "file",
                chosenFile.getName(),
                RequestBody.create(MediaType.parse("image/*"), chosenFile)
        );

        String name = binding.productName.getText().toString();
        String description = binding.productDescription.getText().toString();
        double price = Double.parseDouble(binding.productPrice.getText().toString());
        int quantity = Integer.parseInt(binding.productQuantity.getText().toString());
        List<String> categories = new ArrayList<>(Collections.singletonList("Top"));

        ProductRequest product = new ProductRequest(name, quantity, price, description, categories);

        productService.createProduct(createCallback, session.getAuthState().getJwt(), product, file);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            returnUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), returnUri);
                ImageView uploadedImage = binding.uploadedImage;
                uploadedImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            super.onActivityResult(requestCode, resultCode, data);
            getFilePath();
        }
    }

    private void addPermissionActivityCompat() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission_group.STORAGE) != PackageManager.PERMISSION_GRANTED) {
            String[] list = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(activity, list, 1);
        }
    }

    private void getFilePath() {
        String filePath = DocumentHelper.getPath(activity, returnUri);
        if (filePath == null || filePath.isEmpty()) return;
        chosenFile = new File(filePath);
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