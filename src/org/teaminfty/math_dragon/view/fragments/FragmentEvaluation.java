package org.teaminfty.math_dragon.view.fragments;

import org.teaminfty.math_dragon.R;
import org.teaminfty.math_dragon.view.MathView;
import org.teaminfty.math_dragon.view.math.MathObject;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentEvaluation extends Fragment
{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        
        
       
        return inflater.inflate(R.layout.fragment_evaluation, container, false);
    }
    
    public void showMathObject(MathObject mathObject)
    {

    	
    	MathView mathView = (MathView) getView().findViewById(R.id.mathView);
        mathView.setMathObject(mathObject);
    }
    
}

