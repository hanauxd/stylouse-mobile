package lk.apiit.eea.stylouse.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import lk.apiit.eea.stylouse.ui.InboxFragment;

public class InquiriesPagerAdapter extends FragmentStateAdapter {

    public InquiriesPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new InboxFragment("TYPE_INBOX");
        } else {
            return new InboxFragment("TYPE_UNREAD");
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
