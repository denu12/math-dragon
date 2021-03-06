package org.teaminfty.math_dragon;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import org.teaminfty.math_dragon.exceptions.EmptyChildException;
import org.teaminfty.math_dragon.exceptions.MathException;
import org.teaminfty.math_dragon.model.EvalHelper;
import org.teaminfty.math_dragon.model.ModelHelper;
import org.teaminfty.math_dragon.model.ParenthesesHelper;
import org.teaminfty.math_dragon.view.TypefaceHolder;
import org.teaminfty.math_dragon.view.fragments.FragmentEvaluation;
import org.teaminfty.math_dragon.view.fragments.FragmentMainScreen;
import org.teaminfty.math_dragon.view.fragments.FragmentOperationsSource;
import org.teaminfty.math_dragon.view.math.MathSymbol;
import org.teaminfty.math_dragon.view.math.MathObject;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity implements FragmentOperationsSource.CloseMeListener
{

    /** The ActionBarDrawerToggle that is used to toggle the drawer using the action bar */
    ActionBarDrawerToggle actionBarDrawerToggle = null;

    /** Class that loads the Symja library in a separate thread */
    private class SymjaLoader extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... args)
        {
            // Simply do a simple (yet beautiful :D) calculation to make the system load Symja
            EvalEngine.eval(F.Plus(F.ZZ(1), F.Power(F.E, F.Times(F.Pi, F.I))));
            
            // Return null (return value won't be used)
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        // Load the typefaces
        TypefaceHolder.loadFromAssets(getAssets());

        // Set the default size in the MathObject class
        MathObject.lineWidth = getResources().getDimensionPixelSize(R.dimen.math_object_line_width);
        
        // Load the layout
        setContentView(R.layout.main);
        
        // Load Symja
        new SymjaLoader().execute();

        // Get the DrawerLayout object
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        // Remove the grey overlay
        drawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent));

        // Set the shadow
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.LEFT);

        try
        {
            drawerLayout.closeDrawer(Gravity.LEFT);

            // Set the toggle for the action bar
            actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.string.operation_drawer_open, R.string.operation_drawer_close);
            getActionBar().setDisplayHomeAsUpEnabled(true);

            // Listen when to close the operations drawer
            // TODO only register this event when needed
            ((FragmentOperationsSource) getFragmentManager().findFragmentById(R.id.fragmentOperationDrawer)).setOnCloseMeListener(this);
        }
        catch(IllegalArgumentException e)
        {
            // there was no drawer to open
            // Don't have a way to detect if there is a drawer yet so we just listen for this exception..
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);

        // Sync the toggle state after onRestoreInstanceState has occurred
        if(actionBarDrawerToggle != null)
            actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);

        // Notify actionBarDrawerToggle of any configuration changes
        if(actionBarDrawerToggle != null)
            actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Pass the event to ActionBarDrawerToggle, if it returns true, then it has handled the app icon touch event
        if(actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;

        // Handle other action bar items...
        return super.onOptionsItemSelected(item);
    }
    
    /**
     * Gets called when wolfram alpha needs to be started. It will send the unevaluated IExpr to wolfram alpha for evaluation and inspection
     * @param view
     */
    public void wolfram(View view)
    {
        // Get the MathObject
        FragmentMainScreen fragmentMainScreen = (FragmentMainScreen) getFragmentManager().findFragmentById(R.id.fragmentMainScreen);
        MathObject obj = fragmentMainScreen.getMathObject();
        
        // Only send to Wolfram|Alpha if the MathObject is completed
        if(obj.isCompleted())
        {
            // Get the query
            String query = obj.toString();
            
            // Strip the query of unnecessary outer parentheses
            if(query.startsWith("(") && query.endsWith(")"))
                    query = query.substring(1, query.length() - 1);

            // Start an intent to send the user to Wolfram|Alpha
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://www.wolframalpha.com/input/?i=" + Uri.encode(query)));
            startActivity(intent);
        }
    }

    public void evaluate(View view)
    {
        try
        {
            // Calculate the answer
            FragmentMainScreen fragmentMainScreen = (FragmentMainScreen) getFragmentManager().findFragmentById(R.id.fragmentMainScreen);
            IExpr result = EvalEngine.eval( EvalHelper.eval(fragmentMainScreen.getMathObject()) );

            // Get the evaluation fragment and show the result
            FragmentEvaluation fragmentEvaluation = (FragmentEvaluation) getFragmentManager().findFragmentById(R.id.fragmentEvaluation);
            fragmentEvaluation.showMathObject(ParenthesesHelper.setParentheses(ModelHelper.toMathObject(result)));

            // Get the DrawerLayout object and open the drawer
            DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
            drawerLayout.openDrawer(Gravity.RIGHT | Gravity.BOTTOM);
        }
        catch(EmptyChildException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch(MathException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void approximate(View view)
    {
        // Get the DrawerLayout object
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        drawerLayout.openDrawer(Gravity.RIGHT | Gravity.BOTTOM);
        // TODO: Approximate the MathObject in the drawing space, and display the resulting constant

        FragmentEvaluation fragmentEvaluation = (FragmentEvaluation) getFragmentManager()
                .findFragmentById(R.id.fragmentEvaluation);

        MathSymbol mathConstant = new MathSymbol(42,0,0,0,null);
        fragmentEvaluation.showMathObject(mathConstant);
    }

    public void clear(View view)
    {

        // Simply clear the current formula
        FragmentMainScreen fragmentMainScreen = (FragmentMainScreen) getFragmentManager().findFragmentById(R.id.fragmentMainScreen);
        fragmentMainScreen.clear();
    }

    @Override
    public void closeMe()
    {
        // Get the DrawerLayout object
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        try
        {
            drawerLayout.closeDrawer(Gravity.LEFT);
        }
        catch(IllegalArgumentException e)
        {
            // there was no drawer to open.
            // Don't have a way to detect if there is a drawer yet so we just listen for this exception..
        }
    }   
}
