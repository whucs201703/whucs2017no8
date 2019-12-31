package com.example.translate;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LangAdapter extends RecyclerView.Adapter<LangAdapter.LangHolder> {
    private List<String> mLanguageList;

    public LangAdapter(List<String> mLanguageList) {
        this.mLanguageList = mLanguageList;
    }

    public static class LangHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public LangHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.language);
        }
    }

    @NonNull
    @Override
    public LangHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.language_piece, parent, false);
        return new LangHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LangHolder holder, int position) {
        holder.textView.setText(mLanguageList.get(position));
        if(!holder.itemView.hasOnClickListeners()) {
            holder.itemView.setOnClickListener(v->{
                if(LangChooseActivity.class.isInstance(holder.itemView.getContext())) {
                    LangChooseActivity activity = (LangChooseActivity) holder.itemView.getContext();
                    Intent intent = new Intent();
                    intent.putExtra("language", mLanguageList.get(position));
                    activity.setResult(Activity.RESULT_OK, intent);
                    activity.finish();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mLanguageList.size();
    }
}
