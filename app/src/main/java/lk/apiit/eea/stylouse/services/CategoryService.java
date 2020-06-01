package lk.apiit.eea.stylouse.services;

import java.util.List;

import javax.inject.Inject;

import lk.apiit.eea.stylouse.apis.ApiResponseCallback;
import lk.apiit.eea.stylouse.apis.CategoryAPI;
import lk.apiit.eea.stylouse.apis.RetroFitCallback;
import lk.apiit.eea.stylouse.models.Category;
import retrofit2.Call;
import retrofit2.Retrofit;

import static lk.apiit.eea.stylouse.utils.StringFormatter.formatToken;

public class CategoryService {
    private CategoryAPI categoryAPI;

    @Inject
    public CategoryService(Retrofit retrofit) {
        this.categoryAPI = retrofit.create(CategoryAPI.class);
    }

    public void addCategory(ApiResponseCallback callback, String jwt, Category category) {
        Call<List<Category>> addCall = categoryAPI.addCategory(formatToken(jwt), category);
        addCall.enqueue(new RetroFitCallback<>(callback));
    }

    public void getCategories(ApiResponseCallback callback, String jwt) {
        Call<List<Category>> categoriesCall = categoryAPI.getCategories(formatToken(jwt));
        categoriesCall.enqueue(new RetroFitCallback<>(callback));
    }
}
