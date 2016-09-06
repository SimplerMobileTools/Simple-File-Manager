package com.simplemobiletools.filemanager.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;

import com.simplemobiletools.filemanager.R;
import com.simplemobiletools.filemanager.Utils;
import com.simplemobiletools.filemanager.models.FileDirItem;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

public class PropertiesDialog extends DialogFragment {
    private static FileDirItem mItem;

    public static PropertiesDialog newInstance(FileDirItem item) {
        mItem = item;
        return new PropertiesDialog();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final int title = (mItem.getIsDirectory()) ? R.string.directory_properties : R.string.file_properties;

        final View infoView = getActivity().getLayoutInflater().inflate(R.layout.item_properties, null);
        if (mItem.getIsDirectory()) {
            infoView.findViewById(R.id.properties_files_count_label).setVisibility(View.VISIBLE);
            infoView.findViewById(R.id.properties_files_count).setVisibility(View.VISIBLE);
            ((TextView) infoView.findViewById(R.id.properties_files_count)).setText(String.valueOf(mItem.getChildren()));
        }

        ((TextView) infoView.findViewById(R.id.properties_name)).setText(mItem.getName());
        ((TextView) infoView.findViewById(R.id.properties_path)).setText(mItem.getPath());
        ((TextView) infoView.findViewById(R.id.properties_size)).setText(getItemSize());

        final File file = new File(mItem.getPath());
        ((TextView) infoView.findViewById(R.id.properties_last_modified)).setText(formatLastModified(file.lastModified()));

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getResources().getString(title));
        builder.setView(infoView);
        builder.setPositiveButton(R.string.ok, null);

        return builder.create();
    }

    private String getItemSize() {
        if (mItem.getIsDirectory()) {
            return Utils.formatSize(directorySize(new File(mItem.getPath())));
        }

        return Utils.getFormattedSize(mItem);
    }

    private String formatLastModified(long ts) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(ts);
        return DateFormat.format("dd/MM/yyyy HH:mm", cal).toString();
    }

    private long directorySize(File dir) {
        if (dir.exists()) {
            long size = 0;
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    size += directorySize(files[i]);
                } else {
                    size += files[i].length();
                }
            }
            return size;
        }
        return 0;
    }
}
