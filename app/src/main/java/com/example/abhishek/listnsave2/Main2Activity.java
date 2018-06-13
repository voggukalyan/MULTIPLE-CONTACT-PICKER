package com.example.abhishek.listnsave2;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    ArrayList<SelectUser> selectUsers;
    // Contact List
    ListView listView;
    // Cursor to load contacts list
    Cursor phones,emails;
    String name1,phoneHome,id,emailAdd;
    TextView name, number;
    SQLiteDatabase db;
    // Pop up
    ContentResolver resolver;
    SearchView search;
    SelectUserAdapter adapter;
    SQLiteOpenHelper openHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        selectUsers = new ArrayList<SelectUser>();
        resolver = this.getContentResolver();
        listView = (ListView) findViewById(R.id.contacts_list);
        name = (TextView) findViewById(R.id.name);
        number = (TextView) findViewById(R.id.no);
        openHelper = new ContactDatabase(this);

        phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        LoadContact loadContact = new LoadContact();
        loadContact.execute();

        search = (SearchView) findViewById(R.id.searchView);

        //*** setOnQueryTextListener ***
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO Auto-generated method stub

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // TODO Auto-generated method stub
                adapter.filter(newText);
                return false;
            }
        });
    }

    // Load data on background
    class LoadContact extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Get Contact list from Phone

            if (phones != null) {
                Log.e("count", "" + phones.getCount());
                if (phones.getCount() == 0) {
                    Toast.makeText(Main2Activity.this, "No contacts in your contact list.", Toast.LENGTH_LONG).show();
                }

                while (phones.moveToNext()) {
                    id = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                    emails = resolver.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            new String[]{id}, null);

                    name1 = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    phoneHome = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    //      phoneMobile = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE));
                    SelectUser selectUser = new SelectUser();
                    //         selectUser.setPhoneMobile(phoneMobile);
                    while ((emails.moveToNext())) {
                        emailAdd = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));

                        if (emailAdd != null) {
                            selectUser.setEmail(emailAdd);
                        }
                    }


                    // Get Organizations.........

                    String orgWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                    String[] orgWhereParams = new String[]{id,
                            ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE};
                    Cursor orgCur = resolver.query(ContactsContract.Data.CONTENT_URI,
                            null, orgWhere, orgWhereParams, null);
                    if (orgCur.moveToFirst()) {
                        String orgName = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DATA));
                    selectUser.setOrg(orgName);
                    }

                    //Get Postal Address....

                    String addrWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                    String[] addrWhereParams = new String[]{id,
                            ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE};
                    Cursor addrCur = resolver.query(ContactsContract.Data.CONTENT_URI,
                            null, addrWhere, addrWhereParams, null);
                    while(addrCur.moveToNext()) {

                        String street = addrCur.getString(
                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
                        String city = addrCur.getString(
                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
                        String state = addrCur.getString(
                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
                        String country = addrCur.getString(
                                addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));

                        // Do something with these....
                        selectUser.setStreet(street);
                        selectUser.setCity(city);
                        selectUser.setState(state);
                        selectUser.setCountry(country);
                    }

                    selectUser.setName(name1);
                    selectUser.setPhoneHome(phoneHome);
                    selectUsers.add(selectUser);
                    orgCur.close();
                    emails.close();
                }
                phones.close();

            } else {
                Log.e("Cursor close 1", "----------------");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter = new SelectUserAdapter(selectUsers, Main2Activity.this);
            listView.setAdapter(adapter);

            // Select item on listClick
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Log.e("search", "here---------------- listener");

                    SelectUser data = selectUsers.get(i);
                }
            });

            listView.setFastScrollEnabled(true);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        phones.close();
    }

    public void myClickHandler(View view) {
        View parentRow = (View) view.getParent();
    //    ListView listView = (ListView) parentRow.getParent();
        //     final int position = listView.getPositionForView(parentRow);
        db = openHelper.getWritableDatabase();
        String name2 = ((TextView) parentRow.findViewById(R.id.name)).getText().toString();
        String num2 = ((TextView) parentRow.findViewById(R.id.no)).getText().toString();
        String email2 = ((TextView) parentRow.findViewById(R.id.email)).getText().toString();
        String org = ((TextView) parentRow.findViewById(R.id.org)).getText().toString();
        String street = ((TextView) parentRow.findViewById(R.id.street)).getText().toString();
        String city = ((TextView) parentRow.findViewById(R.id.city)).getText().toString();
        String state = ((TextView) parentRow.findViewById(R.id.state)).getText().toString();
        String country = ((TextView) parentRow.findViewById(R.id.country)).getText().toString();



        long id = addContact(name2,num2,email2,org,street,city,state,country);
        if(id>0) {
            Toast.makeText(getApplicationContext(),"Saved "+ name2+" "+num2+"\n"+email2+"\n"+org+" "
                    +street+" "+city+"\n"+state+" "+country,Toast.LENGTH_SHORT).show();
        }
       else {
            Toast.makeText(getApplicationContext(),"Not Saved ",Toast.LENGTH_SHORT).show();

        }
    }

    public long addContact(String name,String phone,String email,String org,String street,String city,String state,String country) {

        ContentValues values = new ContentValues();
        values.put(ContactDatabase.KEY_NAME, name); // Contact Name
        values.put(ContactDatabase.KEY_PHONE, phone); // Contact Phone
        values.put(ContactDatabase.KEY_EMAIL,email);   //Contact Email
        values.put(ContactDatabase.KEY_ORG,org);   //Contact Email
        values.put(ContactDatabase.KEY_STREET,street);   //Contact Email
        values.put(ContactDatabase.KEY_CITY,city);   //Contact Email
        values.put(ContactDatabase.KEY_STATE,state);   //Contact Email
        values.put(ContactDatabase.KEY_COUNTRY,country);   //Contact Email
        // Inserting Row
        long id=db.insert(ContactDatabase.TABLE_CONTACTS, null, values);
        //2nd argument is String containing nullColumnHack
        return id;
    }
}

