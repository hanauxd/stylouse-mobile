package lk.apiit.eea.stylouse.utils;

import android.content.Context;

import lk.apiit.eea.stylouse.R;

public class UrlBuilder {
    private Context context;

    public UrlBuilder(Context context) {
        this.context = context;
    }

    public String fileUrl(String filename) {
        return context.getResources().getString(R.string.baseURL).concat("product/images/download/").concat(filename);
    }
}
