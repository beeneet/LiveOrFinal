package com.tashambra.mobileapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GraphActivity extends Fragment{
    private final Handler mHandler = new Handler();
    private Runnable mTimer2;
    private TextView mGraphTextView;
    private LineGraphSeries<DataPoint> mSeries2;
    private LineGraphSeries<DataPoint> mSeries3;
    private double graph2LastXValue = 5d;

    private double mBACvalue;
    private String Gender;
    private double Weight;
    private double AlcoholPercent;
    private double AlcoholVolume;
    private double TimePassed;
    private long initialTime;
    private FloatingActionButton mBeerButton;
    private FloatingActionButton mWineButton;
    private FloatingActionButton mShotsButton;
    private FloatingActionButton mMyDrinksButton;
    private FloatingActionButton mAddNewDrinkButton;
    private static double prevVal = 0.0;
    List<Drink> myDrinksList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.activity_graph, container, false);

        mGraphTextView = (TextView) v.findViewById(R.id.graph_text_view);

        //Buttons FABs to be
        mBeerButton = (FloatingActionButton) v.findViewById(R.id.beer_button);
        mBeerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDrinksDialogBox(new Beer());
            }
        });

        mWineButton = (FloatingActionButton) v.findViewById(R.id.wine_button);
        mWineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDrinksDialogBox(new Wine());
            }
        });

        mShotsButton = (FloatingActionButton) v.findViewById(R.id.shots_button);
        mShotsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDrinksDialogBox(new Shot());
            }
        });

        mAddNewDrinkButton = (FloatingActionButton) v.findViewById(R.id.add_new_drink_button);
        mAddNewDrinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewDrinkDialogBox();
            }
        });

        mMyDrinksButton = (FloatingActionButton) v.findViewById(R.id.my_drinks_button);
        mMyDrinksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChooseMyDrinksDialogBox();
            }
        });

        populateDrinks(v);

        //Getting the values stored
        SharedPreferences defaultsSharedPref = PreferenceManager.getDefaultSharedPreferences(v.getContext());
        String weightString = defaultsSharedPref.getString("WeightInPounds", "150");
        Weight = Double.valueOf(weightString);
        Gender = defaultsSharedPref.getString("Gender", "female");

        setInitialTime(System.currentTimeMillis());
        final long initialTimeFinal = getInitialTime();
        calculateBAC(Gender, Weight, AlcoholPercent, AlcoholVolume, TimePassed, prevVal);

        mGraphTextView.setText(String.valueOf((new DecimalFormat(".####").format(getmBACvalue()))));
        mGraphTextView.setShadowLayer(3,3,3,Color.BLACK);
        final GraphView graph = (GraphView) v.findViewById(R.id.graph);
        mSeries2 = new LineGraphSeries<>();
        mSeries3 = new LineGraphSeries<>();
        graph.setBackgroundColor(Color.parseColor("#a7aeba"));
        mSeries2.setThickness(10);
        graph.addSeries(mSeries2);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.addSeries(mSeries3);
        mSeries3.appendData(new DataPoint(0,0.08), true, 10000);

        mTimer2 = new Runnable() {
            @Override
            public void run() {
                mGraphTextView.setShadowLayer(3,3,3,Color.BLACK);
                graph2LastXValue = (System.currentTimeMillis()-initialTimeFinal)/1000;
                mSeries2.appendData(new DataPoint(graph2LastXValue, getBAC()), true, 10000);
                mSeries3.appendData(new DataPoint(graph2LastXValue,0.08), true, 10000);
                graph.getViewport().setMinX(0);
                graph.getViewport().setMaxX(graph2LastXValue);
                graph.getViewport().setMinY(0);
                graph.getViewport().setMaxY(Math.max(0.001, mSeries2.getHighestValueY()*1.1));
                mGraphTextView.setText(String.valueOf((new DecimalFormat(".####").format(getmBACvalue()))));
                mHandler.postDelayed(this, 3000);
            }
        };
        mHandler.postDelayed(mTimer2, 0);
        return v;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public double getWeight() {
        return Weight;
    }

    public void setWeight(double weight) {
        Weight = weight;
    }

    public double getAlcoholPercent() {
        return AlcoholPercent;
    }

    public void setAlcoholPercent(double alcoholPercent) {
        AlcoholPercent = alcoholPercent;
    }

    public double getAlcoholVolume() {
        return AlcoholVolume;
    }

    public void setAlcoholVolume(double alcholVolume) {
        AlcoholVolume = alcholVolume;
    }

    public double getTimePassed() {
        return TimePassed;
    }

    public void setTimePassed(double timePassed) {
        TimePassed = Math.min(timePassed, TimePassed);
    }

    public long getInitialTime() {
        return initialTime;
    }

    public void setInitialTime(long initialTime) {
        this.initialTime = initialTime;
    }

    private void setmBACvalue(double resBAC){
        mBACvalue = resBAC;
    }

    private double getmBACvalue(){
        return  mBACvalue;
    }

//    private DataPoint[] generateData() {
//        int count = 30;
//        DataPoint[] values = new DataPoint[count];
//        for (int i=0; i<count; i++) {
//            double x = i;
//            double f = mRand.nextDouble()*0.15+0.3;
//            double y = Math.sin(i*f+2) + mRand.nextDouble()*0.3;
//            DataPoint v = new DataPoint(x, y);
//            values[i] = v;
//        }
//        return values;
//    }

//    double mLastRandom = 2;
//    Random mRand = new Random();
//    private double getRandom() {
//        return mLastRandom += mRand.nextDouble()*0.5 - 0.25;
//    }

    private double getBAC(){
        return calculateBAC(Gender, Weight, AlcoholPercent, AlcoholVolume, TimePassed, prevVal);
    }

    private double calculateBAC(String gender, double weight, double alcoholP, double alcoholV, double time, double prevVal){
        double genValue;
        if (gender.equals("male")){
            genValue = 0.73;
        } else {
            genValue = 0.66;
        }
        Log.i("VALUES", String.valueOf(gender) + String.valueOf(weight) + String.valueOf(alcoholP) + String.valueOf(alcoholV) + String.valueOf(time));
        long currentTime = System.currentTimeMillis();
        long differenceTime = currentTime - getInitialTime();
        double newTime = time + (differenceTime * 0.000000277778);
        Log.i("TIMES N C", String.valueOf(newTime) + " " + String.valueOf(getInitialTime()));
        double alcoholAmount = (alcoholV * alcoholP)/100;
        double result = prevVal + (alcoholAmount * (5.14/weight)* genValue) - 0.015 * newTime;
        String res = String.valueOf(result);
        Log.i("CURR_BAC", res);
        setmBACvalue(Math.max(0, result));
        return getmBACvalue();
    }
    //ALERT DIALOG AND ADDING DRINKS

    private void showDrinksDialogBox(Drink drink){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_add_drink, null);
        builder.setView(dialogView);
        TextView mDrinkName = (TextView) dialogView.findViewById(R.id.drink_name_text_view);
        final EditText mVolumeEditText = (EditText) dialogView.findViewById(R.id.volume_edit_text);
        final EditText mAlcoholPercentEdit = (EditText) dialogView.findViewById(R.id.alcoholp_edit_text);
        final EditText mTimeSince = (EditText) dialogView.findViewById(R.id.time_edit_text) ;
        Log.i("getName", drink.getName());
        mDrinkName.setText(drink.getName());
        mVolumeEditText.setText(String.valueOf(drink.getVolume()));
        mAlcoholPercentEdit.setText(String.valueOf(drink.getAlcoholPercent()));
        mTimeSince.setText("0");
        builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                prevVal = getBAC();
                setInitialTime(System.currentTimeMillis());
                setAlcoholPercent(Double.valueOf(mAlcoholPercentEdit.getText().toString()));
                setAlcoholVolume(Double.valueOf(mVolumeEditText.getText().toString()));
                setTimePassed(Double.valueOf(mTimeSince.getText().toString()));
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void addNewDrinkDialogBox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_add_drink, null);
        builder.setView(dialogView);
        TextView mDrinkName = (TextView) dialogView.findViewById(R.id.drink_name_text_view);
        final EditText mDrinkNameEdit = (EditText) dialogView.findViewById(R.id.drink_name_edit_text);
        mDrinkName.setVisibility(View.INVISIBLE);
        mDrinkNameEdit.setVisibility(View.VISIBLE);
        final EditText mVolumeEditText = (EditText) dialogView.findViewById(R.id.volume_edit_text);
        final EditText mAlcoholPercentEdit = (EditText) dialogView.findViewById(R.id.alcoholp_edit_text);
//        Button mAddDrink = (Button) getView().findViewById(R.id.ok_button);
        final EditText mTimeSince = (EditText) dialogView.findViewById(R.id.time_edit_text) ;
        mTimeSince.setText("0");
        builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                prevVal = getBAC();

                SharedPreferences defaultsSharedPref = PreferenceManager.getDefaultSharedPreferences(dialogView.getContext());
                String totalCustomDrinks = defaultsSharedPref.getString("TotalCustomDrinks", "0");
                String jsonObjectKey = "customDrink" + (Integer.valueOf(totalCustomDrinks) + 1);
                Drink drink = new Drink(mDrinkNameEdit.getText().toString(), Double.valueOf(mAlcoholPercentEdit.getText().toString()), Double.valueOf(mVolumeEditText.getText().toString()));
                SharedPreferences.Editor edit = defaultsSharedPref.edit();
                Gson gson = new Gson();
                String json = gson.toJson(drink);
                edit.putString(jsonObjectKey, json);
                Log.i("StringObj", drink.toString());
                edit.putString("TotalCustomDrinks", String.valueOf(Integer.valueOf(totalCustomDrinks) + 1));        //Updating the value
                edit.apply();

                setInitialTime(System.currentTimeMillis());
                setAlcoholPercent(drink.getAlcoholPercent());
                setAlcoholVolume(drink.getVolume());
                setTimePassed(Double.parseDouble(mTimeSince.getText().toString()));
                Integer totalDrinks = Integer.valueOf(defaultsSharedPref.getString("TotalCustomDrinks", "0"));

//                for (int i = 1; i <= totalDrinks; i++){
//                    jsonObjectKey = "customDrink" + i;
//                    String strjson = defaultsSharedPref.getString(jsonObjectKey, "0");
//                    Log.i("strjson", strjson);
//                    if (strjson != null){
//                        try {
//                            JSONObject jsondata = new JSONObject(strjson);
//                            Drink adrink = new Drink(jsondata.getString("name"), Double.valueOf(jsondata.getString("AlcoholPercent")), Double.valueOf(jsondata.getString("volume")) );
//                            myDrinksList.add(adrink);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
                populateDrinks(dialogView);

            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void populateDrinks(View v){
        SharedPreferences defaultsSharedPref = PreferenceManager.getDefaultSharedPreferences(v.getContext());
        Integer totalDrinks = Integer.valueOf(defaultsSharedPref.getString("TotalCustomDrinks", "0"));
        String jsonObjectKey;
        for (int i = 1; i <= totalDrinks; i++){
            jsonObjectKey = "customDrink" + i;
            String strjson = defaultsSharedPref.getString(jsonObjectKey, "0");
            Log.i("strjson", strjson);
            if (strjson != null){
                try {
                    JSONObject jsondata = new JSONObject(strjson);
                    Drink adrink = new Drink(jsondata.getString("name"), Double.valueOf(jsondata.getString("AlcoholPercent")), Double.valueOf(jsondata.getString("volume")) );
                    myDrinksList.add(adrink);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void showChooseMyDrinksDialogBox(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.list_fragment, null);
        builder.setView(dialogView);

        ListView mListView = (ListView) dialogView.findViewById(R.id.list_view) ;
        final EditText mTimeSince = (EditText) dialogView.findViewById(R.id.time_since_edit_text);
        DrinkAdapter adapter = new DrinkAdapter(getContext());
        adapter.setItems(myDrinksList);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Drink drink = (Drink) parent.getAdapter().getItem(position);
                //setting stuff up
                prevVal = getBAC();
                setInitialTime(System.currentTimeMillis());
                setAlcoholPercent(drink.getAlcoholPercent());
                Log.i("Percent", String.valueOf(drink.getAlcoholPercent()));
                setAlcoholVolume(drink.getVolume());
                setTimePassed(Double.parseDouble(mTimeSince.getText().toString()));
                Log.i("Volume", String.valueOf(drink.getVolume()));
                Log.i("TimeSince", String.valueOf(getTimePassed()));

            }
        });

//        TextView mDrinkName = (TextView) dialogView.findViewById(R.id.drink_name_text_view);
//        final EditText mDrinkNameEdit = (EditText) dialogView.findViewById(R.id.drink_name_edit_text);
//        mDrinkName.setVisibility(View.INVISIBLE);
//        mDrinkNameEdit.setVisibility(View.VISIBLE);
//        final EditText mVolumeEditText = (EditText) dialogView.findViewById(R.id.volume_edit_text);
//        final EditText mAlcoholPercentEdit = (EditText) dialogView.findViewById(R.id.alcoholp_edit_text);
//        EditText mTimeSince = (EditText) dialogView.findViewById(R.id.time_edit_text) ;
//        mTimeSince.setText("0");
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();



    }

    private class DrinkAdapter extends BaseAdapter {
        private Context mContext;
        private LayoutInflater mInflater;
        private List<Drink> mDataSource;
        private TextView mDrinkName;
        private TextView mDrinkVolume;
        private TextView mDrinkAlcoholPercent;

        public DrinkAdapter(Context context) {
            mContext = context;
            mDataSource = new ArrayList<>();
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void setItems(List<Drink> drinkList) {
            mDataSource.clear();
            mDataSource.addAll(drinkList);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() { return mDataSource.size(); }

        @Override
        public Object getItem(int position) { return mDataSource.get(position); }

        @Override
        public long getItemId(int position) { return position; }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Drink drink = mDataSource.get(position);
            View rowView = mInflater.inflate(R.layout.list_item_drink, parent, false);
            mDrinkName = (TextView) rowView.findViewById(R.id.user_drinkname);
            mDrinkVolume = (TextView) rowView.findViewById(R.id.user_volume);
            mDrinkAlcoholPercent = (TextView) rowView.findViewById(R.id.user_alcoholpercent);
            mDrinkName.setText(drink.getName().toUpperCase());
            mDrinkVolume.append(String.valueOf(drink.getVolume()));
            mDrinkAlcoholPercent.append(String.valueOf(drink.getAlcoholPercent()));
            return rowView;
        }
    }



    }

