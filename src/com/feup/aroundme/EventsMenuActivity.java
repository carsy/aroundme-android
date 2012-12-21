package com.feup.aroundme;

import android.os.Bundle;
import android.view.View;

public class EventsMenuActivity extends SuperActivity 
{

protected void onCreate(Bundle savedInstanceState) 
{
    super.onCreate(savedInstanceState);
    setContentView (R.layout.activity_events_menu);
    setTitleFromActivityLabel (R.id.title_text);
    
    findViewById(R.id.seekBar1).setVisibility(View.GONE);
    findViewById(R.id.txtRadius).setVisibility(View.GONE);
    findViewById(R.id.spinnerCategories).setVisibility(View.GONE);
    
    // spinnerfill
    
}
    
} // end class
