package pt.up.fe.aroundme.android;

import pt.up.fe.aroundme.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class SeekBarPreference extends Preference implements
		OnSeekBarChangeListener {

	private final String TAG = this.getClass().getName();

	private static final String ANDROIDNS =
			"http://schemas.android.com/apk/res/android";
	private static final String AROUNDMENS = "http://around-me.herokuapp.com";
	private static final int DEFAULT_VALUE = 25;

	private int mMaxValue;
	private int mMinValue;
	private int mInterval;
	private int mCurrentValue;
	private String mUnitsLeft;
	private String mUnitsRight;
	private SeekBar mSeekBar;
	private TextView mStatusText;

	public SeekBarPreference(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		this.initPreference(context, attrs);
	}

	public SeekBarPreference(final Context context, final AttributeSet attrs,
			final int defStyle) {
		super(context, attrs, defStyle);
		this.initPreference(context, attrs);
	}

	private void initPreference(final Context context, final AttributeSet attrs) {
		this.setValuesFromXml(attrs);
		this.mSeekBar = new SeekBar(context, attrs);
		this.mSeekBar.setMax(this.mMaxValue - this.mMinValue);
		this.mSeekBar.setOnSeekBarChangeListener(this);
	}

	private void setValuesFromXml(final AttributeSet attrs) {
		this.mMaxValue = attrs.getAttributeIntValue(ANDROIDNS, "max", 100);
		this.mMinValue = attrs.getAttributeIntValue(AROUNDMENS, "min", 0);

		this.mUnitsLeft =
				this.getAttributeStringValue(attrs, AROUNDMENS, "unitsLeft", "");
		final String units =
				this.getAttributeStringValue(attrs, AROUNDMENS, "units", "");
		this.mUnitsRight =
				this.getAttributeStringValue(attrs, AROUNDMENS, "unitsRight",
						units);

		try {
			final String newInterval =
					attrs.getAttributeValue(AROUNDMENS, "interval");
			if( newInterval != null ) {
				this.mInterval = Integer.parseInt(newInterval);
			}
		} catch (final Exception e) {
			Log.e(this.TAG, "Invalid interval value", e);
		}

	}

	private String getAttributeStringValue(final AttributeSet attrs,
			final String namespace, final String name, final String defaultValue) {
		String value = attrs.getAttributeValue(namespace, name);
		if( value == null ) {
			value = defaultValue;
		}

		return value;
	}

	@Override
	protected View onCreateView(final ViewGroup parent) {

		RelativeLayout layout = null;

		try {
			final LayoutInflater mInflater =
					(LayoutInflater) this.getContext().getSystemService(
							Context.LAYOUT_INFLATER_SERVICE);

			layout =
					(RelativeLayout) mInflater.inflate(
							R.layout.seek_bar_preference, parent, false);
		} catch (final Exception e) {
			Log.e(this.TAG, "Error creating seek bar preference", e);
		}

		return layout;

	}

	@SuppressWarnings("deprecation")
	@Override
	public void onBindView(final View view) {
		super.onBindView(view);

		try {
			// move our seekbar to the new view we've been given
			final ViewParent oldContainer = this.mSeekBar.getParent();
			final ViewGroup newContainer =
					(ViewGroup) view.findViewById(R.id.seekBarPrefBarContainer);

			if( oldContainer != newContainer ) {
				// remove the seekbar from the old view
				if( oldContainer != null ) {
					((ViewGroup) oldContainer).removeView(this.mSeekBar);
				}
				// remove the existing seekbar (there may not be one) and add
				// ours
				newContainer.removeAllViews();

				newContainer.addView(this.mSeekBar,
						ViewGroup.LayoutParams.FILL_PARENT,
						ViewGroup.LayoutParams.WRAP_CONTENT);
			}
		} catch (final Exception ex) {
			Log.e(this.TAG, "Error binding view: " + ex.toString());
		}

		this.updateView(view);
	}

	/**
	 * Update a SeekBarPreference view with our current state
	 * 
	 * @param view
	 */
	protected void updateView(final View view) {

		try {
			final RelativeLayout layout = (RelativeLayout) view;

			this.mStatusText =
					(TextView) layout.findViewById(R.id.seekBarPrefValue);
			this.mStatusText.setText(String.valueOf(this.mCurrentValue));
			this.mStatusText.setMinimumWidth(30);

			Log.d("radius", this.mCurrentValue + "");

			this.mSeekBar.setProgress(this.mCurrentValue - this.mMinValue);

			final TextView unitsRight =
					(TextView) layout.findViewById(R.id.seekBarPrefUnitsRight);
			unitsRight.setText(this.mUnitsRight);

			final TextView unitsLeft =
					(TextView) layout.findViewById(R.id.seekBarPrefUnitsLeft);
			unitsLeft.setText(this.mUnitsLeft);

		} catch (final Exception e) {
			Log.e(this.TAG, "Error updating seek bar preference", e);
		}

	}

	@Override
	public void onProgressChanged(final SeekBar seekBar, final int progress,
			final boolean fromUser) {
		int newValue = progress + this.mMinValue;

		if( newValue > this.mMaxValue ) {
			newValue = this.mMaxValue;
		}
		else if( newValue < this.mMinValue ) {
			newValue = this.mMinValue;
		}
		else if( this.mInterval != 1 && newValue % this.mInterval != 0 ) {
			newValue =
					Math.round(((float) newValue) / this.mInterval)
							* this.mInterval;
		}

		// change rejected, revert to the previous value
		if( !this.callChangeListener(newValue) ) {
			seekBar.setProgress(this.mCurrentValue - this.mMinValue);
			return;
		}

		// change accepted, store it
		this.mCurrentValue = newValue;
		this.mStatusText.setText(String.valueOf(newValue));
		this.persistInt(newValue);

	}

	@Override
	public void onStartTrackingTouch(final SeekBar seekBar) {}

	@Override
	public void onStopTrackingTouch(final SeekBar seekBar) {
		this.notifyChanged();
	}

	@Override
	protected Object onGetDefaultValue(final TypedArray ta, final int index) {

		final int defaultValue = ta.getInt(index, DEFAULT_VALUE);
		return defaultValue;

	}

	@Override
	protected void onSetInitialValue(final boolean restoreValue,
			final Object defaultValue) {

		if( restoreValue ) {
			this.mCurrentValue = this.getPersistedInt(this.mCurrentValue);
		}
		else {
			int temp = 0;
			try {
				temp = (Integer) defaultValue;
			} catch (final Exception ex) {
				Log.e(this.TAG, "Invalid default value: "
						+ defaultValue.toString());
			}

			this.persistInt(temp);
			this.mCurrentValue = temp;
		}

	}

}
