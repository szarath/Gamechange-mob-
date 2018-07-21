package com.example.szarathkumar.gamechangemob;


import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Console;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Cs.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Cs#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Cs extends Fragment implements OnChartValueSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Button updatebtn;
    private EditText kills;
    private EditText deaths;
    private int kill;
    private int death;
    private Json js;
    private Button checkbtn;
    private PieChart pi;
    private JSONObject objrank;
    private TextView history;
    private RatingBar ratingbar;

    private JSONObject objkill;
    private JSONObject objdeath;

    public Cs() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Cs.
     */
    // TODO: Rename and change types and number of parameters
    public static Cs newInstance(String param1, String param2) {
        Cs fragment = new Cs();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_cs, container, false);
        kills = (EditText) view.findViewById(R.id.cs_kills);
        deaths = (EditText) view.findViewById(R.id.cs_deaths);
        pi =(PieChart) view.findViewById(R.id.cs_chart);
        pi.setUsePercentValues(true);
        history = (TextView) view.findViewById(R.id.cs_history);
        updatebtn = (Button) view.findViewById(R.id.Cs_update);
        checkbtn = (Button) view.findViewById(R.id.Cs_check);
        ratingbar = (RatingBar) view.findViewById(R.id.ratingBar_cs) ;
        ratingbar.setMax(5);
        getdata();

        updatebtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if( kills.getText().length() ==0 || deaths.getText().length() == 0) {
                    Snackbar.make(getView(),"Enter Details", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }
                else
                {
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String url = "Insertdata/2/"+kills.getText().toString()+"/"+deaths.getText().toString();
                                    String url2 ="SetRating/2/"+ Math.round(ratingbar.getRating());
                                    try {
                                        js.getserverinfo(url);
                                        js.getserverinfo(url2);
                                        getdata();
                                    }
                                    catch(Exception ex)
                                    {


                                    }

                                }
                            });
                        }
                    });


                    t.start();
                }


            }





        });

        checkbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getdata();
                            }
                        });
                    }
                });


                t.start();
            }








        });

        return view;
    }
    public void getdata()
    {


        js = new Json();
        try{
            String url1 = "Getkills/2";
            objkill = js.getserverinfo(url1);
            JSONArray ja1 = objkill.getJSONArray("GetkillsResult");


            String url2 = "Getdeaths/2";
            objdeath = js.getserverinfo(url2);
            JSONArray ja2 = objdeath.getJSONArray("GetdeathsResult");


            String url3 = "GetRanking/2";
            objrank = js.getserverinfo(url3);
            JSONArray ja3 = objrank.getJSONArray("GetrankingResult");

            ratingbar.setRating(Float.parseFloat(ja3.get(0).toString()));


            int totaldeath = 0 ;
            int totalkill = 0 ;

            for(int i = 0; i < ja1.length();i++)
            {
                totalkill += Integer.parseInt(ja1.get(i).toString());

            }
            for(int i = 0; i < ja2.length();i++)
            {
                totaldeath += Integer.parseInt(ja2.get(i).toString());

            }
            history.setText("");
            for (int i = 0; i < ja1.length();i++)
            {
                history.append(i+1+") "+"Kill: " + ja1.get(i).toString() + " Death: " + ja2.get(i).toString()+ System.getProperty("line.separator")) ;


            }

            history.setMovementMethod(new ScrollingMovementMethod());
            Log.d("kill", ja1.toString());
            ArrayList<Entry> yvalues = new ArrayList<Entry>();
            yvalues.add(new Entry(totalkill, 0));
            yvalues.add(new Entry(totaldeath, 1));
            //yvalues.add(new Entry(50f, 0));
            //  yvalues.add(new Entry(50f, 1));


            PieDataSet dataSet = new PieDataSet(yvalues, "");

            ArrayList<String> xVals = new ArrayList<String>();

            xVals.add("Kills");
            xVals.add("Deaths");


            PieData data = new PieData(xVals, dataSet);

            data.setValueFormatter(new PercentFormatter());
            pi.setDescription("");
            pi.setData(data);
            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            // pi.setDrawHoleEnabled(false);

            pi.setDrawHoleEnabled(false);
            pi.setTransparentCircleRadius(25f);
           // pi.setHoleRadius(25f);


            data.setValueTextSize(16f);
            data.setValueTextColor(Color.DKGRAY);
            pi.setOnChartValueSelectedListener(this);

            pi.animateXY(1400, 1400);


            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            Snackbar.make(getView(),"Data changed", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();



        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }





    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
                        + ", DataSet index: " + dataSetIndex);
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
*/
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
