package com.example.translate;




import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;



public class DictionaryAdapter extends CursorAdapter
{
    private LayoutInflater layoutInflater;

    @Override
    public CharSequence convertToString(Cursor cursor)
    {
        return cursor == null ? "" : cursor.getString(cursor
                .getColumnIndex("_id"));
    }

    private void setView(View view, Cursor cursor)
    {
        TextView tvWordItem = (TextView) view;
        tvWordItem.setText(cursor.getString(cursor.getColumnIndex("_id")));
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        setView(view, cursor);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        View view = layoutInflater.inflate(R.layout.word_list_item, null);
        setView(view, cursor);
        return view;
    }

    public DictionaryAdapter(Context context, Cursor c, boolean autoRequery)
    {
        super(context, c, autoRequery);
        layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
}

