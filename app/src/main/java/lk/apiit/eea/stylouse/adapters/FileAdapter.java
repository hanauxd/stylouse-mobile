package lk.apiit.eea.stylouse.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import lk.apiit.eea.stylouse.databinding.FileListItemBinding;
import lk.apiit.eea.stylouse.interfaces.AdapterPositionClickListener;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> {
    private List<File> files = new ArrayList<>();
    private AdapterPositionClickListener listener;

    public FileAdapter(AdapterPositionClickListener listener) {
        this.listener = listener;
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
        FileListItemBinding binding = FileListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(files.get(position), this::removeFile, position);
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public List<File> getFiles() {
        return files;
    }

    static public class ViewHolder extends RecyclerView.ViewHolder{
        private FileListItemBinding binding;

        public ViewHolder(@NonNull FileListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(File file, AdapterPositionClickListener listener, int position) {
            binding.setFilename(file.getName());
            binding.btnRemove.setOnClickListener(v -> listener.onItemClick(position));
        }
    }
}
