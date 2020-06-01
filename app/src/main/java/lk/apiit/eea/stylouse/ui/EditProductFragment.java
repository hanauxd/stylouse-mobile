package lk.apiit.eea.stylouse.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.R;
import lk.apiit.eea.stylouse.apis.ApiResponseCallback;
import lk.apiit.eea.stylouse.application.StylouseApp;
import lk.apiit.eea.stylouse.databinding.FragmentEditProductBinding;
import lk.apiit.eea.stylouse.di.AuthSession;
import lk.apiit.eea.stylouse.models.responses.ProductResponse;
import lk.apiit.eea.stylouse.services.ProductService;
import lk.apiit.eea.stylouse.utils.UrlBuilder;
import retrofit2.Response;

import static android.text.TextUtils.isEmpty;
import static com.bumptech.glide.Glide.with;
import static com.pranavpandey.android.dynamic.toasts.DynamicToast.makeError;
import static com.pranavpandey.android.dynamic.toasts.DynamicToast.makeWarning;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static lk.apiit.eea.stylouse.databinding.FragmentEditProductBinding.inflate;

public class EditProductFragment extends RootBaseFragment {
    private MutableLiveData<Boolean> editing = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private FragmentEditProductBinding binding;

    @Inject
    UrlBuilder urlBuilder;
    @Inject
    ProductService productService;
    @Inject
    AuthSession session;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
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
        ((AppCompatActivity) this.activity).getSupportActionBar().setTitle("Edit Product");
        bindButtonsToClickListener();
        bindProductToView();
        renderReviewFragment();
    }

    private void onUpdateClick(View view) {
        if (validatedProduct() != null) {
            editing.setValue(false);
            loading.setValue(true);
            binding.btnUpdate.startAnimation();
            productService.updateProduct(updateCallback,
                    session.getAuthState().getJwt(),
                    validatedProduct());
        }
    }

    private void onEditClick(View view) {
        editing.setValue(true);
    }

    private void onEditingChange(Boolean editing) {
        binding.setEditing(editing);
    }

    private void bindProductToView() {
        binding.setProduct(product());
        with(binding.getRoot())
                .load(urlBuilder.fileUrl(product().getProductImages().get(0).getFilename()))
                .placeholder(R.drawable.stylouse_placeholder)
                .into(binding.productImage);
    }

    private void bindButtonsToClickListener() {
        editing.observe(getViewLifecycleOwner(), this::onEditingChange);
        loading.observe(getViewLifecycleOwner(), this::onLoadingChange);
        binding.btnEdit.setOnClickListener(this::onEditClick);
        binding.btnUpdate.setOnClickListener(this::onUpdateClick);
        binding.btnCancel.setOnClickListener(this::onCancelClick);
    }

    private void onCancelClick(View view) {
        binding.setProduct(product());
        editing.setValue(false);
    }

    private void onLoadingChange(Boolean loading) {
        binding.setLoading(loading);
    }

    private ProductResponse validatedProduct() {
        String name = binding.productName.getText().toString();
        String price = binding.productPrice.getText().toString();
        String quantity = binding.productQuantity.getText().toString();
        String description = binding.productDescription.getText().toString();

        if (isEmpty(name) || isEmpty(price) ||
                isEmpty(quantity) || isEmpty(description)) {
            makeWarning(activity, "Fields cannot be empty").show();
            return null;
        }

        return new ProductResponse(product().getId(), name, parseInt(quantity), parseDouble(price), description);
    }

    private ProductResponse product() {
        String productJSON = getArguments() != null ? getArguments().getString("product") : null;
        return new Gson().fromJson(productJSON, ProductResponse.class);
    }

    private void renderReviewFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("product", new Gson().toJson(product()));
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.review_layout, ReviewFragment.class, bundle);
        transaction.commit();
    }

    private ApiResponseCallback updateCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(Response<?> response) {
            binding.btnUpdate.revertAnimation();
            loading.setValue(false);
        }

        @Override
        public void onFailure(String message) {
            binding.btnUpdate.revertAnimation();
            editing.setValue(true);
            loading.setValue(false);
            makeError(activity, message).show();
        }
    };
}
