package lk.apiit.eea.stylouse.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.adapters.CategoryAdapter;
import lk.apiit.eea.stylouse.apis.ApiResponseCallback;
import lk.apiit.eea.stylouse.application.StylouseApp;
import lk.apiit.eea.stylouse.databinding.FragmentCategoryBinding;
import lk.apiit.eea.stylouse.di.AuthSession;
import lk.apiit.eea.stylouse.models.Category;
import lk.apiit.eea.stylouse.services.CategoryService;
import retrofit2.Response;

public class CategoryFragment extends AuthFragment {
    private MutableLiveData<String> error = new MutableLiveData<>(null);
    private MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private FragmentCategoryBinding binding;

    @Inject
    CategoryService categoryService;
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
        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) this.activity).getSupportActionBar().setTitle("Categories");
        if (session.getAuthState() != null) {
            error.observe(getViewLifecycleOwner(), this::onErrorChange);
            loading.observe(getViewLifecycleOwner(), this::onLoadingChange);
            binding.btnAddCategory.setOnClickListener(this::onAddClick);
            fetchCategories();
        }
    }

    private void onLoadingChange(Boolean loading) {
        binding.setLoading(loading);
    }

    private void onErrorChange(String error) {
        binding.setError(error);
    }

    private void onAddClick(View view) {
        String categoryText = binding.category.getText().toString();
        if (TextUtils.isEmpty(categoryText)) {
            error.setValue("Category is required");
        } else {
            Category category = new Category(categoryText);
            categoryService.addCategory(categoryCallback, session.getAuthState().getJwt(), category);
        }
    }

    private void fetchCategories() {
        loading.setValue(true);
        categoryService.getCategories(categoryCallback, session.getAuthState().getJwt());
    }

    private ApiResponseCallback categoryCallback = new ApiResponseCallback() {
        @Override
        public void onSuccess(Response<?> response) {
            if (response.body() != null) {
                List<Category> categories = (List<Category>) response.body();
                loading.setValue(false);
                binding.category.setText("");
                CategoryAdapter adapter = new CategoryAdapter(categories);
                binding.categoryList.setAdapter(adapter);
            }
        }

        @Override
        public void onFailure(String message) {
            loading.setValue(false);
            error.setValue(message);
        }
    };
}
