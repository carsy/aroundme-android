package com.feup.aroundme;

import com.feup.aroundme.R;

import android.os.Bundle;

public class EventDescriptionActivity extends SuperActivity 
{

protected void onCreate(Bundle savedInstanceState) 
{
    super.onCreate(savedInstanceState);
    setContentView (R.layout.activity_event_description);
    setTitleFromActivityLabel (R.id.title_text);
}
    
} // end class
