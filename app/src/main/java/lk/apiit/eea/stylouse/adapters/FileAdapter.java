package lk.apiit.eea.stylouse.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lk.apiit.eea.stylouse.databinding.FileListItemBinding;
import lk.apiit.eea.stylouse.interfaces.AdapterPositionClickListener;

import static android.net.Uri.fromFile;
import static android.provider.MediaStore.Images.Media.getBitmap;
import static android.view.LayoutInflater.from;
import static com.pranavpandey.android.dynamic.toasts.DynamicToast.makeWarning;
import static lk.apiit.eea.stylouse.databinding.FileListItemBinding.inflate;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> {
    private List<File> files = new ArrayList<>();
    private AdapterPositionClickListener listener;
    private Activity activity;

    public FileAdapter(AdapterPositionClickListener listener, Activity activity) {
        this.listener = listener;
        this.activity = activity;
    }

    public void addFile(File file) {
        files.add(file);
        this.notifyDataSetChanged();
        listener.onItemClick(files.size());
    }

    public void removeFile(int position) {
        files.remove(position);
        this.notifyDataSetChanged();
        listener.onItemClick(files.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FileListItemBinding binding = inflate(from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(files.get(position), this::removeFile, position, activity);
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public List<File> getFiles() {
        return files;
    }

    public void clearFiles() {
        files.clear();
        this.notifyDataSetChanged();
    }

    static public class ViewHolder extends RecyclerView.ViewHolder{
        private FileListItemBinding binding;

        public ViewHolder(@NonNull FileListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(File file, AdapterPositionClickListener listener, int position, Activity activity) {
            try {
                binding.setFilename(file.getName());
                binding.btnRemove.setOnClickListener(v -> listener.onItemClick(position));
                Uri uri = fromFile(file);
                Bitmap bitmap = getBitmap(activity.getContentResolver(), uri);
                binding.fileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                makeWarning(activity, e.getMessage()).show();
            }
        }
    }
}
