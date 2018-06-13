package com.example.abhishek.listnsave2;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SelectUserAdapter extends BaseAdapter {
    public List<SelectUser> _data;
    private ArrayList<SelectUser> arraylist;
    Context _c;
    ViewHolder v;

    public SelectUserAdapter(List<SelectUser> selectUsers, Context context) {
        _data = selectUsers;
        _c = context;
        this.arraylist = new ArrayList<SelectUser>();
        this.arraylist.addAll(_data);
    }

    @Override
    public int getCount() {
        return _data.size();
    }

    @Override
    public Object getItem(int i) {
        return _data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;

        if (view == null) {

            LayoutInflater li = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.contact_info, null);

            Button btnNxt = (Button) view.findViewById(R.id.save1);
            btnNxt.setTag(i);

            Log.e("Inside", "here--------------------------- In view1");
        } else {
            view = convertView;
            Log.e("Inside", "here--------------------------- In view2");
        }

        v = new ViewHolder();

        v.title = (TextView) view.findViewById(R.id.name);
        v.phoneHome = (TextView) view.findViewById(R.id.no);
        //  v.phoneMobile = (TextView) view.findViewById(R.id.no2);
        v.email = (TextView)view.findViewById(R.id.email);
        v.org = (TextView)view.findViewById(R.id.org);
        v.street = (TextView)view.findViewById(R.id.street);
        v.city = (TextView)view.findViewById(R.id.city);
        v.state = (TextView)view.findViewById(R.id.state);
        v.country =(TextView)view.findViewById(R.id.country);
        final SelectUser data = (SelectUser) _data.get(i);
        v.title.setText(data.getName());
        v.phoneHome.setText(data.getPhoneHome());
        //    v.phoneMobile.setText(data.getPhoneMobile());
        v.email.setText(data.getEmail());
        v.org.setText(data.getOrg());
        v.street.setText(data.getStreet());
        v.city.setText(data.getCity());
        v.state.setText(data.getState());
        v.country.setText(data.getCountry());
        view.setTag(data);
        return view;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        _data.clear();
        if (charText.length() == 0) {
            _data.addAll(arraylist);
        } else {
            for (SelectUser wp : arraylist) {
                if (wp.getName().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    _data.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView title, phoneHome, email, org, street,city,state,country;
    }
}

